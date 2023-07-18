package br.com.mundim.RestaurantReservationManagment.model.entity;

import br.com.mundim.RestaurantReservationManagment.model.dto.OperatingHoursDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalTime;

@Entity
@Table(name = "opening_hours")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class OperatingHours {

    public enum WeekDay {
        MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private WeekDay weekDay;

    @NotNull
    private LocalTime opening;

    @NotNull
    private LocalTime closing;

    public OperatingHours(OperatingHoursDTO dto) {
        this.weekDay = dto.weekDay();
        this.opening = LocalTime.parse(dto.opening());
        this.closing = LocalTime.parse(dto.closing());
    }
}
