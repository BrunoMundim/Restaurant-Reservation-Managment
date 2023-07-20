package br.com.mundim.RestaurantReservationManagment.model.entity;

import br.com.mundim.RestaurantReservationManagment.model.dto.OperatingHourDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.DayOfWeek;
import java.time.LocalTime;

@Entity
@Table(name = "opening_hours")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class OperatingHour implements Comparable<OperatingHour> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private DayOfWeek weekDay;

    @NotNull
    private LocalTime opening;

    @NotNull
    private LocalTime closing;

    @NotNull
    private boolean fullTable;

    public OperatingHour(OperatingHourDTO dto) {
        this.weekDay = dto.weekDay();
        this.opening = LocalTime.parse(dto.opening());
        this.closing = LocalTime.parse(dto.closing());
        this.fullTable = dto.fullTable();
    }

    @Override
    public int compareTo(OperatingHour o) {
        return this.getOpening().compareTo(o.getOpening());
    }
}
