package dev.nikkune.safetynet.alerts.mapper;

import dev.nikkune.safetynet.alerts.dto.FirePersonDTO;
import dev.nikkune.safetynet.alerts.model.MedicalRecord;
import dev.nikkune.safetynet.alerts.model.Person;
import dev.nikkune.safetynet.alerts.utils.AgeCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FirePersonMapperTest {
    private FirePersonMapper firePersonMapper;

    @BeforeEach
    public void setUp() {
        firePersonMapper = Mappers.getMapper(FirePersonMapper.class);
    }

    @Test
    public void testToDTO() {
        // Arrange
        MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecord.setFirstName("John");
        medicalRecord.setLastName("Doe");
        medicalRecord.setBirthdate("01/01/1993");
        medicalRecord.setMedications(List.of("medication1", "medication2"));
        medicalRecord.setAllergies(List.of("allergy1"));
        
        Person person = new Person();
        person.setFirstName("John");
        person.setLastName("Doe");
        person.setAddress("123 Main St");
        person.setCity("Anytown");
        person.setZip("12345");
        person.setPhone("555-555-1234");
        person.setEmail("john.doe@example.com");
        person.setMedicalRecord(medicalRecord);

        // Act
        FirePersonDTO firePersonDTO = firePersonMapper.toDTO(person);

        // Assert
        assertEquals("John", firePersonDTO.getFirstName());
        assertEquals("Doe", firePersonDTO.getLastName());
        assertEquals("555-555-1234", firePersonDTO.getPhone());
        
        // Calculate expected age based on birthdate
        int expectedAge = AgeCalculator.calculateAge("01/01/1993");
        assertEquals(expectedAge, firePersonDTO.getAge());
        
        // Check medical information
        assertEquals(2, firePersonDTO.getMedications().size());
        assertTrue(firePersonDTO.getMedications().contains("medication1"));
        assertTrue(firePersonDTO.getMedications().contains("medication2"));
        
        assertEquals(1, firePersonDTO.getAllergies().size());
        assertTrue(firePersonDTO.getAllergies().contains("allergy1"));
    }

    @Test
    public void testToDTOPersonNull() {
        // Arrange
        Person person = null;

        // Act
        FirePersonDTO firePersonDTO = firePersonMapper.toDTO(person);

        // Assert
        assertNull(firePersonDTO);
    }

    @Test
    public void testBirthdateToAge() {
        // Arrange
        String birthdate = "01/01/1993";
        
        // Act
        int age = FirePersonMapper.birthdateToAge(birthdate);
        
        // Assert
        int expectedAge = AgeCalculator.calculateAge(birthdate);
        assertEquals(expectedAge, age);
    }
}