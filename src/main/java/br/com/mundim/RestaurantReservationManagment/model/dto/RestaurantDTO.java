package br.com.mundim.RestaurantReservationManagment.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record RestaurantDTO(
        AddressDTO address,
        List<OperatingHourDTO> operatingHours,
        @Schema(defaultValue = "08.180.283/0001-02") String cnpj,
        @Schema(defaultValue = "Padaria do seu zé") String name,
        @Schema(defaultValue = "email@email.com") String email,
        @Schema(defaultValue = "password") String password,
        @Schema(defaultValue = "(34) 3432-3432") String cellphone
) {
}
