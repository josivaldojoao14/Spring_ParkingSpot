package com.api.parkingcontrol.services.interfaces;

import java.util.List;
import java.util.UUID;

import com.api.parkingcontrol.dtos.UserDto;


public interface UserService {
    List<UserDto> findAll();

    UserDto findUserById(UUID id);

    UserDto createUser(UserDto user);

    UserDto updateUser(UserDto user, UUID id);

    void deleteUser(UUID id);

    UserDto findByUsername(String username);

    void addRoleToUser(String username, String roleName);
}
