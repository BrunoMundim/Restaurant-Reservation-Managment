package br.com.mundim.RestaurantReservationManagment.controller;

import br.com.mundim.RestaurantReservationManagment.model.dto.CustomerDTO;
import br.com.mundim.RestaurantReservationManagment.model.entity.Customer;
import br.com.mundim.RestaurantReservationManagment.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    @Operation(tags = "CUSTOMER", summary = "Create Customer")
    public Customer create(@RequestBody CustomerDTO dto) {
        return customerService.create(dto);
    }

    @GetMapping
    @Operation(tags = "CUSTOMER", summary = "Find all Customers")
    public List<Customer> findAll() {
        return customerService.findAll();
    }

    @GetMapping("/find-by-id")
    @Operation(tags = "CUSTOMER", summary = "Find Customer by ID")
    public Customer findById(@RequestParam Long id) {
        return customerService.findById(id);
    }

    @GetMapping("/find-by-cpf")
    @Operation(tags = "CUSTOMER", summary = "Find Customer by CPF")
    public Customer findByCpf(@RequestParam String cpf) {
        return customerService.findByCpf(cpf);
    }

    @GetMapping("/find-by-email")
    @Operation(tags = "CUSTOMER", summary = "Find Customer by email")
    public Customer findByEmail(@RequestParam String email) {
        return customerService.findByEmail(email);
    }

    @PutMapping
    @Operation(tags = "CUSTOMER", summary = "Update Customer by ID")
    public Customer update(@RequestParam Long customerId, @RequestBody CustomerDTO customerDTO) {
        return customerService.update(customerId, customerDTO);
    }

    @DeleteMapping
    @Operation(tags = "CUSTOMER", summary = "Delete Customer by ID")
    public Customer delete(@RequestParam Long customerId) {
        return customerService.deleteById(customerId);
    }

}
