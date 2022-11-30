package com.api.parkingcontrol.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.parkingcontrol.dtos.RoleDto;
import com.api.parkingcontrol.services.impl.RoleServiceImpl;

import lombok.AllArgsConstructor;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/parking-spot")
@AllArgsConstructor
public class RoleController {

    private final RoleServiceImpl roleService;

    @GetMapping(value = "/roles")
    public ResponseEntity<List<RoleDto>> findAllRoles() {
        List<RoleDto> listRolesDto = roleService.findAll();
        return ResponseEntity.ok().body(listRolesDto);
    }

    @GetMapping(value = "/role/{id}")
    public ResponseEntity<?> findRoleById(@PathVariable UUID id) {
        RoleDto roleDto = roleService.findRoleById(id);
        return ResponseEntity.ok().body(roleDto);
    }

    @PostMapping(value = "/role")
    public ResponseEntity<?> saveRole(@RequestBody RoleDto roleDto) {
        RoleDto roleCreated = roleService.saveRole(roleDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(roleCreated);
    }

    @PutMapping(value = "/role/{id}")
    public ResponseEntity<?> updateRole(@RequestBody RoleDto roleDto, @PathVariable UUID id) {
        RoleDto roleUpdated = roleService.updateRole(roleDto, id);
        return ResponseEntity.status(HttpStatus.OK).body(roleUpdated);
    }

    @DeleteMapping(value = "/role/{id}")
    public ResponseEntity<?> deleteRole(@PathVariable UUID id) {
        RoleDto role = roleService.findRoleById(id);
        roleService.deleteRole(role.getId());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
