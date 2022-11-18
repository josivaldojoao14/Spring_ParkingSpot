package com.api.parkingcontrol.dtos;

import java.util.UUID;

import com.api.parkingcontrol.enums.RoleName;
import com.api.parkingcontrol.models.RoleModel;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RoleDto {

    private UUID id;
    private RoleName roleName;

    public RoleDto(RoleModel obj) {
        roleName = obj.getRoleName();
        id = obj.getId();
    }
}
