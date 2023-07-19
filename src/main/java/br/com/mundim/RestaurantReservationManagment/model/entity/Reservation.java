package br.com.mundim.RestaurantReservationManagment.model.entity;

import br.com.mundim.RestaurantReservationManagment.model.dto.ReservationDTO;
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
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long customerId;

    @NotNull
    private Long restaurantId;

    @NotNull
    private Long diningAreaId;

    @NotNull
    private LocalDateTime reservationDateTime;

    @Positive
    private Integer partySize;

    private ReservationStatus status;

    private String notes;

    public Reservation(ReservationDTO dto, Long diningAreaId) {
        this.customerId = dto.customerId();
        this.restaurantId = dto.restaurantId();
        this.diningAreaId = diningAreaId;
        this.reservationDateTime = dto.reservationDateTime();
        this.partySize = dto.partySize();
        this.notes = dto.notes();
        this.status = ReservationStatus.PENDING;
    }

}
