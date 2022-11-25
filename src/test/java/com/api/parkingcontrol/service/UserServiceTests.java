package com.api.parkingcontrol.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

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
import org.springframework.security.crypto.password.PasswordEncoder;

import com.api.parkingcontrol.models.RoleModel;
import com.api.parkingcontrol.models.UserModel;
import com.api.parkingcontrol.repositories.RoleRepository;
import com.api.parkingcontrol.repositories.UserRepository;
import com.api.parkingcontrol.services.UserServiceImpl;

@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.Silent.class)
public class UserServiceTests {
    
    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder bcrypt;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    public void UserService_Create_ReturnsUser(){
        // Arrange
        UserModel user = UserModel.builder()
            .fullName("Josivaldo Joao")
            .username("josivaldo55")
            .password("senha123")
            .phone("98672345")
            .build();

        user.setPassword(bcrypt.encode(user.getPassword()));
        when(userRepository.save(Mockito.any(UserModel.class))).thenReturn(user);
        
        // Act
        UserModel savedUser = userService.saveUser(user);

        // Assert
        Assertions.assertThat(savedUser).isNotNull();
        Assertions.assertThat(savedUser.getPassword()).isEqualTo(user.getPassword());
    }

    @Test
    public void UserService_FindAll_ReturnsMoreThanOneUser(){
        // Arrange
        UserModel user = UserModel.builder()
            .fullName("Josivaldo Joao")
            .username("josivaldo55")
            .password("senha123")
            .phone("98672345")
            .build();

        UserModel user2 = UserModel.builder()
            .fullName("Maria Jose")
            .username("maria")
            .password("senha123")
            .phone("98942354")
            .build();

        List<UserModel> users = new ArrayList<>(Arrays.asList(user, user2));

        // Act
        when(userRepository.findAll()).thenReturn(users);

        // Assert
        Assertions.assertThat(users).isNotNull();
        Assertions.assertThat(users).isNotEmpty();
        Assertions.assertThat(users.size()).isEqualTo(2);
    }

    @Test
    public void UserService_FindById_ReturnsUser() {
        // Arrange
        UUID id = UUID.randomUUID();
        UserModel user = UserModel.builder()
            .fullName("Josivaldo Joao") 
            .username("josivaldo55")
            .password("senha123")
            .phone("98672345")
            .build();
        user.setId(id);

        when(userRepository.findById(id)).thenReturn(Optional.ofNullable(user));

        // Act
        UserModel savedUser = userService.findUserById(id).get();

        // Assert
        Assertions.assertThat(savedUser).isNotNull();
        Assertions.assertThat(savedUser.getId()).isEqualTo(user.getId());
    }

    @Test
    public void UserService_DeleteById_ReturnsVoid() {
        // Arrange
        UUID id = UUID.randomUUID();
        UserModel user = UserModel.builder()
                .fullName("Josivaldo Joao")
                .username("josivaldo55")
                .password("senha123")
                .phone("98672345")
                .build();
        user.setId(id);

        // Act
        when(userRepository.findById(id)).thenReturn(Optional.ofNullable(user));

        // Assert
        assertAll(() -> userService.deleteUser(id));
    }

    @Test
    public void UserService_FindByUsername_ReturnsUser() {
        // Arrange
        UserModel user = UserModel.builder()
            .fullName("Josivaldo Joao")
            .username("josivaldo55")
            .password("senha123")
            .phone("98672345")
            .build();

        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.ofNullable(user));

        // Act
        UserModel savedUser = userService.findByUsername(user.getUsername()).get();

        // Assert
        Assertions.assertThat(savedUser).isNotNull();
        Assertions.assertThat(savedUser.getUsername()).isEqualTo(user.getUsername());
    }

    @Test
    public void UserService_AddRoleToUser_ReturnsVoid() {
        // Arrange
        UserModel user = UserModel.builder()
            .fullName("Josivaldo Joao")
            .username("josivaldo55")
            .password("senha123")
            .phone("98672345")
            .roles(new ArrayList<>())
            .build();

        RoleModel role = RoleModel.builder()
            .name("ADMIN")
            .build();

        when(roleRepository.save(Mockito.any(RoleModel.class))).thenReturn(role);
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.ofNullable(user));
        when(roleRepository.findByName(role.getName())).thenReturn(role);

        // Act
        userService.addRoleToUser(user.getUsername(), role.getName());

        // Assert
        Assertions.assertThat(user).isNotNull();
        Assertions.assertThat(user.getRoles()).isNotEmpty();
        Assertions.assertThat(user.getRoles().size()).isGreaterThan(0);
        Assertions.assertThat(user.getRoles().stream().filter(x -> x.getName()
            .equals(role.getName())).findFirst().get().getName()).isEqualTo(role.getName());
    }
}
