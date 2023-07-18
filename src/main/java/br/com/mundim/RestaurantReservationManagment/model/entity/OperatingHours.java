package br.com.mundim.RestaurantReservationManagment.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.time.LocalTime;

@Entity
@Table(name = "opening-hours")
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

    @NotEmpty
    private WeekDay weekDay;

    @NotEmpty
    private LocalTime opening;

    @NotEmpty
    private LocalTime closing;

}
