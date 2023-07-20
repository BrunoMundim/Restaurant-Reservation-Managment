package br.com.mundim.RestaurantReservationManagment.service;

import br.com.mundim.RestaurantReservationManagment.exceptions.BadRequestException;
import br.com.mundim.RestaurantReservationManagment.model.dto.OperatingHourDTO;
import br.com.mundim.RestaurantReservationManagment.model.dto.RestaurantDTO;
import br.com.mundim.RestaurantReservationManagment.model.entity.Address;
import br.com.mundim.RestaurantReservationManagment.model.entity.DiningArea;
import br.com.mundim.RestaurantReservationManagment.model.entity.OperatingHour;
import br.com.mundim.RestaurantReservationManagment.model.entity.Restaurant;
import br.com.mundim.RestaurantReservationManagment.model.view.RestaurantView;
import br.com.mundim.RestaurantReservationManagment.repository.RestaurantRepository;
import br.com.mundim.RestaurantReservationManagment.security.AuthenticationService;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import static br.com.mundim.RestaurantReservationManagment.exceptions.config.BaseErrorMessage.*;

@Service
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final AddressService addressService;
    private final OperatingHourService operatingHourService;
    private final DiningAreaService diningAreaService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationService authenticationService;

    public RestaurantService(
            RestaurantRepository restaurantRepository,
            AddressService addressService,
            OperatingHourService operatingHourService,
            @Lazy DiningAreaService diningAreaService,
            PasswordEncoder passwordEncoder, AuthenticationService authenticationService) {
        this.restaurantRepository = restaurantRepository;
        this.addressService = addressService;
        this.operatingHourService = operatingHourService;
        this.diningAreaService = diningAreaService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationService = authenticationService;
    }

    public RestaurantView create(RestaurantDTO dto) {
        authenticationService.verifyEmailAvailability(dto.email());
        Address address = addressService.create(dto.address());
        List<OperatingHour> operatingHours = generateOperatingHours(dto);
        String password = passwordEncoder.encode(dto.password());
        Restaurant restaurant = restaurantRepository.save(new Restaurant(address, operatingHours, dto, password));
        return new RestaurantView(restaurant);
    }

    public List<RestaurantView> findAll() {
        List<Restaurant> restaurants = restaurantRepository.findAll();
        return generateRestaurantViews(restaurants);
    }

    public RestaurantView findByIdReturnView(Long id) {
        Restaurant restaurant = findById(id);
        return new RestaurantView(restaurant);
    }

    public Restaurant findById(Long id) {
        return restaurantRepository.findById(id)
                .orElseThrow(() -> new BadRequestException(
                        RESTAURANT_NOT_FOUND_BY_ID.params(id.toString()).getMessage()));
    }

    public RestaurantView findLoggedRestaurant() {
        String email = authenticationService.findUserByBearer().getUsername();
        return new RestaurantView(restaurantRepository.findByEmail(email));
    }

    public RestaurantView update(Long id, RestaurantDTO dto) {
        Restaurant restaurant = findById(id);
        authenticationService.verifyRestaurantOwnership(id);

        if (dto.address() == null)
            throw new BadRequestException(ADDRESS_NULL.getMessage());

        restaurant.setOperatingHours(generateOperatingHours(dto));
        restaurant.setAddress(new Address(dto.address()));
        restaurant.setCnpj(dto.cnpj());
        restaurant.setName(dto.name());
        restaurant.setEmail(dto.email());
        restaurant.setCellphone(dto.cellphone());

        restaurantRepository.save(restaurant);
        return new RestaurantView(restaurant);
    }

    public RestaurantView deleteById(Long id) {
        Restaurant restaurant = findById(id);
        authenticationService.verifyRestaurantOwnership(id);
        restaurantRepository.deleteById(id);
        deleteAllDiningAreasFromRestaurant(restaurant.getId());
        return new RestaurantView(restaurant);
    }

    private void deleteAllDiningAreasFromRestaurant(Long restaurantId) {
        List<DiningArea> diningAreas = diningAreaService.findByRestaurantId(restaurantId);
        for (DiningArea diningArea : diningAreas) {
            diningAreaService.deleteById(diningArea.getId());
        }
    }

    private List<OperatingHour> generateOperatingHours(RestaurantDTO dto) {
        if (dto.operatingHours() == null) {
            throw new BadRequestException(OPERATING_HOURS_NULL.getMessage());
        }

        validateOperatingHours(dto.operatingHours());

        return dto.operatingHours().stream()
                .map(operatingHourService::create)
                .collect(Collectors.toList());
    }

    private void validateOperatingHours(List<OperatingHourDTO> operatingHours) {
        for (int i = 0; i < operatingHours.size(); i++) {
            for (int j = i + 1; j < operatingHours.size(); j++) {
                verifyOperationHoursDTO(operatingHours.get(i), operatingHours.get(j));
            }
        }
    }

    private void verifyOperationHoursDTO(OperatingHourDTO dto1, OperatingHourDTO dto2) {
        LocalTime dto1Opening = LocalTime.parse(dto1.opening());
        LocalTime dto1Closing = LocalTime.parse(dto1.closing());
        LocalTime dto2Opening = LocalTime.parse(dto2.opening());
        LocalTime dto2Closing = LocalTime.parse(dto2.closing());

        boolean isDto1WithinDto2 = (dto1Opening.isAfter(dto2Opening) && dto1Opening.isBefore(dto2Closing)) ||
                (dto1Closing.isAfter(dto2Opening) && dto1Closing.isBefore(dto2Closing));

        boolean isDto2WithinDto1 = (dto2Opening.isAfter(dto1Opening) && dto2Opening.isBefore(dto1Closing)) ||
                (dto2Closing.isAfter(dto1Opening) && dto2Closing.isBefore(dto1Closing));

        if (isDto1WithinDto2 || isDto2WithinDto1) {
            throw new BadRequestException(CONFLICTING_OPERATING_HOURS.getMessage());
        }
    }

    private List<RestaurantView> generateRestaurantViews(List<Restaurant> restaurants) {
        return restaurants.stream()
                .map(RestaurantView::new)
                .collect(Collectors.toList());
    }

}
