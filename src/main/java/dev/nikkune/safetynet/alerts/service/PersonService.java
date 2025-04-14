package dev.nikkune.safetynet.alerts.service;

import dev.nikkune.safetynet.alerts.config.JsonDatabase;
import dev.nikkune.safetynet.alerts.model.Person;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The PersonService class is responsible for managing the person data in the database.
 * <p>
 * It provides methods to get all the people, get a person by their first and last name, create a new person, update an existing person, and delete a person.
 */
@Service
public class PersonService {
    private final JsonDatabase jsonDatabase;

    public PersonService(JsonDatabase jsonDatabase) {
        this.jsonDatabase = jsonDatabase;
    }

    /**
     * Gets all the people from the database.
     * <p>
     * Returns the list of all people stored in the database.
     * @return a list of all people
     */
    public List<Person> getAll() {
        return jsonDatabase.people();
    }

    /**
     * Gets a person by their first and last name.
     * <p>
     * Finds a person in the database with the given first and last name, and if
     * found, returns the person. If not found, throws a RuntimeException with the
     * message "Person not found".
     * @param firstName the first name of the person to get
     * @param lastName the last name of the person to get
     * @return the person with the given first and last name
     * @throws RuntimeException if the person is not found
     */
    public Person getPerson(String firstName, String lastName) {
        Person existingPerson = jsonDatabase.people().stream()
                .filter(person -> person.getFirstName().equals(firstName) && person.getLastName().equals(lastName))
                .findFirst().orElse(null);
        if (existingPerson != null) {
            return existingPerson;
        } else {
            throw new RuntimeException("Person not found");
        }
    }


    /**
     * Creates a new person in the database.
     * <p>
     * Constructs a new Person object with the provided details and adds it to the database
     * if a person with the same first and last name does not already exist.
     * If a person with the same first and last name exists, a RuntimeException is thrown.
     * <p>
     * @param firstName the first name of the person
     * @param lastName the last name of the person
     * @param address the address of the person
     * @param city the city where the person resides
     * @param zip the zip code of the person's address
     * @param phone the phone number of the person
     * @param email the email address of the person
     * @return the created Person object
     * @throws RuntimeException if a person with the same first and last name already exists
     */
    public Person create(String firstName, String lastName, String address, String city, String zip, String phone, String email) {
        Person person = new Person();
        person.setFirstName(firstName);
        person.setLastName(lastName);
        person.setAddress(address);
        person.setCity(city);
        person.setZip(zip);
        person.setPhone(phone);
        person.setEmail(email);
        Person existingPerson = getPerson(firstName, lastName);
        if (existingPerson == null) {
            jsonDatabase.people().add(person);
            jsonDatabase.saveData();
            return person;
        } else {
            throw new RuntimeException("Person already exists");
        }
    }

    /**
     * Updates a person in the database.
     * <p>
     * Finds the person by their first and last name, updates the person's information
     * with the provided information, and persists the updated person to the database.
     * <p>
     * If the person is not found, a RuntimeException is thrown with the message "Person not found".
     * @param person the person to update
     * @return the updated person
     * @throws RuntimeException if the person is not found
     */
    public Person update(Person person) {
        Person existingPerson = getPerson(person.getFirstName(), person.getLastName());
        if (existingPerson != null) {
            existingPerson.setAddress(person.getAddress());
            existingPerson.setCity(person.getCity());
            existingPerson.setEmail(person.getEmail());
            existingPerson.setFirstName(person.getFirstName());
            existingPerson.setLastName(person.getLastName());
            existingPerson.setZip(person.getZip());
            existingPerson.setPhone(person.getPhone());
            jsonDatabase.saveData();
            return existingPerson;
        } else {
            throw new RuntimeException("Person not found");
        }
    }

    /**
     * Deletes a person from the database.
     * <p>
     * Finds the person by their first and last name, removes them from the list of people,
     * and persists the updated list to the database.
     * <p>
     * If the person is not found, a RuntimeException is thrown with the message "Person not found".
     * @param firstName the first name of the person to delete
     * @param lastName the last name of the person to delete
     * @throws RuntimeException if the person is not found
     */
    public void delete(String firstName, String lastName) {
        Person person = getPerson(firstName, lastName);
        if (person != null) {
            jsonDatabase.people().remove(person);
            jsonDatabase.saveData();
        } else {
            throw new RuntimeException("Person not found");
        }
    }
}
