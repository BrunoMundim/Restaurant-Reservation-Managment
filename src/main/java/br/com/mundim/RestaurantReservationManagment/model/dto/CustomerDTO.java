package br.com.mundim.RestaurantReservationManagment.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record CustomerDTO(
        @Schema(defaultValue = "Bruno Mundim") String name,
        @Schema(defaultValue = "492.102.860-57") String cpf,
        @Schema(defaultValue = "bruno@email.com") String email,
        @Schema(defaultValue = "password") String password,
        @Schema(defaultValue = "(34) 991970167") String phoneNumber
) {
}
