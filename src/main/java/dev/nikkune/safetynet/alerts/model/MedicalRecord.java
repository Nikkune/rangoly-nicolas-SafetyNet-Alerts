package dev.nikkune.safetynet.alerts.model;

import lombok.Data;

import java.util.List;

/**
 * The MedicalRecord class represents a medical record for a person.
 * <p>
 * It includes personal information such as the person's first name, last name, birthdate,
 * as well as lists of medications and allergies associated with that person.
 */
@Data
public class MedicalRecord {
    private String firstName;
    private String lastName;
    private String birthdate;
    private List<String> medications;
    private List<String> allergies;
}
