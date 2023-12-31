package br.com.mundim.RestaurantReservationManagment.model.entity;

import br.com.mundim.RestaurantReservationManagment.model.dto.ReservationDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "reservation")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Reservation implements Comparable<Reservation> {

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

    public Reservation(WaitList waitList, Long diningAreaId) {
        this.customerId = waitList.getCustomerId();
        this.restaurantId = waitList.getRestaurantId();
        this.diningAreaId = diningAreaId;
        this.reservationDateTime = waitList.getReservationDateTime();
        this.partySize = waitList.getPartySize();
        this.notes = waitList.getNotes();
        this.status = ReservationStatus.PENDING;
    }

    @Override
    public int compareTo(Reservation o) {
        return o.getReservationDateTime().compareTo(this.getReservationDateTime());
    }

}
