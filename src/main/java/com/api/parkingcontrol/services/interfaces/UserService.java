package com.api.parkingcontrol.services.interfaces;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.api.parkingcontrol.models.UserModel;

public interface UserService {
    List<UserModel> findAll();

    Optional<UserModel> findUserById(UUID id);

    UserModel saveUser(UserModel user);

    void deleteUser(UUID id);

    Optional<UserModel> findByUsername(String username);

    void addRoleToUser(String username, String roleName);
}
