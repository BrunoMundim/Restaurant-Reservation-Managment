package br.com.mundim.RestaurantReservationManagment.service;

import br.com.mundim.RestaurantReservationManagment.exceptions.BadRequestException;
import br.com.mundim.RestaurantReservationManagment.model.dto.DiningAreaDTO;
import br.com.mundim.RestaurantReservationManagment.model.entity.DiningArea;
import br.com.mundim.RestaurantReservationManagment.model.entity.OperatingHour;
import br.com.mundim.RestaurantReservationManagment.model.entity.Reservation;
import br.com.mundim.RestaurantReservationManagment.repository.DiningAreaRepository;
import br.com.mundim.RestaurantReservationManagment.repository.ReservationRepository;
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

    public DiningAreaService(
            DiningAreaRepository diningAreaRepository,
            ReservationRepository reservationRepository,
            RestaurantService restaurantService
    ) {
        this.diningAreaRepository = diningAreaRepository;
        this.reservationRepository = reservationRepository;
        this.restaurantService = restaurantService;
    }

    public DiningArea create(DiningAreaDTO dto) {
        return diningAreaRepository.save(new DiningArea(dto));
    }

    public DiningArea findById(Long id) {
        DiningArea diningArea = diningAreaRepository.findById(id)
                .orElseThrow(() -> new BadRequestException(
                        DINING_AREA_NOT_FOUND_BY_ID.params(id.toString()).getMessage())
                );
        verifyReservations(diningArea, findOperatingHourNow(diningArea));
        return diningArea;
    }

    public List<DiningArea> findByRestaurantId(Long restaurantId) {
        List<DiningArea> diningAreas = diningAreaRepository.findByRestaurantId(restaurantId);
        diningAreas.sort(Collections.reverseOrder());
        if(diningAreas.size()>0) {
            diningAreas.forEach(diningArea -> verifyReservations(diningArea, findOperatingHourNow(diningAreas.get(0))));
        }
        return diningAreas;
    }

    private void verifyReservations(DiningArea diningArea, OperatingHour operatingHour) {
        if(operatingHour != null){
            List<Reservation> reservations = findTodayReservations(diningArea);
            for(Reservation reservation:reservations) {
                if(reservationIsAfterOpening(reservation.getReservationDateTime(), operatingHour .getOpening())
                        && reservationIsBeforeClosing(reservation.getReservationDateTime(), operatingHour.getClosing())) {
                    reserveDiningArea(diningArea.getId());
                }
            }
        }
    }

    private OperatingHour findOperatingHourNow(DiningArea diningArea) {
        return restaurantService.findById(diningArea.getRestaurantId())
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

    public DiningArea update(Long diningAreaId, DiningAreaDTO dto) {
        DiningArea diningArea = findById(diningAreaId);

        diningArea.setDiningAreaName(dto.diningAreaName());
        diningArea.setCapacity(dto.capacity());

        return diningAreaRepository.save(diningArea);
    }

    public DiningArea deleteById(Long diningAreaId) {
        DiningArea diningArea = findById(diningAreaId);
        diningAreaRepository.deleteById(diningAreaId);
        return diningArea;
    }

    public DiningArea freeDiningArea(Long diningAreaId) {
        DiningArea diningArea = findById(diningAreaId);
        diningArea.setAvailability(AVAILABLE);
        return diningAreaRepository.save(diningArea);
    }

    public DiningArea occupyDiningArea(Long diningAreaId) {
        DiningArea diningArea = findById(diningAreaId);
        diningArea.setAvailability(OCCUPIED);
        return diningAreaRepository.save(diningArea);
    }

    public DiningArea reserveDiningArea(Long diningAreaId) {
        DiningArea diningArea = findById(diningAreaId);
        diningArea.setAvailability(RESERVED);
        return diningAreaRepository.save(diningArea);
    }

}
