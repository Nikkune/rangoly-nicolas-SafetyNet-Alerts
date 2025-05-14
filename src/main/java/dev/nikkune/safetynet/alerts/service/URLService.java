package dev.nikkune.safetynet.alerts.service;

import dev.nikkune.safetynet.alerts.config.JsonDatabase;
import dev.nikkune.safetynet.alerts.dto.*;
import dev.nikkune.safetynet.alerts.mapper.*;
import dev.nikkune.safetynet.alerts.model.FireStation;
import dev.nikkune.safetynet.alerts.model.Person;
import dev.nikkune.safetynet.alerts.utils.AgeCalculator;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The URLService class provides functionalities to interact with and retrieve
 * information about persons, fire stations, and their relationships stored in a
 * JSON database. It includes methods to query data based on fire stations, addresses,
 * phone numbers, last names, and cities. These methods return specific datasets
 * such as persons covered by specific stations, children living at an address,
 * community emails, and more.
 */
@Service
public class URLService {
    private final JsonDatabase jsonDatabase;
    private final FireStationCoveragePersonMapper coveragePersonMapper;
    private final ChildAlertChildMapper childMapper;
    private final PersonBaseMapper personBaseMapper;
    private final FirePersonMapper firePersonMapper;
    private final PersonInfoMapper personInfoMapper;

    public URLService(JsonDatabase jsonDatabase, FireStationCoveragePersonMapper coveragePersonMapper, ChildAlertChildMapper childMapper, PersonBaseMapper personBaseMapper, FirePersonMapper firePersonMapper, PersonInfoMapper personInfoMapper) {
        this.jsonDatabase = jsonDatabase;
        this.coveragePersonMapper = coveragePersonMapper;
        this.childMapper = childMapper;
        this.personBaseMapper = personBaseMapper;
        this.firePersonMapper = firePersonMapper;
        this.personInfoMapper = personInfoMapper;
    }

    public FireStationCoverageDTO getPersonCoveredByStation(String stationNumber) {
        List<FireStation> fireStations = jsonDatabase.fireStations().stream().filter(fireStation -> fireStation.getStation().equals(stationNumber)).toList();
        List<Person> people = fireStations.stream().flatMap(fireStation -> fireStation.getPersons().stream()).toList();
        List<FireStationCoveragePersonDTO> personDTOS = people.stream().map(coveragePersonMapper::toDTO).toList();
        long numberOfChildren = people.stream().filter(person -> AgeCalculator.calculateAge(person.getMedicalRecord().getBirthdate()) <= 18).count();
        long numberOfAdult = people.size() - numberOfChildren;
        FireStationCoverageDTO fireStationCoverageDTO = new FireStationCoverageDTO();
        fireStationCoverageDTO.setPersons(personDTOS);
        fireStationCoverageDTO.setNumberOfAdults((int) numberOfAdult);
        fireStationCoverageDTO.setNumberOfChildren((int) numberOfChildren);
        return fireStationCoverageDTO;
    }

    public List<ChildAlertDTO> getChildAlert(String address) {
        List<Person> persons = jsonDatabase.people().stream().filter(person -> person.getAddress().equals(address)).toList();
        List<Person> children = persons.stream().filter(person -> AgeCalculator.calculateAge(person.getMedicalRecord().getBirthdate()) <= 18).toList();

        if (children.isEmpty()){
            return new ArrayList<>();
        }

        return children.stream().map(child -> {
            ChildAlertDTO childAlertDTO = new ChildAlertDTO();
            childAlertDTO.setChild(childMapper.toDTO(child));
            List<PersonBaseDTO> otherMembers = persons.stream().filter(person -> !person.equals(child)).map(personBaseMapper::toDTO).toList();
            childAlertDTO.setOtherHouseholdMembers(otherMembers);
            return childAlertDTO;
        }).toList();
    }

    /**
     * Retrieves a list of phone numbers for all persons covered by the specified fire station.
     *
     * This method queries the list of fire stations to find the ones matching the provided station number.
     * It then collects and returns the phone numbers of all persons associated with these fire stations.
     *
     * @param stationNumber the identifier of the fire station whose associated persons' phone numbers are to be retrieved
     * @return a list of phone numbers of persons associated with the specified fire station
     */
    public Set<String> getPhoneAlert(String stationNumber) {
        List<FireStation> fireStations = jsonDatabase.fireStations().stream().filter(fireStation -> fireStation.getStation().equals(stationNumber)).toList();
        List<Person> people = fireStations.stream().flatMap(fireStation -> fireStation.getPersons().stream()).toList();
        return people.stream().map(Person::getPhone).collect(Collectors.toSet());
    }

    public List<FireAddressDTO> getFire(String address) {
        List<FireStation> fireStations = jsonDatabase.fireStations().stream().filter(fireStation -> fireStation.getAddress().equals(address)).toList();
        return fireStations.stream().map(station -> {
            FireAddressDTO fireAddressDTO = new FireAddressDTO();
            fireAddressDTO.setStationNumber(station.getStation());
            List<FirePersonDTO> residents = station.getPersons().stream().map(firePersonMapper::toDTO).toList();
            fireAddressDTO.setResidents(residents);
            return fireAddressDTO;
        }).toList();
    }

    public List<FloodAddressDTO> getFloodPersonCoveredByStation(String stationNumbers) {
        List<String> stations = List.of(stationNumbers.split(","));
        List<FireStation> fireStations = new ArrayList<>();
        for (String stationNumber : stations) {
            fireStations.addAll(jsonDatabase.fireStations().stream().filter(fireStation -> fireStation.getStation().equals(stationNumber)).toList());
        }
        return fireStations.stream().map(fireStation -> {
            FloodAddressDTO floodAddressDTO = new FloodAddressDTO();
            floodAddressDTO.setAddress(fireStation.getAddress());
            floodAddressDTO.setCoverage(getPersonCoveredByStation(fireStation.getStation()));
            return floodAddressDTO;
        }).toList();
    }

    public List<PersonInfoDTO> getPersonInfo(String lastName) {
        return jsonDatabase.people().stream().filter(person -> person.getLastName().equals(lastName)).map(personInfoMapper::toDTO).toList();
    }

    /**
     * Retrieves a list of email addresses of all persons residing in the specified city.
     *
     * @param city the name of the city for which the email addresses are to be retrieved
     * @return a list of email addresses of persons residing in the specified city
     */
    public Set<String> getCommunityEmail(String city) {
        List<Person> people = jsonDatabase.people().stream().filter(person -> person.getCity().equals(city)).toList();
        return people.stream().map(Person::getEmail).collect(Collectors.toSet());
    }
}
