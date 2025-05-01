package dev.nikkune.safetynet.alerts.controller;

import dev.nikkune.safetynet.alerts.dto.MedicalRecordDTO;
import dev.nikkune.safetynet.alerts.mapper.MedicalRecordMapper;
import dev.nikkune.safetynet.alerts.model.MedicalRecord;
import dev.nikkune.safetynet.alerts.service.MedicalRecordService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The MedicalRecordController class is responsible for managing medical records in the system.
 * <p>
 * It provides endpoints to retrieve all medical records, retrieve a medical record by its first and last name,
 * create a new medical record, update an existing medical record, and delete a medical record.
 */
@RestController
@RequestMapping("/medicalrecord")
@Validated
public class MedicalRecordController {
    private final MedicalRecordService service;
    private final MedicalRecordMapper mapper;

    public MedicalRecordController(MedicalRecordService service, MedicalRecordMapper mapper) {
        this.mapper = mapper;
        this.service = service;
    }

    /**
     * Retrieves all medical records from the database.
     * <p>
     * Returns a list of all medical records stored in the database.
     *
     * @return a list of all medical records
     */
    @GetMapping("/all")
    public ResponseEntity<List<MedicalRecordDTO>> getAllMedicalRecords() {
        List<MedicalRecord> medicalRecords = service.getAll();
        List<MedicalRecordDTO> medicalRecordDTOS = medicalRecords.stream().map(mapper::toDTO).toList();
        return ResponseEntity.ok(medicalRecordDTOS);
    }

    /**
     * Retrieves a medical record by the given first and last name.
     * <p>
     * Attempts to find and return the medical record associated with the specified first and last name.
     * <p>
     * If no medical record is found, a RuntimeException is thrown with the message "Medical record not found".
     *
     * @param firstName the first name of the person whose medical record is to be retrieved
     * @param lastName  the last name of the person whose medical record is to be retrieved
     * @return a ResponseEntity containing the medical record if found, otherwise throws a RuntimeException
     * @throws RuntimeException if no medical record is found with the given first and last name
     */
    @GetMapping
    public ResponseEntity<MedicalRecordDTO> getMedicalRecordByFirstNameAndLastName(@RequestParam @NotBlank(message = "First name is required") String firstName, @RequestParam @NotBlank(message = "Last name is required") String lastName) {
        MedicalRecord medicalRecord = service.get(firstName, lastName);
        MedicalRecordDTO medicalRecordDTO = mapper.toDTO(medicalRecord);
        return ResponseEntity.ok(medicalRecordDTO);
    }

    /**
     * Creates a new medical record for a person.
     * <p>
     * The medical record to create is given in the request body.
     * <p>
     * If a person with the given first and last name does not exist, a RuntimeException is thrown
     * with the message "There is no person with this name".
     * <p>
     * If a medical record for the person already exists, a RuntimeException is thrown
     * with the message "Medical record already exists".
     * <p>
     * The created medical record is returned in the response body.
     *
     * @param medicalRecord the medical record to create
     * @return the created medical record
     * @throws RuntimeException if no person exists with the given name or if the medical record already exists
     */
    @PostMapping
    public ResponseEntity<MedicalRecordDTO> createMedicalRecord(@Valid @RequestBody MedicalRecord medicalRecord) {
        service.create(medicalRecord);
        MedicalRecordDTO medicalRecordDTO = mapper.toDTO(medicalRecord);
        return ResponseEntity.ok(medicalRecordDTO);
    }

    /**
     * Updates an existing medical record in the database.
     * <p>
     * The medical record to update is given in the request body.
     * <p>
     * If no medical record is found with the given first and last name, a RuntimeException is thrown
     * with the message "Medical record not found".
     * <p>
     * The updated medical record is returned in the response body.
     *
     * @param medicalRecord the medical record to update
     * @return the updated medical record
     * @throws RuntimeException if no medical record is found with the given first and last name
     */
    @PutMapping
    public ResponseEntity<MedicalRecordDTO> updateMedicalRecord(@Valid @RequestBody MedicalRecord medicalRecord) {
        service.update(medicalRecord);
        MedicalRecordDTO medicalRecordDTO = mapper.toDTO(medicalRecord);
        return ResponseEntity.ok(medicalRecordDTO);
    }

    /**
     * Deletes a medical record by its first and last name.
     * <p>
     * The medical record is removed from the database, and the database is saved.
     * <p>
     * If no medical record is found with the given first and last name, a RuntimeException is thrown
     * with the message "Medical record not found".
     *
     * @param firstName the first name of the medical record to delete
     * @param lastName  the last name of the medical record to delete
     * @return a ResponseEntity with no content and a 204 status if the medical record was found and deleted,
     * or a ResponseEntity with a RuntimeException if no medical record was found
     */
    @DeleteMapping
    public ResponseEntity<Void> deleteMedicalRecord(@RequestParam @NotBlank(message = "First name is required") String firstName, @RequestParam @NotBlank(message = "Last name is required") String lastName) {
        service.delete(firstName, lastName);
        return ResponseEntity.noContent().build();
    }
}
