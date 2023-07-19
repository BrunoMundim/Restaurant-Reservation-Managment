package br.com.mundim.RestaurantReservationManagment.repository;

import br.com.mundim.RestaurantReservationManagment.model.entity.OperatingHour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OperatingHourRepository extends JpaRepository<OperatingHour, Long> {
}
