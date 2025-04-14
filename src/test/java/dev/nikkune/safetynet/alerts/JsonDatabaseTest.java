package dev.nikkune.safetynet.alerts;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.nikkune.safetynet.alerts.config.JsonDatabase;
import dev.nikkune.safetynet.alerts.model.FireStation;
import dev.nikkune.safetynet.alerts.model.MedicalRecord;
import dev.nikkune.safetynet.alerts.model.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.WritableResource;

import java.io.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JsonDatabaseTest {
    private JsonDatabase jsonDatabase;
    private ResourceLoader resourceLoader;

    @BeforeEach
    public void setup() {
        resourceLoader = mock(ResourceLoader.class);
        jsonDatabase = new JsonDatabase(resourceLoader);
    }

    @Test
    public void testLoadData() throws Exception {
        //Arrange
        String json = """
                {
                  "persons": [
                    {
                      "firstName": "Jane",
                      "lastName": "Doe",
                      "address": "123 Main St",
                      "city": "Paris",
                      "zip": "75000",
                      "phone": "123-456-7890",
                      "email": "jane@example.com"
                    }
                  ],
                  "firestations": [
                    {
                      "address": "123 Main St",
                      "station": "1"
                    }
                  ],
                  "medicalrecords": [
                    {
                      "firstName": "Jane",
                      "lastName": "Doe",
                      "birthdate": "01/01/1990",
                      "medications": ["aspirin"],
                      "allergies": ["pollen"]
                    }
                  ]
                }
                """;
        File tempFile = File.createTempFile("data", ".json");
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write(json);
        }
        Resource mockInputResource = mock(Resource.class);
        when(resourceLoader.getResource("data.json")).thenReturn(mockInputResource);
        when(mockInputResource.getInputStream()).thenReturn(new FileInputStream(tempFile));

        //Act
        jsonDatabase.loadData();

        //Assert
        assertEquals(1, jsonDatabase.people().size());
        assertEquals("Jane", jsonDatabase.people().get(0).getFirstName());
        assertNotNull(jsonDatabase.people().get(0).getMedicalRecord());

        assertEquals(1, jsonDatabase.fireStations().size());
        assertEquals("123 Main St", jsonDatabase.fireStations().get(0).getAddress());
        assertEquals(1, jsonDatabase.fireStations().get(0).getPersons().size());

        assertEquals(1, jsonDatabase.medicalRecords().size());
        assertEquals("Jane", jsonDatabase.medicalRecords().get(0).getFirstName());
    }

    @Test
    public void testSaveData() throws Exception {
        //Arrange
        testLoadData(); // To populate data to save

        File tempFile = File.createTempFile("saved", ".json");
        WritableResource mockOutputResource = mock(WritableResource.class);
        when(resourceLoader.getResource("classpath:data.json")).thenReturn(mockOutputResource);
        when(mockOutputResource.getFile()).thenReturn(tempFile);

        //Act
        jsonDatabase.saveData();

        //Assert
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(tempFile);
        assertTrue(rootNode.has("persons"));
        assertTrue(rootNode.has("firestations"));
        assertTrue(rootNode.has("medicalrecords"));
        assertFalse(rootNode.get("persons").get(0).has("medicalRecord"));
        assertFalse(rootNode.get("firestations").get(0).has("persons"));
    }

    @Test
    public void testGetters() throws Exception {
        //Arrange
        testLoadData();

        //Act
        List<Person> people = jsonDatabase.people();
        List<FireStation> stations = jsonDatabase.fireStations();
        List<MedicalRecord> records = jsonDatabase.medicalRecords();

        //Assert
        assertEquals(1, people.size());
        assertEquals(1, stations.size());
        assertEquals(1, records.size());
    }

    @Test
    public void testLoadDataFailToRead() throws Exception {
        //Arrange
        Resource mockInputResource = mock(Resource.class);
        when(resourceLoader.getResource("data.json")).thenReturn(mockInputResource);
        when(mockInputResource.getInputStream()).thenThrow(new FileNotFoundException());

        //Act & Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> jsonDatabase.loadData());
        assertEquals("Failed to load data from data.json", ex.getMessage());
    }

    @Test
    public void testSaveDataFailToWrite() throws Exception {
        //Arrange
        testLoadData();

        Resource mockOutputResource = mock(WritableResource.class);
        when(resourceLoader.getResource("classpath:data.json")).thenReturn(mockOutputResource);
        when(mockOutputResource.getFile()).thenThrow(new IOException());

        //Act & Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> jsonDatabase.saveData());
        assertEquals("Failed to save data to data.json", ex.getMessage());
    }
}
