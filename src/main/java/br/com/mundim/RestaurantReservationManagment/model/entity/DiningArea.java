package br.com.mundim.RestaurantReservationManagment.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;

@Entity
@Table(name = "dining-area")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class DiningArea {

    public enum Availability {
        AVAILABLE, RESERVED, OCCUPIED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    private Long restaurantId;

    @NotEmpty
    private String tableIdentification;

    @Positive
    private Integer capacity;

    @NotEmpty
    private Availability availability;

}
