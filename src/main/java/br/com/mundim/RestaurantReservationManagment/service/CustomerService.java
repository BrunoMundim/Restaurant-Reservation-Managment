package br.com.mundim.RestaurantReservationManagment.service;

import br.com.mundim.RestaurantReservationManagment.exceptions.BadRequestException;
import br.com.mundim.RestaurantReservationManagment.model.dto.CustomerDTO;
import br.com.mundim.RestaurantReservationManagment.model.entity.Customer;
import br.com.mundim.RestaurantReservationManagment.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

import static br.com.mundim.RestaurantReservationManagment.exceptions.config.BaseErrorMessage.*;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Customer create(CustomerDTO dto) {
        return customerRepository.save(new Customer(dto));
    }

    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    public Customer findById(Long customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new BadRequestException(
                        CUSTOMER_NOT_FOUND_BY_ID.params(customerId.toString()).getMessage())
                );
    }

    public Customer findByCpf(String cpf) {
        Customer customer = customerRepository.findByCpf(cpf);
        if(customer == null)
            throw new BadRequestException(CUSTOMER_NOT_FOUND_BY_CPF.params(cpf).getMessage());
        return customer;
    }

    public Customer findByEmail(String email) {
        Customer customer = customerRepository.findByEmail(email);
        if(customer == null)
            throw new BadRequestException(CUSTOMER_NOT_FOUND_BY_EMAIL.params(email).getMessage());
        return customer;
    }

    public Customer update(Long customerId, CustomerDTO dto) {
        Customer customer = findById(customerId);
        customer.setName(dto.name());
        customer.setCpf(dto.cpf());
        customer.setEmail(dto.email());
        customer.setPhoneNumber(dto.phoneNumber());
        return customerRepository.save(customer);
    }

    public Customer deleteById(Long customerId) {
        Customer customer = findById(customerId);
        customerRepository.deleteById(customerId);
        return customer;
    }

}
