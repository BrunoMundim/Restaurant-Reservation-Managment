package br.com.mundim.RestaurantReservationManagment.repository;

import br.com.mundim.RestaurantReservationManagment.model.entity.WaitList;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

import static br.com.mundim.RestaurantReservationManagment.model.entity.WaitList.WaitListStatus.WAITING;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class WaitListRepositoryTest {

    @Autowired
    private WaitListRepository waitListRepository;

    @Test
    public void findWaitingByRestaurantAndExactPartySize_shouldReturnOneWaitList() {
        WaitList waitList = WaitList.builder()
                .customerId(1L).restaurantId(1L).partySize(6)
                .reservationDateTime(LocalDateTime.now().plusHours(1)).waitListStatus(WAITING)
                .joinedWaitList(LocalDateTime.now())
                .build();
        waitListRepository.save(waitList);

        List<WaitList> foundWaitList = waitListRepository.findWaitingByRestaurantAndExactPartySize(
                1L,
                6
        );

        assertThat(foundWaitList.size()).isGreaterThan(0);
    }

}
