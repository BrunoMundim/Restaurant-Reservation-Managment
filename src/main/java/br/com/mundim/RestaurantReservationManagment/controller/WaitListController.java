package br.com.mundim.RestaurantReservationManagment.controller;

import br.com.mundim.RestaurantReservationManagment.model.dto.WaitListDTO;
import br.com.mundim.RestaurantReservationManagment.model.entity.WaitList;
import br.com.mundim.RestaurantReservationManagment.service.WaitListService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/wait-list")
public class WaitListController {

    private final WaitListService waitListService;

    public WaitListController(WaitListService waitListService) {
        this.waitListService = waitListService;
    }

    @PostMapping
    @Operation(tags = "WAIT LIST", summary = "Create wait list reservation")
    public ResponseEntity<WaitList> create(@RequestBody WaitListDTO dto) {
        return new ResponseEntity<>(waitListService.create(dto), CREATED);
    }

    @GetMapping("find-by-restaurant-id")
    @Operation(tags = "WAIT LIST", summary = "Find by restaurant id")
    public ResponseEntity<List<WaitList>> findByRestaurantId(@RequestParam Long id) {
        return ResponseEntity.ok(waitListService.findByRestaurantId(id));
    }

}
