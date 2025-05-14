package dev.nikkune.safetynet.alerts.mapper;

import dev.nikkune.safetynet.alerts.dto.FireStationCoveragePersonDTO;
import dev.nikkune.safetynet.alerts.model.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

public class FireStationCoveragePersonMapperTest {
    private FireStationCoveragePersonMapper fireStationCoveragePersonMapper;

    @BeforeEach
    public void setUp() {
        fireStationCoveragePersonMapper = Mappers.getMapper(FireStationCoveragePersonMapper.class);
    }

    @Test
    public void testToDTO() {
        // Arrange
        Person person = new Person();
        person.setFirstName("John");
        person.setLastName("Doe");
        person.setAddress("123 Main St");
        person.setCity("Anytown");
        person.setZip("12345");
        person.setPhone("555-555-1234");
        person.setEmail("john.doe@example.com");

        // Act
        FireStationCoveragePersonDTO dto = fireStationCoveragePersonMapper.toDTO(person);

        // Assert
        assertEquals("John", dto.getFirstName());
        assertEquals("Doe", dto.getLastName());
        assertEquals("123 Main St", dto.getAddress());
        assertEquals("555-555-1234", dto.getPhone());
    }

    @Test
    public void testToDTOPersonNull() {
        // Arrange
        Person person = null;

        // Act
        FireStationCoveragePersonDTO dto = fireStationCoveragePersonMapper.toDTO(person);

        // Assert
        assertNull(dto);
    }
}