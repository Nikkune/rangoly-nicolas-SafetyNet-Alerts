package dev.nikkune.safetynet.alerts.service;

import dev.nikkune.safetynet.alerts.config.JsonDatabase;
import dev.nikkune.safetynet.alerts.dto.*;
import dev.nikkune.safetynet.alerts.mapper.*;
import dev.nikkune.safetynet.alerts.model.FireStation;
import dev.nikkune.safetynet.alerts.model.MedicalRecord;
import dev.nikkune.safetynet.alerts.model.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class URLServiceTest {

    @Mock
    private JsonDatabase jsonDatabase;

    @Mock
    private FireStationCoveragePersonMapper coveragePersonMapper;

    @Mock
    private ChildAlertChildMapper childMapper;

    @Mock
    private PersonBaseMapper personBaseMapper;

    @Mock
    private FirePersonMapper firePersonMapper;

    @Mock
    private PersonInfoMapper personInfoMapper;

    @InjectMocks
    private URLService urlService;

    private List<Person> people;
    private List<FireStation> fireStations;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Setup test data
        setupTestData();

        // Setup mock behavior
        when(jsonDatabase.people()).thenReturn(people);
        when(jsonDatabase.fireStations()).thenReturn(fireStations);
    }

    private void setupTestData() {
        // Create people
        people = new ArrayList<>();

        Person person1 = new Person();
        person1.setFirstName("John");
        person1.setLastName("Doe");
        person1.setAddress("123 Main St");
        person1.setCity("Anytown");
        person1.setZip("12345");
        person1.setPhone("555-555-1234");
        person1.setEmail("john.doe@example.com");

        MedicalRecord medicalRecord1 = new MedicalRecord();
        medicalRecord1.setBirthdate("01/01/1993");
        medicalRecord1.setMedications(List.of("medication1", "medication2"));
        medicalRecord1.setAllergies(List.of("allergy1"));
        person1.setMedicalRecord(medicalRecord1);

        Person person2 = new Person();
        person2.setFirstName("Jane");
        person2.setLastName("Doe");
        person2.setAddress("123 Main St");
        person2.setCity("Anytown");
        person2.setZip("12345");
        person2.setPhone("555-555-5678");
        person2.setEmail("jane.doe@example.com");

        MedicalRecord medicalRecord2 = new MedicalRecord();
        medicalRecord2.setBirthdate("02/15/1995");
        medicalRecord2.setMedications(List.of("medication3"));
        medicalRecord2.setAllergies(List.of("allergy2", "allergy3"));
        person2.setMedicalRecord(medicalRecord2);

        Person child = new Person();
        child.setFirstName("Jimmy");
        child.setLastName("Doe");
        child.setAddress("123 Main St");
        child.setCity("Anytown");
        child.setZip("12345");
        child.setPhone("555-555-9012");
        child.setEmail("jimmy.doe@example.com");

        MedicalRecord childMedicalRecord = new MedicalRecord();
        childMedicalRecord.setBirthdate("01/15/2013");
        childMedicalRecord.setMedications(List.of());
        childMedicalRecord.setAllergies(List.of("allergy4"));
        child.setMedicalRecord(childMedicalRecord);

        people.add(person1);
        people.add(person2);
        people.add(child);

        // Create fire stations
        fireStations = new ArrayList<>();

        FireStation fireStation1 = new FireStation();
        fireStation1.setStation("1");
        fireStation1.setAddress("123 Main St");
        fireStation1.setPersons(List.of(person1, person2, child));

        FireStation fireStation2 = new FireStation();
        fireStation2.setStation("2");
        fireStation2.setAddress("456 Oak St");

        Person person3 = new Person();
        person3.setFirstName("Bob");
        person3.setLastName("Smith");
        person3.setAddress("456 Oak St");
        person3.setCity("Othertown");
        person3.setZip("54321");
        person3.setPhone("555-555-4321");
        person3.setEmail("bob.smith@example.com");

        MedicalRecord medicalRecord3 = new MedicalRecord();
        medicalRecord3.setBirthdate("05/20/1983");
        medicalRecord3.setMedications(List.of("medication4"));
        medicalRecord3.setAllergies(List.of());
        person3.setMedicalRecord(medicalRecord3);

        fireStation2.setPersons(List.of(person3));

        fireStations.add(fireStation1);
        fireStations.add(fireStation2);
    }

    @Test
    public void testGetPersonCoveredByStation() {
        // Arrange
        FireStationCoveragePersonDTO personDTO = new FireStationCoveragePersonDTO();
        personDTO.setFirstName("John");
        personDTO.setLastName("Doe");
        personDTO.setAddress("123 Main St");
        personDTO.setPhone("555-555-1234");

        when(coveragePersonMapper.toDTO(any(Person.class))).thenReturn(personDTO);

        // Act
        FireStationCoverageDTO result = urlService.getPersonCoveredByStation("1");

        // Assert
        assertNotNull(result);
        assertEquals(2, result.getNumberOfAdults());
        assertEquals(1, result.getNumberOfChildren());
        assertEquals(3, result.getPersons().size());

        verify(coveragePersonMapper, times(3)).toDTO(any(Person.class));
    }

    @Test
    public void testGetChildAlert() {
        // Arrange
        ChildAlertChildDTO childDTO = new ChildAlertChildDTO();
        childDTO.setFirstName("Jimmy");
        childDTO.setLastName("Doe");
        childDTO.setAge(10);

        PersonBaseDTO adultDTO = new PersonBaseDTO();
        adultDTO.setFirstName("John");
        adultDTO.setLastName("Doe");

        when(childMapper.toDTO(any(Person.class))).thenReturn(childDTO);
        when(personBaseMapper.toDTO(any(Person.class))).thenReturn(adultDTO);

        // Act
        List<ChildAlertDTO> result = urlService.getChildAlert("123 Main St");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Jimmy", result.get(0).getChild().getFirstName());
        assertEquals("Doe", result.get(0).getChild().getLastName());
        assertEquals(10, result.get(0).getChild().getAge());
        assertEquals(2, result.get(0).getOtherHouseholdMembers().size());

        verify(childMapper, times(1)).toDTO(any(Person.class));
        verify(personBaseMapper, times(2)).toDTO(any(Person.class));
    }

    @Test
    public void testGetChildAlert_NoChildren() {
        // Act
        List<ChildAlertDTO> result = urlService.getChildAlert("456 Oak St");

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(childMapper, never()).toDTO(any(Person.class));
        verify(personBaseMapper, never()).toDTO(any(Person.class));
    }

    @Test
    public void testGetPhoneAlert() {
        // Act
        Set<String> result = urlService.getPhoneAlert("1");

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
        assertTrue(result.contains("555-555-1234"));
        assertTrue(result.contains("555-555-5678"));
        assertTrue(result.contains("555-555-9012"));
    }

    @Test
    public void testGetFire() {
        // Arrange
        FirePersonDTO firePersonDTO = new FirePersonDTO();
        firePersonDTO.setFirstName("John");
        firePersonDTO.setLastName("Doe");
        firePersonDTO.setPhone("555-555-1234");
        firePersonDTO.setAge(30);
        firePersonDTO.setMedications(List.of("medication1", "medication2"));
        firePersonDTO.setAllergies(List.of("allergy1"));

        when(firePersonMapper.toDTO(any(Person.class))).thenReturn(firePersonDTO);

        // Act
        List<FireAddressDTO> result = urlService.getFire("123 Main St");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("1", result.get(0).getStationNumber());
        assertEquals(3, result.get(0).getResidents().size());

        verify(firePersonMapper, times(3)).toDTO(any(Person.class));
    }

    @Test
    public void testGetFloodPersonCoveredByStation() {
        // Arrange
        FireStationCoveragePersonDTO personDTO = new FireStationCoveragePersonDTO();
        personDTO.setFirstName("John");
        personDTO.setLastName("Doe");
        personDTO.setAddress("123 Main St");
        personDTO.setPhone("555-555-1234");

        when(coveragePersonMapper.toDTO(any(Person.class))).thenReturn(personDTO);

        // Act
        List<FloodAddressDTO> result = urlService.getFloodPersonCoveredByStation("1,2");

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());

        // Verify first address
        FloodAddressDTO address1 = result.stream().filter(a -> a.getAddress().equals("123 Main St")).findFirst().orElse(null);
        assertNotNull(address1);
        assertEquals(3, address1.getResidents().size());

        // Verify the second address
        FloodAddressDTO address2 = result.stream().filter(a -> a.getAddress().equals("456 Oak St")).findFirst().orElse(null);
        assertNotNull(address2);
        assertEquals(1, address2.getResidents().size());

//        verify(coveragePersonMapper, times(4)).toDTO(any(Person.class));
    }

    @Test
    public void testGetPersonInfo() {
        // Arrange
        PersonInfoDTO personInfoDTO = new PersonInfoDTO();
        personInfoDTO.setFirstName("John");
        personInfoDTO.setLastName("Doe");
        personInfoDTO.setAddress("123 Main St");
        personInfoDTO.setAge(30);
        personInfoDTO.setEmail("john.doe@example.com");
        personInfoDTO.setMedications(List.of("medication1", "medication2"));
        personInfoDTO.setAllergies(List.of("allergy1"));

        when(personInfoMapper.toDTO(any(Person.class))).thenReturn(personInfoDTO);

        // Act
        List<PersonInfoDTO> result = urlService.getPersonInfo("Doe");

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());

        verify(personInfoMapper, times(3)).toDTO(any(Person.class));
    }

    @Test
    public void testGetCommunityEmail() {
        // Act
        Set<String> result = urlService.getCommunityEmail("Anytown");

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
        assertTrue(result.contains("john.doe@example.com"));
        assertTrue(result.contains("jane.doe@example.com"));
        assertTrue(result.contains("jimmy.doe@example.com"));
    }
}
