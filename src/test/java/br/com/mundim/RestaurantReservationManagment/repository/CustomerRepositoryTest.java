package br.com.mundim.RestaurantReservationManagment.repository;

import br.com.mundim.RestaurantReservationManagment.model.entity.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    private static Customer customer;
    private static Customer customer2;

    @BeforeEach
    public void setup() {
        customer = Customer.builder()
                .name("Bruno Mundim").email("bruno@email.com").phoneNumber("(34) 99197-0268")
                .cpf("977.648.910-96").password("password").role("CUSTOMER")
                .build();
        customer2 = Customer.builder()
                .name("Bruno Mundim").email("bruno2@email.com").phoneNumber("(34) 99197-0268")
                .cpf("588.314.520-53").password("password").role("CUSTOMER")
                .build();
    }

    @Test
    public void save_shouldReturnSavedCustomer() {
        Customer savedCustomer = customerRepository.save(customer);

        assertThat(savedCustomer).isNotNull();
        assertThat(savedCustomer.getId()).isGreaterThan(0);
        assertThat(savedCustomer.getName()).isEqualTo(customer.getName());
        assertThat(savedCustomer.getEmail()).isEqualTo(customer.getEmail());
        assertThat(savedCustomer.getPhoneNumber()).isEqualTo(customer.getPhoneNumber());
        assertThat(savedCustomer.getCpf()).isEqualTo(customer.getCpf());
        assertThat(savedCustomer.getPassword()).isEqualTo(customer.getPassword());
        assertThat(savedCustomer.getRole()).isEqualTo(customer.getRole());
    }

    @Test
    public void save_shouldThrowDataIntegrityViolationExceptionForEmail() {
        Customer repeatedEmailCustomer = Customer.builder()
                .name("Bruno Mundim").email("bruno@email.com").phoneNumber("(34) 99197-0268")
                .cpf("588.314.520-53").password("password").role("CUSTOMER")
                .build();
        customerRepository.save(customer);
        Throwable throwable = catchThrowable(() -> customerRepository.save(repeatedEmailCustomer));

        assertThat(throwable).isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    public void save_shouldThrowDataIntegrityViolationExceptionForCpf() {
        Customer repeatedEmailCustomer = Customer.builder()
                .name("Bruno Mundim").email("bruno2@email.com").phoneNumber("(34) 99197-0268")
                .cpf("977.648.910-96").password("password").role("CUSTOMER")
                .build();
        customerRepository.save(customer);
        Throwable throwable = catchThrowable(() -> customerRepository.save(repeatedEmailCustomer));

        assertThat(throwable).isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    public void findAll_shouldReturnListOfTwoCustomers() {
        customerRepository.save(customer);
        customerRepository.save(customer2);

        List<Customer> customers = customerRepository.findAll();

        assertThat(customers).isNotNull();
        assertThat(customers.size()).isEqualTo(2);
        assertThat(customers.contains(customer)).isTrue();
        assertThat(customers.contains(customer2)).isTrue();
    }

    @Test
    public void findById_shouldReturnFoundCustomer() {
        customerRepository.save(customer);

        Customer foundCustomer = customerRepository.findById(customer.getId()).orElse(null);

        assertThat(foundCustomer).isNotNull();
        assertThat(foundCustomer).isEqualTo(customer);
    }

    @Test
    public void findByEmail_shouldReturnFoundCustomer() {
        customerRepository.save(customer);

        Customer foundCustomer = customerRepository.findByEmail(customer.getEmail());

        assertThat(foundCustomer).isNotNull();
        assertThat(foundCustomer).isEqualTo(customer);
    }

    @Test
    public void findByCpf_shouldReturnFoundCustomer() {
        customerRepository.save(customer);

        Customer foundCustomer = customerRepository.findByCpf(customer.getCpf());

        assertThat(foundCustomer).isNotNull();
        assertThat(foundCustomer).isEqualTo(customer);
    }

    @Test
    public void deleteById_shouldDeleteCustomer() {
        customerRepository.save(customer);
        customerRepository.deleteById(customer.getId());
        Customer foundCustomer = customerRepository.findById(customer.getId()).orElse(null);

        assertThat(foundCustomer).isNull();
    }

}
