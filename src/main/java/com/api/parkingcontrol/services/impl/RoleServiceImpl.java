package com.api.parkingcontrol.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.api.parkingcontrol.models.RoleModel;
import com.api.parkingcontrol.repositories.RoleRepository;
import com.api.parkingcontrol.services.interfaces.RoleService;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public List<RoleModel> findAll() {
        return roleRepository.findAll();
    }

    @Override
    public Optional<RoleModel> findRoleById(UUID id) {
        Optional<RoleModel> role = roleRepository.findById(id);
        return role;
    }

    @Override
    public RoleModel saveRole(RoleModel role) {
        return roleRepository.save(role);
    }

    @Override
    public void deleteRole(UUID id) {
        findRoleById(id);
        roleRepository.deleteById(id);
    }

    @Override
    public RoleModel findByName(String roleName) {
        return roleRepository.findByName(roleName);
    }

}
