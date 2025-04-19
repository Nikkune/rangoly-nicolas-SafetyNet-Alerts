package dev.nikkune.safetynet.alerts.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dev.nikkune.safetynet.alerts.model.FireStation;
import dev.nikkune.safetynet.alerts.model.MedicalRecord;
import dev.nikkune.safetynet.alerts.model.Person;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.util.List;

/**
 * The JsonDatabase class is responsible for loading and saving data from/to a JSON file.
 * <p>
 * It manages collections of `Person`, `FireStation`, and `MedicalRecord` objects.
 * <p>
 * The data is structured in a specific format and is loaded from the classpath resource 'data.json'.
 * <p>
 * This class is annotated with `@Configuration` to indicate that it provides Spring configuration.
 */
@Configuration
public class JsonDatabase {

    private List<Person> people;
    private List<FireStation> fireStations;
    private List<MedicalRecord> medicalRecords;
    @Value("${json.data.url}")
    private Resource resource;

    public JsonDatabase() {
    }

    /**
     * Load data from resources 'data.json'.
     * <p>
     * The data is loaded in the following order: persons, firestations, medicalrecords.
     *
     * @see #people()
     * @see #fireStations()
     * @see #medicalRecords()
     */
    @PostConstruct
    public void loadData() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(resource.getInputStream());
            people = objectMapper.convertValue(rootNode.get("persons"), objectMapper.getTypeFactory().constructCollectionType(List.class, Person.class));
            fireStations = objectMapper.convertValue(rootNode.get("firestations"), objectMapper.getTypeFactory().constructCollectionType(List.class, FireStation.class));
            medicalRecords = objectMapper.convertValue(rootNode.get("medicalrecords"), objectMapper.getTypeFactory().constructCollectionType(List.class, MedicalRecord.class));

            people.forEach(person -> {
                MedicalRecord medicalRecord = medicalRecords.stream().filter(record -> record.getFirstName().equals(person.getFirstName()) && record.getLastName().equals(person.getLastName())).findFirst().orElse(null);
                person.setMedicalRecord(medicalRecord);
            });
            fireStations.forEach(fireStation -> fireStation.setPersons(people.stream().filter(person -> person.getAddress().equals(fireStation.getAddress())).toList()));
        } catch (Exception e) {
            throw new RuntimeException("Failed to load data from data.json", e);
        }
    }


    /**
     * Saves the data to the classpath resource 'data.json'.
     * <p>
     * The data is saved in the following order: persons, firestations, medicalrecords.
     * <p>
     * This method is used by the application to persist the data when it is shut down.
     */
    public void saveData() {
        try {
            // Create a new ObjectMapper
            ObjectMapper objectMapper = new ObjectMapper();

            // Crete a new root node to structure the data
            ObjectNode rootNode = objectMapper.createObjectNode();

            // Retrieve data
            ArrayNode personsNode = objectMapper.valueToTree(people());
            ArrayNode fireStationsNode = objectMapper.valueToTree(fireStations());
            ArrayNode medicalRecordsNode = objectMapper.valueToTree(medicalRecords());

            //Ensure correct data is saved
            personsNode.forEach(person -> {
                ((ObjectNode) person).remove("medicalRecord");
            });
            fireStationsNode.forEach(fireStation -> {
                ((ObjectNode) fireStation).remove("persons");
            });

            // Add the data to the root node
            rootNode.set("persons", personsNode);
            rootNode.set("firestations", fireStationsNode);
            rootNode.set("medicalrecords", medicalRecordsNode);

            // Write the data into the file
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(resource.getFile(), rootNode);
        } catch (Exception e) {
            throw new RuntimeException("Failed to save data to data.json", e);
        }
    }

    /**
     * A list of people that was loaded from the data.json file.
     *
     * @return a list of people
     */
    @Bean
    public List<Person> people() {
        return people;
    }

    /**
     * A list of firestations that was loaded from the data.json file.
     *
     * @return a list of firestations
     */
    @Bean
    public List<FireStation> fireStations() {
        return fireStations;
    }

    /**
     * A list of medical records that was loaded from the data.json file.
     *
     * @return a list of medical records
     */
    @Bean
    public List<MedicalRecord> medicalRecords() {
        return medicalRecords;
    }
}
