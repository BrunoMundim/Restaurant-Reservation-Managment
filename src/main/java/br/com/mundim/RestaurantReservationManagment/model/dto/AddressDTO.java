package br.com.mundim.RestaurantReservationManagment.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record AddressDTO(
        @Schema(defaultValue = "38400-025") String cep,
        @Schema(defaultValue = "Rua dos Anjos") String street,
        @Schema(defaultValue = "1000") String number,
        @Schema(defaultValue = "Apto. 103") String addressLine2,
        @Schema(defaultValue = "Uberlândia") String city,
        @Schema(defaultValue = "Minas Gerais") String state
) {
}
