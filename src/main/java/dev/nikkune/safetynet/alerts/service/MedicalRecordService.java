package dev.nikkune.safetynet.alerts.service;

import dev.nikkune.safetynet.alerts.config.JsonDatabase;
import dev.nikkune.safetynet.alerts.model.MedicalRecord;
import dev.nikkune.safetynet.alerts.model.Person;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The MedicalRecordService class is responsible for managing the medical record data in the database.
 * <p>
 * It provides methods to get all the medical records, get a medical record by its first and last name, create a new medical record, update an existing medical record, and delete a medical record.
 */
@Service
public class MedicalRecordService {
    private final JsonDatabase jsonDatabase;

    public MedicalRecordService(JsonDatabase jsonDatabase) {
        this.jsonDatabase = jsonDatabase;
    }

    /**
     * Retrieves all medical records from the database.
     * <p>
     * Returns a list of all medical records that are stored in the database.
     *
     * @return a list of all medical records
     */
    public List<MedicalRecord> getAll() {
        return jsonDatabase.medicalRecords();
    }

    /**
     * Retrieves a medical record from the database by its first and last name.
     * <p>
     * Returns the medical record associated with the given first and last name.
     * <p>
     * If no medical record is associated with the given first and last name, a RuntimeException is thrown
     * with the message "Medical record not found".
     *
     * @param firstName the first name of the person to retrieve
     * @param lastName the last name of the person to retrieve
     * @return the medical record associated with the given first and last name
     * @throws RuntimeException if no medical record is associated with the given first and last name
     */
    public MedicalRecord get(String firstName, String lastName) throws RuntimeException {
        MedicalRecord existingRecord = jsonDatabase.medicalRecords().stream().filter(record -> record.getFirstName().equals(firstName) && record.getLastName().equals(lastName)).findFirst().orElse(null);
        if (existingRecord != null) {
            return existingRecord;
        } else {
            throw new RuntimeException("Medical record not found");
        }
    }

    /**
     * Creates a new medical record for a person.
     * <p>
     * If a person with the given first and last name does not exist, a RuntimeException is thrown
     * with the message "There is no person with this name".
     * <p>
     * If a medical record for the person already exists, a RuntimeException is thrown
     * with the message "Medical record already exists".
     * <p>
     * Otherwise, the medical record is added to the database and the database is saved.
     *
     * @param medicalRecord the medical record to create
     * @throws RuntimeException if no person exists with the given name or if the medical record already exists
     */
    public void create(MedicalRecord medicalRecord) throws RuntimeException {
        Person existingPerson = jsonDatabase.people().stream().filter(p -> p.getFirstName().equals(medicalRecord.getFirstName()) && p.getLastName().equals(medicalRecord.getLastName())).findFirst().orElse(null);
        if (existingPerson == null) {
            throw new RuntimeException("There is no person with this name");
        } else {
            MedicalRecord existingRecord = jsonDatabase.medicalRecords().stream().filter(record -> record.getFirstName().equals(medicalRecord.getFirstName()) && record.getLastName().equals(medicalRecord.getLastName())).findFirst().orElse(null);
            if (existingRecord == null) {
                existingPerson.setMedicalRecord(medicalRecord);
                jsonDatabase.medicalRecords().add(medicalRecord);
                jsonDatabase.saveData();
            } else {
                throw new RuntimeException("Medical record already exists");
            }
        }
    }

    /**
     * Updates an existing medical record in the database.
     * <p>
     * Finds the medical record by the person's first and last name, removes the existing entry,
     * and adds the updated medical record details. Persists the changes to the database.
     * <p>
     * If no medical record is found with the given first and last name, a RuntimeException is thrown
     * with the message "Medical record not found".
     *
     * @param medicalRecord the medical record with updated details
     * @throws RuntimeException if no medical record is found with the given first and last name
     */
    public void update(MedicalRecord medicalRecord) throws RuntimeException {
        MedicalRecord existingRecord = jsonDatabase.medicalRecords().stream().filter(record -> record.getFirstName().equals(medicalRecord.getFirstName()) && record.getLastName().equals(medicalRecord.getLastName())).findFirst().orElse(null);
        if (existingRecord != null) {
            jsonDatabase.medicalRecords().remove(existingRecord);
            jsonDatabase.medicalRecords().add(medicalRecord);
            jsonDatabase.saveData();
        } else {
            throw new RuntimeException("Medical record not found");
        }
    }

    /**
     * Deletes a medical record from the database.
     * <p>
     * Finds the medical record by the person's first and last name and removes it from the database.
     * Persists the changes to the database.
     * <p>
     * If no medical record is found with the given first and last name, a RuntimeException is thrown
     * with the message "Medical record not found".
     *
     * @param medicalRecord the medical record to delete
     * @throws RuntimeException if no medical record is found with the given first and last name
     */
    public void delete(MedicalRecord medicalRecord) throws RuntimeException {
        MedicalRecord existingRecord = jsonDatabase.medicalRecords().stream().filter(record -> record.getFirstName().equals(medicalRecord.getFirstName()) && record.getLastName().equals(medicalRecord.getLastName())).findFirst().orElse(null);
        if (existingRecord != null) {
            jsonDatabase.medicalRecords().remove(existingRecord);
            jsonDatabase.saveData();
        } else {
            throw new RuntimeException("Medical record not found");
        }
    }
}
