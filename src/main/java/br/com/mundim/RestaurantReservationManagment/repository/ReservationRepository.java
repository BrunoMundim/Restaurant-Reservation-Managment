package br.com.mundim.RestaurantReservationManagment.repository;

import br.com.mundim.RestaurantReservationManagment.model.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByRestaurantId(Long restaurantId);

    List<Reservation> findByDiningAreaId(Long diningAreaId);

    @Query("SELECT r.reservationDateTime " +
            "FROM Reservation r " +
            "WHERE r.diningAreaId = :diningAreaId " +
            "AND r.status NOT IN (2, 3, 4)")
    List<LocalDateTime> findReservedDatesByDiningAreaId(@Param("diningAreaId") Long diningAreaId);

}
