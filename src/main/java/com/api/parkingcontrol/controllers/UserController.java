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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.api.parkingcontrol.dtos.UserDto;
import com.api.parkingcontrol.models.UserModel;
import com.api.parkingcontrol.services.UserService;
import com.api.parkingcontrol.util.URL;

import lombok.AllArgsConstructor;
import lombok.Data;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/parking-spot")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping(value = "/users")
    public ResponseEntity<List<UserDto>> findAllUsers() {
        List<UserModel> listUserModel = userService.findAll();
        List<UserDto> listUsersDto = listUserModel.stream().map(x -> new UserDto(x)).collect(Collectors.toList());
        return ResponseEntity.ok().body(listUsersDto);
    }

    @GetMapping(value = "/user/{id}")
    public ResponseEntity<?> findUserById(@PathVariable UUID id) {
        Optional<UserModel> userModel = userService.findUserById(id);
        if (!userModel.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found!");
        }
        return ResponseEntity.ok().body(new UserDto(userModel.get()));
    }

    @PostMapping(value = "/user")
    public ResponseEntity<UserDto> saveUser(@RequestBody UserDto userDto) {
        UserModel obj = new UserModel();
        BeanUtils.copyProperties(userDto, obj);
        userService.saveUser(obj);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping(value = "/user/{id}")
    public ResponseEntity<?> updateUser(@RequestBody UserDto userDto, @PathVariable UUID id) {
        Optional<UserModel> originalUser = userService.findUserById(id);
        if (!originalUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found!");
        }

        UserModel obj = new UserModel();
        BeanUtils.copyProperties(userDto, obj);
        obj.setId(originalUser.get().getId());
        obj = userService.saveUser(obj);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping(value = "/user/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable UUID id) {
        Optional<UserModel> userModel = userService.findUserById(id);
        if (!userModel.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found!");
        }

        userService.deleteUser(userModel.get().getId());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping(value = "/user/findByUsername")
    public ResponseEntity<?> findByUsername(@RequestParam(value = "username") String username) {
        username = URL.decodeParam(username);
        Optional<UserModel> obj = userService.findByUsername(username);
        if (!obj.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Username not found!");
        }

        return ResponseEntity.ok().body(new UserDto(obj.get()));
    }

    @PostMapping(value = "/user/addRoleToUser")
    public ResponseEntity<?> addRoleToUser(@RequestBody RoleToUserForm form) {
        if (form.getUsername().isEmpty() || form.getRoleName().isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The username/role name cannot be empty!");
        }
        userService.addRoleToUser(form.getUsername(), form.getRoleName());
        return ResponseEntity.noContent().build();
    }
}

@Data
class RoleToUserForm {
    private String username;
    private String roleName;
}