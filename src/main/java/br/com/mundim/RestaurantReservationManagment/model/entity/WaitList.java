package br.com.mundim.RestaurantReservationManagment.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@Entity
@Table(name = "wait-list")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class WaitList {

    public enum WaitListStatus {
        WAITING,
        NOTIFIED,
        CANCELLED,
        DECLINED_SEATING
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    private Long customerId;

    @NotEmpty
    private Long restaurantId;

    @Positive
    private Integer partySize;

    @NotEmpty
    private LocalDateTime joinedWaitList;

    @NotEmpty
    private WaitListStatus waitListStatus;


}
