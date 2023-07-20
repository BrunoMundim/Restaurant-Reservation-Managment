package br.com.mundim.RestaurantReservationManagment.model.entity;

import br.com.mundim.RestaurantReservationManagment.model.dto.WaitListDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@Entity
@Table(name = "wait_list")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class WaitList implements Comparable<WaitList> {

    public enum WaitListStatus {
        WAITING,
        NOTIFIED,
        CANCELLED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long customerId;

    @NotNull
    private Long restaurantId;

    @Positive
    private Integer partySize;

    @NotNull
    private LocalDateTime reservationDateTime;

    @NotNull
    private LocalDateTime joinedWaitList;

    @NotNull
    private WaitListStatus waitListStatus;

    private String notes;

    public WaitList(WaitListDTO dto) {
        this.customerId = dto.customerId();
        this.restaurantId = dto.restaurantId();
        this.partySize = dto.partySize();
        this.reservationDateTime = dto.reservationDateTime();
        this.notes = dto.notes();
        this.joinedWaitList = LocalDateTime.now();
        this.waitListStatus = WaitListStatus.WAITING;
    }

    @Override
    public int compareTo(WaitList o) {
        return this.joinedWaitList.compareTo(o.getJoinedWaitList());
    }

}
