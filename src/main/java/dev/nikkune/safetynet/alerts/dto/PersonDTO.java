package dev.nikkune.safetynet.alerts.dto;

import lombok.Data;

/**
 * A PersonDTO is an object that represents a person in the system.
 * <p>
 * It contains information about the person's name, address, city, zip code, phone number, and email address.
 */
@Data
public class PersonDTO {
    private String firstName;
    private String lastName;
    private String address;
    private String city;
    private String zip;
    private String phone;
    private String email;
}
