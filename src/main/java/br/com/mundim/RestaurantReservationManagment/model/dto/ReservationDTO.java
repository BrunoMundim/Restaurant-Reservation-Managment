package br.com.mundim.RestaurantReservationManagment.model.dto;

import java.time.LocalDateTime;

public record ReservationDTO(
        Long customerId,
        Long restaurantId,
        LocalDateTime reservationDateTime,
        Integer partySize,
        String notes
) {
}
