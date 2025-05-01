package dev.nikkune.safetynet.alerts.mapper;

import dev.nikkune.safetynet.alerts.dto.MedicalRecordDTO;
import dev.nikkune.safetynet.alerts.model.MedicalRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class MedicalRecordMapperTest {
    private MedicalRecordMapper medicalRecordMapper;

    @BeforeEach
    public void setUp() {
        medicalRecordMapper = Mappers.getMapper(MedicalRecordMapper.class);
    }

    @Test
    public void testToDTO() {
        // Arrange
        MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecord.setFirstName("John");
        medicalRecord.setLastName("Doe");
        medicalRecord.setBirthdate("01/01/2000");
        medicalRecord.setMedications(List.of(new String[]{"medication1", "medication2"}));
        medicalRecord.setAllergies(List.of(new String[]{"allergy1", "allergy2"}));

        // Act
        MedicalRecordDTO johnRecordDTO = medicalRecordMapper.toDTO(medicalRecord);

        // Assert
        assertEquals("John", johnRecordDTO.getFirstName());
        assertEquals("Doe", johnRecordDTO.getLastName());
        assertEquals("01/01/2000", johnRecordDTO.getBirthdate());
        assertEquals(List.of(new String[]{"medication1", "medication2"}), johnRecordDTO.getMedications());
        assertEquals(List.of(new String[]{"allergy1", "allergy2"}), johnRecordDTO.getAllergies());
    }

    @Test
    public void testToDTOMedicationAndAllergyNull() {
        // Arrange
        MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecord.setFirstName("John");
        medicalRecord.setLastName("Doe");
        medicalRecord.setBirthdate("01/01/2000");
        medicalRecord.setMedications(null);
        medicalRecord.setAllergies(null);

        // Act
        MedicalRecordDTO johnRecordDTO = medicalRecordMapper.toDTO(medicalRecord);

        // Assert
        assertEquals("John", johnRecordDTO.getFirstName());
        assertEquals("Doe", johnRecordDTO.getLastName());
        assertEquals("01/01/2000", johnRecordDTO.getBirthdate());
        assertNull(johnRecordDTO.getMedications());
        assertNull(johnRecordDTO.getAllergies());
    }

    @Test
    public void testToDTORecordNull() {
        // Arrange
        MedicalRecord medicalRecord = null;

        // Act
        MedicalRecordDTO johnRecordDTO = medicalRecordMapper.toDTO(medicalRecord);

        // Assert
        assertNull(johnRecordDTO);
    }

    @Test
    public void testToEntity() {
        // Arrange
        MedicalRecordDTO medicalRecordDTO = new MedicalRecordDTO();
        medicalRecordDTO.setFirstName("John");
        medicalRecordDTO.setLastName("Doe");
        medicalRecordDTO.setBirthdate("01/01/2000");
        medicalRecordDTO.setMedications(List.of(new String[]{"medication1", "medication2"}));
        medicalRecordDTO.setAllergies(List.of(new String[]{"allergy1", "allergy2"}));

        // Act
        MedicalRecord johnRecord = medicalRecordMapper.toEntity(medicalRecordDTO);

        // Assert
        assertEquals("John", johnRecord.getFirstName());
        assertEquals("Doe", johnRecord.getLastName());
        assertEquals("01/01/2000", johnRecord.getBirthdate());
        assertEquals(List.of(new String[]{"medication1", "medication2"}), johnRecord.getMedications());
        assertEquals(List.of(new String[]{"allergy1", "allergy2"}), johnRecord.getAllergies());
    }

    @Test
    public void testToEntityMedicationAndAllergyNull() {
        // Arrange
        MedicalRecordDTO medicalRecordDTO = new MedicalRecordDTO();
        medicalRecordDTO.setFirstName("John");
        medicalRecordDTO.setLastName("Doe");
        medicalRecordDTO.setBirthdate("01/01/2000");
        medicalRecordDTO.setMedications(null);
        medicalRecordDTO.setAllergies(null);

        // Act
        MedicalRecord johnRecord = medicalRecordMapper.toEntity(medicalRecordDTO);

        // Assert
        assertEquals("John", johnRecord.getFirstName());
        assertEquals("Doe", johnRecord.getLastName());
        assertEquals("01/01/2000", johnRecord.getBirthdate());
        assertNull(johnRecord.getMedications());
        assertNull(johnRecord.getAllergies());
    }

    @Test
    public void testToEntityRecordNull() {
        // Arrange
        MedicalRecordDTO medicalRecordDTO = null;

        // Act
        MedicalRecord johnRecord = medicalRecordMapper.toEntity(medicalRecordDTO);

        // Assert
        assertNull(johnRecord);
    }
}
