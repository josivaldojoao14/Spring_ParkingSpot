package com.api.parkingcontrol.services.interfaces;

import java.util.List;
import java.util.UUID;

import com.api.parkingcontrol.dtos.RoleDto;

public interface RoleService {
    List<RoleDto> findAll();

    RoleDto findRoleById(UUID id);

    RoleDto saveRole(RoleDto role);

    RoleDto updateRole(RoleDto role, UUID id);

    void deleteRole(UUID id);

    RoleDto findByName(String roleName);
}
