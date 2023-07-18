package br.com.mundim.RestaurantReservationManagment.controller;

import br.com.mundim.RestaurantReservationManagment.model.dto.RestaurantDTO;
import br.com.mundim.RestaurantReservationManagment.model.entity.Restaurant;
import br.com.mundim.RestaurantReservationManagment.service.RestaurantService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/restaurant")
public class RestaurantController {

    private final RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @PostMapping
    @Operation(tags = "RESTAURANT", summary = "Create a restaurant")
    public ResponseEntity<Restaurant> create(@RequestBody RestaurantDTO dto) {
        return new ResponseEntity<Restaurant>(restaurantService.create(dto), CREATED);
    }

}
