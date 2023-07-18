package br.com.mundim.RestaurantReservationManagment.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import static br.com.mundim.RestaurantReservationManagment.model.entity.OperatingHours.WeekDay;

public record OperatingHoursDTO(
        @Schema(defaultValue = "MONDAY") WeekDay weekDay,
        @Schema(defaultValue = "12:00:00") String opening,
        @Schema(defaultValue = "16:00:00") String closing
) {
}
