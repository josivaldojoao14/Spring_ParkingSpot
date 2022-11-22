package com.api.parkingcontrol.controllers;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
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
import com.api.parkingcontrol.models.RoleModel;
import com.api.parkingcontrol.services.RoleServiceImpl;

import lombok.AllArgsConstructor;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/parking-spot")
@AllArgsConstructor
public class RoleController {

    private final RoleServiceImpl roleService;

    @GetMapping(value = "/roles")
    public ResponseEntity<List<RoleDto>> findAllRoles() {
        List<RoleModel> listRoleModel = roleService.findAll();
        List<RoleDto> listRolesDto = listRoleModel.stream().map(x -> new RoleDto(x)).collect(Collectors.toList());
        return ResponseEntity.ok().body(listRolesDto);
    }

    @GetMapping(value = "/role/{id}")
    public ResponseEntity<?> findRoleById(@PathVariable UUID id) {
        Optional<RoleModel> roleModel = roleService.findRoleById(id);
        if (!roleModel.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Role not found!");
        }
        return ResponseEntity.ok().body(new RoleDto(roleModel.get()));
    }

    @PostMapping(value = "/role")
    public ResponseEntity<?> saveRole(@RequestBody RoleDto roleDto) {
        RoleModel roleExist = roleService.findByName(roleDto.getName());
        if(roleExist != null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Role name already exists!");
        }
        
        RoleModel obj = new RoleModel();
        BeanUtils.copyProperties(roleDto, obj);
        roleService.saveRole(obj);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping(value = "/role/{id}")
    public ResponseEntity<?> updateRole(@RequestBody RoleDto roleDto, @PathVariable UUID id) {
        Optional<RoleModel> originalRole = roleService.findRoleById(id);
        if (!originalRole.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Role not found!");
        }

        RoleModel obj = new RoleModel();
        BeanUtils.copyProperties(roleDto, obj);
        obj.setId(originalRole.get().getId());
        obj = roleService.saveRole(obj);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping(value = "/role/{id}")
    public ResponseEntity<?> deleteRole(@PathVariable UUID id) {
        Optional<RoleModel> roleModel = roleService.findRoleById(id);
        if (!roleModel.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Role not found!");
        }

        roleService.deleteRole(roleModel.get().getId());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
