package dev.nikkune.safetynet.alerts.mapper;

import dev.nikkune.safetynet.alerts.dto.PersonBaseDTO;
import dev.nikkune.safetynet.alerts.model.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class PersonBaseMapperTest {
    private PersonBaseMapper personBaseMapper;

    @BeforeEach
    public void setUp() {
        personBaseMapper = Mappers.getMapper(PersonBaseMapper.class);
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
        PersonBaseDTO dto = personBaseMapper.toDTO(person);

        // Assert
        assertEquals("John", dto.getFirstName());
        assertEquals("Doe", dto.getLastName());
    }

    @Test
    public void testToDTOPersonNull() {
        // Arrange
        Person person = null;

        // Act
        PersonBaseDTO dto = personBaseMapper.toDTO(person);

        // Assert
        assertNull(dto);
    }
}