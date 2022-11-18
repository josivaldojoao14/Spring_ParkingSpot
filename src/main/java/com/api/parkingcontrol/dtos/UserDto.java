package com.api.parkingcontrol.dtos;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;

import com.api.parkingcontrol.models.UserModel;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDto {

    private UUID id;
    private String fullName;
    private String phone;
    private String username;
    private String password;
    private Collection<RoleDto> roles = new ArrayList<>();

    public UserDto(UserModel obj) {
        id = obj.getId();
        fullName = obj.getFullName();
        phone = obj.getPhone();
        username = obj.getUsername();
        password = obj.getPassword();
        roles = obj.getRoles().stream().map(x -> new RoleDto(x)).collect(Collectors.toList());
    }
}
