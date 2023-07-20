package br.com.mundim.RestaurantReservationManagment.security;

import br.com.mundim.RestaurantReservationManagment.exceptions.BadRequestException;
import br.com.mundim.RestaurantReservationManagment.exceptions.config.BaseErrorMessage;
import br.com.mundim.RestaurantReservationManagment.repository.CustomerRepository;
import br.com.mundim.RestaurantReservationManagment.repository.RestaurantRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

import static br.com.mundim.RestaurantReservationManagment.exceptions.config.BaseErrorMessage.DUPLICATED_EMAIL;

@Service
public class AuthenticationService implements UserDetailsService {

    private final RestaurantRepository restaurantRepository;
    private final CustomerRepository customerRepository;

    public AuthenticationService(RestaurantRepository restaurantRepository, CustomerRepository customerRepository) {
        this.restaurantRepository = restaurantRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserDetails user = restaurantRepository.findByEmail(email);
        if (user == null) {
            user = customerRepository.findByEmail(email);
        }
        return user;
    }

    public UserDetails findUserByEmail(String email) {
        UserDetails user = restaurantRepository.findByEmail(email);
        if(user == null)
            user = customerRepository.findByEmail(email);
        return user;
    }

    public void verifyEmailAvailability(String email) {
        UserDetails restaurant = restaurantRepository.findByEmail(email);
        UserDetails customer = customerRepository.findByEmail(email);
        if(restaurant != null ||customer != null) {
            throw new BadRequestException(DUPLICATED_EMAIL.getMessage());
        }
    }

}
