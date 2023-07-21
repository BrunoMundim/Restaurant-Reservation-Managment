package br.com.mundim.RestaurantReservationManagment.repository;

import br.com.mundim.RestaurantReservationManagment.model.entity.WaitList;
import org.junit.jupiter.api.BeforeEach;
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

    private static WaitList waitList;
    private static WaitList waitList2;

    @BeforeEach
    public void setup() {
        waitList = WaitList.builder()
                .customerId(1L).restaurantId(1L).partySize(6)
                .reservationDateTime(LocalDateTime.now().plusHours(1)).waitListStatus(WAITING)
                .joinedWaitList(LocalDateTime.now())
                .build();
        waitList2 = WaitList.builder()
                .customerId(1L).restaurantId(1L).partySize(4)
                .reservationDateTime(LocalDateTime.now().plusHours(1)).waitListStatus(WAITING)
                .joinedWaitList(LocalDateTime.now())
                .build();
    }

    @Test
    public void save_shouldReturnSavedWaitList() {
        WaitList savedWaitList = waitListRepository.save(waitList);
        
        assertThat(savedWaitList).isNotNull();
        assertThat(savedWaitList.getId()).isGreaterThan(0);
        assertThat(savedWaitList.getCustomerId()).isEqualTo(waitList.getCustomerId());
        assertThat(savedWaitList.getRestaurantId()).isEqualTo(waitList.getRestaurantId());
        assertThat(savedWaitList.getPartySize()).isEqualTo(waitList.getPartySize());
        assertThat(savedWaitList.getReservationDateTime()).isEqualTo(waitList.getReservationDateTime());
        assertThat(savedWaitList.getWaitListStatus()).isEqualTo(waitList.getWaitListStatus());
        assertThat(savedWaitList.getJoinedWaitList()).isEqualTo(waitList.getJoinedWaitList());
    }

    @Test
    public void findAll() {
        waitListRepository.save(waitList);
        waitListRepository.save(waitList2);

        List<WaitList> foundWaitList = waitListRepository.findAll();

        assertThat(foundWaitList.size()).isEqualTo(2);
        assertThat(foundWaitList.contains(waitList)).isTrue();
        assertThat(foundWaitList.contains(waitList2)).isTrue();
    }

    @Test
    public void findWaitingByRestaurantAndExactPartySize_shouldReturnOneWaitList() {
        waitListRepository.save(waitList);
        waitListRepository.save(waitList2);

        List<WaitList> foundWaitList = waitListRepository.findWaitingByRestaurantAndExactPartySize(
                waitList.getRestaurantId(),
                waitList.getPartySize()
        );

        assertThat(foundWaitList.size()).isEqualTo(1);
        assertThat(foundWaitList.contains(waitList)).isTrue();
        assertThat(foundWaitList.contains(waitList2)).isFalse();
    }

    @Test
    public void findByRestaurantWithPartySizeEqualOrSmaller_shouldReturnTwoWaitLists() {
        waitListRepository.save(waitList);
        waitListRepository.save(waitList2);

        List<WaitList> foundWaitList = waitListRepository.findByRestaurantWithPartySizeEqualOrSmaller(
                waitList.getRestaurantId(),
                waitList.getPartySize()
        );

        assertThat(foundWaitList.size()).isEqualTo(2);
        assertThat(foundWaitList.contains(waitList)).isTrue();
        assertThat(foundWaitList.contains(waitList2)).isTrue();
    }

    @Test
    public void findByRestaurantId_shouldReturnTwoWaitLists() {
        waitListRepository.save(waitList);
        waitListRepository.save(waitList2);

        List<WaitList> foundWaitList = waitListRepository.findByRestaurantId(waitList.getRestaurantId());

        assertThat(foundWaitList.size()).isEqualTo(2);
        assertThat(foundWaitList.contains(waitList)).isTrue();
        assertThat(foundWaitList.contains(waitList2)).isTrue();
    }

    @Test
    public void findById_shouldReturnFoundWaitList() {
        waitListRepository.save(waitList);

        WaitList foundWaitList = waitListRepository.findById(waitList.getId()).orElse(null);

        assertThat(foundWaitList).isNotNull();
        assertThat(foundWaitList).isEqualTo(waitList);
    }

    @Test
    public void deleteById_shouldDeleteWaitList() {
        waitListRepository.save(waitList);

        waitListRepository.deleteById(waitList.getId());
        WaitList foundWaitList = waitListRepository.findById(waitList.getId()).orElse(null);

        assertThat(foundWaitList).isNull();
    }

}
