package com.api.parkingcontrol.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.api.parkingcontrol.dtos.AuthResponseDto;
import com.api.parkingcontrol.dtos.UserDto;
import com.api.parkingcontrol.dtos.UserLoginDto;
import com.api.parkingcontrol.models.form.RoleToUserForm;
import com.api.parkingcontrol.security.JWTGenerator;
import com.api.parkingcontrol.services.impl.UserServiceImpl;
import com.api.parkingcontrol.util.URL;

import lombok.AllArgsConstructor;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/parking-spot")
@AllArgsConstructor
public class UserController {

    private AuthenticationManager authenticationManager;
    private UserServiceImpl userService;
    private JWTGenerator jwtGenerator;

    @GetMapping(value = "/users")
    public ResponseEntity<List<UserDto>> findAllUsers() {
        List<UserDto> listUserDto = userService.findAll();
        return ResponseEntity.ok().body(listUserDto);
    }

    @GetMapping(value = "/user/{id}")
    public ResponseEntity<?> findUserById(@PathVariable UUID id) {
        UserDto user = userService.findUserById(id);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found!");
        }
        return ResponseEntity.ok().body(user);
    }

    @PostMapping(value = "/auth/user/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> register(@RequestBody UserDto userDto) {
        UserDto user = userService.findByUsername(userDto.getUsername());
        if (user != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username is already taken!");
        }
        UserDto userCreated = userService.createUser(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(userCreated);
    }

    @PostMapping(value = "/auth/user/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody UserLoginDto userLoginDto) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                userLoginDto.getUsername(),
                userLoginDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtGenerator.generateToken(authentication);
        return ResponseEntity.status(HttpStatus.OK).body(new AuthResponseDto(token));
    }

    @PutMapping(value = "/user/{id}")
    public ResponseEntity<?> updateUser(@RequestBody UserDto userDto, @PathVariable UUID id) {
        UserDto userUpdated = userService.updateUser(userDto, id);
        return ResponseEntity.status(HttpStatus.OK).body(userUpdated);
    }

    @DeleteMapping(value = "/user/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable UUID id) {
        UserDto user = userService.findUserById(id);
        userService.deleteUser(user.getId());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping(value = "/user/findByUsername")
    public ResponseEntity<?> findByUsername(@RequestParam(value = "username") String username) {
        username = URL.decodeParam(username);
        UserDto user = userService.findByUsername(username);
        return ResponseEntity.ok().body(user);
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
