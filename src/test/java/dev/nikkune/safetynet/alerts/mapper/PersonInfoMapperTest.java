package dev.nikkune.safetynet.alerts.mapper;

import dev.nikkune.safetynet.alerts.dto.PersonInfoDTO;
import dev.nikkune.safetynet.alerts.model.MedicalRecord;
import dev.nikkune.safetynet.alerts.model.Person;
import dev.nikkune.safetynet.alerts.utils.AgeCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PersonInfoMapperTest {
    private PersonInfoMapper personInfoMapper;

    @BeforeEach
    public void setUp() {
        personInfoMapper = Mappers.getMapper(PersonInfoMapper.class);
    }

    @Test
    public void testToDTO() {
        // Arrange
        MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecord.setFirstName("John");
        medicalRecord.setLastName("Doe");
        medicalRecord.setBirthdate("01/01/1993");
        medicalRecord.setMedications(List.of("medication1", "medication2"));
        medicalRecord.setAllergies(List.of("allergy1", "allergy2"));

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
        PersonInfoDTO dto = personInfoMapper.toDTO(person);

        // Assert
        assertEquals("John", dto.getFirstName());
        assertEquals("Doe", dto.getLastName());
        assertEquals("123 Main St", dto.getAddress());
        assertEquals("john.doe@example.com", dto.getEmail());

        // Calculate expected age based on birthdate
        int expectedAge = AgeCalculator.calculateAge("01/01/1993");
        assertEquals(expectedAge, dto.getAge());

        // Check medical information
        assertEquals(2, dto.getMedications().size());
        assertTrue(dto.getMedications().contains("medication1"));
        assertTrue(dto.getMedications().contains("medication2"));

        assertEquals(2, dto.getAllergies().size());
        assertTrue(dto.getAllergies().contains("allergy1"));
        assertTrue(dto.getAllergies().contains("allergy2"));
    }

    @Test
    public void testToDTOPersonNull() {
        // Arrange
        Person person = null;

        // Act
        PersonInfoDTO dto = personInfoMapper.toDTO(person);

        // Assert
        assertNull(dto);
    }

    @Test
    public void testBirthdateToAge() {
        // Arrange
        String birthdate = "01/01/1993";

        // Act
        int age = PersonInfoMapper.birthdateToAge(birthdate);

        // Assert
        int expectedAge = AgeCalculator.calculateAge(birthdate);
        assertEquals(expectedAge, age);
    }
}