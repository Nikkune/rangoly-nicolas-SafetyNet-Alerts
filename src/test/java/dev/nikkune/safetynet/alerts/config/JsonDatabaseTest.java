package dev.nikkune.safetynet.alerts.config;

import dev.nikkune.safetynet.alerts.model.FireStation;
import dev.nikkune.safetynet.alerts.model.MedicalRecord;
import dev.nikkune.safetynet.alerts.model.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.Resource;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.List;

import static java.nio.file.Files.readAllBytes;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JsonDatabaseTest {

    private JsonDatabase jsonDatabase;
    private Resource resourceMock;

    @BeforeEach
    void setUp() throws Exception {
        jsonDatabase = new JsonDatabase();

        String jsonContent = """
                {
                    "persons": [
                        {
                            "firstName": "John",
                            "lastName": "Doe",
                            "address": "123 Main St",
                            "city": "Anytown",
                            "zip": "12345",
                            "phone": "555-1234",
                            "email": "john.doe@example.com"
                        },
                        {
                            "firstName": "Jane",
                            "lastName": "Doe",
                            "address": "123 Main St",
                            "city": "Anytown",
                            "zip": "12345",
                            "phone": "555-5678",
                            "email": "jane.doe@example.com"
                        }
                    ],
                    "firestations": [
                        {
                            "address": "123 Main St",
                            "station": 1
                        }
                    ],
                    "medicalrecords": [
                        {
                            "firstName": "John",
                            "lastName": "Doe",
                            "birthdate": "01/01/2000",
                            "medications": [
                                "medication:1",
                                "medication:2"
                            ],
                            "allergies": [
                                "allergy1",
                                "allergy2"
                            ]
                        }
                    ]
                }
                """;

        resourceMock = mock(Resource.class);
        when(resourceMock.getInputStream()).thenReturn(new ByteArrayInputStream(jsonContent.getBytes()));

        // Replace the real resource field with the mock
        var ressourceField = JsonDatabase.class.getDeclaredField("resource");
        ressourceField.setAccessible(true);
        ressourceField.set(jsonDatabase, resourceMock);
    }

    @Test
    public void testLoadData() {
        // Arrange

        // Act
        assertDoesNotThrow(() -> jsonDatabase.loadData());

        List<Person> people = jsonDatabase.people();
        List<FireStation> fireStations = jsonDatabase.fireStations();
        List<MedicalRecord> medicalRecords = jsonDatabase.medicalRecords();

        // Assert
        assertNotNull(people);
        assertEquals(2, people.size());
        assertEquals("John", people.get(0).getFirstName());
        assertNotNull(people.get(0).getMedicalRecord());
        assertNull(people.get(1).getMedicalRecord());

        assertNotNull(fireStations);
        assertEquals(1, fireStations.size());
        assertEquals(2, fireStations.get(0).getPersons().size());

        assertNotNull(medicalRecords);
        assertEquals(1, medicalRecords.size());
        assertEquals("John", medicalRecords.get(0).getFirstName());
    }

    @Test
    public void testSaveData() throws Exception {
        // Arrange
        jsonDatabase.loadData();

        File tempFile = File.createTempFile("test", ".json");
        when(resourceMock.getFile()).thenReturn(tempFile);

        // Act
        assertDoesNotThrow(() -> jsonDatabase.saveData());

        // Assert
        assertTrue(tempFile.length() > 0);

        String writtenContent = new String(readAllBytes(tempFile.toPath()));
        assertTrue(writtenContent.contains("\"persons\""));
        assertTrue(writtenContent.contains("\"firestations\""));
        assertTrue(writtenContent.contains("\"medicalrecords\""));

        // Clean up
        tempFile.delete();
    }

    @Test
    public void testLoadDataThrowsException() throws Exception {
        // Arrange
        when(resourceMock.getInputStream()).thenThrow(new RuntimeException("Test exception"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> jsonDatabase.loadData());
    }

    @Test
    public void testSaveDataThrowsException() throws Exception {
        // Arrange
        when(resourceMock.getFile()).thenThrow(new RuntimeException("Test exception"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> jsonDatabase.saveData());
    }
}
