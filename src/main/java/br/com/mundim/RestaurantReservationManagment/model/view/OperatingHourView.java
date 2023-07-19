package br.com.mundim.RestaurantReservationManagment.model.view;

import br.com.mundim.RestaurantReservationManagment.model.entity.OperatingHour;
import lombok.Data;

import java.time.DayOfWeek;

@Data
public class OperatingHourView {

    private DayOfWeek weekDay;
    private String opening;
    private String closing;

    public OperatingHourView(OperatingHour operatingHour) {
        this.weekDay = operatingHour.getWeekDay();
        this.opening = operatingHour.getOpening().toString();
        this.closing = operatingHour.getClosing().toString();
    }

}
