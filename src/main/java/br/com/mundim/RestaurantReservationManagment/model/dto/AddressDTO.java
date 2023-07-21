package br.com.mundim.RestaurantReservationManagment.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record AddressDTO(
        @Schema(defaultValue = "38408-902") String cep,
        @Schema(defaultValue = "Av. João Naves de Ávila") String street,
        @Schema(defaultValue = "1331") String number,
        @Schema(defaultValue = "Piso 1") String addressLine2,
        @Schema(defaultValue = "Tibery") String district,
        @Schema(defaultValue = "Uberlândia") String city,
        @Schema(defaultValue = "Minas Gerais") String state
) {
}
