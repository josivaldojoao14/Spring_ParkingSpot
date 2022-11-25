package com.api.parkingcontrol.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;

import com.api.parkingcontrol.models.ParkingSpotModel;
import com.api.parkingcontrol.repositories.ParkingSpotRepository;
import com.api.parkingcontrol.services.ParkingSpotService;

@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.Silent.class)
public class ParkingSpotServiceTests {

    @Mock
    private ParkingSpotRepository parkingSpotRepository;

    @InjectMocks
    private ParkingSpotService parkingSpotService;

    @Test
    public void ParkingSpotService_Create_ReturnsParkingSpot() {
        // Arrange
        ParkingSpotModel parkingSpot = ParkingSpotModel.builder()
            .parkingSpotNumber("7482a")
            .licensePlateCar("PGT5321")
            .brandCar("Volkswagen")
            .modelCar("Virtus")
            .colorCar("Black")
            .responsibleName("Joao")
            .registrationDate(LocalDateTime.now(ZoneId.of("UTC")))
            .apartment("445")
            .block("A")
            .build();

        when(parkingSpotRepository.save(Mockito.any(ParkingSpotModel.class))).thenReturn(parkingSpot);

        // Act
        ParkingSpotModel savedParkingSpot = parkingSpotService.save(parkingSpot);

        // Assert
        Assertions.assertThat(savedParkingSpot).isNotNull();
        Assertions.assertThat(savedParkingSpot.getParkingSpotNumber()).isNotBlank();
    }

    @Test
    public void ParkingSpotService_FindAll_ReturnsMoreThanOneParkingSpot() {
        // Arrange
        ParkingSpotModel parkingSpot = ParkingSpotModel.builder()
            .parkingSpotNumber("7482a")
            .licensePlateCar("PGT5321")
            .brandCar("Volkswagen")
            .modelCar("Virtus")
            .colorCar("Black")
            .responsibleName("Joao")
            .registrationDate(LocalDateTime.now(ZoneId.of("UTC")))
            .apartment("445")
            .block("A")
            .build();

        ParkingSpotModel parkingSpot2 = ParkingSpotModel.builder()
            .parkingSpotNumber("7563b")
            .licensePlateCar("WST4273")
            .brandCar("Fiat")
            .modelCar("Toro")
            .colorCar("Red")
            .responsibleName("Maria")
            .registrationDate(LocalDateTime.now(ZoneId.of("UTC")))
            .apartment("321")
            .block("B")
            .build();

        List<ParkingSpotModel> parkingSpots = new ArrayList<>(Arrays.asList(parkingSpot, parkingSpot2));

        // Act
        when(parkingSpotRepository.findAll()).thenReturn(parkingSpots);

        // Assert
        Assertions.assertThat(parkingSpots).isNotNull();
        Assertions.assertThat(parkingSpots).isNotEmpty();
        Assertions.assertThat(parkingSpots.size()).isEqualTo(2);
    }

    @Test
    public void ParkingSpotService_FindById_ReturnsParkingSpot() {
        // Arrange
        UUID id = UUID.randomUUID();
        ParkingSpotModel parkingSpot = ParkingSpotModel.builder()
            .parkingSpotNumber("7563q")
            .licensePlateCar("WSB4273")
            .brandCar("Fiat")
            .modelCar("Toro")
            .colorCar("Red")
            .responsibleName("Maria")
            .registrationDate(LocalDateTime.now(ZoneId.of("UTC")))
            .apartment("321")
            .block("B")
            .build();
        parkingSpot.setId(id);

        when(parkingSpotRepository.findById(id)).thenReturn(Optional.ofNullable(parkingSpot));

        // Act
        ParkingSpotModel savedParkingSpot = parkingSpotService.findById(id).get();

        // Assert
        Assertions.assertThat(savedParkingSpot).isNotNull();
        Assertions.assertThat(savedParkingSpot.getId()).isEqualTo(parkingSpot.getId());
    }

    @Test
    public void ParkingSpotService_Delete_ReturnsVoid() {
        // Arrange
        UUID id = UUID.randomUUID();
        ParkingSpotModel parkingSpot = ParkingSpotModel.builder()
            .parkingSpotNumber("6563q")
            .licensePlateCar("TSB3273")
            .brandCar("Fiat")
            .modelCar("Toro")
            .colorCar("Red")
            .responsibleName("Maria")
            .registrationDate(LocalDateTime.now(ZoneId.of("UTC")))
            .apartment("321")
            .block("B")
            .build();
        parkingSpot.setId(id);

        // Act
        when(parkingSpotRepository.findById(id)).thenReturn(Optional.ofNullable(parkingSpot));

        // Assert
        assertAll(() -> parkingSpotService.delete(parkingSpot));
    }

    @Test
    public void ParkingSpotRepository_ExistsByLicensePlateCar_ReturnABoolean() {
        // Arrange
        ParkingSpotModel parkingSpot = ParkingSpotModel.builder()
            .parkingSpotNumber("7563t")
            .licensePlateCar("WST4476")
            .brandCar("Fiat")
            .modelCar("Toro")
            .colorCar("Red")
            .responsibleName("Maria")
            .registrationDate(LocalDateTime.now(ZoneId.of("UTC")))
            .apartment("321")
            .block("B")
            .build();

        when(parkingSpotRepository.existsByLicensePlateCar(parkingSpot.getLicensePlateCar())).thenReturn(true);

        // Act
        boolean bool = parkingSpotService.existsByLicensePlateCar(parkingSpot.getLicensePlateCar());

        // Assert
        Assertions.assertThat(bool).isTrue();
    }

    @Test
    public void ParkingSpotRepository_ExistsByParkingSpotNumber_ReturnABoolean() {
        // Arrange
        ParkingSpotModel parkingSpot = ParkingSpotModel.builder()
            .parkingSpotNumber("7563y")
            .licensePlateCar("WST4270")
            .brandCar("Fiat")
            .modelCar("Toro")
            .colorCar("Red")
            .responsibleName("Maria")
            .registrationDate(LocalDateTime.now(ZoneId.of("UTC")))
            .apartment("321")
            .block("B")
            .build();

        when(parkingSpotRepository.existsByParkingSpotNumber(parkingSpot.getParkingSpotNumber())).thenReturn(true);

        // Act
        boolean bool = parkingSpotService.existsByParkingSpotNumber(parkingSpot.getParkingSpotNumber());

        // Assert
        Assertions.assertThat(bool).isTrue();
    }

    @Test
    public void ParkingSpotRepository_ExistsByApartmentAndBlock_ReturnABoolean() {
        // Arrange
        ParkingSpotModel parkingSpot = ParkingSpotModel.builder()
            .parkingSpotNumber("7563y")
            .licensePlateCar("WST4270")
            .brandCar("Fiat")
            .modelCar("Toro")
            .colorCar("Red")
            .responsibleName("Maria")
            .registrationDate(LocalDateTime.now(ZoneId.of("UTC")))
            .apartment("321")
            .block("B")
            .build();

        when(parkingSpotRepository.existsByApartmentAndBlock(parkingSpot.getApartment(), parkingSpot.getBlock())).thenReturn(true);

        // Act
        boolean bool = parkingSpotService.existsByApartmentAndBlock(parkingSpot.getApartment(), parkingSpot.getBlock());

        // Assert
        Assertions.assertThat(bool).isTrue();
    }
}