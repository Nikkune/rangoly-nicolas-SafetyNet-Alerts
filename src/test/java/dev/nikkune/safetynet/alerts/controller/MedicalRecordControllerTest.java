package dev.nikkune.safetynet.alerts.controller;

import dev.nikkune.safetynet.alerts.dto.MedicalRecordDTO;
import dev.nikkune.safetynet.alerts.mapper.MedicalRecordMapper;
import dev.nikkune.safetynet.alerts.model.MedicalRecord;
import dev.nikkune.safetynet.alerts.service.MedicalRecordService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class MedicalRecordControllerTest {

    private MockMvc mockMvc;

    @Mock
    private MedicalRecordService medicalRecordService;

    @Mock
    private MedicalRecordMapper medicalRecordMapper;

    @InjectMocks
    private MedicalRecordController medicalRecordController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(medicalRecordController).build();
    }

    @Test
    public void testGetAllMedicalRecords() throws Exception {
        // Arrange
        List<MedicalRecord> medicalRecords = new ArrayList<>();
        MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecord.setFirstName("John");
        medicalRecord.setLastName("Doe");
        medicalRecord.setBirthdate("01/01/1990");
        medicalRecord.setMedications(Arrays.asList("medication1", "medication2"));
        medicalRecord.setAllergies(Arrays.asList("allergy1", "allergy2"));
        medicalRecords.add(medicalRecord);

        MedicalRecordDTO medicalRecordDTO = new MedicalRecordDTO();
        medicalRecordDTO.setFirstName("John");
        medicalRecordDTO.setLastName("Doe");
        medicalRecordDTO.setBirthdate("01/01/1990");
        medicalRecordDTO.setMedications(Arrays.asList("medication1", "medication2"));
        medicalRecordDTO.setAllergies(Arrays.asList("allergy1", "allergy2"));

        when(medicalRecordService.getAll()).thenReturn(medicalRecords);
        when(medicalRecordMapper.toDTO(any(MedicalRecord.class))).thenReturn(medicalRecordDTO);

        // Act & Assert
        mockMvc.perform(get("/medicalrecord/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].firstName", is("John")))
                .andExpect(jsonPath("$[0].lastName", is("Doe")))
                .andExpect(jsonPath("$[0].birthdate", is("01/01/1990")))
                .andExpect(jsonPath("$[0].medications", hasSize(2)))
                .andExpect(jsonPath("$[0].allergies", hasSize(2)));

        verify(medicalRecordService, times(1)).getAll();
        verify(medicalRecordMapper, times(1)).toDTO(any(MedicalRecord.class));
    }

    @Test
    public void testGetMedicalRecordByFirstNameAndLastName() throws Exception {
        // Arrange
        MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecord.setFirstName("John");
        medicalRecord.setLastName("Doe");
        medicalRecord.setBirthdate("01/01/1990");
        medicalRecord.setMedications(Arrays.asList("medication1", "medication2"));
        medicalRecord.setAllergies(Arrays.asList("allergy1", "allergy2"));

        MedicalRecordDTO medicalRecordDTO = new MedicalRecordDTO();
        medicalRecordDTO.setFirstName("John");
        medicalRecordDTO.setLastName("Doe");
        medicalRecordDTO.setBirthdate("01/01/1990");
        medicalRecordDTO.setMedications(Arrays.asList("medication1", "medication2"));
        medicalRecordDTO.setAllergies(Arrays.asList("allergy1", "allergy2"));

        when(medicalRecordService.get("John", "Doe")).thenReturn(medicalRecord);
        when(medicalRecordMapper.toDTO(any(MedicalRecord.class))).thenReturn(medicalRecordDTO);

        // Act & Assert
        mockMvc.perform(get("/medicalrecord")
                        .param("firstName", "John")
                        .param("lastName", "Doe"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.firstName", is("John")))
                .andExpect(jsonPath("$.lastName", is("Doe")))
                .andExpect(jsonPath("$.birthdate", is("01/01/1990")))
                .andExpect(jsonPath("$.medications", hasSize(2)))
                .andExpect(jsonPath("$.allergies", hasSize(2)));

        verify(medicalRecordService, times(1)).get("John", "Doe");
        verify(medicalRecordMapper, times(1)).toDTO(any(MedicalRecord.class));
    }

    @Test
    public void testCreateMedicalRecord() throws Exception {
        // Arrange
        MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecord.setFirstName("John");
        medicalRecord.setLastName("Doe");
        medicalRecord.setBirthdate("01/01/1990");
        medicalRecord.setMedications(Arrays.asList("medication1", "medication2"));
        medicalRecord.setAllergies(Arrays.asList("allergy1", "allergy2"));

        MedicalRecordDTO medicalRecordDTO = new MedicalRecordDTO();
        medicalRecordDTO.setFirstName("John");
        medicalRecordDTO.setLastName("Doe");
        medicalRecordDTO.setBirthdate("01/01/1990");
        medicalRecordDTO.setMedications(Arrays.asList("medication1", "medication2"));
        medicalRecordDTO.setAllergies(Arrays.asList("allergy1", "allergy2"));

        doNothing().when(medicalRecordService).create(any(MedicalRecord.class));
        when(medicalRecordMapper.toDTO(any(MedicalRecord.class))).thenReturn(medicalRecordDTO);

        // Act & Assert
        mockMvc.perform(post("/medicalrecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"John\",\"lastName\":\"Doe\",\"birthdate\":\"01/01/1990\",\"medications\":[\"medication1\",\"medication2\"],\"allergies\":[\"allergy1\",\"allergy2\"]}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.firstName", is("John")))
                .andExpect(jsonPath("$.lastName", is("Doe")))
                .andExpect(jsonPath("$.birthdate", is("01/01/1990")))
                .andExpect(jsonPath("$.medications", hasSize(2)))
                .andExpect(jsonPath("$.allergies", hasSize(2)));

        verify(medicalRecordService, times(1)).create(any(MedicalRecord.class));
        verify(medicalRecordMapper, times(1)).toDTO(any(MedicalRecord.class));
    }

    @Test
    public void testUpdateMedicalRecord() throws Exception {
        // Arrange
        MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecord.setFirstName("John");
        medicalRecord.setLastName("Doe");
        medicalRecord.setBirthdate("01/01/1990");
        medicalRecord.setMedications(Arrays.asList("medication1", "medication2"));
        medicalRecord.setAllergies(Arrays.asList("allergy1", "allergy2"));

        MedicalRecordDTO medicalRecordDTO = new MedicalRecordDTO();
        medicalRecordDTO.setFirstName("John");
        medicalRecordDTO.setLastName("Doe");
        medicalRecordDTO.setBirthdate("01/01/1990");
        medicalRecordDTO.setMedications(Arrays.asList("medication1", "medication2"));
        medicalRecordDTO.setAllergies(Arrays.asList("allergy1", "allergy2"));

        doNothing().when(medicalRecordService).update(any(MedicalRecord.class));
        when(medicalRecordMapper.toDTO(any(MedicalRecord.class))).thenReturn(medicalRecordDTO);

        // Act & Assert
        mockMvc.perform(put("/medicalrecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"John\",\"lastName\":\"Doe\",\"birthdate\":\"01/01/1990\",\"medications\":[\"medication1\",\"medication2\"],\"allergies\":[\"allergy1\",\"allergy2\"]}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.firstName", is("John")))
                .andExpect(jsonPath("$.lastName", is("Doe")))
                .andExpect(jsonPath("$.birthdate", is("01/01/1990")))
                .andExpect(jsonPath("$.medications", hasSize(2)))
                .andExpect(jsonPath("$.allergies", hasSize(2)));

        verify(medicalRecordService, times(1)).update(any(MedicalRecord.class));
        verify(medicalRecordMapper, times(1)).toDTO(any(MedicalRecord.class));
    }

    @Test
    public void testDeleteMedicalRecord() throws Exception {
        // Arrange
        doNothing().when(medicalRecordService).delete("John", "Doe");

        // Act & Assert
        mockMvc.perform(delete("/medicalrecord")
                        .param("firstName", "John")
                        .param("lastName", "Doe"))
                .andExpect(status().isNoContent());

        verify(medicalRecordService, times(1)).delete("John", "Doe");
    }
}