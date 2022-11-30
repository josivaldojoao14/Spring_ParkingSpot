package com.api.parkingcontrol.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.lenient;
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
import org.springframework.security.crypto.password.PasswordEncoder;

import com.api.parkingcontrol.dtos.UserDto;
import com.api.parkingcontrol.models.RoleModel;
import com.api.parkingcontrol.models.UserModel;
import com.api.parkingcontrol.repositories.RoleRepository;
import com.api.parkingcontrol.repositories.UserRepository;
import com.api.parkingcontrol.services.impl.UserServiceImpl;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder bcrypt;

    @InjectMocks
    private UserServiceImpl userService;

    private UserModel user;
    private UserModel user2;

    @BeforeEach
    public void init() {
        user = UserModel.builder()
            .fullName("Josivaldo Joao")
            .username("josivaldo55")
            .password("senha123")
            .phone("98672345")
            .roles(new ArrayList<>())
            .build();

        user2 = UserModel.builder()
            .fullName("Maria Jose")
            .username("maria")
            .password("senha123")
            .phone("98942354")
            .build();
    }

    @Test
    public void UserService_Create_ReturnsUser() {
        // Arrange
        user.setPassword(bcrypt.encode(user.getPassword()));
        when(userRepository.save(Mockito.any(UserModel.class))).thenReturn(user);

        // Act
        UserDto savedUser = userService.createUser(new UserDto(user));

        // Assert
        Assertions.assertThat(savedUser).isNotNull();
        Assertions.assertThat(savedUser.getPassword()).isEqualTo(user.getPassword());
    }

    @Test
    public void UserService_FindAll_ReturnsMoreThanOneUser() {
        // Arrange
        List<UserModel> users = new ArrayList<>(Arrays.asList(user, user2));

        // Act
        lenient().when(userRepository.findAll()).thenReturn(users);

        // Assert
        Assertions.assertThat(users).isNotNull();
        Assertions.assertThat(users).isNotEmpty();
        Assertions.assertThat(users.size()).isEqualTo(2);
    }

    @Test
    public void UserService_FindById_ReturnsUser() {
        // Arrange
        UUID id = UUID.randomUUID();
        user.setId(id);

        when(userRepository.findById(id)).thenReturn(Optional.ofNullable(user));

        // Act
        UserDto savedUser = userService.findUserById(id);

        // Assert
        Assertions.assertThat(savedUser).isNotNull();
        Assertions.assertThat(savedUser.getId()).isEqualTo(user.getId());
    }

    @Test
    public void UserService_DeleteById_ReturnsVoid() {
        // Arrange
        UUID id = UUID.randomUUID();
        user.setId(id);

        // Act
        when(userRepository.findById(id)).thenReturn(Optional.ofNullable(user));

        // Assert
        assertAll(() -> userService.deleteUser(id));
    }

    @Test
    public void UserService_FindByUsername_ReturnsUser() {
        // Arrange
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.ofNullable(user));

        // Act
        UserDto savedUser = userService.findByUsername(user.getUsername());

        // Assert
        Assertions.assertThat(savedUser).isNotNull();
        Assertions.assertThat(savedUser.getUsername()).isEqualTo(user.getUsername());
    }

    @Test
    public void UserService_AddRoleToUser_ReturnsVoid() {
        // Arrange
        RoleModel role = RoleModel.builder()
            .name("ADMIN")
            .build();

        lenient().when(roleRepository.save(Mockito.any(RoleModel.class))).thenReturn(role);
        lenient().when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.ofNullable(user));
        lenient().when(roleRepository.findByName(role.getName())).thenReturn(role);

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
