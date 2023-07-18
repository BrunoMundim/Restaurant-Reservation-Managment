package br.com.mundim.RestaurantReservationManagment.model.entity;

import br.com.mundim.RestaurantReservationManagment.model.dto.RestaurantDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CNPJ;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "restaurant")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Restaurant {

    @Bean
    private PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long addressId;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "restaurant_id")
    private List<OperatingHours> operatingHours;

    @CNPJ
    @Column(unique = true)
    private String cnpj;

    @NotEmpty
    private String name;

    @Email
    @Column(unique = true)
    @NotEmpty
    private String email;

    @NotEmpty
    private String password;

    public Restaurant(Long addressId, List<OperatingHours> operatingHours, RestaurantDTO dto) {
        this.addressId = addressId;
        this.operatingHours = operatingHours;
        this.cnpj = dto.cnpj();
        this.name = dto.name();
        this.email = dto.email();
        this.password = passwordEncoder().encode(dto.password());
    }

}
