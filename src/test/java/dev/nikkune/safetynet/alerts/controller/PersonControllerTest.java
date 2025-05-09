package dev.nikkune.safetynet.alerts.controller;

import dev.nikkune.safetynet.alerts.dto.PersonDTO;
import dev.nikkune.safetynet.alerts.mapper.PersonMapper;
import dev.nikkune.safetynet.alerts.model.Person;
import dev.nikkune.safetynet.alerts.service.PersonService;
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

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class PersonControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PersonService personService;

    @Mock
    private PersonMapper personMapper;

    @InjectMocks
    private PersonController personController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(personController).build();
    }

    @Test
    public void testGetAllPersons() throws Exception {
        // Arrange
        List<Person> persons = new ArrayList<>();
        Person person = new Person();
        person.setFirstName("John");
        person.setLastName("Doe");
        person.setAddress("123 Main St");
        person.setCity("Anytown");
        person.setZip("12345");
        person.setPhone("555-555-1234");
        person.setEmail("john.doe@example.com");
        persons.add(person);

        PersonDTO personDTO = new PersonDTO();
        personDTO.setFirstName("John");
        personDTO.setLastName("Doe");
        personDTO.setAddress("123 Main St");
        personDTO.setCity("Anytown");
        personDTO.setZip("12345");
        personDTO.setPhone("555-555-1234");
        personDTO.setEmail("john.doe@example.com");

        when(personService.getAll()).thenReturn(persons);
        when(personMapper.toDTO(any(Person.class))).thenReturn(personDTO);

        // Act & Assert
        mockMvc.perform(get("/person/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].firstName", is("John")))
                .andExpect(jsonPath("$[0].lastName", is("Doe")))
                .andExpect(jsonPath("$[0].address", is("123 Main St")))
                .andExpect(jsonPath("$[0].city", is("Anytown")))
                .andExpect(jsonPath("$[0].zip", is("12345")))
                .andExpect(jsonPath("$[0].phone", is("555-555-1234")))
                .andExpect(jsonPath("$[0].email", is("john.doe@example.com")));

        verify(personService, times(1)).getAll();
        verify(personMapper, times(1)).toDTO(any(Person.class));
    }

    @Test
    public void testGetPersonByFirstNameAndLastName() throws Exception {
        // Arrange
        Person person = new Person();
        person.setFirstName("John");
        person.setLastName("Doe");
        person.setAddress("123 Main St");
        person.setCity("Anytown");
        person.setZip("12345");
        person.setPhone("555-555-1234");
        person.setEmail("john.doe@example.com");

        PersonDTO personDTO = new PersonDTO();
        personDTO.setFirstName("John");
        personDTO.setLastName("Doe");
        personDTO.setAddress("123 Main St");
        personDTO.setCity("Anytown");
        personDTO.setZip("12345");
        personDTO.setPhone("555-555-1234");
        personDTO.setEmail("john.doe@example.com");

        when(personService.get("John", "Doe")).thenReturn(person);
        when(personMapper.toDTO(any(Person.class))).thenReturn(personDTO);

        // Act & Assert
        mockMvc.perform(get("/person")
                .param("firstName", "John")
                .param("lastName", "Doe"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.firstName", is("John")))
                .andExpect(jsonPath("$.lastName", is("Doe")))
                .andExpect(jsonPath("$.address", is("123 Main St")))
                .andExpect(jsonPath("$.city", is("Anytown")))
                .andExpect(jsonPath("$.zip", is("12345")))
                .andExpect(jsonPath("$.phone", is("555-555-1234")))
                .andExpect(jsonPath("$.email", is("john.doe@example.com")));

        verify(personService, times(1)).get("John", "Doe");
        verify(personMapper, times(1)).toDTO(any(Person.class));
    }

    @Test
    public void testGetPersonsByAddress() throws Exception {
        // Arrange
        List<Person> persons = new ArrayList<>();
        Person person = new Person();
        person.setFirstName("John");
        person.setLastName("Doe");
        person.setAddress("123 Main St");
        person.setCity("Anytown");
        person.setZip("12345");
        person.setPhone("555-555-1234");
        person.setEmail("john.doe@example.com");
        persons.add(person);

        PersonDTO personDTO = new PersonDTO();
        personDTO.setFirstName("John");
        personDTO.setLastName("Doe");
        personDTO.setAddress("123 Main St");
        personDTO.setCity("Anytown");
        personDTO.setZip("12345");
        personDTO.setPhone("555-555-1234");
        personDTO.setEmail("john.doe@example.com");

        when(personService.getByAddress("123 Main St")).thenReturn(persons);
        when(personMapper.toDTO(any(Person.class))).thenReturn(personDTO);

        // Act & Assert
        mockMvc.perform(get("/person/address")
                .param("address", "123 Main St"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].firstName", is("John")))
                .andExpect(jsonPath("$[0].lastName", is("Doe")))
                .andExpect(jsonPath("$[0].address", is("123 Main St")))
                .andExpect(jsonPath("$[0].city", is("Anytown")))
                .andExpect(jsonPath("$[0].zip", is("12345")))
                .andExpect(jsonPath("$[0].phone", is("555-555-1234")))
                .andExpect(jsonPath("$[0].email", is("john.doe@example.com")));

        verify(personService, times(1)).getByAddress("123 Main St");
        verify(personMapper, times(1)).toDTO(any(Person.class));
    }

    @Test
    public void testCreatePerson() throws Exception {
        // Arrange
        PersonDTO personDTO = new PersonDTO();
        personDTO.setFirstName("John");
        personDTO.setLastName("Doe");
        personDTO.setAddress("123 Main St");
        personDTO.setCity("Anytown");
        personDTO.setZip("12345");
        personDTO.setPhone("555-555-1234");
        personDTO.setEmail("john.doe@example.com");

        Person person = new Person();
        person.setFirstName("John");
        person.setLastName("Doe");
        person.setAddress("123 Main St");
        person.setCity("Anytown");
        person.setZip("12345");
        person.setPhone("555-555-1234");
        person.setEmail("john.doe@example.com");

        when(personMapper.toEntity(any(PersonDTO.class))).thenReturn(person);
        doNothing().when(personService).create(any(Person.class));

        // Act & Assert
        mockMvc.perform(post("/person")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"firstName\":\"John\",\"lastName\":\"Doe\",\"address\":\"123 Main St\",\"city\":\"Anytown\",\"zip\":\"12345\",\"phone\":\"555-555-1234\",\"email\":\"john.doe@example.com\"}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.firstName", is("John")))
                .andExpect(jsonPath("$.lastName", is("Doe")))
                .andExpect(jsonPath("$.address", is("123 Main St")))
                .andExpect(jsonPath("$.city", is("Anytown")))
                .andExpect(jsonPath("$.zip", is("12345")))
                .andExpect(jsonPath("$.phone", is("555-555-1234")))
                .andExpect(jsonPath("$.email", is("john.doe@example.com")));

        verify(personMapper, times(1)).toEntity(any(PersonDTO.class));
        verify(personService, times(1)).create(any(Person.class));
    }

    @Test
    public void testUpdatePerson() throws Exception {
        // Arrange
        PersonDTO personDTO = new PersonDTO();
        personDTO.setFirstName("John");
        personDTO.setLastName("Doe");
        personDTO.setAddress("456 Main St");
        personDTO.setCity("Othertown");
        personDTO.setZip("54321");
        personDTO.setPhone("555-555-5678");
        personDTO.setEmail("john.doe.updated@example.com");

        Person person = new Person();
        person.setFirstName("John");
        person.setLastName("Doe");
        person.setAddress("456 Main St");
        person.setCity("Othertown");
        person.setZip("54321");
        person.setPhone("555-555-5678");
        person.setEmail("john.doe.updated@example.com");

        when(personMapper.toEntity(any(PersonDTO.class))).thenReturn(person);
        doNothing().when(personService).update(any(Person.class));

        // Act & Assert
        mockMvc.perform(put("/person")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"firstName\":\"John\",\"lastName\":\"Doe\",\"address\":\"456 Main St\",\"city\":\"Othertown\",\"zip\":\"54321\",\"phone\":\"555-555-5678\",\"email\":\"john.doe.updated@example.com\"}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.firstName", is("John")))
                .andExpect(jsonPath("$.lastName", is("Doe")))
                .andExpect(jsonPath("$.address", is("456 Main St")))
                .andExpect(jsonPath("$.city", is("Othertown")))
                .andExpect(jsonPath("$.zip", is("54321")))
                .andExpect(jsonPath("$.phone", is("555-555-5678")))
                .andExpect(jsonPath("$.email", is("john.doe.updated@example.com")));

        verify(personMapper, times(1)).toEntity(any(PersonDTO.class));
        verify(personService, times(1)).update(any(Person.class));
    }

    @Test
    public void testDeletePerson() throws Exception {
        // Arrange
        doNothing().when(personService).delete("John", "Doe");

        // Act & Assert
        mockMvc.perform(delete("/person")
                .param("firstName", "John")
                .param("lastName", "Doe"))
                .andExpect(status().isNoContent());

        verify(personService, times(1)).delete("John", "Doe");
    }
}