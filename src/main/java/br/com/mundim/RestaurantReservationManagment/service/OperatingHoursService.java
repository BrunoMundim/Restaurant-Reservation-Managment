package br.com.mundim.RestaurantReservationManagment.service;

import br.com.mundim.RestaurantReservationManagment.model.dto.OperatingHoursDTO;
import br.com.mundim.RestaurantReservationManagment.model.entity.OperatingHours;
import br.com.mundim.RestaurantReservationManagment.repository.OperatingHoursRepository;
import org.springframework.stereotype.Service;

@Service
public class OperatingHoursService {

    private final OperatingHoursRepository operatingHoursRepository;

    public OperatingHoursService(OperatingHoursRepository operatingHoursRepository) {
        this.operatingHoursRepository = operatingHoursRepository;
    }

    public OperatingHours create(OperatingHoursDTO dto) {
        return operatingHoursRepository.save(new OperatingHours(dto));
    }

}
