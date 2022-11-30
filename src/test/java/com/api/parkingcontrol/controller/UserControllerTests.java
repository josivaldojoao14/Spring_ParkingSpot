package com.api.parkingcontrol.controller;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import java.util.ArrayList;
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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.api.parkingcontrol.controllers.UserController;
import com.api.parkingcontrol.dtos.RoleDto;
import com.api.parkingcontrol.dtos.UserDto;
import com.api.parkingcontrol.dtos.UserLoginDto;
import com.api.parkingcontrol.models.UserModel;
import com.api.parkingcontrol.models.form.RoleToUserForm;
import com.api.parkingcontrol.security.JWTGenerator;
import com.api.parkingcontrol.services.impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class UserControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserServiceImpl userService;

    @MockBean
    private AuthenticationManager authenticationManager;
    
    @MockBean
    private JWTGenerator jwtGenerator;

    private UserDto userDto;
    private UserModel userModel;
    private UserLoginDto userLoginDto;
    private RoleDto roleDto;
    private RoleToUserForm roleToUserForm;

    @BeforeEach
    public void init() {
        userDto = userDto.builder()
            .fullName("Josivaldo Joao")
            .phone("98672345")
            .username("josivaldo55")
            .password("senha123")
            .build();

        userModel = userModel.builder()
            .fullName("Maria Jose")
            .phone("986345342")
            .username("mariajose")
            .password("senha123")
            .roles(new ArrayList<>())
            .build();

        userLoginDto = userLoginDto.builder()
            .username("josivaldo55")
            .password("senha123")
            .build();

        roleDto = roleDto.builder().name("ADMIN").build();

        roleToUserForm = roleToUserForm.builder()
            .username(userDto.getUsername())
            .roleName(roleDto.getName())
            .build();
    }

    @Test
    public void UserController_Register_ReturnCreated() throws Exception{
        given(userService.createUser(ArgumentMatchers.any()))
            .willAnswer((invocation -> invocation.getArgument(0)));

        ResultActions response = mockMvc.perform(post("/parking-spot/auth/user/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(userDto)));
        
        response.andExpect(MockMvcResultMatchers.status().isCreated())
            .andExpect(MockMvcResultMatchers.jsonPath("$.username", CoreMatchers.is(userDto.getUsername())))
            .andExpect(MockMvcResultMatchers.jsonPath("$.fullName", CoreMatchers.is(userDto.getFullName())))
            .andExpect(MockMvcResultMatchers.jsonPath("$.phone", CoreMatchers.is(userDto.getPhone())));
    }

    @Test
    public void UserController_Login_ReturnOk() throws Exception {
        given(userService.loadUserByUsername(ArgumentMatchers.any()))
            .willAnswer((invocation -> invocation.getArgument(0)));

        ResultActions response = mockMvc.perform(post("/parking-spot/auth/user/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(userLoginDto)));

        response.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void UserController_GetAllUsers_ReturnListOfUsers() throws Exception {
        List<UserDto> users = Arrays.asList(userDto);
        when(userService.findAll()).thenReturn(users);

        ResultActions response = mockMvc.perform(get("/parking-spot/users")
            .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk());
            
    }

    @Test
    public void UserController_FindById_ReturnUserDto() throws Exception {
        UUID id = UUID.randomUUID();
        when(userService.findUserById(id)).thenReturn(userDto);

        ResultActions response = mockMvc.perform(get("/parking-spot/user/" + id)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(userDto)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.username", CoreMatchers.is(userDto.getUsername())))
            .andExpect(MockMvcResultMatchers.jsonPath("$.fullName", CoreMatchers.is(userDto.getFullName())))
            .andExpect(MockMvcResultMatchers.jsonPath("$.phone", CoreMatchers.is(userDto.getPhone())));
    }

    @Test
    public void UserController_Update_ReturnUserDto() throws Exception {
        UUID id = UUID.randomUUID();
        when(userService.updateUser(userDto, id)).thenReturn(userDto);
        
        ResultActions response = mockMvc.perform(put("/parking-spot/user/" + id)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(userDto)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.jsonPath("$.username", CoreMatchers.is(userDto.getUsername())))
            .andExpect(MockMvcResultMatchers.jsonPath("$.fullName", CoreMatchers.is(userDto.getFullName())))
            .andExpect(MockMvcResultMatchers.jsonPath("$.phone", CoreMatchers.is(userDto.getPhone())));
    }

    @Test
    public void UserController_Delete_ReturnNoContent() throws Exception {
        UUID id = UUID.randomUUID();
        when(userService.findUserById(id)).thenReturn(userDto);
        doNothing().when(userService).deleteUser(userDto.getId());

        ResultActions response = mockMvc.perform(delete("/parking-spot/user/" + id)
            .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void UserController_FindByUserName_ReturnUserDto() throws Exception {
        when(userService.findByUsername(userDto.getUsername())).thenReturn(userDto);

        ResultActions response = mockMvc.perform(get("/parking-spot/user/findByUsername")
            .param("username", userDto.getUsername())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(userDto)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.username", CoreMatchers.is(userDto.getUsername())))
            .andExpect(MockMvcResultMatchers.jsonPath("$.fullName", CoreMatchers.is(userDto.getFullName())))
            .andExpect(MockMvcResultMatchers.jsonPath("$.phone", CoreMatchers.is(userDto.getPhone())));
    }

    @Test
    public void UserController_AddRoleToUser_ReturnNoContent() throws Exception {

        doNothing().when(userService).addRoleToUser(roleToUserForm.getUsername(), roleToUserForm.getRoleName());

        ResultActions response = mockMvc.perform(post("/parking-spot/user/addRoleToUser")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(roleToUserForm)));

        response.andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}
