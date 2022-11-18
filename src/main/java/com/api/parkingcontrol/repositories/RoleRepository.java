package com.api.parkingcontrol.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.api.parkingcontrol.models.RoleModel;

@Repository
public interface RoleRepository extends JpaRepository<RoleModel, UUID> {
    @Query(value = "SELECT * FROM tb_role_model WHERE role_name = ?1", nativeQuery = true)
    RoleModel findByRoleName(@Param("roleName") String roleName);
}
