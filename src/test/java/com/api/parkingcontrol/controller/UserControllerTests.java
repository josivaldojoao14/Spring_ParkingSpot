package com.api.parkingcontrol.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.util.ArrayList;

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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.api.parkingcontrol.controllers.UserController;
import com.api.parkingcontrol.dtos.RoleDto;
import com.api.parkingcontrol.dtos.UserDto;
import com.api.parkingcontrol.models.UserModel;
import com.api.parkingcontrol.security.JWTGenerator;
import com.api.parkingcontrol.services.UserServiceImpl;
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

    private UserModel userModel;
    private UserDto userDto;
    private RoleDto roleDto;

    @BeforeEach
    public void init() {
        userModel = UserModel.builder()
            .fullName("Josivaldo Joao")
            .username("josivaldo55")
            .password("senha123")
            .phone("98672345")
            .roles(new ArrayList<>())
            .build();

        userDto = userDto.builder()
            .fullName("Josivaldo Joao")
            .username("josivaldo55")
            .password("senha123")
            .phone("98672345")
            .build();

        roleDto = roleDto.builder().name("ADMIN").build();
    }

    @Test
    public void UserController_Register_ReturnCreated() throws Exception{
        given(userService.saveUser(ArgumentMatchers.any()))
            .willAnswer((invocation -> invocation.getArgument(0)));
        
        ResultActions response = mockMvc.perform(post("/parking-spot/auth/user/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(userDto)));
        
        response.andExpect(MockMvcResultMatchers.status().isCreated());
    }
}
