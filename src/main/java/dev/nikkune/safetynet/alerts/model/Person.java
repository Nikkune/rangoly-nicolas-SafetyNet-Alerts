package dev.nikkune.safetynet.alerts.model;

import lombok.Data;

/**
 * The Person class represents an individual in the system.
 * <p>
 * It contains personal details such as first name, last name, address, city, zip code, phone number,
 * email address, and a medical record associated with the person.
 */
@Data
public class Person {
    private String firstName;
    private String lastName;
    private String address;
    private String city;
    private String zip;
    private String phone;
    private String email;
    private MedicalRecord medicalRecord;
}
