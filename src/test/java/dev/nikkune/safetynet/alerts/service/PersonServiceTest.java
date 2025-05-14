package dev.nikkune.safetynet.alerts.service;

import dev.nikkune.safetynet.alerts.config.JsonDatabase;
import dev.nikkune.safetynet.alerts.model.MedicalRecord;
import dev.nikkune.safetynet.alerts.model.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
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

        // Set up test data for people
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

        Person alice = new Person();
        alice.setFirstName("Alice");
        alice.setLastName("Smith");
        alice.setAddress("456 Oak St");
        alice.setCity("Othertown");
        alice.setZip("67890");
        alice.setPhone("555-555-9012");
        alice.setEmail("alice.smith@example.com");
        people.add(alice);

        when(jsonDatabase.people()).thenReturn(people);

        // Set up test data for medical records
        List<MedicalRecord> medicalRecords = new ArrayList<>();

        MedicalRecord johnMedical = new MedicalRecord();
        johnMedical.setFirstName("John");
        johnMedical.setLastName("Doe");
        johnMedical.setBirthdate("01/01/1980");
        johnMedical.setMedications(Arrays.asList("medication1", "medication2"));
        johnMedical.setAllergies(List.of("allergy1"));
        medicalRecords.add(johnMedical);

        MedicalRecord janeMedical = new MedicalRecord();
        janeMedical.setFirstName("Jane");
        janeMedical.setLastName("Doe");
        janeMedical.setBirthdate("02/02/1985");
        janeMedical.setMedications(List.of("medication3"));
        janeMedical.setAllergies(Arrays.asList("allergy2", "allergy3"));
        medicalRecords.add(janeMedical);

        when(jsonDatabase.medicalRecords()).thenReturn(medicalRecords);
    }

    @Test
    public void testGetAll() {
        // Act
        List<Person> result = personService.getAll();

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
        verify(jsonDatabase, times(1)).people();
    }

    @Test
    public void testGetPersonFound() {
        // Arrange
        String firstName = "John";
        String lastName = "Doe";

        // Act
        Person result = personService.get(firstName, lastName);

        // Assert
        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertEquals("123 Main St", result.getAddress());
        assertEquals("Anytown", result.getCity());
        assertEquals("12345", result.getZip());
        assertEquals("555-555-1234", result.getPhone());
        assertEquals("john.doe@example.com", result.getEmail());
        verify(jsonDatabase, times(1)).people();
    }

    @Test
    public void testGetPersonNotFound() {
        // Arrange
        String firstName = "Nonexistent";
        String lastName = "Person";

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> personService.get(firstName, lastName));
        assertEquals("Person not found", exception.getMessage());
        verify(jsonDatabase, times(1)).people();
    }

    @Test
    public void testGetByAddressFound() {
        // Arrange
        String address = "123 Main St";

        // Act
        List<Person> result = personService.getByAddress(address);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(p -> p.getFirstName().equals("John") && p.getLastName().equals("Doe")));
        assertTrue(result.stream().anyMatch(p -> p.getFirstName().equals("Jane") && p.getLastName().equals("Doe")));
        verify(jsonDatabase, times(1)).people();
    }

    @Test
    public void testGetByAddressNotFound() {
        // Arrange
        String address = "789 Nonexistent St";

        // Act
        List<Person> result = personService.getByAddress(address);

        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());
        verify(jsonDatabase, times(1)).people();
    }

    @Test
    public void testCreatePersonSuccess() {
        // Arrange
        Person newPerson = new Person();
        newPerson.setFirstName("Bob");
        newPerson.setLastName("Johnson");
        newPerson.setAddress("789 Pine St");
        newPerson.setCity("Newtown");
        newPerson.setZip("54321");
        newPerson.setPhone("555-555-4321");
        newPerson.setEmail("bob.johnson@example.com");

        // Act
        personService.create(newPerson);

        // Assert
        verify(jsonDatabase, times(2)).people();
        verify(jsonDatabase, times(1)).saveData();
    }

    @Test
    public void testCreatePersonAlreadyExists() {
        // Arrange
        Person existingPerson = new Person();
        existingPerson.setFirstName("John");
        existingPerson.setLastName("Doe");
        existingPerson.setAddress("Different Address");
        existingPerson.setCity("Different City");
        existingPerson.setZip("Different Zip");
        existingPerson.setPhone("Different Phone");
        existingPerson.setEmail("different.email@example.com");

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> personService.create(existingPerson));
        assertEquals("Person already exists", exception.getMessage());
        verify(jsonDatabase, times(1)).people();
        verify(jsonDatabase, never()).saveData();
    }

    @Test
    public void testUpdatePersonSuccess() {
        // Arrange
        Person updatedPerson = new Person();
        updatedPerson.setFirstName("John");
        updatedPerson.setLastName("Doe");
        updatedPerson.setAddress("Updated Address");
        updatedPerson.setCity("Updated City");
        updatedPerson.setZip("Updated Zip");
        updatedPerson.setPhone("Updated Phone");
        updatedPerson.setEmail("updated.email@example.com");

        // Act
        personService.update(updatedPerson);

        // Assert
        verify(jsonDatabase, times(1)).people();
        verify(jsonDatabase, times(1)).saveData();

        // Verify the person was updated correctly
        Person john = jsonDatabase.people().get(0);
        assertEquals("Updated Address", john.getAddress());
        assertEquals("Updated City", john.getCity());
        assertEquals("Updated Zip", john.getZip());
        assertEquals("Updated Phone", john.getPhone());
        assertEquals("updated.email@example.com", john.getEmail());
    }

    @Test
    public void testUpdatePersonNotFound() {
        // Arrange
        Person nonexistentPerson = new Person();
        nonexistentPerson.setFirstName("Nonexistent");
        nonexistentPerson.setLastName("Person");
        nonexistentPerson.setAddress("Some Address");
        nonexistentPerson.setCity("Some City");
        nonexistentPerson.setZip("Some Zip");
        nonexistentPerson.setPhone("Some Phone");
        nonexistentPerson.setEmail("some.email@example.com");

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> personService.update(nonexistentPerson));
        assertEquals("Person not found", exception.getMessage());
        verify(jsonDatabase, times(1)).people();
        verify(jsonDatabase, never()).saveData();
    }

    @Test
    public void testDeletePersonWithMedicalRecordSuccess() {
        // Arrange
        String firstName = "John";
        String lastName = "Doe";

        // Act
        personService.delete(firstName, lastName);

        // Assert
        verify(jsonDatabase, times(2)).people();
        verify(jsonDatabase, times(2)).medicalRecords();
        verify(jsonDatabase, times(1)).saveData();
    }

    @Test
    public void testDeletePersonWithoutMedicalRecordSuccess() {
        // Arrange
        String firstName = "Alice";
        String lastName = "Smith";

        // Act
        personService.delete(firstName, lastName);

        // Assert
        verify(jsonDatabase, times(2)).people();
        verify(jsonDatabase, times(1)).medicalRecords();
        verify(jsonDatabase, times(1)).saveData();
    }

    @Test
    public void testDeletePersonNotFound() {
        // Arrange
        String firstName = "Nonexistent";
        String lastName = "Person";

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> personService.delete(firstName, lastName));
        assertEquals("Person not found", exception.getMessage());
        verify(jsonDatabase, times(1)).people();
        verify(jsonDatabase, times(1)).medicalRecords();
        verify(jsonDatabase, never()).saveData();
    }
}
