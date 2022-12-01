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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.api.parkingcontrol.controllers.ParkingSpotController;
import com.api.parkingcontrol.dtos.ParkingSpotDto;
import com.api.parkingcontrol.services.impl.ParkingSpotServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(controllers = ParkingSpotController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class ParkingSpotControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ParkingSpotServiceImpl parkingSpotService;

    private ParkingSpotDto parkingSpotDto;

    @BeforeEach
    public void init() {
        parkingSpotDto = ParkingSpotDto.builder()
            .parkingSpotNumber("7482a")
            .licensePlateCar("PGT5321")
            .brandCar("Volkswagen")
            .modelCar("Virtus")
            .colorCar("Black")
            .responsibleName("Joao")
            .apartment("445")
            .block("A")
            .build();
    }

    @Test
    public void ParkingSpotController_Register_ReturnCreated() throws Exception{
        given(parkingSpotService.save(ArgumentMatchers.any()))
            .willAnswer((invocation -> invocation.getArgument(0)));

        ResultActions response = mockMvc.perform(post("/parking-spot")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(parkingSpotDto)));
        
        response.andExpect(MockMvcResultMatchers.status().isCreated())
            .andExpect(MockMvcResultMatchers.jsonPath("$.licensePlateCar", CoreMatchers.is(parkingSpotDto.getLicensePlateCar())));
    }


    @Test
    public void ParkingSpotController_GetAllParkingSpots_ReturnListOfParkingSpots() throws Exception {
        Pageable pageable = PageRequest.of(0, 8);
        List<ParkingSpotDto> parkingSpots = Arrays.asList(parkingSpotDto);

        when(parkingSpotService.findAll(pageable)).thenReturn(new PageImpl<>(parkingSpots));

        ResultActions response = mockMvc.perform(get("/parking-spot")
            .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void ParkingSpotController_FindById_ReturnParkingSpotDto() throws Exception {
        UUID id = UUID.randomUUID();
        when(parkingSpotService.findById(id)).thenReturn(parkingSpotDto);

        ResultActions response = mockMvc.perform(get("/parking-spot/" + id)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(parkingSpotDto)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.modelCar", CoreMatchers.is(parkingSpotDto.getModelCar())));
    }

    @Test
    public void ParkingSpotController_Update_ReturnParkingSpotDto() throws Exception {
        UUID id = UUID.randomUUID();
        when(parkingSpotService.update(id, parkingSpotDto)).thenReturn(parkingSpotDto);
        
        ResultActions response = mockMvc.perform(put("/parking-spot/" + id)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(parkingSpotDto)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.jsonPath("$.brandCar", CoreMatchers.is(parkingSpotDto.getBrandCar())));
    }

    @Test
    public void ParkingSpotController_Delete_ReturnNoContent() throws Exception {
        UUID id = UUID.randomUUID();
        when(parkingSpotService.findById(id)).thenReturn(parkingSpotDto);
        doNothing().when(parkingSpotService).delete(parkingSpotDto.getId());

        ResultActions response = mockMvc.perform(delete("/parking-spot/" + id)
            .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isNoContent());
    } 
}
