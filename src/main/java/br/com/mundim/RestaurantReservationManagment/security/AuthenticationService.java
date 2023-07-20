package br.com.mundim.RestaurantReservationManagment.security;

import br.com.mundim.RestaurantReservationManagment.exceptions.BadRequestException;
import br.com.mundim.RestaurantReservationManagment.exceptions.UnauthorizedException;
import br.com.mundim.RestaurantReservationManagment.model.entity.Customer;
import br.com.mundim.RestaurantReservationManagment.model.entity.Restaurant;
import br.com.mundim.RestaurantReservationManagment.repository.CustomerRepository;
import br.com.mundim.RestaurantReservationManagment.repository.RestaurantRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static br.com.mundim.RestaurantReservationManagment.exceptions.config.BaseErrorMessage.*;

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

    public void verifyEmailAvailability(String email) {
        UserDetails user = loadUserByUsername(email);
        if (user != null) {
            throw new BadRequestException(DUPLICATED_EMAIL.getMessage());
        }
    }

    public UserDetails findUserByBearer() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return loadUserByUsername(authentication.getName());
    }

    public void verifyRestaurantOwnership(Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElse(null);
        if (restaurant != null) {
            UserDetails user = findUserByBearer();
            if (!user.getUsername().equals(restaurant.getEmail()) && !user.getAuthorities().contains("ROLE_ADMIN"))
                throw new UnauthorizedException(UNAUTHORIZED_RESTAURANT_EXCEPTION.getMessage());
        }
    }

    public void verifyCustomerOwnership(Long customerId) {
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if (customer != null) {
            UserDetails user = findUserByBearer();
            if (!user.getUsername().equals(customer.getEmail()) && !user.getAuthorities().contains("ROLE_ADMIN"))
                throw new UnauthorizedException(UNAUTHORIZED_CUSTOMER_EXCEPTION.getMessage());
        }
    }

}
