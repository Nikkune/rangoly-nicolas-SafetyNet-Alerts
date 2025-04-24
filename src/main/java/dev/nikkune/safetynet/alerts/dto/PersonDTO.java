package dev.nikkune.safetynet.alerts.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * A PersonDTO is an object that represents a person in the system.
 * <p>
 * It contains information about the person's name, address, city, zip code, phone number, and email address.
 */
@Data
public class PersonDTO {
    @NotBlank(message = "First name is required")
    @Size(min = 2, message = "First name must be at least 2 characters long")
    private String firstName;
    @NotBlank(message = "Last name is required")
    private String lastName;
    @NotBlank(message = "Address is required")
    private String address;
    @NotBlank(message = "City is required")
    private String city;
    @NotBlank(message = "Zip code is required")
    @Size(min = 5, max = 5, message = "Zip code must be 5 digits long")
    private String zip;
    @NotBlank(message = "Phone number is required")
//    @Phone
    private String phone;
    @NotBlank(message = "Email is required")
    @Email
    private String email;
}
