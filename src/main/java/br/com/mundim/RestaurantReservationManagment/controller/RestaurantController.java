package br.com.mundim.RestaurantReservationManagment.controller;

import br.com.mundim.RestaurantReservationManagment.model.dto.RestaurantDTO;
import br.com.mundim.RestaurantReservationManagment.model.view.RestaurantView;
import br.com.mundim.RestaurantReservationManagment.service.RestaurantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/restaurant")
@SecurityRequirement(name = "jwt")
public class RestaurantController {

    private final RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @PostMapping
    @Operation(tags = "RESTAURANT", summary = "Create a restaurant")
    public ResponseEntity<RestaurantView> create(@RequestBody RestaurantDTO dto) {
        return new ResponseEntity<RestaurantView>(restaurantService.create(dto), CREATED);
    }

    @GetMapping("/find-all")
    @Operation(tags = "RESTAURANT", summary = "Find all restaurants")
    public ResponseEntity<List<RestaurantView>> findAll() {
        return ResponseEntity.ok(restaurantService.findAll());
    }

    @GetMapping("/find-by-id")
    @Operation(tags = "RESTAURANT", summary = "Find Restaurant by ID")
    public ResponseEntity<RestaurantView> findById(@RequestParam Long restaurantId) {
        return ResponseEntity.ok(restaurantService.findByIdReturnView(restaurantId));
    }

    @PutMapping
    @Operation(tags = "RESTAURANT", summary = "Update Restaurant by ID")
    public ResponseEntity<RestaurantView> update(@RequestParam Long restaurantId, @RequestBody RestaurantDTO dto) {
        return ResponseEntity.ok(restaurantService.update(restaurantId, dto));
    }

    @DeleteMapping
    @Operation(tags = "RESTAURANT", summary = "Delete Restaurant by ID")
    public ResponseEntity<RestaurantView> delete(@RequestParam Long restaurantId) {
        return ResponseEntity.ok(restaurantService.deleteById(restaurantId));
    }

}
