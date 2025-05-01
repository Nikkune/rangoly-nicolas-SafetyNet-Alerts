package dev.nikkune.safetynet.alerts.mapper;

import dev.nikkune.safetynet.alerts.dto.FireStationDTO;
import dev.nikkune.safetynet.alerts.model.FireStation;
import dev.nikkune.safetynet.alerts.model.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class FireStationMapperTest {
    private FireStationMapper fireStationMapper;

    @BeforeEach
    public void setUp() {
        fireStationMapper = Mappers.getMapper(FireStationMapper.class);
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
        person.setPhone("555-1234");
        person.setEmail("Gx4h9@example.com");
        FireStation fireStation = new FireStation();
        fireStation.setAddress("123 Main St");
        fireStation.setStation("1");
        fireStation.setPersons(List.of(person));

        // Act
        FireStationDTO fireStationDTO = fireStationMapper.toDTO(fireStation);

        // Assert
        assertEquals("123 Main St", fireStationDTO.getAddress());
        assertEquals("1", fireStationDTO.getStation());
    }

    @Test
    public void testToDTOFireStationNull() {
        // Arrange
        FireStation fireStation = null;

        // Act
        FireStationDTO fireStationDTO = fireStationMapper.toDTO(fireStation);

        // Assert
        assertNull(fireStationDTO);
    }

    @Test
    public void testToEntity() {
        // Arrange
        FireStationDTO fireStationDTO = new FireStationDTO();
        fireStationDTO.setAddress("123 Main St");
        fireStationDTO.setStation("1");

        // Act
        FireStation fireStation = fireStationMapper.toEntity(fireStationDTO);

        // Assert
        assertEquals("123 Main St", fireStation.getAddress());
        assertEquals("1", fireStation.getStation());
        assertNull(fireStation.getPersons());
    }

    @Test
    public void testToEntityFireStationNull() {
        // Arrange
        FireStationDTO fireStationDTO = null;

        // Act
        FireStation fireStation = fireStationMapper.toEntity(fireStationDTO);

        // Assert
        assertNull(fireStation);
    }
}
