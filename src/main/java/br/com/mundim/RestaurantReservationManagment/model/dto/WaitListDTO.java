package br.com.mundim.RestaurantReservationManagment.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record WaitListDTO(
        @Schema(defaultValue = "1") Long customerId,
        @Schema(defaultValue = "1") Long restaurantId,
        @Schema(defaultValue = "4") Integer partySize,
        LocalDateTime reservationDateTime,
        @Schema(defaultValue = "Waiting notes") String notes
) {
}
