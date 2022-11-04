package com.api.parkingcontrol.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.parkingcontrol.models.ParkingSpotModel;
import com.api.parkingcontrol.services.ParkingSpotService;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/parking-spot")
public class ParkingSpotController {
	
	@Autowired
	private ParkingSpotService service;
	
	@GetMapping
	public ResponseEntity<List<ParkingSpotModel>> findAll(){
		List<ParkingSpotModel> list = service.findAll();
		return ResponseEntity.ok().body(list);
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<ParkingSpotModel> findById(@PathVariable UUID id){
		ParkingSpotModel parkingSpot = service.findById(id);
		return ResponseEntity.ok().body(parkingSpot);
	}
	
}
