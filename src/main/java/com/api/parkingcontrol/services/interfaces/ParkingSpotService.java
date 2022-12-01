package com.api.parkingcontrol.services.interfaces;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.api.parkingcontrol.dtos.ParkingSpotDto;

public interface ParkingSpotService {
    Page<ParkingSpotDto> findAll(Pageable pageable);
    ParkingSpotDto findById(UUID id);
    ParkingSpotDto save(ParkingSpotDto parkingSpotDto);
    ParkingSpotDto update(UUID id, ParkingSpotDto parkingSpotDto);
    void delete(UUID id);

    boolean existsByLicensePlateCar(String licensePlateCar);
    boolean existsByParkingSpotNumber(String parkingSpotNumber);
    boolean existsByApartmentAndBlock(String apartment, String block);
}
