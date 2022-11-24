package com.api.parkingcontrol.repository;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.api.parkingcontrol.models.ParkingSpotModel;
import com.api.parkingcontrol.repositories.ParkingSpotRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class ParkingSpotRepositoryTests {

    @Autowired
    private ParkingSpotRepository parkingSpotRepository;

    @Test
    public void ParkingSpotRepository_SaveAll_ReturnSavedParkingSpot() {
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

        // Act
        ParkingSpotModel savedParkingSpot = parkingSpotRepository.save(parkingSpot);

        // Assert
        Assertions.assertThat(savedParkingSpot).isNotNull();
        Assertions.assertThat(savedParkingSpot.getId()).isNotNull();
    }

    @Test
    public void ParkingSpotRepository_FindAll_ReturnMoreThanOneParkingSpots() {
        // Arrange
        ParkingSpotModel parkingSpot = ParkingSpotModel.builder()
            .parkingSpotNumber("7482b")
            .licensePlateCar("PGT5322")
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

        parkingSpotRepository.save(parkingSpot);
        parkingSpotRepository.save(parkingSpot2);

        // Act
        List<ParkingSpotModel> parkingSpots = parkingSpotRepository.findAll();

        // Assert
        Assertions.assertThat(parkingSpots).isNotNull();
        Assertions.assertThat(parkingSpots.size()).isEqualTo(2);
    }

    @Test
    public void ParkingSpotRepository_FindById_ReturnParkingSpotNotNull() {
        // Arrange
        ParkingSpotModel parkingSpot = ParkingSpotModel.builder()
            .parkingSpotNumber("7563u")
            .licensePlateCar("WST4276")
            .brandCar("Fiat")
            .modelCar("Toro")
            .colorCar("Red")
            .responsibleName("Maria")
            .registrationDate(LocalDateTime.now(ZoneId.of("UTC")))
            .apartment("321")
            .block("B")
            .build();

        parkingSpotRepository.save(parkingSpot);

        // Act
        ParkingSpotModel retrievedParkingSpot = parkingSpotRepository.findById(parkingSpot.getId()).get();

        // Assert
        Assertions.assertThat(retrievedParkingSpot).isNotNull();
        Assertions.assertThat(retrievedParkingSpot.getId()).isNotNull();
        Assertions.assertThat(retrievedParkingSpot.getId()).isEqualTo(parkingSpot.getId());
    }


    @Test
    public void ParkingSpotRepository_UpdateParkingSpot_ReturnOneParkingSpot() {
        // Arrange
        ParkingSpotModel parkingSpot = ParkingSpotModel.builder()
            .parkingSpotNumber("7563p")
            .licensePlateCar("WST4279")
            .brandCar("Fiat")
            .modelCar("Toro")
            .colorCar("Red")
            .responsibleName("Maria")
            .registrationDate(LocalDateTime.now(ZoneId.of("UTC")))
            .apartment("321")
            .block("B")
            .build();

        parkingSpotRepository.save(parkingSpot);

        ParkingSpotModel parkingSpotSaved = parkingSpotRepository.findById(parkingSpot.getId()).get();
        parkingSpotSaved.setLicensePlateCar("GTS4573");

        // Act
        ParkingSpotModel updatedParkingSpot = parkingSpotRepository.save(parkingSpotSaved);

        // Assert
        Assertions.assertThat(updatedParkingSpot).isNotNull();
        Assertions.assertThat(updatedParkingSpot.getLicensePlateCar()).isNotEqualTo(parkingSpot.getLicensePlateCar());
    }

    @Test
    public void ParkingSpotRepository_DeleteParkingSpot_ReturnParkingSpotNotNull() {
        // Arrange
        ParkingSpotModel parkingSpot = ParkingSpotModel.builder()
            .parkingSpotNumber("7523y")
            .licensePlateCar("TET4270")
            .brandCar("Fiat")
            .modelCar("Toro")
            .colorCar("Red")
            .responsibleName("Maria")
            .registrationDate(LocalDateTime.now(ZoneId.of("UTC")))
            .apartment("321")
            .block("B")
            .build();

        parkingSpotRepository.save(parkingSpot);

        // Act
        parkingSpotRepository.deleteById(parkingSpot.getId());
        Optional<ParkingSpotModel> parkingSpotReturn = parkingSpotRepository.findById(parkingSpot.getId());

        // Assert
        Assertions.assertThat(parkingSpotReturn).isEmpty();
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

        parkingSpotRepository.save(parkingSpot);

        // Act
        boolean bool = parkingSpotRepository.existsByLicensePlateCar(parkingSpot.getLicensePlateCar());

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

        parkingSpotRepository.save(parkingSpot);

        // Act
        boolean bool = parkingSpotRepository.existsByParkingSpotNumber(parkingSpot.getParkingSpotNumber());

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

        // Act
        boolean bool = parkingSpotRepository.existsByApartmentAndBlock(parkingSpot.getApartment(), parkingSpot.getBlock());

        // Assert
        Assertions.assertThat(bool).isTrue();
    }
}
