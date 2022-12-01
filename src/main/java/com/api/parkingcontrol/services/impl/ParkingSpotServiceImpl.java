package com.api.parkingcontrol.services.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.api.parkingcontrol.dtos.ParkingSpotDto;
import com.api.parkingcontrol.models.ParkingSpotModel;
import com.api.parkingcontrol.repositories.ParkingSpotRepository;
import com.api.parkingcontrol.services.exceptions.ParkingSpotNotFoundException;
import com.api.parkingcontrol.services.interfaces.ParkingSpotService;

@Service
@Transactional
public class ParkingSpotServiceImpl implements ParkingSpotService {

	@Autowired
	private ParkingSpotRepository repository;

	@Override
	public Page<ParkingSpotDto> findAll(Pageable pageable) {
		Page<ParkingSpotModel> parkingModels = repository.findAll(pageable);
		List<ParkingSpotDto> parkingDto = parkingModels.stream().map(x -> new ParkingSpotDto(x))
				.collect(Collectors.toList());
		return new PageImpl<>(parkingDto);
	}

	@Override
	public ParkingSpotDto findById(UUID id) {
		ParkingSpotModel parkingSpotModel = repository.findById(id)
				.orElseThrow(() -> new ParkingSpotNotFoundException("Parking spot not found in the database"));
		return new ParkingSpotDto(parkingSpotModel);
	}

	@Override
	public ParkingSpotDto save(ParkingSpotDto parkingSpotDto) {
		ParkingSpotModel parkingSpotModel = new ParkingSpotModel(parkingSpotDto);
		parkingSpotModel.setRegistrationDate(LocalDateTime.now(ZoneId.of("UTC")));

		parkingSpotModel = repository.save(parkingSpotModel);
		return new ParkingSpotDto(parkingSpotModel);
	}

	@Override
	public ParkingSpotDto update(UUID id, ParkingSpotDto parkingSpotDto) {
		ParkingSpotDto parkingExist = findById(id);
		BeanUtils.copyProperties(parkingExist, parkingSpotDto);

		ParkingSpotModel parkingUpdated = repository.save(new ParkingSpotModel(parkingExist));
		return new ParkingSpotDto(parkingUpdated);
	}

	@Override
	public void delete(UUID id) {
		findById(id);
		repository.deleteById(id);
	}

	public boolean existsByLicensePlateCar(String licensePlateCar) {
		if (licensePlateCar != null) {
			throw new ParkingSpotNotFoundException("License plate car already taken");
		}
		return repository.existsByLicensePlateCar(licensePlateCar);
	}

	public boolean existsByParkingSpotNumber(String parkingSpotNumber) {
		if (parkingSpotNumber != null) {
			throw new ParkingSpotNotFoundException("Parking spot number already taken");
		}
		return repository.existsByParkingSpotNumber(parkingSpotNumber);
	}

	public boolean existsByApartmentAndBlock(String apartment, String block) {
		if (apartment != null || block != null) {
			throw new ParkingSpotNotFoundException("Apartment/Block number already taken");
		}
		return repository.existsByApartmentAndBlock(apartment, block);
	}
}
