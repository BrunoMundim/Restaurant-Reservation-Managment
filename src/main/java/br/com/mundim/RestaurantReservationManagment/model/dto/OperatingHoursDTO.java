package br.com.mundim.RestaurantReservationManagment.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import static br.com.mundim.RestaurantReservationManagment.model.entity.OperatingHours.WeekDay;

import java.time.LocalTime;

public record OperatingHoursDTO(
        @Schema(defaultValue = "MONDAY") WeekDay weekDay,
        @Schema(defaultValue = "12:00:00") LocalTime opening,
        @Schema(defaultValue = "16:00:00") LocalTime closing
) {
}
