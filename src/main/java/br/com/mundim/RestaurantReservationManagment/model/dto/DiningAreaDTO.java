package br.com.mundim.RestaurantReservationManagment.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record DiningAreaDTO(
        @Schema(defaultValue = "1") Long restaurantId,
        @Schema(defaultValue = "Table 1") String diningAreaName,
        @Schema(defaultValue = "6") Integer capacity
) {
}
