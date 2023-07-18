package br.com.mundim.RestaurantReservationManagment.service;

import br.com.mundim.RestaurantReservationManagment.exceptions.BadRequestException;
import br.com.mundim.RestaurantReservationManagment.model.dto.RestaurantDTO;
import br.com.mundim.RestaurantReservationManagment.model.entity.Address;
import br.com.mundim.RestaurantReservationManagment.model.entity.DiningArea;
import br.com.mundim.RestaurantReservationManagment.model.entity.OperatingHours;
import br.com.mundim.RestaurantReservationManagment.model.entity.Restaurant;
import br.com.mundim.RestaurantReservationManagment.model.view.RestaurantView;
import br.com.mundim.RestaurantReservationManagment.repository.RestaurantRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static br.com.mundim.RestaurantReservationManagment.exceptions.config.BaseErrorMessage.*;

@Service
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final AddressService addressService;
    private final OperatingHoursService operatingHoursService;
    private final DiningAreaService diningAreaService;

    public RestaurantService(RestaurantRepository restaurantRepository, AddressService addressService, OperatingHoursService operatingHoursService, DiningAreaService diningAreaService) {
        this.restaurantRepository = restaurantRepository;
        this.addressService = addressService;
        this.operatingHoursService = operatingHoursService;
        this.diningAreaService = diningAreaService;
    }

    public RestaurantView create(RestaurantDTO dto) {
        Address address = addressService.create(dto.addressDTO());
        List<OperatingHours> operatingHours = generateOperatingHours(dto);
        Restaurant restaurant = restaurantRepository.save(new Restaurant(address, operatingHours, dto));
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

    public RestaurantView update(Long id, RestaurantDTO dto) {
        Restaurant restaurant = findById(id);

        if (dto.addressDTO() == null)
            throw new BadRequestException(ADDRESS_NULL.getMessage());

        restaurant.setOperatingHours(generateOperatingHours(dto));
        restaurant.setAddress(new Address(dto.addressDTO()));
        restaurant.setCnpj(dto.cnpj());
        restaurant.setName(dto.name());
        restaurant.setEmail(dto.email());
        restaurant.setPassword(dto.password());
        restaurant.setCellphone(dto.cellphone());

        restaurantRepository.save(restaurant);
        return new RestaurantView(restaurant);
    }

    public RestaurantView deleteById(Long id) {
        Restaurant restaurant = findById(id);
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

    private List<OperatingHours> generateOperatingHours(RestaurantDTO dto) {
        if (dto.operatingHoursDtos() == null)
            throw new BadRequestException(OPERATING_HOURS_NULL.getMessage());

        return dto.operatingHoursDtos().stream()
                .map(operatingHoursService::create)
                .collect(Collectors.toList());
    }

    private List<RestaurantView> generateRestaurantViews(List<Restaurant> restaurants) {
        return restaurants.stream()
                .map(RestaurantView::new)
                .collect(Collectors.toList());
    }

}
