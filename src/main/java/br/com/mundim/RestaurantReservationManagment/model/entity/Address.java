package br.com.mundim.RestaurantReservationManagment.model.entity;

import br.com.mundim.RestaurantReservationManagment.model.dto.AddressDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private String district;

    @NotEmpty
    private String city;

    @NotEmpty
    private String state;

    public Address(AddressDTO dto) {
        this.cep = dto.cep();
        this.street = dto.street();
        this.number = dto.number();
        this.addressLine2 = dto.addressLine2();
        this.district = dto.district();
        this.city = dto.city();
        this.state = dto.state();
    }
}
