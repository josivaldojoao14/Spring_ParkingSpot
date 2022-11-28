package com.api.parkingcontrol.dtos;

import java.util.UUID;

import com.api.parkingcontrol.models.RoleModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoleDto {

    private UUID id;
    private String name;

    public RoleDto(RoleModel obj) {
        name = obj.getName();
        id = obj.getId();
    }
}
