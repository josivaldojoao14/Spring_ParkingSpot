package com.api.parkingcontrol.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.parkingcontrol.models.RoleModel;

@Repository
public interface RoleRepository extends JpaRepository<RoleModel, UUID> {
    //@Query(value = "SELECT r FROM tb_roles r WHERE r.role_name = ?1", nativeQuery = true)
    RoleModel findByName(String roleName);
}
