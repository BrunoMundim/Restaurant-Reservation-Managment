package br.com.mundim.RestaurantReservationManagment.repository;

import br.com.mundim.RestaurantReservationManagment.model.entity.Address;
import br.com.mundim.RestaurantReservationManagment.model.entity.OperatingHour;
import br.com.mundim.RestaurantReservationManagment.model.entity.Restaurant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class RestaurantRepositoryTest {

    @Autowired
    private RestaurantRepository restaurantRepository;

    private static Restaurant restaurant;
    private static Restaurant restaurant2;
    private static Address address;
    private static Address address2;
    private static OperatingHour operatingHour;

    @BeforeEach
    public void setup() {
        address = Address.builder()
                .cep("38408-902").street("Av. João Naves de Ávila").number("1331")
                .addressLine2("Piso 1").district("Tibery").city("Uberlândia").state("Minas Gerais")
                .build();
        address2 = Address.builder()
                .cep("38408-902").street("Av. João Naves de Ávila").number("1331")
                .addressLine2("Piso 2").district("Tibery").city("Uberlândia").state("Minas Gerais")
                .build();
        operatingHour = OperatingHour.builder()
                .weekDay(LocalDateTime.now().getDayOfWeek()).opening(LocalTime.now().minusHours(1))
                .closing(LocalTime.now().plusHours(1)).fullTable(true)
                .build();
        restaurant = Restaurant.builder()
                .address(address).operatingHours(List.of(operatingHour))
                .cnpj("73.054.594/0001-97").name("Coco Bambu").email("cocobambu2@email.com")
                .password("password").cellphone("(34) 99197-0287").role("RESTAURANT")
                .build();
        restaurant2 = Restaurant.builder()
                .address(address2).operatingHours(List.of(operatingHour))
                .cnpj("18.811.706/0001-07").name("Coco Bambu").email("cocobambu@email.com")
                .password("password").cellphone("(34) 99197-0287").role("RESTAURANT")
                .build();
    }

    @Test
    public void save_shouldReturnSavedRestaurant() {
        Restaurant savedRestaurant = restaurantRepository.save(restaurant);

        assertThat(savedRestaurant).isNotNull();
        assertThat(savedRestaurant.getId()).isGreaterThan(0);
        assertThat(savedRestaurant.getAddress()).isEqualTo(restaurant.getAddress());
        assertThat(savedRestaurant.getOperatingHours()).isEqualTo(restaurant.getOperatingHours());
        assertThat(savedRestaurant.getCnpj()).isEqualTo(restaurant.getCnpj());
        assertThat(savedRestaurant.getName()).isEqualTo(restaurant.getName());
        assertThat(savedRestaurant.getEmail()).isEqualTo(restaurant.getEmail());
        assertThat(savedRestaurant.getPassword()).isEqualTo(restaurant.getPassword());
        assertThat(savedRestaurant.getCellphone()).isEqualTo(restaurant.getCellphone());
        assertThat(savedRestaurant.getRole()).isEqualTo(restaurant.getRole());
    }

    @Test
    public void findAll_shouldReturnListOfTwoRestaurants() {
        restaurantRepository.save(restaurant);
        restaurantRepository.save(restaurant2);

        List<Restaurant> restaurants = restaurantRepository.findAll();

        assertThat(restaurants).isNotNull();
        assertThat(restaurants.size()).isEqualTo(2);
        assertThat(restaurants.contains(restaurant)).isTrue();
        assertThat(restaurants.contains(restaurant2)).isTrue();
    }

    @Test
    public void findById_shouldReturnFoundRestaurant() {
        restaurantRepository.save(restaurant);

        Restaurant foundRestaurant = restaurantRepository.findById(restaurant.getId()).orElse(null);

        assertThat(foundRestaurant).isNotNull();
        assertThat(foundRestaurant).isEqualTo(restaurant);
    }

    @Test
    public void findByEmail_shouldReturnFoundRestaurant() {
        restaurantRepository.save(restaurant);

        Restaurant foundRestaurant = restaurantRepository.findByEmail(restaurant.getEmail());

        assertThat(foundRestaurant).isNotNull();
        assertThat(foundRestaurant).isEqualTo(restaurant);
    }

    @Test
    public void deleteById_shouldDeleteRestaurant() {
        restaurantRepository.save(restaurant);

        restaurantRepository.deleteById(restaurant.getId());
        Restaurant foundRestaurant = restaurantRepository.findByEmail(restaurant.getEmail());

        assertThat(foundRestaurant).isNull();
    }

}
