package com.api.parkingcontrol.controller;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.api.parkingcontrol.controllers.RoleController;
import com.api.parkingcontrol.dtos.RoleDto;
import com.api.parkingcontrol.models.RoleModel;
import com.api.parkingcontrol.services.impl.RoleServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(controllers = RoleController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class RoleControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RoleServiceImpl roleService;

    private RoleModel roleModel;
    private RoleDto roleDto;

    @BeforeEach
    public void init() {
        roleDto = roleDto.builder()
            .name("ADMIN")
            .build();

        roleModel = roleModel.builder()
            .name("USER")
            .build();
    }

    @Test
    public void RoleController_Register_ReturnCreated() throws Exception{
        given(roleService.saveRole(ArgumentMatchers.any()))
            .willAnswer((invocation -> invocation.getArgument(0)));

        ResultActions response = mockMvc.perform(post("/parking-spot/role")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(roleDto)));
        
        response.andExpect(MockMvcResultMatchers.status().isCreated())
            .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(roleDto.getName())));
    }


    @Test
    public void RoleController_GetAllRoles_ReturnListOfRoles() throws Exception {
        List<RoleDto> roles = Arrays.asList(roleDto);
        when(roleService.findAll()).thenReturn(roles);

        ResultActions response = mockMvc.perform(get("/parking-spot/roles")
            .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk());
            
    }

    @Test
    public void RoleController_FindById_ReturnRoleDto() throws Exception {
        UUID id = UUID.randomUUID();
        when(roleService.findRoleById(id)).thenReturn(roleDto);

        ResultActions response = mockMvc.perform(get("/parking-spot/role/" + id)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(roleDto)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(roleDto.getName())));
    }

    @Test
    public void RoleController_Update_ReturnRoleDto() throws Exception {
        UUID id = UUID.randomUUID();
        when(roleService.updateRole(roleDto, id)).thenReturn(roleDto);
        
        ResultActions response = mockMvc.perform(put("/parking-spot/role/" + id)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(roleDto)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(roleDto.getName())));
    }

    @Test
    public void RoleController_Delete_ReturnNoContent() throws Exception {
        UUID id = UUID.randomUUID();
        when(roleService.findRoleById(id)).thenReturn(roleDto);
        doNothing().when(roleService).deleteRole(roleDto.getId());

        ResultActions response = mockMvc.perform(delete("/parking-spot/role/" + id)
            .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isNoContent());
    } 
}
