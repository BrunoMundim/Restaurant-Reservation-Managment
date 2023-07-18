package br.com.mundim.RestaurantReservationManagment.model.entity;

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
    private Long restaurantId;

    @NotEmpty
    private String street;

    @NotEmpty
    private String number;
    private String addressLine2;

    @NotEmpty
    private String city;

    @NotEmpty
    private String state;

    @NotEmpty
    private String cep;
    private String phoneNumber;

}
