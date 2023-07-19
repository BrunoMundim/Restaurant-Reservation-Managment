package br.com.mundim.RestaurantReservationManagment.model.view;

import br.com.mundim.RestaurantReservationManagment.model.entity.OperatingHours;
import lombok.Data;

import java.time.DayOfWeek;

@Data
public class OperatingHoursView {

    private DayOfWeek weekDay;
    private String opening;
    private String closing;

    public OperatingHoursView(OperatingHours operatingHours) {
        this.weekDay = operatingHours.getWeekDay();
        this.opening = operatingHours.getOpening().toString();
        this.closing = operatingHours.getClosing().toString();
    }

}
