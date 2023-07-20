package br.com.mundim.RestaurantReservationManagment.service;

import br.com.mundim.RestaurantReservationManagment.model.dto.WaitListDTO;
import br.com.mundim.RestaurantReservationManagment.model.entity.DiningArea;
import br.com.mundim.RestaurantReservationManagment.model.entity.OperatingHour;
import br.com.mundim.RestaurantReservationManagment.model.entity.Reservation;
import br.com.mundim.RestaurantReservationManagment.model.entity.WaitList;
import br.com.mundim.RestaurantReservationManagment.repository.ReservationRepository;
import br.com.mundim.RestaurantReservationManagment.repository.WaitListRepository;
import br.com.mundim.RestaurantReservationManagment.security.AuthenticationService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

import static br.com.mundim.RestaurantReservationManagment.model.entity.WaitList.WaitListStatus.NOTIFIED;

@Service
public class WaitListService {

    private final WaitListRepository waitListRepository;
    private final DiningAreaService diningAreaService;
    private final ReservationRepository reservationRepository;
    private final AuthenticationService authenticationService;

    public WaitListService(
            WaitListRepository waitListRepository,
            @Lazy DiningAreaService diningAreaService,
            ReservationRepository reservationRepository,
            AuthenticationService authenticationService
    ) {
        this.waitListRepository = waitListRepository;
        this.diningAreaService = diningAreaService;
        this.reservationRepository = reservationRepository;
        this.authenticationService = authenticationService;
    }

    public WaitList create(WaitListDTO dto) {
        authenticationService.verifyCustomerOwnership(dto.customerId());
        return waitListRepository.save(new WaitList(dto));
    }

    public List<WaitList> findByRestaurantId(Long restaurantId) {
        authenticationService.verifyRestaurantOwnership(restaurantId);
        return waitListRepository.findByRestaurantId(restaurantId);
    }

    public List<WaitList> findByRestaurantWithExactPartySize(Long restaurantId, Integer partySize) {
        List<WaitList> waitLists = waitListRepository.findWaitingByRestaurantAndExactPartySize(restaurantId, partySize);
        Collections.sort(waitLists);
        return waitLists;
    }

    public List<WaitList> findByRestaurantWithPartySizeEqualOrSmaller(Long restaurantId, Integer partySize) {
        List<WaitList> waitLists = waitListRepository.findByRestaurantWithPartySizeEqualOrSmaller(restaurantId, partySize);
        Collections.sort(waitLists);
        return waitLists;
    }

    private List<WaitList> findWaitListWithCorrectReservationDateTime(List<WaitList> waitLists, OperatingHour operatingHour) {
        return waitLists.stream()
                .filter(waitList -> operatingHour.getWeekDay().equals(waitList.getReservationDateTime().getDayOfWeek())
                        && operatingHour.getOpening().minusMinutes(1).isBefore(waitList.getReservationDateTime().toLocalTime())
                        && operatingHour.getClosing().plusMinutes(1).isAfter(waitList.getReservationDateTime().toLocalTime()))
                .toList();
    }

    private List<WaitList> findWaitListBasedOnOperatingHour(Long diningAreaId, OperatingHour operatingHour) {
        DiningArea diningArea = diningAreaService.findById(diningAreaId);
        List<WaitList> waitLists;
        if(operatingHour.isFullTable()) {
            waitLists = findByRestaurantWithExactPartySize(diningArea.getRestaurantId(), diningArea.getCapacity());
        } else {
            waitLists = findByRestaurantWithPartySizeEqualOrSmaller(diningArea.getRestaurantId(), diningArea.getCapacity());
        }
        return findWaitListWithCorrectReservationDateTime(waitLists, operatingHour);
    }

    public Reservation checkWaitList(Long diningAreaId, OperatingHour operatingHour) {
        List<WaitList> waitLists = findWaitListBasedOnOperatingHour(diningAreaId, operatingHour);
        if(waitLists.size() > 0){
            WaitList foundWaitList = waitLists.get(0);
            foundWaitList.setWaitListStatus(NOTIFIED);
            // TODO: Send customer email confirming reservation
            waitListRepository.save(foundWaitList);
            Reservation newReservation = new Reservation(foundWaitList, diningAreaId);
            reservationRepository.save(newReservation);
            return newReservation;
        }
        else
            return null;
    }

}
