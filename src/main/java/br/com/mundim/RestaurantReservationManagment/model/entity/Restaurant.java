package br.com.mundim.RestaurantReservationManagment.model.entity;

import br.com.mundim.RestaurantReservationManagment.model.dto.RestaurantDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CNPJ;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "restaurant")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Restaurant implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "address_id", unique = true)
    private Address address;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "restaurant_id")
    @NotNull
    private List<OperatingHour> operatingHours;

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

    @NotEmpty
    private String role;

    public Restaurant(Address address, List<OperatingHour> operatingHours, RestaurantDTO dto, String password) {
        this.address = address;
        this.operatingHours = operatingHours;
        this.cnpj = dto.cnpj();
        this.name = dto.name();
        this.email = dto.email();
        this.password = password;
        this.cellphone = dto.cellphone();
        this.role = "RESTAURANT";
    }

    public void setOperatingHours(List<OperatingHour> operatingHours) {
        this.operatingHours.clear();
        this.operatingHours.addAll(operatingHours);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + this.role));
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


}
