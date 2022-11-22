package com.api.parkingcontrol.dtos;

import java.util.UUID;

import com.api.parkingcontrol.models.RoleModel;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RoleDto {

    private UUID id;
    private String name;

    public RoleDto(RoleModel obj) {
        name = obj.getName();
        id = obj.getId();
    }
}
