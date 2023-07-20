package br.com.mundim.RestaurantReservationManagment.service;

import br.com.mundim.RestaurantReservationManagment.exceptions.BadRequestException;
import br.com.mundim.RestaurantReservationManagment.model.dto.CustomerDTO;
import br.com.mundim.RestaurantReservationManagment.model.entity.Customer;
import br.com.mundim.RestaurantReservationManagment.repository.CustomerRepository;
import br.com.mundim.RestaurantReservationManagment.security.AuthenticationService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

import static br.com.mundim.RestaurantReservationManagment.exceptions.config.BaseErrorMessage.*;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationService authenticationService;

    public CustomerService(CustomerRepository customerRepository, PasswordEncoder passwordEncoder, AuthenticationService authenticationService) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationService = authenticationService;
    }

    public Customer create(CustomerDTO dto) {
        authenticationService.verifyEmailAvailability(dto.email());
        String password = passwordEncoder.encode(dto.password());
        return customerRepository.save(new Customer(dto, password));
    }

    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    public Customer findCurrentCustomer() {
        UserDetails user = authenticationService.findUserByBearer();
        return customerRepository.findByEmail(user.getUsername());
    }

    public Customer findById(Long customerId) {
        authenticationService.verifyCustomerOwnership(customerId);
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new BadRequestException(
                        CUSTOMER_NOT_FOUND_BY_ID.params(customerId.toString()).getMessage())
                );
    }

    public Customer findByCpf(String cpf) {
        Customer customer = customerRepository.findByCpf(cpf);
        if (customer == null)
            throw new BadRequestException(CUSTOMER_NOT_FOUND_BY_CPF.params(cpf).getMessage());
        authenticationService.verifyCustomerOwnership(customer.getId());
        return customer;
    }

    public Customer findByEmail(String email) {
        Customer customer = customerRepository.findByEmail(email);
        if (customer == null)
            throw new BadRequestException(CUSTOMER_NOT_FOUND_BY_EMAIL.params(email).getMessage());
        authenticationService.verifyCustomerOwnership(customer.getId());
        return customer;
    }

    public Customer update(Long customerId, CustomerDTO dto) {
        authenticationService.verifyCustomerOwnership(customerId);
        Customer customer = findById(customerId);
        customer.setName(dto.name());
        customer.setCpf(dto.cpf());
        customer.setEmail(dto.email());
        customer.setPhoneNumber(dto.phoneNumber());
        return customerRepository.save(customer);
    }

    public Customer deleteById(Long customerId) {
        authenticationService.verifyCustomerOwnership(customerId);
        Customer customer = findById(customerId);
        customerRepository.deleteById(customerId);
        return customer;
    }

}
