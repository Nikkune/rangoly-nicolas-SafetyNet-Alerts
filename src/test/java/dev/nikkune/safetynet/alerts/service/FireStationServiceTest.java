package dev.nikkune.safetynet.alerts.service;

import dev.nikkune.safetynet.alerts.config.JsonDatabase;
import dev.nikkune.safetynet.alerts.model.FireStation;
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

public class FireStationServiceTest {

    @Mock
    private JsonDatabase jsonDatabase;

    @InjectMocks
    private FireStationService fireStationService;

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
        List<FireStation> fireStations = new ArrayList<>();
        FireStation fireStation = new FireStation();
        fireStation.setAddress("123 Main St");
        fireStation.setStation("1");
        fireStation.setPersons(people);
        fireStations.add(fireStation);
        when(jsonDatabase.fireStations()).thenReturn(fireStations);
    }

    @Test
    public void testGetAll() {
        // Arrange

        // Act
        List<FireStation> result = fireStationService.getAll();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(jsonDatabase, times(1)).fireStations();
    }

    @Test
    public void testGetFireStationByAddressFound() {
        // Arrange
        String address = "123 Main St";

        // Act
        List<FireStation> results = fireStationService.getByAddress(address);

        // Assert
        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals("123 Main St", results.get(0).getAddress());
        assertEquals("1", results.get(0).getStation());
        assertEquals(2, results.get(0).getPersons().size());
        verify(jsonDatabase, times(1)).fireStations();
    }

    @Test
    public void testGetFireStationByAddressNotFound() {
        // Arrange
        String address = "456 Main St";
        // Act
        List<FireStation> results = fireStationService.getByAddress(address);

        // Assert
        assertNotNull(results);
        assertEquals(0, results.size());
        verify(jsonDatabase, times(1)).fireStations();
    }

    @Test
    public void testGetFireStationByStationFound() {
        // Arrange
        String station = "1";

        // Act
        List<FireStation> result = fireStationService.get(station);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("123 Main St", result.get(0).getAddress());
        verify(jsonDatabase, times(1)).fireStations();
    }

    @Test
    public void testGetFireStationByStationNotFound() {
        // Arrange
        String station = "2";

        // Act
        List<FireStation> results = fireStationService.get(station);

        // Assert
        assertNotNull(results);
        assertEquals(0, results.size());
        verify(jsonDatabase, times(1)).fireStations();
    }

    @Test
    public void testCreateFireStationSuccess() {
        // Arrange
        FireStation fireStation = new FireStation();
        fireStation.setAddress("456 Main St");
        fireStation.setStation("2");

        // Act
        fireStationService.create(fireStation);

        // Assert
        verify(jsonDatabase, times(2)).fireStations();
        verify(jsonDatabase, times(1)).saveData();
        assertEquals(2, jsonDatabase.fireStations().size());
    }

    @Test
    public void testCreateFireStationAlreadyExists() {
        // Arrange
        FireStation fireStation = new FireStation();
        fireStation.setAddress("123 Main St");
        fireStation.setStation("1");

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> fireStationService.create(fireStation));
        assertEquals("Fire station already exists", exception.getMessage());
        verify(jsonDatabase, times(1)).fireStations();
    }

    @Test
    public void testUpdateFireStationSuccess() {
        // Arrange
        FireStation fireStation = new FireStation();
        fireStation.setAddress("123 Main St");
        fireStation.setStation("2");

        // Act
        fireStationService.update(fireStation);

        // Assert
        verify(jsonDatabase, times(1)).fireStations();
        verify(jsonDatabase, times(1)).saveData();
        assertEquals(1, jsonDatabase.fireStations().size());
        assertEquals("123 Main St", jsonDatabase.fireStations().get(0).getAddress());
        assertEquals("2", jsonDatabase.fireStations().get(0).getStation());
        assertEquals(2, jsonDatabase.fireStations().get(0).getPersons().size());
    }

    @Test
    public void testUpdateFireStationNotFound() {
        // Arrange
        FireStation fireStation = new FireStation();
        fireStation.setAddress("456 Main St");
        fireStation.setStation("2");

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> fireStationService.update(fireStation));
        assertEquals("Fire station not found", exception.getMessage());
        verify(jsonDatabase, times(1)).fireStations();
    }

    @Test
    public void testDeleteFireStationByAddressSuccess() {
        // Arrange
        String address = "123 Main St";

        // Act
        fireStationService.deleteByAddress(address);

        // Assert
        verify(jsonDatabase, times(2)).fireStations();
        verify(jsonDatabase, times(1)).saveData();
        assertEquals(0, jsonDatabase.fireStations().size());
    }

    @Test
    public void testDeleteFireStationByAddressNotFound() {
        // Arrange
        String address = "456 Main St";

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> fireStationService.deleteByAddress(address));
        assertEquals("Fire station not found", exception.getMessage());
        verify(jsonDatabase, times(1)).fireStations();
    }

    @Test
    public void testDeleteFireStationByStationSuccess() {
        // Arrange
        String station = "1";

        // Act
        fireStationService.deleteByStation(station);

        // Assert
        verify(jsonDatabase, times(2)).fireStations();
        verify(jsonDatabase, times(1)).saveData();
        assertEquals(0, jsonDatabase.fireStations().size());
    }

    @Test
    public void testDeleteFireStationByStationNotFound() {
        // Arrange
        String station = "2";

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> fireStationService.deleteByStation(station));
        assertEquals("Fire station not found", exception.getMessage());
        verify(jsonDatabase, times(1)).fireStations();
    }
}
