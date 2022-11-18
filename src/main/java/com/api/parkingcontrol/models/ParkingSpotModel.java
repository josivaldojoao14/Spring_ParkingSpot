package com.api.parkingcontrol.models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TB_PARKING_SPOT")
public class ParkingSpotModel implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(name = "uuid", columnDefinition = "char(36)")
	@Type(type = "org.hibernate.type.UUIDCharType")
	public UUID id;

	@Column(nullable = false, unique = true, length = 10)
	public String parkingSpotNumber;

	@Column(nullable = false, unique = true, length = 7)
	public String licensePlateCar;

	@Column(nullable = false, length = 70)
	public String brandCar;

	@Column(nullable = false, length = 70)
	public String modelCar;

	@Column(nullable = false, length = 70)
	public String colorCar;

	@Column(nullable = false)
	public LocalDateTime registrationDate;

	@Column(nullable = false, length = 130)
	public String responsibleName;

	@Column(nullable = false, length = 30)
	public String apartment;

	@Column(nullable = false, length = 30)
	public String block;

}
