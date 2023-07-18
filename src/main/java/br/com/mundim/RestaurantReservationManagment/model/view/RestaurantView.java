package br.com.mundim.RestaurantReservationManagment.model.view;

import br.com.mundim.RestaurantReservationManagment.model.entity.Address;
import br.com.mundim.RestaurantReservationManagment.model.entity.OperatingHours;
import br.com.mundim.RestaurantReservationManagment.model.entity.Restaurant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RestaurantView {

    private Long id;
    private String cnpj;
    private String name;
    private String email;
    private String cellphone;

    // Address
    private String cep;
    private String street;
    private String number;
    private String addressLine2;
    private String city;
    private String state;

    private List<OperatingHoursView> operatingHoursList;

    public RestaurantView(Restaurant restaurant) {
        this.id = restaurant.getId();
        this.cnpj = restaurant.getCnpj();
        this.name = restaurant.getName();
        this.email = restaurant.getEmail();
        this.cellphone = restaurant.getCellphone();

        this.cep = restaurant.getAddress().getCep();
        this.street = restaurant.getAddress().getStreet();
        this.number = restaurant.getAddress().getNumber();
        this.addressLine2 = restaurant.getAddress().getAddressLine2();
        this.city = restaurant.getAddress().getCity();
        this.state = restaurant.getAddress().getState();

        this.operatingHoursList = restaurant.getOperatingHours()
                .stream()
                .map(OperatingHoursView::new)
                .collect(Collectors.toList());
    }

}
