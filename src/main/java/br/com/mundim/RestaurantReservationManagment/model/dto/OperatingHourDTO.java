package br.com.mundim.RestaurantReservationManagment.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.DayOfWeek;

public record OperatingHourDTO(
        @Schema(defaultValue = "MONDAY") DayOfWeek weekDay,
        @Schema(defaultValue = "12:00:00") String opening,
        @Schema(defaultValue = "16:00:00") String closing
) {
}
