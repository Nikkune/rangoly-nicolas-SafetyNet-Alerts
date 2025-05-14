package dev.nikkune.safetynet.alerts.mapper;

import dev.nikkune.safetynet.alerts.dto.ChildAlertChildDTO;
import dev.nikkune.safetynet.alerts.model.MedicalRecord;
import dev.nikkune.safetynet.alerts.model.Person;
import dev.nikkune.safetynet.alerts.utils.AgeCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ChildAlertChildMapperTest {
    private ChildAlertChildMapper childAlertChildMapper;

    @BeforeEach
    public void setUp() {
        childAlertChildMapper = Mappers.getMapper(ChildAlertChildMapper.class);
    }

    @Test
    public void testToDTO() {
        // Arrange
        MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecord.setFirstName("Jimmy");
        medicalRecord.setLastName("Doe");
        medicalRecord.setBirthdate("01/15/2013");
        medicalRecord.setMedications(List.of());
        medicalRecord.setAllergies(List.of("allergy1"));
        
        Person person = new Person();
        person.setFirstName("Jimmy");
        person.setLastName("Doe");
        person.setAddress("123 Main St");
        person.setCity("Anytown");
        person.setZip("12345");
        person.setPhone("555-9012");
        person.setEmail("jimmy.doe@example.com");
        person.setMedicalRecord(medicalRecord);

        // Act
        ChildAlertChildDTO childDTO = childAlertChildMapper.toDTO(person);

        // Assert
        assertEquals("Jimmy", childDTO.getFirstName());
        assertEquals("Doe", childDTO.getLastName());
        
        // Calculate expected age based on birthdate
        int expectedAge = AgeCalculator.calculateAge("01/15/2013");
        assertEquals(expectedAge, childDTO.getAge());
    }

    @Test
    public void testToDTOPersonNull() {
        // Arrange
        Person person = null;

        // Act
        ChildAlertChildDTO childDTO = childAlertChildMapper.toDTO(person);

        // Assert
        assertNull(childDTO);
    }

    @Test
    public void testBirthdateToAge() {
        // Arrange
        String birthdate = "01/15/2013";
        
        // Act
        int age = ChildAlertChildMapper.birthdateToAge(birthdate);
        
        // Assert
        int expectedAge = AgeCalculator.calculateAge(birthdate);
        assertEquals(expectedAge, age);
    }
}