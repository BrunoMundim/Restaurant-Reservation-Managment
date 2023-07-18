package br.com.mundim.RestaurantReservationManagment.model.entity;

import br.com.mundim.RestaurantReservationManagment.model.dto.OperatingHoursDTO;
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

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "address_id", unique = true)
    private Address address;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "restaurant_id")
    @NotNull
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

    @NotEmpty
    private String cellphone;

    public Restaurant(Address address, List<OperatingHours> operatingHours, RestaurantDTO dto) {
        this.address = address;
        this.operatingHours = operatingHours;
        this.cnpj = dto.cnpj();
        this.name = dto.name();
        this.email = dto.email();
        this.password = passwordEncoder().encode(dto.password());
        this.cellphone = dto.cellphone();
    }

    public void setPassword(String password) {
        this.password = passwordEncoder().encode(password);
    }

    public void setOperatingHours(List<OperatingHours> operatingHours) {
        this.operatingHours.clear();
        this.operatingHours.addAll(operatingHours);
    }

}
