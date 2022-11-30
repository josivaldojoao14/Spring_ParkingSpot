package com.api.parkingcontrol.services.impl;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.api.parkingcontrol.dtos.RoleDto;
import com.api.parkingcontrol.models.RoleModel;
import com.api.parkingcontrol.repositories.RoleRepository;
import com.api.parkingcontrol.services.exceptions.RoleNotFoundException;
import com.api.parkingcontrol.services.interfaces.RoleService;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public List<RoleDto> findAll() {
        List<RoleModel> roleModels = roleRepository.findAll();
        List<RoleDto> listRolesDto = roleModels.stream().map(x -> new RoleDto(x)).collect(Collectors.toList());
        return listRolesDto;
    }

    @Override
    public RoleDto findRoleById(UUID id) {
        RoleModel role = roleRepository.findById(id)
            .orElseThrow(() -> new RoleNotFoundException("Role not found in the database"));
        return new RoleDto(role);
    }

    @Override
    public RoleDto saveRole(RoleDto roleDto) {
        RoleModel roleModel = new RoleModel(roleDto);
        roleModel = roleRepository.save(roleModel);
        return new RoleDto(roleModel);
    }

    @Override
    public RoleDto updateRole(RoleDto role, UUID id) {
        RoleDto roleDto = findRoleById(id);
        roleDto.setName(role.getName());
      
        RoleModel updatedRole = roleRepository.save(new RoleModel(roleDto));
        return new RoleDto(updatedRole);
    }

    @Override
    public void deleteRole(UUID id) {
        findRoleById(id);
        roleRepository.deleteById(id);
    }

    @Override
    public RoleDto findByName(String roleName) {
        RoleModel roleModel = roleRepository.findByName(roleName)
            .orElseThrow(() -> new RoleNotFoundException("Role not found in the database"));
        return new RoleDto(roleModel);
    }
}
