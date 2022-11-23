package com.api.parkingcontrol.repository;

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

import com.api.parkingcontrol.models.UserModel;
import com.api.parkingcontrol.repositories.UserRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void UserRepository_SaveAll_ReturnSavedUser() {
        // Arrange
        UserModel user = UserModel.builder()
            .fullName("Josivaldo Joao")
            .username("josivaldo")
            .password("senha123")
            .phone("98672345")
            .build();

        // Act
        UserModel savedUser = userRepository.save(user);

        // Assert
        Assertions.assertThat(savedUser).isNotNull();
        Assertions.assertThat(savedUser.getId()).isNotNull();
    }

    @Test
    public void UserRepository_FindAll_ReturnMoreThanOneUsers() {
        // Arrange
        UserModel user = UserModel.builder()
            .fullName("Josivaldo Joao")
            .username("josivaldo")
            .password("senha123")
            .phone("98672345")
            .build();

        UserModel user2 = UserModel.builder()
            .fullName("Maria Jose")
            .username("maria")
            .password("senha123")
            .phone("98942354")
            .build();

        userRepository.save(user);
        userRepository.save(user2);
        
        // Act
        List<UserModel> users = userRepository.findAll();

        // Assert
        Assertions.assertThat(users).isNotNull();
        Assertions.assertThat(users.size()).isEqualTo(2);
    }

    @Test
    public void UserRepository_FindById_ReturnUserNotNull() {
        // Arrange
        UserModel user = UserModel.builder()
            .fullName("Josivaldo Joao")
            .username("josivaldo")
            .password("senha123")
            .phone("98672345")
            .build();

        userRepository.save(user);

        // Act
        UserModel retrievedUser = userRepository.findById(user.getId()).get();

        // Assert
        Assertions.assertThat(retrievedUser).isNotNull();
        Assertions.assertThat(retrievedUser.getId()).isNotNull();
        Assertions.assertThat(retrievedUser.getId()).isEqualTo(user.getId());
    }

    @Test
    public void UserRepository_FindByUsername_ReturnUserNotNull() {
        // Arrange
        UserModel user = UserModel.builder()
            .fullName("Josivaldo Joao")
            .username("josivaldo55")
            .password("senha123")
            .phone("98672345")
            .build();

        userRepository.save(user);

        // Act
        UserModel retrievedUser = userRepository.findByUsername(user.getUsername()).get();

        // Assert
        Assertions.assertThat(retrievedUser).isNotNull();
        Assertions.assertThat(retrievedUser.getId()).isNotNull();
        Assertions.assertThat(retrievedUser.getUsername()).isEqualTo(user.getUsername());
    }

    @Test
    public void UserRepository_UpdateUser_ReturnOneUser() {
        // Arrange
        UserModel user = UserModel.builder()
            .fullName("Josivaldo Joao")
            .username("josivaldo")
            .password("senha123")
            .phone("98672345")
            .build();

        userRepository.save(user);

        UserModel userSaved = userRepository.findById(user.getId()).get();
        userSaved.setUsername("josivaldojoao");
        userSaved.setPhone("99999999");

        // Act
        UserModel updatedUser = userRepository.save(userSaved);

        // Assert
        Assertions.assertThat(updatedUser).isNotNull();
        Assertions.assertThat(updatedUser.getUsername()).isNotEqualTo(user.getUsername());
        Assertions.assertThat(updatedUser.getPhone()).isNotEqualTo(user.getPhone());
    }

    @Test
    public void UserRepository_DeleteUser_ReturnUserNotNull() {
        // Arrange
        UserModel user = UserModel.builder()
            .fullName("Josivaldo Joao")
            .username("josivaldo")
            .password("senha123")
            .phone("98672345")
            .build();

        userRepository.save(user);

        // Act
        userRepository.deleteById(user.getId());
        Optional<UserModel> userReturn = userRepository.findById(user.getId());

        // Assert
        Assertions.assertThat(userReturn).isEmpty();
    }
}

