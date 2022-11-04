package com.api.parkingcontrol.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.parkingcontrol.models.ParkingSpotModel;
import com.api.parkingcontrol.repositories.ParkingSpotRepository;

@Service
public class ParkingSpotService {

	@Autowired
	private ParkingSpotRepository repository;
	
	public List<ParkingSpotModel> findAll(){
		return repository.findAll();
	}
	
	public ParkingSpotModel findById(UUID id) {
		Optional<ParkingSpotModel> parkingSpotModel = repository.findById(id);
		return parkingSpotModel.orElseThrow(() -> new RuntimeException());
	}
}
