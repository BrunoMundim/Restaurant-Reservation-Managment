package br.com.mundim.RestaurantReservationManagment.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservation")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Reservation {

    public enum ReservationStatus {
        PENDING,    // The reservation is awaiting confirmation from the restaurant.
        CONFIRMED,  // The reservation has been successfully confirmed by the restaurant.
        CANCELLED,  // The reservation has been cancelled, either by the customer or the restaurant.
        COMPLETED,  // The reservation has been successfully completed and the dining experience has concluded.
        NO_SHOW,    // The customer did not show up for the reservation without cancelling.
        SEATED,     // The reservation is currently in progress, indicating that the customer is currently dining.
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long customerId;

    @NotNull
    private Long restaurantId;

    @NotNull
    private Long tableId;

    @NotNull
    private LocalDateTime reservationDateTime;

    @Positive
    private Integer partySize;

    private ReservationStatus status;

    private String notes;

}
