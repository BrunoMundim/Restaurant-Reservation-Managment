package br.com.mundim.RestaurantReservationManagment.service;

import br.com.mundim.RestaurantReservationManagment.exceptions.BadRequestException;
import br.com.mundim.RestaurantReservationManagment.model.dto.ReservationDTO;
import br.com.mundim.RestaurantReservationManagment.model.entity.DiningArea;
import br.com.mundim.RestaurantReservationManagment.model.entity.Reservation;
import br.com.mundim.RestaurantReservationManagment.model.entity.Restaurant;
import br.com.mundim.RestaurantReservationManagment.repository.ReservationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static br.com.mundim.RestaurantReservationManagment.exceptions.config.BaseErrorMessage.*;
import static br.com.mundim.RestaurantReservationManagment.model.entity.Reservation.ReservationStatus.*;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final RestaurantService restaurantService;
    private final DiningAreaService diningAreaService;
    private final CustomerService customerService;

    public ReservationService(
            ReservationRepository reservationRepository,
            RestaurantService restaurantService,
            DiningAreaService diningAreaService,
            CustomerService customerService
    ) {
        this.reservationRepository = reservationRepository;
        this.restaurantService = restaurantService;
        this.diningAreaService = diningAreaService;
        this.customerService = customerService;
    }

    public Reservation findById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new BadRequestException(
                        RESERVATION_NOT_FOUND_BY_ID.params(id.toString()).getMessage())
                );
    }

    public List<Reservation> findByRestaurantId(Long restaurantId) {
        return reservationRepository.findByRestaurantId(restaurantId);
    }

    public List<Reservation> findByDiningAreaId(Long diningAreaId) {
        return reservationRepository.findByDiningAreaId(diningAreaId);
    }

    public Reservation customerCreateReservation(ReservationDTO dto) {
        // TODO: Notify customer
        verifyIfRestaurantIsOperating(dto);
        DiningArea diningArea =
                findAvailableDiningArea(
                        dto.restaurantId(),
                        dto.partySize(),
                        dto.reservationDateTime()
                );
        return reservationRepository.save(createReservation(dto, diningArea));
    }

    private void verifyIfRestaurantIsOperating(ReservationDTO dto) {
        Restaurant restaurant = restaurantService.findById(dto.restaurantId());
        LocalTime reservationTime = dto.reservationDateTime().toLocalTime();

        boolean isOperating = restaurant.getOperatingHours().stream()
                .anyMatch(operatingHour -> operatingHour.getWeekDay().equals(dto.reservationDateTime().getDayOfWeek())
                        && operatingHour.getOpening().isBefore(reservationTime)
                        && operatingHour.getClosing().isAfter(reservationTime));

        if (!isOperating) {
            throw new BadRequestException(RESERVATION_TIME_UNAVAILABLE.params(reservationTime.toString()).getMessage());
        }
    }

    private DiningArea findAvailableDiningArea(Long restaurantId, Integer partySize, LocalDateTime reservationDateTime) {
        List<DiningArea> diningAreas = diningAreaService.findByRestaurantId(restaurantId);

        Optional<DiningArea> availableDiningArea = diningAreas.stream()
                .filter(diningArea -> diningArea.getCapacity() >= partySize)
                .filter(diningArea -> !verifyDiningAreaAlreadyReserved(diningArea, reservationDateTime))
                .findFirst();

        return availableDiningArea.orElseThrow(() -> new BadRequestException(NO_DINING_AREA_AVAILABLE.getMessage()));
    }

    private boolean verifyDiningAreaAlreadyReserved(DiningArea diningArea, LocalDateTime reservationTime) {
        List<LocalDateTime> reservations = reservationRepository.findReservedDatesByDiningAreaId(diningArea.getId());
        List<LocalDate> reservedDates = reservations.stream()
                .map(LocalDateTime::toLocalDate)
                .toList();
        return reservedDates.contains(reservationTime.toLocalDate());
    }

    private Reservation createReservation(ReservationDTO dto, DiningArea diningArea) {
        verifyCustomerAndRestaurantExist(dto);
        reserveDiningArea(diningArea.getId());
        return new Reservation(dto, diningArea.getId());
    }

    private void verifyCustomerAndRestaurantExist(ReservationDTO dto) {
        customerService.findById(dto.customerId());
        restaurantService.findById(dto.restaurantId());
    }

    private void reserveDiningArea(Long diningAreaId) {
        diningAreaService.reserveDiningArea(diningAreaId);
    }

    private void freeDiningArea(Long diningAreaId) {
        diningAreaService.freeDiningArea(diningAreaId);
    }

    private void occupiedDiningArea(Long diningAreaId) {
        diningAreaService.occupiedDiningArea(diningAreaId);
    }

    public Reservation restaurantConfirmReservation(Long reservationId) {
        // TODO: Notify customer
        Reservation reservation = findById(reservationId);
        reservation.setStatus(CONFIRMED);
        return reservationRepository.save(reservation);
    }

    public Reservation cancelReservation(Long reservationId) {
        // TODO: Notify customer
        Reservation reservation = findById(reservationId);
        reservation.setStatus(CANCELLED);
        freeDiningArea(reservation.getDiningAreaId());
        return reservationRepository.save(reservation);
    }

    public Reservation completeReservation(Long reservationId) {
        // Reservation is completed when customer arrives at the dining area, so dining area becomes occupied
        Reservation reservation = findById(reservationId);
        reservation.setStatus(COMPLETED);
        occupiedDiningArea(reservation.getDiningAreaId());
        return reservationRepository.save(reservation);
    }

    public Reservation noShowReservation(Long reservationId) {
        // TODO: Notify customer
        Reservation reservation = findById(reservationId);
        reservation.setStatus(NO_SHOW);
        freeDiningArea(reservation.getDiningAreaId());
        return reservationRepository.save(reservation);
    }

    public Reservation changeReservationDateTime(Long reservationId, LocalDateTime reservationTime) {
        Reservation reservation = findById(reservationId);
        if(reservation.getReservationDateTime().toLocalDate().equals(reservationTime.toLocalDate())){ // Same day
            reservation.setReservationDateTime(reservationTime);
            return reservation;
        } else {
            DiningArea newDiningArea =
                    findAvailableDiningArea(
                            reservation.getRestaurantId(),
                            reservation.getPartySize(),
                            reservationTime);
            changeDiningArea(reservation, newDiningArea.getId());
            return reservationRepository.save(reservation);
        }
    }

    private void changeDiningArea(Reservation reservation, Long newDiningAreaId) {
        freeDiningArea(reservation.getDiningAreaId());
        reserveDiningArea(newDiningAreaId);
        reservation.setDiningAreaId(newDiningAreaId);
    }

    public Reservation changePartySize(Long reservationId, Integer partySize) {
        Reservation reservation = findById(reservationId);
        freeDiningArea(reservation.getDiningAreaId());
        DiningArea newDiningArea;
        try {
            newDiningArea = findAvailableDiningArea(
                    reservation.getRestaurantId(),
                    partySize,
                    reservation.getReservationDateTime());
        } catch (BadRequestException e) {
            reserveDiningArea(reservation.getDiningAreaId());
            throw e;
        }
        changeDiningArea(reservation, newDiningArea.getId());
        reservation.setPartySize(partySize);
        return reservationRepository.save(reservation);
    }

}
