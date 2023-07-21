package br.com.mundim.RestaurantReservationManagment.model.entity;

import br.com.mundim.RestaurantReservationManagment.model.dto.DiningAreaDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static br.com.mundim.RestaurantReservationManagment.model.entity.DiningArea.Availability.AVAILABLE;

@Entity
@Table(name = "dining_area")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class DiningArea implements Comparable<DiningArea> {

    public enum Availability {
        AVAILABLE, RESERVED, OCCUPIED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long customerId;

    @NotNull
    private Long restaurantId;

    @NotEmpty
    private String diningAreaName;

    @Positive
    private Integer capacity;

    @NotNull
    private Availability availability;

    public DiningArea(DiningAreaDTO dto) {
        this.restaurantId = dto.restaurantId();
        this.diningAreaName = dto.diningAreaName();
        this.capacity = dto.capacity();
        this.availability = AVAILABLE;
    }

    @Override
    public int compareTo(DiningArea other) {
        return other.getCapacity().compareTo(this.getCapacity());
    }

}
