package dev.nikkune.safetynet.alerts.controller;

import dev.nikkune.safetynet.alerts.dto.PersonDTO;
import dev.nikkune.safetynet.alerts.mapper.PersonMapper;
import dev.nikkune.safetynet.alerts.model.Person;
import dev.nikkune.safetynet.alerts.service.PersonService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The PersonController class is responsible for managing the person data in the database.
 * <p>
 * It provides methods to get all the people, get a person by their first and last name, create a new person, update an existing person, and delete a person.
 */
@RestController
@RequestMapping("/person")
public class PersonController {
    private final PersonService service;
    private final PersonMapper mapper;

    public PersonController(PersonService service, PersonMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    /**
     * Retrieves all the people from the database.
     * <p>
     * Returns the list of all people stored in the database.
     * @return a list of all people
     */
    @GetMapping
    public ResponseEntity<List<PersonDTO>> getAllPersons() {
        List<Person> persons = service.getAll();
        List<PersonDTO> personsDTO = persons.stream().map(mapper::toDTO).toList();
        return ResponseEntity.ok(personsDTO);
    }

    /**
     * Retrieves a person by their first and last name.
     * <p>
     * Converts the given first and last name to a Person entity and attempts to retrieve the person
     * from the database that matches the given first and last name.
     * <p>
     * If no person is found with the given first and last name, a RuntimeException is thrown
     * with the message "Person not found".
     *
     * @param firstName the first name of the person to retrieve
     * @param lastName the last name of the person to retrieve
     * @return the person with the given first and last name
     * @throws RuntimeException if no person is found with the given first and last name
     */
    @GetMapping("/")
    public ResponseEntity<PersonDTO> getPersonByFirstNameAndLastName(@RequestParam String firstName, @RequestParam String lastName) {
        try {
            Person person = service.get(firstName, lastName);
            PersonDTO personDTO = mapper.toDTO(person);
            return ResponseEntity.ok(personDTO);
        } catch (RuntimeException e) {
            throw new RuntimeException("Person not found");
        }
    }

    /**
     * Retrieves a list of people by their address.
     * <p>
     * Converts the given address to a Person entity and attempts to retrieve a list of people
     * from the database that match the given address.
     * <p>
     * If no person is found with the given address, a RuntimeException is thrown
     * with the message "Person not found".
     *
     * @param address the address of the people to retrieve
     * @return a list of people that match the given address
     * @throws RuntimeException if no person is found with the given address
     */
    @GetMapping("/address")
    public ResponseEntity<List<PersonDTO>> getPersonsByAddress(@RequestParam String address) {
        List<Person> persons = service.getByAddress(address);
        List<PersonDTO> personsDTO = persons.stream().map(mapper::toDTO).toList();
        return ResponseEntity.ok(personsDTO);
    }

    /**
     * Creates a new person in the database.
     * <p>
     * Converts the given PersonDTO to a Person entity and attempts to create it in the database.
     * If a person with the same first and last name already exists, a RuntimeException is thrown
     * with the message "Person already exists".
     *
     * @param personDTO the data transfer object containing the person's information
     */
    @PostMapping
    public ResponseEntity<PersonDTO> createPerson(@Valid @RequestBody PersonDTO personDTO) {
        Person person = mapper.toEntity(personDTO);
        try {
            service.create(person);
            return ResponseEntity.ok(personDTO);
        } catch (RuntimeException e) {
            throw new RuntimeException("Person already exists");
        }
    }

    /**
     * Updates a person in the database.
     * <p>
     * Converts the given PersonDTO to a Person entity and attempts to update it in the database.
     * If the person is not found, a RuntimeException is thrown with the message "Person not found".
     *
     * @param personDTO the data transfer object containing the person's updated information
     */
    @PutMapping
    public ResponseEntity<PersonDTO> updatePerson(@Valid @RequestBody PersonDTO personDTO) {
        Person person = mapper.toEntity(personDTO);
        try {
            service.update(person);
            return ResponseEntity.ok(personDTO);
        } catch (RuntimeException e) {
            throw new RuntimeException("Person not found");
        }
    }

    /**
     * Deletes a person from the database.
     * <p>
     * Attempts to delete the person with the given first and last name. If the person is not found,
     * a RuntimeException is thrown with the message "Person not found".
     *
     * @param firstName the first name of the person to delete
     * @param lastName the last name of the person to delete
     */
    @DeleteMapping
    public ResponseEntity<Void> deletePerson(@RequestParam String firstName, @RequestParam String lastName) {
        try {
            service.delete(firstName, lastName);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            throw new RuntimeException("Person not found");
        }
    }

    // TODO Exceptions Handling
}
