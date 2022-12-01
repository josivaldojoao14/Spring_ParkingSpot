package com.api.parkingcontrol.dtos;

import java.io.Serializable;
import java.util.UUID;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.api.parkingcontrol.models.ParkingSpotModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ParkingSpotDto implements Serializable{
	private static final long serialVersionUID = 1L;
	public UUID id;

	@NotBlank
	private String parkingSpotNumber;
	
	@NotBlank
	@Size(max = 7)
	private String licensePlateCar;
	
	@NotBlank
	private String brandCar;
	
	@NotBlank
	private String modelCar;
	
	@NotBlank
	private String colorCar;
	
	@NotBlank
	private String responsibleName;
	
	@NotBlank
	private String apartment;
	
	@NotBlank
	private String block;

	public ParkingSpotDto(ParkingSpotModel obj){
		id = obj.getId();
		parkingSpotNumber = obj.getParkingSpotNumber();
		licensePlateCar = obj.getLicensePlateCar();
		brandCar = obj.getBrandCar();
		modelCar = obj.getModelCar();
		colorCar = obj.getColorCar();
		responsibleName = obj.getResponsibleName();
		apartment = obj.getApartment();
		block = obj.getBlock();
	}

}
