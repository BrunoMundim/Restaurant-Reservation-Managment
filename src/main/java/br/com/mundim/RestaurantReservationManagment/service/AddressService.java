package br.com.mundim.RestaurantReservationManagment.service;

import br.com.mundim.RestaurantReservationManagment.exceptions.BadRequestException;
import br.com.mundim.RestaurantReservationManagment.exceptions.config.BaseErrorMessage;
import br.com.mundim.RestaurantReservationManagment.model.dto.AddressDTO;
import br.com.mundim.RestaurantReservationManagment.model.entity.Address;
import br.com.mundim.RestaurantReservationManagment.repository.AddressRepository;
import org.springframework.stereotype.Service;

import static br.com.mundim.RestaurantReservationManagment.exceptions.config.BaseErrorMessage.ADDRESS_NOT_FOUND_BY_ID;

@Service
public class AddressService {

    private final AddressRepository addressRepository;

    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    public Address create(AddressDTO dto) {
        return addressRepository.save(new Address(dto));
    }

    public Address findById(Long id) {
        return addressRepository.findById(id)
                .orElseThrow(() -> new BadRequestException(ADDRESS_NOT_FOUND_BY_ID.params(id.toString()).getMessage()));
    }

    public void deleteById(Long id) {
        addressRepository.deleteById(id);
    }

}
