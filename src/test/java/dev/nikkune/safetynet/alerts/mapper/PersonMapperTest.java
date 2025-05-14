package dev.nikkune.safetynet.alerts.mapper;

import dev.nikkune.safetynet.alerts.dto.PersonDTO;
import dev.nikkune.safetynet.alerts.model.MedicalRecord;
import dev.nikkune.safetynet.alerts.model.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class PersonMapperTest {
    private PersonMapper personMapper;

    @BeforeEach
    public void setUp() {
        personMapper = Mappers.getMapper(PersonMapper.class);
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
        Person person = new Person();
        person.setFirstName("John");
        person.setLastName("Doe");
        person.setAddress("123 Main St");
        person.setCity("Anytown");
        person.setZip("12345");
        person.setPhone("555-1234");
        person.setEmail("Gx4h9@example.com");
        person.setMedicalRecord(medicalRecord);

        // Act
        PersonDTO personDTO = personMapper.toDTO(person);

        // Assert
        assertEquals("John", personDTO.getFirstName());
        assertEquals("Doe", personDTO.getLastName());
        assertEquals("123 Main St", personDTO.getAddress());
        assertEquals("Anytown", personDTO.getCity());
        assertEquals("12345", personDTO.getZip());
        assertEquals("555-1234", personDTO.getPhone());
        assertEquals("Gx4h9@example.com", personDTO.getEmail());
    }

    @Test
    public void testToDTOPersonNull() {
        // Arrange
        Person person = null;

        // Act
        PersonDTO personDTO = personMapper.toDTO(person);

        // Assert
        assertNull(personDTO);
    }

    @Test
    public void testToEntity() {
        // Arrange
        PersonDTO personDTO = new PersonDTO();
        personDTO.setFirstName("John");
        personDTO.setLastName("Doe");
        personDTO.setAddress("123 Main St");
        personDTO.setCity("Anytown");
        personDTO.setZip("12345");
        personDTO.setPhone("555-1234");
        personDTO.setEmail("Gx4h9@example.com");

        // Act
        Person person = personMapper.toEntity(personDTO);

        // Assert
        assertEquals("John", person.getFirstName());
        assertEquals("Doe", person.getLastName());
        assertEquals("123 Main St", person.getAddress());
        assertEquals("Anytown", person.getCity());
        assertEquals("12345", person.getZip());
        assertEquals("555-1234", person.getPhone());
        assertEquals("Gx4h9@example.com", person.getEmail());
        assertNull(person.getMedicalRecord());
    }

    @Test
    public void testToEntityPersonNull() {
        // Arrange
        PersonDTO personDTO = null;

        // Act
        Person person = personMapper.toEntity(personDTO);

        // Assert
        assertNull(person);
    }
}
