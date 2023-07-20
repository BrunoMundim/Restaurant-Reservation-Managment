package br.com.mundim.RestaurantReservationManagment.service;

import br.com.mundim.RestaurantReservationManagment.exceptions.BadRequestException;
import br.com.mundim.RestaurantReservationManagment.model.dto.DiningAreaDTO;
import br.com.mundim.RestaurantReservationManagment.model.entity.DiningArea;
import br.com.mundim.RestaurantReservationManagment.model.entity.OperatingHour;
import br.com.mundim.RestaurantReservationManagment.model.entity.Reservation;
import br.com.mundim.RestaurantReservationManagment.model.entity.Restaurant;
import br.com.mundim.RestaurantReservationManagment.repository.DiningAreaRepository;
import br.com.mundim.RestaurantReservationManagment.repository.ReservationRepository;
import br.com.mundim.RestaurantReservationManagment.security.AuthenticationService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

import static br.com.mundim.RestaurantReservationManagment.exceptions.config.BaseErrorMessage.DINING_AREA_NOT_FOUND_BY_ID;
import static br.com.mundim.RestaurantReservationManagment.model.entity.DiningArea.Availability.*;

@Service
public class DiningAreaService {

    private final DiningAreaRepository diningAreaRepository;
    private final ReservationRepository reservationRepository;
    private final RestaurantService restaurantService;
    private final WaitListService waitListService;
    private final AuthenticationService authenticationService;

    public DiningAreaService(
            DiningAreaRepository diningAreaRepository,
            ReservationRepository reservationRepository,
            RestaurantService restaurantService,
            WaitListService waitListService, AuthenticationService authenticationService) {
        this.diningAreaRepository = diningAreaRepository;
        this.reservationRepository = reservationRepository;
        this.restaurantService = restaurantService;
        this.waitListService = waitListService;
        this.authenticationService = authenticationService;
    }

    // CRUD Operations

    public DiningArea create(DiningAreaDTO dto) {
        DiningArea diningArea = diningAreaRepository.save(new DiningArea(dto));
        OperatingHour operatingHour = findOperatingHourNow(diningArea);
        if(operatingHour != null){
            waitListService.checkWaitList(diningArea.getId(), operatingHour);
        }
        return diningArea;
    }

    public DiningArea findById(Long id) {
        DiningArea diningArea = diningAreaRepository.findById(id)
                .orElseThrow(() -> new BadRequestException(
                        DINING_AREA_NOT_FOUND_BY_ID.params(id.toString()).getMessage())
                );
        authenticationService.verifyRestaurantOwnership(diningArea.getRestaurantId());
        verifyReservations(diningArea, findOperatingHourNow(diningArea));
        return diningArea;
    }

    public List<DiningArea> findByRestaurantId(Long restaurantId) {
        authenticationService.verifyRestaurantOwnership(restaurantId);
        List<DiningArea> diningAreas = diningAreaRepository.findByRestaurantId(restaurantId);
        diningAreas.sort(Collections.reverseOrder());
        if(diningAreas.size()>0) {
            diningAreas.forEach(diningArea -> verifyReservations(diningArea, findOperatingHourNow(diningAreas.get(0))));
        }
        return diningAreas;
    }

    public DiningArea update(Long diningAreaId, DiningAreaDTO dto) {
        DiningArea diningArea = findById(diningAreaId);
        authenticationService.verifyRestaurantOwnership(diningArea.getRestaurantId());

        diningArea.setDiningAreaName(dto.diningAreaName());
        diningArea.setCapacity(dto.capacity());

        return diningAreaRepository.save(diningArea);
    }

    public DiningArea deleteById(Long diningAreaId) {
        DiningArea diningArea = findById(diningAreaId);
        authenticationService.verifyRestaurantOwnership(diningArea.getRestaurantId());
        diningAreaRepository.deleteById(diningAreaId);
        return diningArea;
    }

    // Availability Operations

    public DiningArea freeDiningArea(Long diningAreaId) {
        DiningArea diningArea = findById(diningAreaId);
        authenticationService.verifyRestaurantOwnership(diningArea.getRestaurantId());
        diningArea.setAvailability(AVAILABLE);
        verifyReservations(diningArea, findOperatingHourNow(diningArea));
        OperatingHour operatingHour = findOperatingHourNow(diningArea);
        waitListService.checkWaitList(diningAreaId, operatingHour);
        return diningAreaRepository.save(diningArea);
    }

    public DiningArea occupyDiningArea(Long diningAreaId) {
        DiningArea diningArea = findById(diningAreaId);
        authenticationService.verifyRestaurantOwnership(diningArea.getRestaurantId());
        diningArea.setAvailability(OCCUPIED);
        return diningAreaRepository.save(diningArea);
    }

    public void reserveDiningArea(Long diningAreaId) {
        DiningArea diningArea = findById(diningAreaId);
        authenticationService.verifyRestaurantOwnership(diningArea.getRestaurantId());
        diningArea.setAvailability(RESERVED);
        diningAreaRepository.save(diningArea);
    }

    // Helper Methods

    private void verifyReservations(DiningArea diningArea, OperatingHour operatingHour) {
        if(operatingHour != null){
            List<Reservation> reservations = findTodayReservations(diningArea);
            for(Reservation reservation:reservations) {
                if(reservationIsAfterOpening(reservation.getReservationDateTime(), operatingHour .getOpening())
                        && reservationIsBeforeClosing(reservation.getReservationDateTime(), operatingHour.getClosing())
                        && reservation.getStatus() != Reservation.ReservationStatus.COMPLETED
                        && reservation.getStatus() != Reservation.ReservationStatus.CANCELLED
                        && reservation.getStatus() != Reservation.ReservationStatus.NO_SHOW) {
                    reserveDiningArea(diningArea.getId());
                }
            }
        }
    }

    private OperatingHour findOperatingHourNow(DiningArea diningArea) {
        Restaurant restaurant = restaurantService.findById(diningArea.getRestaurantId());
        return restaurant
                .getOperatingHours()
                .stream()
                .filter(oh -> oh.getWeekDay().equals(LocalDateTime.now().getDayOfWeek()))
                .filter(oh -> oh.getOpening().minusMinutes(1).isBefore(LocalTime.now()))
                .filter(oh -> oh.getClosing().plusMinutes(1).isAfter(LocalTime.now()))
                .findFirst()
                .orElse(null);
    }

    private List<Reservation> findTodayReservations(DiningArea diningArea) {
        return reservationRepository.findByDiningAreaId(diningArea.getId())
                .stream()
                .filter(reservation -> reservation.getReservationDateTime().toLocalDate().isEqual(LocalDate.now()))
                .toList();
    }

    private boolean reservationIsAfterOpening(LocalDateTime reservationDateTime, LocalTime opening) {
        return opening.minusMinutes(1).isBefore(reservationDateTime.toLocalTime());
    }

    private boolean reservationIsBeforeClosing(LocalDateTime reservationDateTime, LocalTime closing) {
        return closing.minusMinutes(1).isBefore(reservationDateTime.toLocalTime());
    }

}
