package br.com.mundim.RestaurantReservationManagment.service;

import br.com.mundim.RestaurantReservationManagment.exceptions.BadRequestException;
import br.com.mundim.RestaurantReservationManagment.model.dto.DiningAreaDTO;
import br.com.mundim.RestaurantReservationManagment.model.entity.DiningArea;
import br.com.mundim.RestaurantReservationManagment.repository.DiningAreaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

import static br.com.mundim.RestaurantReservationManagment.exceptions.config.BaseErrorMessage.DINING_AREA_NOT_FOUND_BY_ID;

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
        return diningAreaRepository.findByRestaurantId(restaurantId);
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

}
