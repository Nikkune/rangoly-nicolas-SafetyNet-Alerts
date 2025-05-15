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

    /**
     * Retrieves information about persons covered by a specific fire station.
     * This includes a list of persons, the number of adults, and the number of children.
     *
     * @param stationNumber the identifier of the fire station for which the information is to be retrieved
     * @return a FireStationCoverageDTO containing lists of persons covered, the number of adults, and the number of children
     */
    public FireStationCoverageDTO getPersonCoveredByStation(String stationNumber) {
        List<FireStation> fireStations = jsonDatabase.fireStations().stream().filter(fireStation -> fireStation.getStation().equals(stationNumber)).toList();
        List<Person> people = fireStations.stream().flatMap(fireStation -> fireStation.getPersons().stream()).toList();
        List<FireStationCoveragePersonDTO> personDTOS = people.stream().map(coveragePersonMapper::toDTO).toList();
        long numberOfChildren = AgeCalculator.numberOfChildren(people.stream().map(person -> person.getMedicalRecord().getBirthdate()).toList());
        long numberOfAdult = AgeCalculator.numberOfAdults(people.stream().map(person -> person.getMedicalRecord().getBirthdate()).toList());
        FireStationCoverageDTO fireStationCoverageDTO = new FireStationCoverageDTO();
        fireStationCoverageDTO.setPersons(personDTOS);
        fireStationCoverageDTO.setNumberOfAdults((int) numberOfAdult);
        fireStationCoverageDTO.setNumberOfChildren((int) numberOfChildren);
        return fireStationCoverageDTO;
    }

    /**
     * Retrieves a list of children residing at the specified address along with
     * other household members. For each child, this method returns their details
     * and a list of other non-child persons living in the same household.
     *
     * @param address the address for which the children and household member details
     *                are to be retrieved
     * @return a list of ChildAlertDTO objects, each containing details of a child
     *         and a list of other household members; if no children are found at
     *         the address, an empty list is returned
     */
    public List<ChildAlertDTO> getChildAlert(String address) {
        List<Person> persons = jsonDatabase.people().stream().filter(person -> person.getAddress().equals(address)).toList();
        List<Person> children = persons.stream().filter(person -> AgeCalculator.calculateAge(person.getMedicalRecord().getBirthdate()) <= 18).toList();

        if (children.isEmpty()) {
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
     * <p>
     * This method queries the list of fire stations to find the ones matching the provided station number.
     * It then collects and returns the phone numbers of all persons associated with these fire stations.
     *
     * @param stationNumber the identifier of the fire station whose associated persons' phone numbers are to be retrieved
     * @return a set of phone numbers of persons associated with the specified fire station
     */
    public Set<String> getPhoneAlert(String stationNumber) {
        List<FireStation> fireStations = jsonDatabase.fireStations().stream().filter(fireStation -> fireStation.getStation().equals(stationNumber)).toList();
        List<Person> people = fireStations.stream().flatMap(fireStation -> fireStation.getPersons().stream()).toList();
        return people.stream().map(Person::getPhone).collect(Collectors.toSet());
    }

    /**
     * Retrieves a list of fire-related information for the specified address.
     * This includes the fire station details and the residents living at the address,
     * represented by their mapped DTO objects.
     *
     * @param address the address for which fire-related information is to be retrieved
     * @return a list of FireAddressDTO objects containing the fire station details and
     *         the corresponding residents living at the specified address
     */
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

    /**
     * Retrieves a list of addresses covered by the specified fire stations, including details
     * about persons grouped by each address.
     *
     * @param stationNumbers a comma-separated string of fire station identifiers for which
     *                       the coverage information is to be retrieved
     * @return a list of FloodAddressDTO objects, each containing the address and the coverage
     *         information (details about persons grouped by address) for the specified fire stations
     */
    public List<FloodAddressDTO> getFloodPersonCoveredByStation(String stationNumbers) {
        List<String> stations = List.of(stationNumbers.split(","));
        List<FireStation> fireStations = new ArrayList<>();
        for (String stationNumber : stations) {
            fireStations.addAll(jsonDatabase.fireStations().stream().filter(fireStation -> fireStation.getStation().equals(stationNumber)).toList());
        }
        return fireStations.stream().map(fireStation -> {
            FloodAddressDTO floodAddressDTO = new FloodAddressDTO();
            floodAddressDTO.setAddress(fireStation.getAddress());
            List<FirePersonDTO> residents = fireStation.getPersons().stream().map(firePersonMapper::toDTO).toList();
            floodAddressDTO.setResidents(residents);
            return floodAddressDTO;
        }).toList();
    }

    /**
     * Retrieves a list of detailed information about all persons with the specified last name.
     *
     * @param lastName the last name of the persons whose information is to be retrieved
     * @return a list of PersonInfoDTO objects containing the detailed information of the persons with the specified last name
     */
    public List<PersonInfoDTO> getPersonInfo(String lastName) {
        return jsonDatabase.people().stream().filter(person -> person.getLastName().equals(lastName)).map(personInfoMapper::toDTO).toList();
    }

    /**
     * Retrieves a list of email addresses of all persons residing in the specified city.
     *
     * @param city the name of the city for which the email addresses are to be retrieved
     * @return a set of email addresses of persons residing in the specified city
     */
    public Set<String> getCommunityEmail(String city) {
        List<Person> people = jsonDatabase.people().stream().filter(person -> person.getCity().equals(city)).toList();
        return people.stream().map(Person::getEmail).collect(Collectors.toSet());
    }
}
