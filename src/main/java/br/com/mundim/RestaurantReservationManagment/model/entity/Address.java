package br.com.mundim.RestaurantReservationManagment.model.entity;

import br.com.mundim.RestaurantReservationManagment.model.dto.AddressDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "address")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    private String cep;

    @NotEmpty
    private String street;

    @NotEmpty
    private String number;

    private String addressLine2;

    @NotEmpty
    private String city;

    @NotEmpty
    private String state;

    public Address(AddressDTO dto) {
        this.cep = dto.cep();
        this.street = dto.street();
        this.number = dto.number();
        this.addressLine2 = dto.addressLine2();
        this.city = dto.city();
        this.state = dto.state();
    }
}
