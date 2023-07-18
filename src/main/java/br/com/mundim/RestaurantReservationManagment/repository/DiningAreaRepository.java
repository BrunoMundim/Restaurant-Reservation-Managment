package br.com.mundim.RestaurantReservationManagment.repository;

import br.com.mundim.RestaurantReservationManagment.model.entity.DiningArea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiningAreaRepository extends JpaRepository<DiningArea, Long> {

    List<DiningArea> findByRestaurantId(Long restaurantId);

}
