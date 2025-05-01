package dev.nikkune.safetynet.alerts.service;

import dev.nikkune.safetynet.alerts.config.JsonDatabase;
import dev.nikkune.safetynet.alerts.model.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PersonServiceTest {

    @Mock
    private JsonDatabase jsonDatabase;

    @InjectMocks
    private PersonService personService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        List<Person> people = new ArrayList<>();
        Person john = new Person();
        john.setFirstName("John");
        john.setLastName("Doe");
        john.setAddress("123 Main St");
        john.setCity("Anytown");
        john.setZip("12345");
        john.setPhone("555-555-1234");
        john.setEmail("john.doe@example.com");
        people.add(john);
        Person jane = new Person();
        jane.setFirstName("Jane");
        jane.setLastName("Doe");
        jane.setAddress("123 Main St");
        jane.setCity("Anytown");
        jane.setZip("12345");
        jane.setPhone("555-555-5678");
        jane.setEmail("jane.doe@example.com");
        people.add(jane);
        when(jsonDatabase.people()).thenReturn(people);
    }

    @Test
    public void testGetAll() {
        //Arrange

        //Act
        List<Person> result = personService.getAll();

        //Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(jsonDatabase, times(1)).people();
    }

    @Test
    public void testGetPersonFound() {
        //Arrange
        String firstName = "John";
        String lastName = "Doe";

        //Act
        Person result = personService.get(firstName, lastName);

        //Assert
        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        verify(jsonDatabase, times(1)).people();
    }

    @Test
    public void testGetPersonNotFound() {
        //Arrange
        String firstName = "Alice";
        String lastName = "Wonderland";

        //Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> personService.get(firstName, lastName));
        assertEquals("Person not found", exception.getMessage());
        verify(jsonDatabase, times(1)).people();
    }

    @Test
    public void testGetPersonByAddressFound() {
        //Arrange
        String address = "123 Main St";

        //Act
        List<Person> result = personService.getByAddress(address);

        //Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(jsonDatabase, times(1)).people();
    }

    @Test
    public void testCreatePersonSuccess() {
        //Arrange
        Person person = new Person();
        person.setFirstName("Alice");
        person.setLastName("Wonderland");
        person.setAddress("123 Main St");
        person.setCity("Anytown");
        person.setZip("12345");
        person.setPhone("555-555-1234");
        person.setEmail("alice.wonderland@example.com");

        //Act
        personService.create(person);

        //Assert
        verify(jsonDatabase, times(2)).people();
        verify(jsonDatabase, times(1)).saveData();
        assertEquals(3, jsonDatabase.people().size());
    }

    @Test
    public void testCreatePersonExists() {
        //Arrange
        Person person = new Person();
        person.setFirstName("John");
        person.setLastName("Doe");
        person.setAddress("123 Main St");
        person.setCity("Anytown");
        person.setZip("12345");
        person.setPhone("555-555-1234");
        person.setEmail("john.doe@example.com");

        //Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> personService.create(person));
        assertEquals("Person already exists", exception.getMessage());
        verify(jsonDatabase, times(1)).people();
    }

    @Test
    public void testUpdatePersonSuccess() {
        //Arrange
        Person person = new Person();
        person.setFirstName("John");
        person.setLastName("Doe");
        person.setAddress("124 Main St");
        person.setCity("Anytown");
        person.setZip("12345");
        person.setPhone("555-555-1234");
        person.setEmail("john.doe@example.com");

        //Act
        personService.update(person);

        //Assert
        verify(jsonDatabase, times(1)).people();
        verify(jsonDatabase, times(1)).saveData();
        assertEquals("124 Main St", jsonDatabase.people().get(0).getAddress());
        assertEquals(2, jsonDatabase.people().size());
    }

    @Test
    public void testUpdatePersonNotFound() {
        //Arrange
        Person person = new Person();
        person.setFirstName("Alice");
        person.setLastName("Wonderland");
        person.setAddress("124 Main St");
        person.setCity("Anytown");
        person.setZip("12345");
        person.setPhone("555-555-1234");
        person.setEmail("alice.wonderland@example.com");

        //Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> personService.update(person));
        assertEquals("Person not found", exception.getMessage());
        verify(jsonDatabase, times(1)).people();
    }

    @Test
    public void testDeletePersonSuccess() {
        //Arrange
        String firstName = "John";
        String lastName = "Doe";

        //Act
        personService.delete(firstName, lastName);

        //Assert
        verify(jsonDatabase, times(2)).people();
        verify(jsonDatabase, times(1)).saveData();
        assertEquals(1, jsonDatabase.people().size());
    }

    @Test
    public void testDeletePersonNotFound() {
        //Arrange
        String firstName = "Alice";
        String lastName = "Wonderland";

        //Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> personService.delete(firstName, lastName));
        assertEquals("Person not found", exception.getMessage());
        verify(jsonDatabase, times(1)).people();
    }
}
