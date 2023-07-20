package br.com.mundim.RestaurantReservationManagment.controller;

import br.com.mundim.RestaurantReservationManagment.model.dto.ReservationDTO;
import br.com.mundim.RestaurantReservationManagment.model.entity.Reservation;
import br.com.mundim.RestaurantReservationManagment.service.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/reservation")
@SecurityRequirement(name = "jwt")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    @Operation(tags = "RESERVATION", summary = "Create a reservation")
    public ResponseEntity<Reservation> create(@RequestBody ReservationDTO dto) {
        return new ResponseEntity<>(reservationService.customerCreateReservation(dto), CREATED);
    }

    @GetMapping
    @Operation(tags = "RESERVATION", summary = "Find reservations by Restaurant ID")
    public ResponseEntity<List<Reservation>> findByRestaurantId(@RequestParam Long restaurantId) {
        return ResponseEntity.ok(reservationService.findByRestaurantId(restaurantId));
    }

    @PutMapping("/restaurant-confirm")
    @Operation(tags = "RESERVATION", summary = "Restaurant confirm reservation")
    public ResponseEntity<Reservation> restaurantConfirmReservation(@RequestParam Long reservationId) {
        return ResponseEntity.ok(reservationService.restaurantConfirmReservation(reservationId));
    }

    @PutMapping("/cancel")
    @Operation(tags = "RESERVATION", summary = "Cancel reservation")
    public ResponseEntity<Reservation> cancelReservation(@RequestParam Long reservationId) {
        return ResponseEntity.ok(reservationService.cancelReservation(reservationId));
    }

    @PutMapping("/complete")
    @Operation(tags = "RESERVATION", summary = "Complete reservation")
    public ResponseEntity<Reservation> completeReservation(@RequestParam Long reservationId) {
        return ResponseEntity.ok(reservationService.completeReservation(reservationId));
    }

    @PutMapping("/no-show")
    @Operation(tags = "RESERVATION", summary = "No show reservation")
    public ResponseEntity<Reservation> noShowReservation(@RequestParam Long reservationId) {
        return ResponseEntity.ok(reservationService.noShowReservation(reservationId));
    }

    @PutMapping("/change-date-time")
    @Operation(tags = "RESERVATION", summary = "Change Date Time")
    public ResponseEntity<Reservation> changeReservationDateTime(
            @RequestParam Long reservationId,
            @RequestBody LocalDateTime reservationDateTime
            ) {
        return ResponseEntity.ok(reservationService.changeReservationDateTime(reservationId, reservationDateTime));
    }

    @PutMapping("/change-party-size")
    @Operation(tags = "RESERVATION", summary = "Change party size")
    public ResponseEntity<Reservation> changePartySize(
            @RequestParam Long reservationId, Integer partySize
    ) {
        return ResponseEntity.ok(reservationService.changePartySize(reservationId, partySize));
    }

}
