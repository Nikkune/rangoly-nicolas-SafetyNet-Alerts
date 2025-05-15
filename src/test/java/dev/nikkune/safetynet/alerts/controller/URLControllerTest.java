package dev.nikkune.safetynet.alerts.controller;

import dev.nikkune.safetynet.alerts.dto.*;
import dev.nikkune.safetynet.alerts.service.URLService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class URLControllerTest {

    @Test
    public void testGetFloodPersonCoveredByStation() throws Exception {
        // Arrange
        List<FloodAddressDTO> floodAddresses = new ArrayList<>();
        FloodAddressDTO floodAddress = new FloodAddressDTO();
        floodAddress.setAddress("123 Main St");

        List<FirePersonDTO> residents = new ArrayList<>();
        FirePersonDTO resident = new FirePersonDTO();
        resident.setFirstName("John");
        resident.setLastName("Doe");
        resident.setPhone("555-555-1234");
        resident.setAge(30);
        resident.setMedications(List.of("medication1", "medication2"));
        resident.setAllergies(List.of("allergy1"));
        residents.add(resident);

        floodAddress.setResidents(residents);
        floodAddresses.add(floodAddress);

        when(urlService.getFloodPersonCoveredByStation("1")).thenReturn(floodAddresses);

        // Act & Assert
        mockMvc.perform(get("/flood/stations")
                        .param("stations", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].address", is("123 Main St")))
                .andExpect(jsonPath("$[0].residents", hasSize(1)))
                .andExpect(jsonPath("$[0].residents[0].firstName", is("John")))
                .andExpect(jsonPath("$[0].residents[0].lastName", is("Doe")))
                .andExpect(jsonPath("$[0].residents[0].phone", is("555-555-1234")))
                .andExpect(jsonPath("$[0].residents[0].age", is(30)))
                .andExpect(jsonPath("$[0].residents[0].medications", hasSize(2)))
                .andExpect(jsonPath("$[0].residents[0].medications", containsInAnyOrder("medication1", "medication2")))
                .andExpect(jsonPath("$[0].residents[0].allergies", hasSize(1)))
                .andExpect(jsonPath("$[0].residents[0].allergies[0]", is("allergy1")));

        verify(urlService, times(1)).getFloodPersonCoveredByStation("1");
    }

    private MockMvc mockMvc;

    @Mock
    private URLService urlService;

    @InjectMocks
    private URLController urlController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(urlController).build();
    }

    @Test
    public void testGetPersonCoveredByStation() throws Exception {
        // Arrange
        FireStationCoverageDTO coverageDTO = new FireStationCoverageDTO();
        List<FireStationCoveragePersonDTO> persons = new ArrayList<>();
        FireStationCoveragePersonDTO person = new FireStationCoveragePersonDTO();
        person.setFirstName("John");
        person.setLastName("Doe");
        person.setAddress("123 Main St");
        person.setPhone("555-555-1234");
        persons.add(person);
        coverageDTO.setPersons(persons);
        coverageDTO.setNumberOfAdults(1);
        coverageDTO.setNumberOfChildren(0);

        when(urlService.getPersonCoveredByStation("1")).thenReturn(coverageDTO);

        // Act & Assert
        mockMvc.perform(get("/firestation")
                        .param("stationNumber", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].persons", hasSize(1)))
                .andExpect(jsonPath("$[0].persons[0].firstName", is("John")))
                .andExpect(jsonPath("$[0].persons[0].lastName", is("Doe")))
                .andExpect(jsonPath("$[0].persons[0].address", is("123 Main St")))
                .andExpect(jsonPath("$[0].persons[0].phone", is("555-555-1234")))
                .andExpect(jsonPath("$[0].numberOfAdults", is(1)))
                .andExpect(jsonPath("$[0].numberOfChildren", is(0)));

        verify(urlService, times(1)).getPersonCoveredByStation("1");
    }

    @Test
    public void testGetPersonCoveredByStation_NotFound() throws Exception {
        // Arrange
        FireStationCoverageDTO coverageDTO = new FireStationCoverageDTO();
        coverageDTO.setPersons(new ArrayList<>());
        coverageDTO.setNumberOfAdults(0);
        coverageDTO.setNumberOfChildren(0);

        when(urlService.getPersonCoveredByStation("999")).thenReturn(coverageDTO);

        // Act & Assert
        mockMvc.perform(get("/firestation")
                        .param("stationNumber", "999"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));

        verify(urlService, times(1)).getPersonCoveredByStation("999");
    }

    @Test
    public void testGetPersonWithChildAlert() throws Exception {
        // Arrange
        List<ChildAlertDTO> childAlerts = new ArrayList<>();
        ChildAlertDTO childAlert = new ChildAlertDTO();
        ChildAlertChildDTO child = new ChildAlertChildDTO();
        child.setFirstName("John");
        child.setLastName("Doe");
        child.setAge(10);
        childAlert.setChild(child);
        List<PersonBaseDTO> otherMembers = new ArrayList<>();
        PersonBaseDTO parent = new PersonBaseDTO();
        parent.setFirstName("Jane");
        parent.setLastName("Doe");
        otherMembers.add(parent);
        childAlert.setOtherHouseholdMembers(otherMembers);
        childAlerts.add(childAlert);

        when(urlService.getChildAlert("123 Main St")).thenReturn(childAlerts);

        // Act & Assert
        mockMvc.perform(get("/childAlert")
                        .param("address", "123 Main St"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].child.firstName", is("John")))
                .andExpect(jsonPath("$[0].child.lastName", is("Doe")))
                .andExpect(jsonPath("$[0].child.age", is(10)))
                .andExpect(jsonPath("$[0].otherHouseholdMembers", hasSize(1)))
                .andExpect(jsonPath("$[0].otherHouseholdMembers[0].firstName", is("Jane")))
                .andExpect(jsonPath("$[0].otherHouseholdMembers[0].lastName", is("Doe")));

        verify(urlService, times(1)).getChildAlert("123 Main St");
    }

    @Test
    public void testGetPersonWithChildAlert_NotFound() throws Exception {
        // Arrange
        when(urlService.getChildAlert("Unknown Address")).thenReturn(new ArrayList<>());

        // Act & Assert
        mockMvc.perform(get("/childAlert")
                        .param("address", "Unknown Address"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));

        verify(urlService, times(1)).getChildAlert("Unknown Address");
    }

    @Test
    public void testGetPhoneAlert() throws Exception {
        // Arrange
        Set<String> phones = new HashSet<>();
        phones.add("555-555-1234");
        phones.add("555-555-5678");

        when(urlService.getPhoneAlert("1")).thenReturn(phones);

        // Act & Assert
        mockMvc.perform(get("/phoneAlert")
                        .param("firestation", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$", containsInAnyOrder("555-555-1234", "555-555-5678")));

        verify(urlService, times(1)).getPhoneAlert("1");
    }

    @Test
    public void testGetPhoneAlert_NotFound() throws Exception {
        // Arrange
        when(urlService.getPhoneAlert("999")).thenReturn(new HashSet<>());

        // Act & Assert
        mockMvc.perform(get("/phoneAlert")
                        .param("firestation", "999"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));

        verify(urlService, times(1)).getPhoneAlert("999");
    }

    @Test
    public void testGetFire() throws Exception {
        // Arrange
        List<FireAddressDTO> fireAddressDTOs = new ArrayList<>();
        FireAddressDTO fireAddressDTO = new FireAddressDTO();
        fireAddressDTO.setStationNumber("1");
        List<FirePersonDTO> residents = new ArrayList<>();
        FirePersonDTO resident = new FirePersonDTO();
        resident.setFirstName("John");
        resident.setLastName("Doe");
        resident.setPhone("555-555-1234");
        resident.setAge(30);
        resident.setMedications(List.of("medication1", "medication2"));
        resident.setAllergies(List.of("allergy1"));
        residents.add(resident);
        fireAddressDTO.setResidents(residents);
        fireAddressDTOs.add(fireAddressDTO);

        when(urlService.getFire("123 Main St")).thenReturn(fireAddressDTOs);

        // Act & Assert
        mockMvc.perform(get("/fire")
                        .param("address", "123 Main St"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].stationNumber", is("1")))
                .andExpect(jsonPath("$[0].residents", hasSize(1)))
                .andExpect(jsonPath("$[0].residents[0].firstName", is("John")))
                .andExpect(jsonPath("$[0].residents[0].lastName", is("Doe")))
                .andExpect(jsonPath("$[0].residents[0].phone", is("555-555-1234")))
                .andExpect(jsonPath("$[0].residents[0].age", is(30)))
                .andExpect(jsonPath("$[0].residents[0].medications", hasSize(2)))
                .andExpect(jsonPath("$[0].residents[0].medications", containsInAnyOrder("medication1", "medication2")))
                .andExpect(jsonPath("$[0].residents[0].allergies", hasSize(1)))
                .andExpect(jsonPath("$[0].residents[0].allergies[0]", is("allergy1")));

        verify(urlService, times(1)).getFire("123 Main St");
    }

    @Test
    public void testGetFire_NotFound() throws Exception {
        // Arrange
        when(urlService.getFire("Unknown Address")).thenReturn(new ArrayList<>());

        // Act & Assert
        mockMvc.perform(get("/fire")
                        .param("address", "Unknown Address"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));

        verify(urlService, times(1)).getFire("Unknown Address");
    }

    @Test
    public void testGetFloodPersonCoveredByStation_NotFound() throws Exception {
        // Arrange
        when(urlService.getFloodPersonCoveredByStation("999")).thenReturn(new ArrayList<>());

        // Act & Assert
        mockMvc.perform(get("/flood/stations")
                        .param("stations", "999"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));

        verify(urlService, times(1)).getFloodPersonCoveredByStation("999");
    }

    @Test
    public void testGetPersonInfo() throws Exception {
        // Arrange
        List<PersonInfoDTO> personInfoDTOs = new ArrayList<>();
        PersonInfoDTO personInfoDTO = new PersonInfoDTO();
        personInfoDTO.setFirstName("John");
        personInfoDTO.setLastName("Doe");
        personInfoDTO.setAddress("123 Main St");
        personInfoDTO.setAge(30);
        personInfoDTO.setEmail("john.doe@example.com");
        personInfoDTO.setMedications(List.of("medication1", "medication2"));
        personInfoDTO.setAllergies(List.of("allergy1"));
        personInfoDTOs.add(personInfoDTO);

        when(urlService.getPersonInfo("Doe")).thenReturn(personInfoDTOs);

        // Act & Assert
        mockMvc.perform(get("/personInfo")
                        .param("lastName", "Doe"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].firstName", is("John")))
                .andExpect(jsonPath("$[0].lastName", is("Doe")))
                .andExpect(jsonPath("$[0].address", is("123 Main St")))
                .andExpect(jsonPath("$[0].age", is(30)))
                .andExpect(jsonPath("$[0].email", is("john.doe@example.com")))
                .andExpect(jsonPath("$[0].medications", hasSize(2)))
                .andExpect(jsonPath("$[0].medications", containsInAnyOrder("medication1", "medication2")))
                .andExpect(jsonPath("$[0].allergies", hasSize(1)))
                .andExpect(jsonPath("$[0].allergies[0]", is("allergy1")));

        verify(urlService, times(1)).getPersonInfo("Doe");
    }

    @Test
    public void testGetPersonInfo_NotFound() throws Exception {
        // Arrange
        when(urlService.getPersonInfo("Unknown")).thenReturn(new ArrayList<>());

        // Act & Assert
        mockMvc.perform(get("/personInfo")
                        .param("lastName", "Unknown"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));

        verify(urlService, times(1)).getPersonInfo("Unknown");
    }

    @Test
    public void testGetCommunityEmail() throws Exception {
        // Arrange
        Set<String> emails = new HashSet<>();
        emails.add("john.doe@example.com");
        emails.add("jane.doe@example.com");

        when(urlService.getCommunityEmail("Anytown")).thenReturn(emails);

        // Act & Assert
        mockMvc.perform(get("/communityEmail")
                        .param("city", "Anytown"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$", containsInAnyOrder("john.doe@example.com", "jane.doe@example.com")));

        verify(urlService, times(1)).getCommunityEmail("Anytown");
    }

    @Test
    public void testGetCommunityEmail_NotFound() throws Exception {
        // Arrange
        when(urlService.getCommunityEmail("Unknown")).thenReturn(new HashSet<>());

        // Act & Assert
        mockMvc.perform(get("/communityEmail")
                        .param("city", "Unknown"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));

        verify(urlService, times(1)).getCommunityEmail("Unknown");
    }
}