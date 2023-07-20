package br.com.mundim.RestaurantReservationManagment.controller;

import br.com.mundim.RestaurantReservationManagment.model.dto.DiningAreaDTO;
import br.com.mundim.RestaurantReservationManagment.model.entity.DiningArea;
import br.com.mundim.RestaurantReservationManagment.service.DiningAreaService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dining-area")
public class DiningAreaController {

    private final DiningAreaService diningAreaService;

    public DiningAreaController(DiningAreaService diningAreaService) {
        this.diningAreaService = diningAreaService;
    }

    @PostMapping
    @Operation(tags = "DINING AREA", summary = "Create Dining Area")
    public DiningArea create(@RequestBody DiningAreaDTO dto) {
        return diningAreaService.create(dto);
    }

    @GetMapping("find-by-restaurant")
    @Operation(tags = "DINING AREA", summary = "Find Dining Area by restaurant")
    public List<DiningArea> findByRestaurant(@RequestParam Long restaurantId) {
        return diningAreaService.findByRestaurantId(restaurantId);
    }

    @GetMapping("find-by-id")
    @Operation(tags = "DINING AREA", summary = "Find Dining Area by id")
    public DiningArea findById(@RequestParam Long diningAreaId) {
        return diningAreaService.findById(diningAreaId);
    }

    @PutMapping
    @Operation(tags = "DINING AREA", summary = "Update Dining Area by id")
    public DiningArea updateById(@RequestParam Long id, @RequestBody DiningAreaDTO dto) {
        return diningAreaService.update(id, dto);
    }

    @PutMapping("/free")
    @Operation(tags = "DINING AREA", summary = "Free Dining Area by id")
    public DiningArea freeDiningArea(@RequestParam Long id) {
        return diningAreaService.freeDiningArea(id);
    }

    @PutMapping("/occupy")
    @Operation(tags = "DINING AREA", summary = "Ocuppy Dining Area by id")
    public DiningArea occupyDiningArea(@RequestParam Long id) {
        return diningAreaService.occupyDiningArea(id);
    }

    @DeleteMapping
    @Operation(tags = "DINING AREA", summary = "Delete Dining Area by id")
    public DiningArea deleteById(@RequestParam Long diningAreaId) {
        return diningAreaService.deleteById(diningAreaId);
    }

}
