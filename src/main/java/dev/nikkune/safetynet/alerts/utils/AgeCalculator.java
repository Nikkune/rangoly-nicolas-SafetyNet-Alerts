package dev.nikkune.safetynet.alerts.utils;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

/**
 * The AgeCalculator class provides utility methods for calculating
 * the age of a person based on their birthdate.
 */
public class AgeCalculator {
    /**
     * Calculates the age of a person given their birthdate in the format MM/dd/yyyy.
     *
     * @param birthdate the birthdate of the person to calculate the age of
     * @return the age of the person
     * @throws IllegalArgumentException if the birthdate is null or empty, or if the birthdate is invalid
     */
    public static int calculateAge(String birthdate) {
        if (birthdate == null || birthdate.isEmpty()) {
            throw new IllegalArgumentException("Birthdate cannot be null or empty.");
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDate birthDate;
        try {
            birthDate = LocalDate.parse(birthdate, formatter);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid birthdate format.");
        }
        LocalDate currentDate = LocalDate.now();
        return Period.between(birthDate, currentDate).getYears();
    }
}
