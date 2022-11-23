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

import com.api.parkingcontrol.models.RoleModel;
import com.api.parkingcontrol.repositories.RoleRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class RoleRepositoryTests {
    
    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void RoleRepository_SaveAll_ReturnSavedRole() {
        // Arrange
        RoleModel role = RoleModel.builder()
            .name("ADMIN_1")
            .build();

        // Act
        RoleModel savedRole = roleRepository.save(role);

        // Assert
        Assertions.assertThat(savedRole).isNotNull();
        Assertions.assertThat(savedRole.getId()).isNotNull();
        Assertions.assertThat(savedRole.getName()).isNotEmpty();
    }

    @Test
    public void RoleRepository_FindAll_ReturnMoreThanOneRoles() {
        // Arrange
        RoleModel role = RoleModel.builder()
            .name("USER_1")
            .build();

        RoleModel role2 = RoleModel.builder()
            .name("ADMIN_2")
            .build();

        roleRepository.save(role);
        roleRepository.save(role2);

        // Act
        List<RoleModel> roles = roleRepository.findAll();

        // Assert
        Assertions.assertThat(roles).isNotNull();
        Assertions.assertThat(roles.size()).isEqualTo(2);
    }

    @Test
    public void RoleRepository_FindById_ReturnRoleNotNull() {
        // Arrange
        RoleModel role = RoleModel.builder()
            .name("ADMIN_3")
            .build();

        roleRepository.save(role);

        // Act
        RoleModel retrievedRole = roleRepository.findById(role.getId()).get();

        // Assert
        Assertions.assertThat(retrievedRole).isNotNull();
        Assertions.assertThat(retrievedRole.getId()).isNotNull();
        Assertions.assertThat(retrievedRole.getId()).isEqualTo(role.getId());
    }

    @Test
    public void RoleRepository_FindByName_ReturnRoleNotNull() {
        // Arrange
        RoleModel role = RoleModel.builder()
            .name("ADMIN_4")
            .build();

        roleRepository.save(role);

        // Act
        RoleModel retrievedRole = roleRepository.findByName(role.getName());

        // Assert
        Assertions.assertThat(retrievedRole).isNotNull();
        Assertions.assertThat(retrievedRole.getId()).isNotNull();
        Assertions.assertThat(retrievedRole.getName()).isEqualTo(role.getName());
    }

    @Test
    public void RoleRepository_UpdateRole_ReturnOneRole() {
        // Arrange
        RoleModel role = RoleModel.builder()
            .name("ADMIN_5")
            .build();

        roleRepository.save(role);

        RoleModel roleSaved = roleRepository.findById(role.getId()).get();
        roleSaved.setName("USER_5");

        // Act
        RoleModel updatedRole = roleRepository.save(roleSaved);

        // Assert
        Assertions.assertThat(updatedRole).isNotNull();
        Assertions.assertThat(updatedRole.getName()).isNotEqualTo(role.getName());
    }

    @Test
    public void RoleRepository_DeleteRole_ReturnRoleNotNull() {
        // Arrange
        RoleModel role = RoleModel.builder()
            .name("SUPER_USER")
            .build();

        roleRepository.save(role);

        // Act
        roleRepository.deleteById(role.getId());
        Optional<RoleModel> roleReturn = roleRepository.findById(role.getId());

        // Assert
        Assertions.assertThat(roleReturn).isEmpty();
    }
}
