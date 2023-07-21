package br.com.mundim.RestaurantReservationManagment.repository;

import br.com.mundim.RestaurantReservationManagment.model.entity.Reservation;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

import static br.com.mundim.RestaurantReservationManagment.model.entity.Reservation.ReservationStatus.PENDING;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class ReservationRepositoryTest {

    @Autowired
    private ReservationRepository reservationRepository;

    private static Reservation reservation;
    private static Reservation reservation2;

    @BeforeEach
    public void setup() {
        reservation = Reservation.builder()
                .customerId(1L).restaurantId(1L).diningAreaId(1L)
                .reservationDateTime(LocalDateTime.now())
                .partySize(4).status(PENDING).notes("notes")
                .build();
        reservation2 = Reservation.builder()
                .customerId(2L).restaurantId(1L).diningAreaId(1L)
                .reservationDateTime(LocalDateTime.now().plusDays(1))
                .partySize(4).status(PENDING).notes("notes")
                .build();
    }

    @Test
    public void save_shouldReturnSavedReservation() {
        Reservation savedReservation = reservationRepository.save(reservation);

        assertThat(savedReservation).isNotNull();
        assertThat(savedReservation.getId()).isGreaterThan(0);
        assertThat(savedReservation.getCustomerId()).isEqualTo(reservation.getCustomerId());
        assertThat(savedReservation.getRestaurantId()).isEqualTo(reservation.getRestaurantId());
        assertThat(savedReservation.getReservationDateTime()).isEqualTo(reservation.getReservationDateTime());
        assertThat(savedReservation.getPartySize()).isEqualTo(reservation.getPartySize());
        assertThat(savedReservation.getStatus()).isEqualTo(reservation.getStatus());
    }

    @Test
    public void findAll_shouldReturnListOfTwoReservations() {
        reservationRepository.save(reservation);
        reservationRepository.save(reservation2);

        List<Reservation> reservations = reservationRepository.findAll();

        assertThat(reservations).isNotNull();
        assertThat(reservations.size()).isEqualTo(2);
        assertThat(reservations.contains(reservation)).isTrue();
        assertThat(reservations.contains(reservation2)).isTrue();
    }

    @Test
    public void findByRestaurantId_shouldReturnListOfTwoReservations() {
        reservationRepository.save(reservation);
        reservationRepository.save(reservation2);

        List<Reservation> reservations = reservationRepository.findByRestaurantId(reservation.getRestaurantId());

        assertThat(reservations).isNotNull();
        assertThat(reservations.size()).isEqualTo(2);
        assertThat(reservations.contains(reservation)).isTrue();
        assertThat(reservations.contains(reservation2)).isTrue();
    }

    @Test
    public void findByDiningAreaId_shouldReturnListOfTwoReservations() {
        reservationRepository.save(reservation);
        reservationRepository.save(reservation2);

        List<Reservation> reservations = reservationRepository.findByDiningAreaId(reservation.getDiningAreaId());

        assertThat(reservations).isNotNull();
        assertThat(reservations.size()).isEqualTo(2);
        assertThat(reservations.contains(reservation)).isTrue();
        assertThat(reservations.contains(reservation2)).isTrue();
    }

    @Test
    public void findById_shouldReturnFoundReservation() {
        reservationRepository.save(reservation);

        Reservation foundReservation = reservationRepository.findById(reservation.getId()).orElse(null);

        assertThat(foundReservation).isNotNull();
        assertThat(foundReservation).isEqualTo(reservation);
    }

    @Test
    public void deleteById_shouldReturnFoundReservation() {
        reservationRepository.save(reservation);

        reservationRepository.deleteById(reservation.getId());
        Reservation foundReservation = reservationRepository.findById(reservation.getId()).orElse(null);

        assertThat(foundReservation).isNull();
    }

}
