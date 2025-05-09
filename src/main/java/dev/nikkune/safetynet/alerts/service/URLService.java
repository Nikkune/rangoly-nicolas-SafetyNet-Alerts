package dev.nikkune.safetynet.alerts.service;

import dev.nikkune.safetynet.alerts.config.JsonDatabase;
import dev.nikkune.safetynet.alerts.model.FireResponse;
import dev.nikkune.safetynet.alerts.model.FireStation;
import dev.nikkune.safetynet.alerts.model.Person;
import dev.nikkune.safetynet.alerts.utils.AgeCalculator;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
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

    public URLService(JsonDatabase jsonDatabase) {
        this.jsonDatabase = jsonDatabase;
    }

    /**
     * Retrieves a list of persons covered by the specified fire station number.
     *
     * @param stationNumber the identifier of the fire station whose associated persons are to be retrieved
     * @return a list of persons associated with the specified fire station
     */
    public List<Person> getPersonCoveredByStation(String stationNumber) {
        List<FireStation> fireStations = jsonDatabase.fireStations().stream().filter(fireStation -> fireStation.getStation().equals(stationNumber)).toList();
        return fireStations.stream().flatMap(fireStation -> fireStation.getPersons().stream()).toList();
    }

    /**
     * Retrieves a list of children living at the specified address.
     * A child is defined as a person who is 18 years old or younger.
     *
     * @param address the address to search for children
     * @return a list of persons classified as children living at the specified address
     */
    public List<Person> getChildAlert(String address) {
        List<Person> persons = jsonDatabase.people().stream().filter(person -> person.getAddress().equals(address)).toList();
        return persons.stream().filter(person -> AgeCalculator.calculateAge(person.getMedicalRecord().getBirthdate()) <= 18).toList();
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
    public List<String> getPhoneAlert(String stationNumber) {
        List<FireStation> fireStations = jsonDatabase.fireStations().stream().filter(fireStation -> fireStation.getStation().equals(stationNumber)).toList();
        List<Person> people = fireStations.stream().flatMap(fireStation -> fireStation.getPersons().stream()).toList();
        return people.stream().map(Person::getPhone).collect(Collectors.toList());
    }

    /**
     * Retrieves information about the fire stations and residents associated with the specified address.
     *
     * This method gathers a list of fire stations and persons whose addresses match the provided address
     * and organizes the data into a FireResponse object.
     *
     * @param address the address for which fire station and resident information is to be retrieved
     * @return a FireResponse object containing a list of fire stations and persons associated with the specified address
     */
    public FireResponse getFire(String address) {
        List<FireStation> fireStations = jsonDatabase.fireStations().stream().filter(fireStation -> fireStation.getAddress().equals(address)).toList();
        List<Person> people = jsonDatabase.people().stream().filter(person -> person.getAddress().equals(address)).toList();

        FireResponse fireResponse = new FireResponse();
        fireResponse.setFireStations(fireStations);
        fireResponse.setPeople(people);
        return fireResponse;
    }

    /**
     * Retrieves a list of persons covered by the specified fire station numbers.
     *
     * This method determines which fire stations correspond to the provided
     * station numbers and collects all associated persons across those fire stations.
     *
     * @param stationNumbers a list of fire station identifiers for which the persons are to be retrieved
     * @return a list of persons associated with the specified fire stations
     */
    public List<Person> getFloodPersonCoveredByStation(List<String> stationNumbers) {
        List<FireStation> fireStations = new ArrayList<>();
        for (String stationNumber : stationNumbers) {
            fireStations.addAll(jsonDatabase.fireStations().stream().filter(fireStation -> fireStation.getStation().equals(stationNumber)).toList());
        }
        return fireStations.stream().flatMap(fireStation -> fireStation.getPersons().stream()).toList();
    }

    /**
     * Retrieves a list of persons with the specified last name.
     *
     * This method filters the list of persons in the database and returns those whose last name matches
     * the provided value.
     *
     * @param lastName the last name used to filter persons in the database
     * @return a list of persons whose last name matches the specified value
     */
    public List<Person> getPersonInfo(String lastName) {
        return jsonDatabase.people().stream().filter(person -> person.getLastName().equals(lastName)).toList();
    }

    /**
     * Retrieves a list of email addresses of all persons residing in the specified city.
     *
     * @param city the name of the city for which the email addresses are to be retrieved
     * @return a list of email addresses of persons residing in the specified city
     */
    public List<String> getCommunityEmail(String city) {
        List<Person> people = jsonDatabase.people().stream().filter(person -> person.getCity().equals(city)).toList();
        return people.stream().map(Person::getEmail).collect(Collectors.toList());
    }
}
