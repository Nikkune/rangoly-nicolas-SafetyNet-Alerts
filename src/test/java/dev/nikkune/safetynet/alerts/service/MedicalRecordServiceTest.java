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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MedicalRecordServiceTest {

    @Mock
    private JsonDatabase jsonDatabase;

    @InjectMocks
    private MedicalRecordService medicalRecordService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        List<MedicalRecord> records = new ArrayList<>();
        MedicalRecord johnRecord = new MedicalRecord();
        johnRecord.setFirstName("John");
        johnRecord.setLastName("Doe");
        johnRecord.setBirthdate("01/01/2000");
        johnRecord.setMedications(List.of(new String[]{"medication1", "medication2"}));
        johnRecord.setAllergies(List.of(new String[]{"allergy1", "allergy2"}));
        records.add(johnRecord);
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
        when(jsonDatabase.medicalRecords()).thenReturn(records);
    }

    @Test
    public void testGetAll() {
        // Arrange

        // Act
        List<MedicalRecord> result = medicalRecordService.getAll();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(jsonDatabase, times(1)).medicalRecords();
    }

    @Test
    public void testGetRecordFound() {
        // Arrange
        String firstName = "John";
        String lastName = "Doe";

        // Act
        MedicalRecord result = medicalRecordService.get(firstName, lastName);

        // Assert
        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        verify(jsonDatabase, times(1)).medicalRecords();
    }

    @Test
    public void testGetRecordNotFound() {
        // Arrange
        String firstName = "Alice";
        String lastName = "Wonderland";

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> medicalRecordService.get(firstName, lastName));
        assertEquals("Medical record not found", exception.getMessage());
        verify(jsonDatabase, times(1)).medicalRecords();
    }

    @Test
    public void testCreateRecordSuccess() {
        // Arrange
        MedicalRecord record = new MedicalRecord();
        record.setFirstName("Jane");
        record.setLastName("Doe");
        record.setBirthdate("01/01/2000");
        record.setMedications(List.of(new String[]{"medication1", "medication2"}));
        record.setAllergies(List.of(new String[]{"allergy1", "allergy2"}));

        // Act
        medicalRecordService.create(record);

        // Assert
        verify(jsonDatabase, times(2)).medicalRecords();
        verify(jsonDatabase, times(1)).people();
        verify(jsonDatabase, times(1)).saveData();
        assertEquals(2, jsonDatabase.medicalRecords().size());
    }

    @Test
    public void testCreateRecordPersonNotFound() {
        // Arrange
        MedicalRecord record = new MedicalRecord();
        record.setFirstName("Alice");
        record.setLastName("Wonderland");
        record.setBirthdate("01/01/2000");
        record.setMedications(List.of(new String[]{"medication1", "medication2"}));
        record.setAllergies(List.of(new String[]{"allergy1", "allergy2"}));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> medicalRecordService.create(record));
        assertEquals("Person not found", exception.getMessage());
        verify(jsonDatabase, times(1)).people();
    }

    @Test
    public void testCreateRecordExists() {
        // Arrange
        MedicalRecord record = new MedicalRecord();
        record.setFirstName("John");
        record.setLastName("Doe");
        record.setBirthdate("01/01/2000");
        record.setMedications(List.of(new String[]{"medication1", "medication2"}));
        record.setAllergies(List.of(new String[]{"allergy1", "allergy2"}));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> medicalRecordService.create(record));
        assertEquals("Medical record already exists", exception.getMessage());
        verify(jsonDatabase, times(1)).medicalRecords();
    }

    @Test
    public void testUpdateRecordSuccess() {
        // Arrange
        MedicalRecord record = new MedicalRecord();
        record.setFirstName("John");
        record.setLastName("Doe");
        record.setBirthdate("01/01/2001");
        record.setMedications(List.of(new String[]{"medication1", "medication2"}));
        record.setAllergies(List.of(new String[]{"allergy1", "allergy2"}));

        // Act
        medicalRecordService.update(record);

        // Assert
        verify(jsonDatabase, times(1)).medicalRecords();
        verify(jsonDatabase, times(1)).saveData();
        assertEquals("01/01/2001", jsonDatabase.medicalRecords().get(0).getBirthdate());
    }

    @Test
    public void testUpdateRecordNotFound() {
        // Arrange
        MedicalRecord record = new MedicalRecord();
        record.setFirstName("Alice");
        record.setLastName("Wonderland");
        record.setBirthdate("01/01/2001");
        record.setMedications(List.of(new String[]{"medication1", "medication2"}));
        record.setAllergies(List.of(new String[]{"allergy1", "allergy2"}));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> medicalRecordService.update(record));
        assertEquals("Medical record not found", exception.getMessage());
        verify(jsonDatabase, times(1)).medicalRecords();
    }

    @Test
    public void testDeleteRecordSuccess() {
        // Arrange
        String firstName = "John";
        String lastName = "Doe";

        // Act
        medicalRecordService.delete(firstName, lastName);

        // Assert
        verify(jsonDatabase, times(2)).medicalRecords();
        verify(jsonDatabase, times(1)).saveData();
        assertEquals(0, jsonDatabase.medicalRecords().size());
    }

    @Test
    public void testDeleteRecordNotFound() {
        // Arrange
        String firstName = "Alice";
        String lastName = "Wonderland";

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> medicalRecordService.delete(firstName, lastName));
        assertEquals("Medical record not found", exception.getMessage());
        verify(jsonDatabase, times(1)).medicalRecords();
    }
}
