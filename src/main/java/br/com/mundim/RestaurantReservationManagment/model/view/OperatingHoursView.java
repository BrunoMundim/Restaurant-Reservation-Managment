package br.com.mundim.RestaurantReservationManagment.model.view;

import br.com.mundim.RestaurantReservationManagment.model.entity.OperatingHours;
import lombok.Data;

@Data
public class OperatingHoursView {

    private OperatingHours.WeekDay weekDay;
    private String opening;
    private String closing;

    public OperatingHoursView(OperatingHours operatingHours) {
        this.weekDay = operatingHours.getWeekDay();
        this.opening = operatingHours.getOpening().toString();
        this.closing = operatingHours.getClosing().toString();
    }

}
