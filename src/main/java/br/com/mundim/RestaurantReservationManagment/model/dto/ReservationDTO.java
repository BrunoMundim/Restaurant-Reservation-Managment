package br.com.mundim.RestaurantReservationManagment.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record ReservationDTO(
        @Schema(defaultValue = "1") Long customerId,
        @Schema(defaultValue = "1") Long restaurantId,
        @Schema(defaultValue = "2023-07-19T12:00:00") LocalDateTime reservationDateTime,
        @Schema(defaultValue = "2") Integer partySize,
        @Schema(defaultValue = "Romantic Lunch") String notes
) {
}
