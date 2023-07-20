package br.com.mundim.RestaurantReservationManagment.repository;

import br.com.mundim.RestaurantReservationManagment.model.entity.WaitList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WaitListRepository extends JpaRepository<WaitList, Long> {

    List<WaitList> findByRestaurantId(Long restaurantId);

    @Query("SELECT w FROM WaitList w " +
            "WHERE w.restaurantId = :restaurantId " +
            "AND w.partySize = :partySize " +
            "AND w.waitListStatus = br.com.mundim.RestaurantReservationManagment.model.entity.WaitList$WaitListStatus.WAITING")
    List<WaitList> findWaitingByRestaurantAndExactPartySize(Long restaurantId, Integer partySize);

    @Query("SELECT w FROM WaitList w " +
            "WHERE w.restaurantId = :restaurantId " +
            "AND w.partySize <= :partySize " +
            "AND w.waitListStatus = br.com.mundim.RestaurantReservationManagment.model.entity.WaitList$WaitListStatus.WAITING")
    List<WaitList> findByRestaurantWithPartySizeEqualOrSmaller(Long restaurantId, Integer partySize);

}
