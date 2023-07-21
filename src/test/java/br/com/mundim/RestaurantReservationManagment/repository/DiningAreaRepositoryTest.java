package br.com.mundim.RestaurantReservationManagment.repository;

import br.com.mundim.RestaurantReservationManagment.model.entity.DiningArea;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static br.com.mundim.RestaurantReservationManagment.model.entity.DiningArea.Availability.AVAILABLE;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class DiningAreaRepositoryTest {

    @Autowired
    private DiningAreaRepository diningAreaRepository;

    private static DiningArea diningArea;
    private static DiningArea diningArea2;

    @BeforeEach
    public void setup() {
        diningArea = DiningArea.builder()
                .restaurantId(1L).customerId(null).diningAreaName("Table 1")
                .capacity(4).availability(AVAILABLE)
                .build();
        diningArea2 = DiningArea.builder()
                .restaurantId(1L).customerId(null).diningAreaName("Table 2")
                .capacity(6).availability(AVAILABLE)
                .build();
    }

    @Test
    public void save_shouldReturnSavedDiningArea() {
        DiningArea createDiningArea = diningAreaRepository.save(diningArea);

        assertThat(createDiningArea).isNotNull();
        assertThat(createDiningArea.getId()).isGreaterThan(0);
        assertThat(createDiningArea.getRestaurantId()).isEqualTo(diningArea.getRestaurantId());
        assertThat(createDiningArea.getCustomerId()).isEqualTo(diningArea.getCustomerId());
        assertThat(createDiningArea.getDiningAreaName()).isEqualTo(diningArea.getDiningAreaName());
        assertThat(createDiningArea.getCapacity()).isEqualTo(diningArea.getCapacity());
        assertThat(createDiningArea.getAvailability()).isEqualTo(diningArea.getAvailability());
    }

    @Test
    public void save_shouldThrowConstraintViolationExceptionForNegativeCapacity() {
        DiningArea diningArea = DiningArea.builder()
                .restaurantId(1L).customerId(null).diningAreaName("Table 1")
                .capacity(-1).availability(AVAILABLE)
                .build();
        Throwable throwable = catchThrowable(() -> diningAreaRepository.save(diningArea));

        assertThat(throwable).isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    public void findAll_shouldReturnListOfTwoDiningAreas() {
        diningAreaRepository.save(diningArea);
        diningAreaRepository.save(diningArea2);

        List<DiningArea> diningAreas = diningAreaRepository.findAll();

        assertThat(diningAreas).isNotNull();
        assertThat(diningAreas.size()).isEqualTo(2);
        assertThat(diningAreas.contains(diningArea)).isTrue();
        assertThat(diningAreas.contains(diningArea2)).isTrue();
    }

    @Test
    public void findByRestaurantId_shouldReturnListOfTwoDiningAreas() {
        diningAreaRepository.save(diningArea);
        diningAreaRepository.save(diningArea2);

        List<DiningArea> diningAreas = diningAreaRepository.findByRestaurantId(diningArea.getRestaurantId());

        assertThat(diningAreas).isNotNull();
        assertThat(diningAreas.size()).isEqualTo(2);
        assertThat(diningAreas.contains(diningArea)).isTrue();
        assertThat(diningAreas.contains(diningArea2)).isTrue();
    }

    @Test
    public void findById_shouldReturnFoundDiningArea() {
        diningAreaRepository.save(diningArea);

        DiningArea foundDiningArea = diningAreaRepository.findById(diningArea.getId()).orElse(null);

        assertThat(foundDiningArea).isNotNull();
        assertThat(foundDiningArea).isEqualTo(diningArea);
    }


}
