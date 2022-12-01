package com.api.parkingcontrol.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.api.parkingcontrol.dtos.ParkingSpotDto;
import com.api.parkingcontrol.models.ParkingSpotModel;
import com.api.parkingcontrol.repositories.ParkingSpotRepository;
import com.api.parkingcontrol.services.impl.ParkingSpotServiceImpl;

@ExtendWith(MockitoExtension.class)
public class ParkingSpotServiceTests {

    @Mock
    private ParkingSpotRepository parkingSpotRepository;

    @InjectMocks
    private ParkingSpotServiceImpl parkingSpotService;

    private ParkingSpotModel parkingSpot;

    @BeforeEach
    public void init() {
        parkingSpot = ParkingSpotModel.builder()
            .parkingSpotNumber("7482a")
            .licensePlateCar("PGT5321")
            .brandCar("Volkswagen")
            .modelCar("Virtus")
            .colorCar("Black")
            .responsibleName("Joao")
            .apartment("445")
            .block("A")
            .build();
    }

    @Test
    public void ParkingSpotService_Create_ReturnsParkingSpotDto() {
        // Arrange
        when(parkingSpotRepository.save(Mockito.any(ParkingSpotModel.class))).thenReturn(parkingSpot);

        // Act
        ParkingSpotDto savedParkingSpot = parkingSpotService.save(new ParkingSpotDto(parkingSpot));

        // Assert
        Assertions.assertThat(savedParkingSpot).isNotNull();
        Assertions.assertThat(savedParkingSpot.getParkingSpotNumber()).isNotBlank();
    }

    @Test
    public void ParkingSpotService_FindAll_ReturnsMoreThanOneParkingSpot() {
        // Arrange
        List<ParkingSpotModel> parkingSpots = new ArrayList<>(Arrays.asList(parkingSpot));

        // Act
        Mockito.lenient().when(parkingSpotRepository.findAll()).thenReturn(parkingSpots);

        // Assert
        Assertions.assertThat(parkingSpots).isNotNull();
        Assertions.assertThat(parkingSpots).isNotEmpty();
        Assertions.assertThat(parkingSpots.size()).isEqualTo(1);
    }

    @Test
    public void ParkingSpotService_FindById_ReturnsParkingSpot() {
        // Arrange
        UUID id = UUID.randomUUID();
        parkingSpot.setId(id);

        when(parkingSpotRepository.findById(id)).thenReturn(Optional.ofNullable(parkingSpot));

        // Act
        ParkingSpotDto savedParkingSpot = parkingSpotService.findById(id);

        // Assert
        Assertions.assertThat(savedParkingSpot).isNotNull();
        Assertions.assertThat(savedParkingSpot.getId()).isEqualTo(parkingSpot.getId());
    }

    @Test
    public void ParkingSpotService_Delete_ReturnsVoid() {
        // Arrange
        UUID id = UUID.randomUUID();
        parkingSpot.setId(id);

        // Act
        Mockito.lenient().when(parkingSpotRepository.findById(id)).thenReturn(Optional.ofNullable(parkingSpot));

        // Assert
        assertAll(() -> parkingSpotService.delete(id));
    }

    @Test
    public void ParkingSpotRepository_ExistsByLicensePlateCar_ReturnABoolean() {
        // Arrange
        when(parkingSpotRepository.existsByLicensePlateCar(parkingSpot.getLicensePlateCar())).thenReturn(true);

        // Act
        boolean bool = parkingSpotService.existsByLicensePlateCar(parkingSpot.getLicensePlateCar());

        // Assert
        Assertions.assertThat(bool).isTrue();
    }

    @Test
    public void ParkingSpotRepository_ExistsByParkingSpotNumber_ReturnABoolean() {
        // Arrange
        when(parkingSpotRepository.existsByParkingSpotNumber(parkingSpot.getParkingSpotNumber())).thenReturn(true);

        // Act
        boolean bool = parkingSpotService.existsByParkingSpotNumber(parkingSpot.getParkingSpotNumber());

        // Assert
        Assertions.assertThat(bool).isTrue();
    }

    @Test
    public void ParkingSpotRepository_ExistsByApartmentAndBlock_ReturnABoolean() {
        // Arrange
        when(parkingSpotRepository.existsByApartmentAndBlock(parkingSpot.getApartment(), parkingSpot.getBlock()))
                .thenReturn(true);

        // Act
        boolean bool = parkingSpotService.existsByApartmentAndBlock(parkingSpot.getApartment(), parkingSpot.getBlock());

        // Assert
        Assertions.assertThat(bool).isTrue();
    }
}