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

import com.api.parkingcontrol.models.RoleModel;
import com.api.parkingcontrol.repositories.RoleRepository;
import com.api.parkingcontrol.services.impl.RoleServiceImpl;

@ExtendWith(MockitoExtension.class)
public class RoleServiceTests {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleServiceImpl roleService;

    private RoleModel role;
    private RoleModel role2;

    @BeforeEach
    public void init() {
        role = RoleModel.builder().name("ADMIN").build();
        role2 = RoleModel.builder().name("USER").build();
    }

    @Test
    public void RoleService_Create_ReturnsRole() {
        // Arrange
        when(roleRepository.save(Mockito.any(RoleModel.class))).thenReturn(role);

        // Act
        RoleModel savedRole = roleService.saveRole(role);

        // Assert
        Assertions.assertThat(savedRole).isNotNull();
        Assertions.assertThat(savedRole.getName()).isNotBlank();
    }

    @Test
    public void RoleService_FindAll_ReturnsMoreThanOneRole() {
        // Arrange
        List<RoleModel> roles = new ArrayList<>(Arrays.asList(role, role2));

        // Act
        Mockito.lenient().when(roleRepository.findAll()).thenReturn(roles);

        // Assert
        Assertions.assertThat(roles).isNotNull();
        Assertions.assertThat(roles).isNotEmpty();
        Assertions.assertThat(roles.size()).isEqualTo(2);
    }

    @Test
    public void RoleService_FindById_ReturnsRole() {
        // Arrange
        UUID id = UUID.randomUUID();
        role.setId(id);
        when(roleRepository.findById(id)).thenReturn(Optional.ofNullable(role));

        // Act
        RoleModel savedRole = roleService.findRoleById(id).get();

        // Assert
        Assertions.assertThat(savedRole).isNotNull();
        Assertions.assertThat(savedRole.getId()).isEqualTo(role.getId());
    }

    @Test
    public void RoleService_DeleteById_ReturnsVoid() {
        // Arrange
        UUID id = UUID.randomUUID();
        role.setId(id);

        // Act
        when(roleRepository.findById(id)).thenReturn(Optional.ofNullable(role));

        // Assert
        assertAll(() -> roleService.deleteRole(id));
    }

    @Test
    public void RoleService_FindByRolename_ReturnsRole() {
        // Arrange
        when(roleRepository.findByName(role.getName())).thenReturn(role);

        // Act
        RoleModel savedRole = roleService.findByName(role.getName());

        // Assert
        Assertions.assertThat(savedRole).isNotNull();
        Assertions.assertThat(savedRole.getName()).isEqualTo(role.getName());
    }
}
