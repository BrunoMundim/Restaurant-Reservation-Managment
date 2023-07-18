package br.com.mundim.RestaurantReservationManagment.service;

import br.com.mundim.RestaurantReservationManagment.model.dto.OperatingHoursDTO;
import br.com.mundim.RestaurantReservationManagment.model.dto.RestaurantDTO;
import br.com.mundim.RestaurantReservationManagment.model.entity.Address;
import br.com.mundim.RestaurantReservationManagment.model.entity.OperatingHours;
import br.com.mundim.RestaurantReservationManagment.model.entity.Restaurant;
import br.com.mundim.RestaurantReservationManagment.repository.RestaurantRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final AddressService addressService;
    private final OperatingHoursService operatingHoursService;

    public RestaurantService(RestaurantRepository restaurantRepository, AddressService addressService, OperatingHoursService operatingHoursService) {
        this.restaurantRepository = restaurantRepository;
        this.addressService = addressService;
        this.operatingHoursService = operatingHoursService;
    }

    public Restaurant create(RestaurantDTO dto) {
        Address address = addressService.create(dto.addressDTO());

        List<OperatingHours> operatingHours = new ArrayList<>();
        for (OperatingHoursDTO operatingHoursDTO : dto.operatingHoursDtos()) {
            operatingHours.add(operatingHoursService.create(operatingHoursDTO));
        }

        return restaurantRepository.save(new Restaurant(address.getId(), operatingHours, dto));
    }
}
