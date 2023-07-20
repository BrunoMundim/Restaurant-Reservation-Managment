package br.com.mundim.RestaurantReservationManagment.service;

import br.com.mundim.RestaurantReservationManagment.exceptions.BadRequestException;
import br.com.mundim.RestaurantReservationManagment.model.dto.ReservationDTO;
import br.com.mundim.RestaurantReservationManagment.model.entity.DiningArea;
import br.com.mundim.RestaurantReservationManagment.model.entity.OperatingHour;
import br.com.mundim.RestaurantReservationManagment.model.entity.Reservation;
import br.com.mundim.RestaurantReservationManagment.model.entity.Restaurant;
import br.com.mundim.RestaurantReservationManagment.repository.ReservationRepository;
import br.com.mundim.RestaurantReservationManagment.security.AuthenticationService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

import static br.com.mundim.RestaurantReservationManagment.exceptions.config.BaseErrorMessage.*;
import static br.com.mundim.RestaurantReservationManagment.model.entity.Reservation.ReservationStatus.*;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final RestaurantService restaurantService;
    private final DiningAreaService diningAreaService;
    private final CustomerService customerService;
    private final WaitListService waitListService;
    private final AuthenticationService authenticationService;

    public ReservationService(
            ReservationRepository reservationRepository,
            RestaurantService restaurantService,
            DiningAreaService diningAreaService,
            CustomerService customerService,
            WaitListService waitListService, AuthenticationService authenticationService) {
        this.reservationRepository = reservationRepository;
        this.restaurantService = restaurantService;
        this.diningAreaService = diningAreaService;
        this.customerService = customerService;
        this.waitListService = waitListService;
        this.authenticationService = authenticationService;
    }

    // CRUD Operations

    public Reservation customerCreateReservation(ReservationDTO dto) {
        authenticationService.verifyCustomerOwnership(dto.customerId());
        findOperatingHourOfReservation(dto.restaurantId(), dto.reservationDateTime());
        DiningArea diningArea =
                findAvailableDiningArea(
                        dto.restaurantId(),
                        dto.partySize(),
                        dto.reservationDateTime()
                );
        return reservationRepository.save(createReservation(dto, diningArea));
    }

    private Reservation findById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new BadRequestException(
                        RESERVATION_NOT_FOUND_BY_ID.params(id.toString()).getMessage())
                );
    }

    public List<Reservation> findByRestaurantId(Long restaurantId) {
        authenticationService.verifyRestaurantOwnership(restaurantId);
        List<Reservation> reservations = reservationRepository.findByRestaurantId(restaurantId);
        reservations.sort(Collections.reverseOrder());
        return reservations;
    }

    public Reservation restaurantConfirmReservation(Long reservationId) {
        // TODO: Notify customer
        Reservation reservation = findById(reservationId);
        authenticationService.verifyRestaurantOwnership(reservation.getRestaurantId());
        reservation.setStatus(CONFIRMED);
        return reservationRepository.save(reservation);
    }

    public Reservation cancelReservation(Long reservationId) {
        verifyOwnershipCancelReservation(reservationId);
        // TODO: Notify customer
        Reservation reservation = findById(reservationId);
        reservation.setStatus(CANCELLED);
        createNewReservationFromWaitList(reservation);
        return reservationRepository.save(reservation);
    }

    private void verifyOwnershipCancelReservation(Long reservationId) {
        Reservation reservation = findById(reservationId);
        if(authenticationService.findUserByBearer().getAuthorities().equals("ROLE_CUSTOMER")){
            authenticationService.verifyCustomerOwnership(reservation.getCustomerId());
        } else {
            authenticationService.verifyRestaurantOwnership(reservation.getRestaurantId());
        }
    }

    public Reservation noShowReservation(Long reservationId) {
        // TODO: Notify customer
        Reservation reservation = findById(reservationId);
        authenticationService.verifyRestaurantOwnership(reservation.getRestaurantId());
        reservation.setStatus(NO_SHOW);
        createNewReservationFromWaitList(reservation);
        return reservationRepository.save(reservation);
    }

    public Reservation completeReservation(Long reservationId) {
        // Reservation is completed when customer arrives in the dining area, so dining area becomes occupied
        Reservation reservation = findById(reservationId);
        authenticationService.verifyRestaurantOwnership(reservation.getRestaurantId());
        reservation.setStatus(COMPLETED);
        diningAreaService.occupyDiningArea(reservation.getDiningAreaId());
        return reservationRepository.save(reservation);
    }

    public Reservation changeReservationDateTime(Long reservationId, LocalDateTime reservationTime) {
        Reservation reservation = findById(reservationId);
        authenticationService.verifyCustomerOwnership(reservation.getCustomerId());
        if (reservation.getReservationDateTime().toLocalDate().equals(reservationTime.toLocalDate())) { // Same day
            reservation.setReservationDateTime(reservationTime);
            return reservation;
        } else {
            DiningArea newDiningArea =
                    findAvailableDiningArea(
                            reservation.getRestaurantId(),
                            reservation.getPartySize(),
                            reservationTime);
            reservation.setDiningAreaId(newDiningArea.getId());
            reservation.setReservationDateTime(reservationTime);
            return reservationRepository.save(reservation);
        }
    }

    public Reservation changePartySize(Long reservationId, Integer partySize) {
        Reservation reservation = findById(reservationId);
        authenticationService.verifyCustomerOwnership(reservation.getCustomerId());
        DiningArea actualDiningArea = diningAreaService.findById(reservation.getDiningAreaId());
        if (actualDiningArea.getCapacity() < partySize) {
            DiningArea newDiningArea = findAvailableDiningArea(
                    reservation.getRestaurantId(),
                    partySize,
                    reservation.getReservationDateTime());
            reservation.setDiningAreaId(newDiningArea.getId());
        }
        reservation.setPartySize(partySize);
        return reservationRepository.save(reservation);
    }

    // Helper Methods

    private OperatingHour findOperatingHourOfReservation(Long restaurantId, LocalDateTime reservationDateTime) {
        Restaurant restaurant = restaurantService.findById(restaurantId);
        LocalTime reservationTime = reservationDateTime.toLocalTime();

        return restaurant.getOperatingHours().stream()
                .filter(operatingHour -> operatingHour.getWeekDay().equals(reservationDateTime.getDayOfWeek())
                        && operatingHour.getOpening().minusMinutes(1).isBefore(reservationTime)
                        && operatingHour.getClosing().plusMinutes(1).isAfter(reservationTime))
                .findFirst()
                .orElseThrow(() -> new BadRequestException(
                        RESERVATION_TIME_UNAVAILABLE
                                .params(
                                        reservationDateTime.getDayOfWeek().toString(),
                                        reservationTime.toString())
                                .getMessage()));
    }

    private DiningArea findAvailableDiningArea(Long restaurantId, Integer partySize, LocalDateTime reservationDateTime) {
        List<DiningArea> diningAreas = diningAreaService.findByRestaurantId(restaurantId);

        // Check if the reservation requires a full table
        boolean requiresFullTable = findOperatingHourOfReservation(restaurantId, reservationDateTime).isFullTable();

        // Filter dining areas based on capacity and availability
        List<DiningArea> availableDiningAreas = diningAreas.stream()
                .filter(diningArea -> diningArea.getCapacity() >= partySize)
                .filter(diningArea -> !verifyDiningAreaAlreadyReserved(diningArea, reservationDateTime))
                .toList();

        // If reservation requires a full table, filter based on exact capacity
        if (requiresFullTable) {
            return availableDiningAreas.stream()
                    .filter(diningArea -> diningArea.getCapacity().equals(partySize))
                    .findFirst()
                    .orElseThrow(() -> new BadRequestException(NO_DINING_AREA_WITH_PARTY_SIZE.getMessage()));
        }

        if (availableDiningAreas.isEmpty()) {
            throw new BadRequestException(NO_DINING_AREA_AVAILABLE.getMessage());
        }

        // Return the first available dining area
        return availableDiningAreas.get(0);
    }

    private boolean verifyDiningAreaAlreadyReserved(DiningArea diningArea, LocalDateTime reservationTime) {
        List<Reservation> reservations = reservationRepository.findByDiningAreaId(diningArea.getId());
        OperatingHour operatingHourDesired = findOperatingHourOfReservation(diningArea.getRestaurantId(), reservationTime);
        reservations = reservations.stream()
                .filter(reservation -> reservation.getReservationDateTime().toLocalDate().equals(reservationTime.toLocalDate()))
                .filter(reservation ->
                        operatingHourDesired.equals(
                                findOperatingHourOfReservation(
                                        reservation.getRestaurantId(),
                                        reservation.getReservationDateTime()
                                )
                        )
                )
                .toList();
        return !reservations.isEmpty();
    }

    private Reservation createReservation(ReservationDTO dto, DiningArea diningArea) {
        verifyCustomerAndRestaurantExist(dto);
        return new Reservation(dto, diningArea.getId());
    }

    private void verifyCustomerAndRestaurantExist(ReservationDTO dto) {
        customerService.findById(dto.customerId());
        restaurantService.findById(dto.restaurantId());
    }

    public void createNewReservationFromWaitList(Reservation reservation) {
        OperatingHour operatingHour = findOperatingHourOfReservation(reservation.getRestaurantId(), reservation.getReservationDateTime());
        waitListService.checkWaitList(reservation.getDiningAreaId(), operatingHour);
    }

}
