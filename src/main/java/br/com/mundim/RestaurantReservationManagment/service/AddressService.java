package br.com.mundim.RestaurantReservationManagment.service;

import br.com.mundim.RestaurantReservationManagment.model.dto.AddressDTO;
import br.com.mundim.RestaurantReservationManagment.model.entity.Address;
import br.com.mundim.RestaurantReservationManagment.repository.AddressRepository;
import org.springframework.stereotype.Service;

@Service
public class AddressService {

    private final AddressRepository addressRepository;

    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    public Address create(AddressDTO dto) {
        return addressRepository.save(new Address(dto));
    }

}
