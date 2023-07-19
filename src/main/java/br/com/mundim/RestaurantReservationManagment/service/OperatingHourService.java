package br.com.mundim.RestaurantReservationManagment.service;

import br.com.mundim.RestaurantReservationManagment.model.dto.OperatingHourDTO;
import br.com.mundim.RestaurantReservationManagment.model.entity.OperatingHour;
import br.com.mundim.RestaurantReservationManagment.repository.OperatingHourRepository;
import org.springframework.stereotype.Service;

@Service
public class OperatingHourService {

    private final OperatingHourRepository operatingHourRepository;

    public OperatingHourService(OperatingHourRepository operatingHourRepository) {
        this.operatingHourRepository = operatingHourRepository;
    }

    public OperatingHour create(OperatingHourDTO dto) {
        return operatingHourRepository.save(new OperatingHour(dto));
    }

}
