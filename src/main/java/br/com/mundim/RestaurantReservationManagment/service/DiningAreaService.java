package br.com.mundim.RestaurantReservationManagment.service;

import br.com.mundim.RestaurantReservationManagment.exceptions.BadRequestException;
import br.com.mundim.RestaurantReservationManagment.model.dto.DiningAreaDTO;
import br.com.mundim.RestaurantReservationManagment.model.entity.DiningArea;
import br.com.mundim.RestaurantReservationManagment.repository.DiningAreaRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

import static br.com.mundim.RestaurantReservationManagment.exceptions.config.BaseErrorMessage.DINING_AREA_NOT_FOUND_BY_ID;
import static br.com.mundim.RestaurantReservationManagment.model.entity.DiningArea.Availability.*;

@Service
public class DiningAreaService {

    private final DiningAreaRepository diningAreaRepository;

    public DiningAreaService(DiningAreaRepository diningAreaRepository) {
        this.diningAreaRepository = diningAreaRepository;
    }

    public DiningArea create(DiningAreaDTO dto) {
        return diningAreaRepository.save(new DiningArea(dto));
    }

    public List<DiningArea> findAll() {
        return diningAreaRepository.findAll();
    }

    public DiningArea findById(Long id) {
        return diningAreaRepository.findById(id)
                .orElseThrow(() -> new BadRequestException(
                        DINING_AREA_NOT_FOUND_BY_ID.params(id.toString()).getMessage())
                );
    }

    public List<DiningArea> findByRestaurantId(Long restaurantId) {
        List<DiningArea> diningAreas = diningAreaRepository.findByRestaurantId(restaurantId);
        Collections.sort(diningAreas, Collections.reverseOrder());
        return diningAreas;
    }

    public DiningArea update(Long diningAreaId, DiningAreaDTO dto) {
        DiningArea diningArea = findById(diningAreaId);

        diningArea.setDiningAreaName(dto.diningAreaName());
        diningArea.setCapacity(dto.capacity());

        return diningAreaRepository.save(diningArea);
    }

    public DiningArea deleteById(Long diningAreaId) {
        DiningArea diningArea = findById(diningAreaId);
        diningAreaRepository.deleteById(diningAreaId);
        return diningArea;
    }

    public DiningArea freeDiningArea(Long diningAreaId) {
        DiningArea diningArea = findById(diningAreaId);
        diningArea.setAvailability(AVAILABLE);
        return diningAreaRepository.save(diningArea);
    }

    public DiningArea occupiedDiningArea(Long diningAreaId) {
        DiningArea diningArea = findById(diningAreaId);
        diningArea.setAvailability(OCCUPIED);
        return diningAreaRepository.save(diningArea);
    }

    public DiningArea reserveDiningArea(Long diningAreaId) {
        DiningArea diningArea = findById(diningAreaId);
        diningArea.setAvailability(RESERVED);
        return diningAreaRepository.save(diningArea);
    }

}
