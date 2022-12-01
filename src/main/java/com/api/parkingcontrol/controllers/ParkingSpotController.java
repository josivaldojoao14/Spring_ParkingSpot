package com.api.parkingcontrol.controllers;

import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.parkingcontrol.dtos.ParkingSpotDto;
import com.api.parkingcontrol.services.impl.ParkingSpotServiceImpl;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/parking-spot")
public class ParkingSpotController {
	
	@Autowired
	private ParkingSpotServiceImpl service;
	
	@GetMapping
	public ResponseEntity<Page<?>> findAll(@PageableDefault(page=0, size=10, sort="id", direction = Sort.Direction.ASC) Pageable pageable){ 
		return ResponseEntity.status(HttpStatus.OK).body(service.findAll(pageable));
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> findById(@PathVariable(value = "id") UUID id){
		ParkingSpotDto parkingSpotOpt = service.findById(id);
		return ResponseEntity.status(HttpStatus.OK).body(parkingSpotOpt);
	}
	
	@PostMapping
	public ResponseEntity<?> saveParkingSpot(@RequestBody @Valid ParkingSpotDto parkingSpotDto){
		ParkingSpotDto parkingSpotCreated = service.save(parkingSpotDto);
		return ResponseEntity.status(HttpStatus.CREATED).body(parkingSpotCreated);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteParkingSpot(@PathVariable(value = "id") UUID id){
		ParkingSpotDto parkingSpotDto = service.findById(id);
		service.delete(parkingSpotDto.getId());
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?> updateParkingSpot(@PathVariable(value = "id") UUID id, @RequestBody @Valid ParkingSpotDto parkingSpotDto){
		ParkingSpotDto parkingUpdated = service.update(id, parkingSpotDto);
		return ResponseEntity.status(HttpStatus.OK).body(parkingUpdated);
	}
}
