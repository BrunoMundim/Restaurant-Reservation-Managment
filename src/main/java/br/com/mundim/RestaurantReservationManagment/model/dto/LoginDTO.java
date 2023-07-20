package br.com.mundim.RestaurantReservationManagment.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record LoginDTO(
        @Schema(defaultValue = "bruno@email.com") String email,
        @Schema(defaultValue = "password") String password
) {
}
