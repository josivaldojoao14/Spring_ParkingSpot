package com.api.parkingcontrol.services.interfaces;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.api.parkingcontrol.models.RoleModel;

public interface RoleService {
    List<RoleModel> findAll();

    Optional<RoleModel> findRoleById(UUID id);

    RoleModel saveRole(RoleModel role);

    void deleteRole(UUID id);

    RoleModel findByName(String roleName);
}
