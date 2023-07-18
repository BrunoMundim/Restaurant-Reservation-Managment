package br.com.mundim.RestaurantReservationManagment.repository;

import br.com.mundim.RestaurantReservationManagment.model.entity.OperatingHours;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OperatingHoursRepository extends JpaRepository<OperatingHours, Long> {
}
