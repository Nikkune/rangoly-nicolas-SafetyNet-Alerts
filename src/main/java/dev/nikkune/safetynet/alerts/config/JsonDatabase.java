package dev.nikkune.safetynet.alerts.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dev.nikkune.safetynet.alerts.model.FireStation;
import dev.nikkune.safetynet.alerts.model.MedicalRecord;
import dev.nikkune.safetynet.alerts.model.Person;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.WritableResource;

import java.util.List;

@Configuration
public class JsonDatabase {

    private final ResourceLoader resourceLoader;
    private List<Person> people;
    private List<FireStation> fireStations;
    private List<MedicalRecord> medicalRecords;

    public JsonDatabase(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    /**
     * Load data from the classpath resource 'data.json'.
     *
     * The data is loaded in the following order: persons, firestations, medicalrecords.
     *
     * @see #people()
     * @see #fireStations()
     * @see #medicalRecords()
     */
    @PostConstruct
    public void loadData() {
        Resource dataFile = resourceLoader.getResource("classpath:data.json");
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode rootNode = objectMapper.readTree(dataFile.getInputStream());
            this.people = objectMapper.convertValue(rootNode.get("persons").traverse(), objectMapper.getTypeFactory().constructCollectionType(List.class, Person.class));
            this.fireStations = objectMapper.convertValue(rootNode.get("firestations").traverse(), objectMapper.getTypeFactory().constructCollectionType(List.class, FireStation.class));
            this.medicalRecords = objectMapper.convertValue(rootNode.get("medicalrecords").traverse(), objectMapper.getTypeFactory().constructCollectionType(List.class, MedicalRecord.class));
        } catch (Exception e) {
            throw new RuntimeException("Failed to load data from data.json",e);
        }
    }

    /**
     * Saves the current state of the data to the classpath resource 'data.json'.
     * <p>
     * The data is saved in the following order: persons, firestations, medicalrecords.
     *
     * @see #people()
     * @see #fireStations()
     * @see #medicalRecords()
     */
    public void saveData() {
        WritableResource dataFile = (WritableResource) resourceLoader.getResource("classpath:data.json");
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // Crete a new root node to structure the data
            ObjectNode rootNode = objectMapper.createObjectNode();

            // Set the data
            rootNode.set("persons", objectMapper.convertValue(people, objectMapper.getTypeFactory().constructCollectionType(List.class, Person.class)));
            rootNode.set("firestations", objectMapper.convertValue(fireStations, objectMapper.getTypeFactory().constructCollectionType(List.class, FireStation.class)));
            rootNode.set("medicalrecords", objectMapper.convertValue(medicalRecords, objectMapper.getTypeFactory().constructCollectionType(List.class, MedicalRecord.class)));

            // Write the data into the file
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(dataFile.getFile(), rootNode);
        } catch (Exception e) {
            throw new RuntimeException("Failed to save data to data.json",e);
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
