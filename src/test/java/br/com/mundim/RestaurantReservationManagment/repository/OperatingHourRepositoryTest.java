package br.com.mundim.RestaurantReservationManagment.repository;

import br.com.mundim.RestaurantReservationManagment.model.entity.OperatingHour;
import jakarta.validation.constraints.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class OperatingHourRepositoryTest {

    @Autowired
    private OperatingHourRepository operatingHourRepository;

    private static OperatingHour operatingHour;
    private static OperatingHour operatingHour2;

    @BeforeEach
    public void setup() {
        operatingHour = OperatingHour.builder()
                .weekDay(LocalDateTime.now().getDayOfWeek()).opening(LocalTime.now().minusHours(1))
                .closing(LocalTime.now().plusHours(1)).fullTable(true)
                .build();
        operatingHour2 = OperatingHour.builder()
                .weekDay(LocalDateTime.now().getDayOfWeek()).opening(LocalTime.now().plusHours(2))
                .closing(LocalTime.now().plusHours(4)).fullTable(false)
                .build();
    }

    @Test
    public void save_shouldReturnSavedOperatingHour() {
        OperatingHour savedOperatingHour = operatingHourRepository.save(operatingHour);

        assertThat(savedOperatingHour).isNotNull();
        assertThat(savedOperatingHour.getId()).isGreaterThan(0);
        assertThat(savedOperatingHour.getWeekDay()).isEqualTo(operatingHour.getWeekDay());
        assertThat(savedOperatingHour.getOpening()).isEqualTo(operatingHour.getOpening());
        assertThat(savedOperatingHour.getClosing()).isEqualTo(operatingHour.getClosing());
        assertThat(savedOperatingHour.isFullTable()).isEqualTo(operatingHour.isFullTable());
    }

    @Test
    public void findAll_shouldReturnListOfTwoOperatingHours() {
        operatingHourRepository.save(operatingHour);
        operatingHourRepository.save(operatingHour2);

        List<OperatingHour> operatingHours = operatingHourRepository.findAll();

        assertThat(operatingHours).isNotNull();
        assertThat(operatingHours.size()).isEqualTo(2);
        assertThat(operatingHours.contains(operatingHour)).isTrue();
        assertThat(operatingHours.contains(operatingHour2)).isTrue();
    }

    @Test
    public void findById_shouldReturnFoundOperatingHour() {
        operatingHourRepository.save(operatingHour);

        OperatingHour foundOperatingHour = operatingHourRepository.findById(operatingHour.getId()).orElse(null);

        assertThat(foundOperatingHour).isNotNull();
        assertThat(foundOperatingHour).isEqualTo(operatingHour);
    }

    @Test
    public void deleteById_shouldDeleteOperatingHour() {
        operatingHourRepository.save(operatingHour);

        operatingHourRepository.deleteById(operatingHour.getId());
        OperatingHour foundOperatingHour = operatingHourRepository.findById(operatingHour.getId()).orElse(null);

        assertThat(foundOperatingHour).isNull();
    }

}
