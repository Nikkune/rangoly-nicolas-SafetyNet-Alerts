package dev.nikkune.safetynet.alerts.service;

import dev.nikkune.safetynet.alerts.config.JsonDatabase;
import dev.nikkune.safetynet.alerts.model.MedicalRecord;
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
     *
     * @return a list of all people
     */
    public List<Person> getAll() {
        return jsonDatabase.people();
    }

    /**
     * Gets a person by their first and last name.
     * <p>
     * Returns the person with the given first and last name.
     * <p>
     * If no person is found with the given name, a RuntimeException is thrown
     * with the message "Person not found".
     *
     * @param firstName the first name of the person to retrieve
     * @param lastName  the last name of the person to retrieve
     * @return the person with the given first and last name
     * @throws RuntimeException if no person is found with the given name
     */
    public Person get(String firstName, String lastName) throws RuntimeException {
        Person existingPerson = jsonDatabase.people().stream().filter(person -> person.getFirstName().equals(firstName) && person.getLastName().equals(lastName)).findFirst().orElse(null);
        if (existingPerson != null) {
            return existingPerson;
        } else {
            throw new RuntimeException("Person not found");
        }
    }

    /**
     * Gets all the people from the database with the given address.
     * <p>
     * Returns a list of all people stored in the database with the given address.
     *
     * @param address the address of the people to retrieve
     * @return a list of all people with the given address
     */
    public List<Person> getByAddress(String address) {
        return jsonDatabase.people().stream().filter(person -> person.getAddress().equals(address)).toList();
    }

    /**
     * Creates a new person with the given details.
     * <p>
     * If a person with the same first and last name already exists, a RuntimeException is thrown
     * with the message "Person already exists".
     * <p>
     * Otherwise, the person is added to the database and the database is saved.
     *
     * @param person the person to create
     * @throws RuntimeException if a person with the same name already exists
     */
    public void create(Person person) throws RuntimeException {
        Person existingPerson = jsonDatabase.people().stream().filter(p -> p.getFirstName().equals(person.getFirstName()) && p.getLastName().equals(person.getLastName())).findFirst().orElse(null);
        if (existingPerson == null) {
            jsonDatabase.people().add(person);
            jsonDatabase.saveData();
        } else {
            throw new RuntimeException("Person already exists");
        }
    }

    /**
     * Updates a person in the database.
     * <p>
     * Finds the person by their first and last name and updates them with the given information.
     * If the person is not found, a RuntimeException is thrown with the message "Person not found".
     * <p>
     * Otherwise, the person is updated and the database is saved.
     *
     * @param person the person to update
     * @throws RuntimeException if the person is not found
     */
    public void update(Person person) throws RuntimeException {
        Person existingPerson = jsonDatabase.people().stream().filter(p -> p.getFirstName().equals(person.getFirstName()) && p.getLastName().equals(person.getLastName())).findFirst().orElse(null);
        if (existingPerson != null) {
            existingPerson.setAddress(person.getAddress());
            existingPerson.setCity(person.getCity());
            existingPerson.setZip(person.getZip());
            existingPerson.setPhone(person.getPhone());
            existingPerson.setEmail(person.getEmail());
            jsonDatabase.saveData();
        } else {
            throw new RuntimeException("Person not found");
        }
    }


    /**
     * Deletes a person and their associated medical records from the database.
     * <p>
     * If a person with the specified first and last name exists in the database,
     * they and their associated medical records will be removed. If the person
     * is not found, a RuntimeException is thrown with the message "Person not found".
     *
     * @param firstName the first name of the person to delete
     * @param lastName  the last name of the person to delete
     * @throws RuntimeException if the person is not found in the database
     */
    public void delete(String firstName, String lastName) throws RuntimeException {
        Person existingPerson = jsonDatabase.people().stream().filter(p -> p.getFirstName().equals(firstName) && p.getLastName().equals(lastName)).findFirst().orElse(null);
        MedicalRecord existingMedicalRecord = jsonDatabase.medicalRecords().stream().filter(record -> record.getFirstName().equals(firstName) && record.getLastName().equals(lastName)).findFirst().orElse(null);
        if (existingPerson != null) {
            if (existingMedicalRecord != null) {
                jsonDatabase.medicalRecords().remove(existingMedicalRecord);
            }
            jsonDatabase.people().remove(existingPerson);
            jsonDatabase.saveData();
        } else {
            throw new RuntimeException("Person not found");
        }
    }
}
