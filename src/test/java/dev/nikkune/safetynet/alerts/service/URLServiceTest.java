package dev.nikkune.safetynet.alerts.service;

import dev.nikkune.safetynet.alerts.config.JsonDatabase;
import dev.nikkune.safetynet.alerts.model.FireResponse;
import dev.nikkune.safetynet.alerts.model.FireStation;
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

public class URLServiceTest {

    @Mock
    private JsonDatabase jsonDatabase;

    @InjectMocks
    private URLService urlService;

    private List<Person> people;
    private List<FireStation> fireStations;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        
        // Setup people
        people = new ArrayList<>();
        
        // Adult person 1
        Person john = new Person();
        john.setFirstName("John");
        john.setLastName("Doe");
        john.setAddress("123 Main St");
        john.setCity("Culver");
        john.setZip("12345");
        john.setPhone("555-555-1234");
        john.setEmail("john.doe@example.com");
        
        MedicalRecord johnMedical = new MedicalRecord();
        johnMedical.setFirstName("John");
        johnMedical.setLastName("Doe");
        johnMedical.setBirthdate("01/01/1980");
        johnMedical.setMedications(Arrays.asList("medication1", "medication2"));
        johnMedical.setAllergies(Arrays.asList("allergy1"));
        john.setMedicalRecord(johnMedical);
        
        people.add(john);
        
        // Adult person 2
        Person jane = new Person();
        jane.setFirstName("Jane");
        jane.setLastName("Doe");
        jane.setAddress("123 Main St");
        jane.setCity("Culver");
        jane.setZip("12345");
        jane.setPhone("555-555-5678");
        jane.setEmail("jane.doe@example.com");
        
        MedicalRecord janeMedical = new MedicalRecord();
        janeMedical.setFirstName("Jane");
        janeMedical.setLastName("Doe");
        janeMedical.setBirthdate("02/02/1985");
        janeMedical.setMedications(Arrays.asList("medication3"));
        janeMedical.setAllergies(Arrays.asList("allergy2", "allergy3"));
        jane.setMedicalRecord(janeMedical);
        
        people.add(jane);
        
        // Child person
        Person billy = new Person();
        billy.setFirstName("Billy");
        billy.setLastName("Doe");
        billy.setAddress("123 Main St");
        billy.setCity("Culver");
        billy.setZip("12345");
        billy.setPhone("555-555-9012");
        billy.setEmail("billy.doe@example.com");
        
        MedicalRecord billyMedical = new MedicalRecord();
        billyMedical.setFirstName("Billy");
        billyMedical.setLastName("Doe");
        billyMedical.setBirthdate("03/03/2015");
        billyMedical.setMedications(Arrays.asList("medication4"));
        billyMedical.setAllergies(new ArrayList<>());
        billy.setMedicalRecord(billyMedical);
        
        people.add(billy);
        
        // Person at different address
        Person alice = new Person();
        alice.setFirstName("Alice");
        alice.setLastName("Smith");
        alice.setAddress("456 Oak St");
        alice.setCity("Culver");
        alice.setZip("12345");
        alice.setPhone("555-555-3456");
        alice.setEmail("alice.smith@example.com");
        
        MedicalRecord aliceMedical = new MedicalRecord();
        aliceMedical.setFirstName("Alice");
        aliceMedical.setLastName("Smith");
        aliceMedical.setBirthdate("04/04/1990");
        aliceMedical.setMedications(new ArrayList<>());
        aliceMedical.setAllergies(Arrays.asList("allergy4"));
        alice.setMedicalRecord(aliceMedical);
        
        people.add(alice);
        
        // Person in different city
        Person bob = new Person();
        bob.setFirstName("Bob");
        bob.setLastName("Johnson");
        bob.setAddress("789 Pine St");
        bob.setCity("Othertown");
        bob.setZip("67890");
        bob.setPhone("555-555-7890");
        bob.setEmail("bob.johnson@example.com");
        
        MedicalRecord bobMedical = new MedicalRecord();
        bobMedical.setFirstName("Bob");
        bobMedical.setLastName("Johnson");
        bobMedical.setBirthdate("05/05/1975");
        bobMedical.setMedications(Arrays.asList("medication5", "medication6"));
        bobMedical.setAllergies(Arrays.asList("allergy5"));
        bob.setMedicalRecord(bobMedical);
        
        people.add(bob);
        
        // Setup fire stations
        fireStations = new ArrayList<>();
        
        // Fire station 1
        FireStation station1 = new FireStation();
        station1.setAddress("123 Main St");
        station1.setStation("1");
        station1.setPersons(Arrays.asList(john, jane, billy));
        fireStations.add(station1);
        
        // Fire station 2
        FireStation station2 = new FireStation();
        station2.setAddress("456 Oak St");
        station2.setStation("2");
        station2.setPersons(Arrays.asList(alice));
        fireStations.add(station2);
        
        // Fire station 3
        FireStation station3 = new FireStation();
        station3.setAddress("789 Pine St");
        station3.setStation("3");
        station3.setPersons(Arrays.asList(bob));
        fireStations.add(station3);
        
        when(jsonDatabase.people()).thenReturn(people);
        when(jsonDatabase.fireStations()).thenReturn(fireStations);
    }

    @Test
    public void testGetPersonCoveredByStation() {
        // Arrange
        String stationNumber = "1";
        
        // Act
        List<Person> result = urlService.getPersonCoveredByStation(stationNumber);
        
        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
        assertTrue(result.stream().anyMatch(p -> p.getFirstName().equals("John")));
        assertTrue(result.stream().anyMatch(p -> p.getFirstName().equals("Jane")));
        assertTrue(result.stream().anyMatch(p -> p.getFirstName().equals("Billy")));
        verify(jsonDatabase, times(1)).fireStations();
    }
    
    @Test
    public void testGetPersonCoveredByStationNotFound() {
        // Arrange
        String stationNumber = "4";
        
        // Act
        List<Person> result = urlService.getPersonCoveredByStation(stationNumber);
        
        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());
        verify(jsonDatabase, times(1)).fireStations();
    }

    @Test
    public void testGetChildAlert() {
        // Arrange
        String address = "123 Main St";
        
        // Act
        List<Person> result = urlService.getChildAlert(address);
        
        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Billy", result.get(0).getFirstName());
        verify(jsonDatabase, times(1)).people();
    }
    
    @Test
    public void testGetChildAlertNoChildren() {
        // Arrange
        String address = "456 Oak St";
        
        // Act
        List<Person> result = urlService.getChildAlert(address);
        
        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());
        verify(jsonDatabase, times(1)).people();
    }
    
    @Test
    public void testGetChildAlertAddressNotFound() {
        // Arrange
        String address = "999 Nonexistent St";
        
        // Act
        List<Person> result = urlService.getChildAlert(address);
        
        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());
        verify(jsonDatabase, times(1)).people();
    }

    @Test
    public void testGetPhoneAlert() {
        // Arrange
        String stationNumber = "1";
        
        // Act
        List<String> result = urlService.getPhoneAlert(stationNumber);
        
        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
        assertTrue(result.contains("555-555-1234"));
        assertTrue(result.contains("555-555-5678"));
        assertTrue(result.contains("555-555-9012"));
        verify(jsonDatabase, times(1)).fireStations();
    }
    
    @Test
    public void testGetPhoneAlertStationNotFound() {
        // Arrange
        String stationNumber = "4";
        
        // Act
        List<String> result = urlService.getPhoneAlert(stationNumber);
        
        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());
        verify(jsonDatabase, times(1)).fireStations();
    }

    @Test
    public void testGetFire() {
        // Arrange
        String address = "123 Main St";
        
        // Act
        FireResponse result = urlService.getFire(address);
        
        // Assert
        assertNotNull(result);
        assertNotNull(result.getFireStations());
        assertNotNull(result.getPeople());
        assertEquals(1, result.getFireStations().size());
        assertEquals(3, result.getPeople().size());
        assertEquals("1", result.getFireStations().get(0).getStation());
        assertTrue(result.getPeople().stream().anyMatch(p -> p.getFirstName().equals("John")));
        assertTrue(result.getPeople().stream().anyMatch(p -> p.getFirstName().equals("Jane")));
        assertTrue(result.getPeople().stream().anyMatch(p -> p.getFirstName().equals("Billy")));
        verify(jsonDatabase, times(1)).fireStations();
        verify(jsonDatabase, times(1)).people();
    }
    
    @Test
    public void testGetFireAddressNotFound() {
        // Arrange
        String address = "999 Nonexistent St";
        
        // Act
        FireResponse result = urlService.getFire(address);
        
        // Assert
        assertNotNull(result);
        assertNotNull(result.getFireStations());
        assertNotNull(result.getPeople());
        assertEquals(0, result.getFireStations().size());
        assertEquals(0, result.getPeople().size());
        verify(jsonDatabase, times(1)).fireStations();
        verify(jsonDatabase, times(1)).people();
    }

    @Test
    public void testGetFloodPersonCoveredByStation() {
        // Arrange
        List<String> stationNumbers = Arrays.asList("1", "2");
        
        // Act
        List<Person> result = urlService.getFloodPersonCoveredByStation(stationNumbers);
        
        // Assert
        assertNotNull(result);
        assertEquals(4, result.size());
        assertTrue(result.stream().anyMatch(p -> p.getFirstName().equals("John")));
        assertTrue(result.stream().anyMatch(p -> p.getFirstName().equals("Jane")));
        assertTrue(result.stream().anyMatch(p -> p.getFirstName().equals("Billy")));
        assertTrue(result.stream().anyMatch(p -> p.getFirstName().equals("Alice")));
        verify(jsonDatabase, times(2)).fireStations();
    }
    
    @Test
    public void testGetFloodPersonCoveredByStationNotFound() {
        // Arrange
        List<String> stationNumbers = Arrays.asList("4", "5");
        
        // Act
        List<Person> result = urlService.getFloodPersonCoveredByStation(stationNumbers);
        
        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());
        verify(jsonDatabase, times(2)).fireStations();
    }

    @Test
    public void testGetPersonInfo() {
        // Arrange
        String lastName = "Doe";
        
        // Act
        List<Person> result = urlService.getPersonInfo(lastName);
        
        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
        assertTrue(result.stream().anyMatch(p -> p.getFirstName().equals("John")));
        assertTrue(result.stream().anyMatch(p -> p.getFirstName().equals("Jane")));
        assertTrue(result.stream().anyMatch(p -> p.getFirstName().equals("Billy")));
        verify(jsonDatabase, times(1)).people();
    }
    
    @Test
    public void testGetPersonInfoNotFound() {
        // Arrange
        String lastName = "Nonexistent";
        
        // Act
        List<Person> result = urlService.getPersonInfo(lastName);
        
        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());
        verify(jsonDatabase, times(1)).people();
    }

    @Test
    public void testGetCommunityEmail() {
        // Arrange
        String city = "Culver";
        
        // Act
        List<String> result = urlService.getCommunityEmail(city);
        
        // Assert
        assertNotNull(result);
        assertEquals(4, result.size());
        assertTrue(result.contains("john.doe@example.com"));
        assertTrue(result.contains("jane.doe@example.com"));
        assertTrue(result.contains("billy.doe@example.com"));
        assertTrue(result.contains("alice.smith@example.com"));
        verify(jsonDatabase, times(1)).people();
    }
    
    @Test
    public void testGetCommunityEmailCityNotFound() {
        // Arrange
        String city = "Nonexistent";
        
        // Act
        List<String> result = urlService.getCommunityEmail(city);
        
        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());
        verify(jsonDatabase, times(1)).people();
    }
}