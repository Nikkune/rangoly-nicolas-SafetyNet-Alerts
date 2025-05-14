package dev.nikkune.safetynet.alerts.controller;

import dev.nikkune.safetynet.alerts.dto.FireStationDTO;
import dev.nikkune.safetynet.alerts.mapper.FireStationMapper;
import dev.nikkune.safetynet.alerts.model.FireStation;
import dev.nikkune.safetynet.alerts.service.FireStationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class FireStationControllerTest {

    private MockMvc mockMvc;

    @Mock
    private FireStationService fireStationService;

    @Mock
    private FireStationMapper fireStationMapper;

    @InjectMocks
    private FireStationController fireStationController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(fireStationController).build();
    }

    @Test
    public void testGetAllFireStations() throws Exception {
        // Arrange
        List<FireStation> fireStations = new ArrayList<>();
        FireStation fireStation = new FireStation();
        fireStation.setAddress("123 Main St");
        fireStation.setStation("1");
        fireStations.add(fireStation);

        FireStationDTO fireStationDTO = new FireStationDTO();
        fireStationDTO.setAddress("123 Main St");
        fireStationDTO.setStation("1");

        when(fireStationService.getAll()).thenReturn(fireStations);
        when(fireStationMapper.toDTO(any(FireStation.class))).thenReturn(fireStationDTO);

        // Act & Assert
        mockMvc.perform(get("/firestations/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].address", is("123 Main St")))
                .andExpect(jsonPath("$[0].station", is("1")));

        verify(fireStationService, times(1)).getAll();
        verify(fireStationMapper, times(1)).toDTO(any(FireStation.class));
    }

    @Test
    public void testGetFireStationByStationNumber() throws Exception {
        // Arrange
        List<FireStation> fireStations = new ArrayList<>();
        FireStation fireStation = new FireStation();
        fireStation.setAddress("123 Main St");
        fireStation.setStation("1");
        fireStations.add(fireStation);

        FireStationDTO fireStationDTO = new FireStationDTO();
        fireStationDTO.setAddress("123 Main St");
        fireStationDTO.setStation("1");

        when(fireStationService.get("1")).thenReturn(fireStations);
        when(fireStationMapper.toDTO(any(FireStation.class))).thenReturn(fireStationDTO);

        // Act & Assert
        mockMvc.perform(get("/firestations")
                        .param("number", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].address", is("123 Main St")))
                .andExpect(jsonPath("$[0].station", is("1")));

        verify(fireStationService, times(1)).get("1");
        verify(fireStationMapper, times(1)).toDTO(any(FireStation.class));
    }

    @Test
    public void testGetFireStationByStationNumber_NotFound() throws Exception {
        // Arrange
        when(fireStationService.get("999")).thenReturn(new ArrayList<>());

        // Act & Assert
        mockMvc.perform(get("/firestations")
                        .param("number", "999"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));

        verify(fireStationService, times(1)).get("999");
        verify(fireStationMapper, never()).toDTO(any(FireStation.class));
    }

    @Test
    public void testGetFireStationByAddress() throws Exception {
        // Arrange
        List<FireStation> fireStations = new ArrayList<>();
        FireStation fireStation = new FireStation();
        fireStation.setAddress("123 Main St");
        fireStation.setStation("1");
        fireStations.add(fireStation);

        FireStationDTO fireStationDTO = new FireStationDTO();
        fireStationDTO.setAddress("123 Main St");
        fireStationDTO.setStation("1");

        when(fireStationService.getByAddress("123 Main St")).thenReturn(fireStations);
        when(fireStationMapper.toDTO(any(FireStation.class))).thenReturn(fireStationDTO);

        // Act & Assert
        mockMvc.perform(get("/firestations/address")
                        .param("address", "123 Main St"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].address", is("123 Main St")))
                .andExpect(jsonPath("$[0].station", is("1")));

        verify(fireStationService, times(1)).getByAddress("123 Main St");
        verify(fireStationMapper, times(1)).toDTO(any(FireStation.class));
    }

    @Test
    public void testGetFireStationByAddress_NotFound() throws Exception {
        // Arrange
        when(fireStationService.getByAddress("Unknown Address")).thenReturn(new ArrayList<>());

        // Act & Assert
        mockMvc.perform(get("/firestations/address")
                        .param("address", "Unknown Address"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));

        verify(fireStationService, times(1)).getByAddress("Unknown Address");
        verify(fireStationMapper, never()).toDTO(any(FireStation.class));
    }

    @Test
    public void testCreateFireStation() throws Exception {
        // Arrange
        FireStationDTO fireStationDTO = new FireStationDTO();
        fireStationDTO.setAddress("123 Main St");
        fireStationDTO.setStation("1");

        FireStation fireStation = new FireStation();
        fireStation.setAddress("123 Main St");
        fireStation.setStation("1");

        when(fireStationMapper.toEntity(any(FireStationDTO.class))).thenReturn(fireStation);
        doNothing().when(fireStationService).create(any(FireStation.class));

        // Act & Assert
        mockMvc.perform(post("/firestations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"address\":\"123 Main St\",\"station\":\"1\"}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.address", is("123 Main St")))
                .andExpect(jsonPath("$.station", is("1")));

        verify(fireStationMapper, times(1)).toEntity(any(FireStationDTO.class));
        verify(fireStationService, times(1)).create(any(FireStation.class));
    }

    @Test
    public void testUpdateFireStation() throws Exception {
        // Arrange
        FireStationDTO fireStationDTO = new FireStationDTO();
        fireStationDTO.setAddress("123 Main St");
        fireStationDTO.setStation("2");

        FireStation fireStation = new FireStation();
        fireStation.setAddress("123 Main St");
        fireStation.setStation("2");

        when(fireStationMapper.toEntity(any(FireStationDTO.class))).thenReturn(fireStation);
        doNothing().when(fireStationService).update(any(FireStation.class));

        // Act & Assert
        mockMvc.perform(put("/firestations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"address\":\"123 Main St\",\"station\":\"2\"}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.address", is("123 Main St")))
                .andExpect(jsonPath("$.station", is("2")));

        verify(fireStationMapper, times(1)).toEntity(any(FireStationDTO.class));
        verify(fireStationService, times(1)).update(any(FireStation.class));
    }

    @Test
    public void testDeleteFireStationByAddress() throws Exception {
        // Arrange
        doNothing().when(fireStationService).deleteByAddress("123 Main St");

        // Act & Assert
        mockMvc.perform(delete("/firestations")
                        .param("address", "123 Main St"))
                .andExpect(status().isNoContent());

        verify(fireStationService, times(1)).deleteByAddress("123 Main St");
    }

    @Test
    public void testDeleteFireStation() throws Exception {
        // Arrange
        doNothing().when(fireStationService).deleteByStation("1");

        // Act & Assert
        mockMvc.perform(delete("/firestations/station")
                        .param("number", "1"))
                .andExpect(status().isNoContent());

        verify(fireStationService, times(1)).deleteByStation("1");
    }
}