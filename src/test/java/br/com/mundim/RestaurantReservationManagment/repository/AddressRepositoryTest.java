package br.com.mundim.RestaurantReservationManagment.repository;

import br.com.mundim.RestaurantReservationManagment.model.entity.Address;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class AddressRepositoryTest {

    @Autowired
    private AddressRepository addressRepository;

    private static Address address;

    @BeforeEach
    public void setup() {
        address = Address.builder()
                .cep("38408-902").street("Av. João Naves de Ávila").number("1331")
                .addressLine2("Piso 1").district("Tibery").city("Uberlândia").state("Minas Gerais")
                .build();
    }

    @Test
    public void save_shouldReturnSavedAddress() {
        Address savedAddress = addressRepository.save(address);

        assertThat(savedAddress).isNotNull();
        assertThat(savedAddress.getId()).isGreaterThan(0);
        assertThat(savedAddress.getCep()).isEqualTo(address.getCep());
        assertThat(savedAddress.getStreet()).isEqualTo(address.getStreet());
        assertThat(savedAddress.getNumber()).isEqualTo(address.getNumber());
        assertThat(savedAddress.getAddressLine2()).isEqualTo(address.getAddressLine2());
        assertThat(savedAddress.getDistrict()).isEqualTo(address.getDistrict());
        assertThat(savedAddress.getCity()).isEqualTo(address.getCity());
        assertThat(savedAddress.getState()).isEqualTo(address.getState());
    }
}
