package dev.nikkune.safetynet.alerts.utils;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class AgeCalculatorTest {

    @Test
    public void testCalculateAge() {
        // Arrange
        String birthdate = "01/01/2000";
        // Act
        int expectedAge = calculateExpectedAge(birthdate);
        int actualAge = AgeCalculator.calculateAge(birthdate);
        // Assert
        assertEquals(expectedAge, actualAge);
    }

    @Test
    public void testInvalidBirthdate() {
        // Arrange
        String birthdate = "2000/01/01";
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> AgeCalculator.calculateAge(birthdate));
        assertEquals("Invalid birthdate format.", exception.getMessage());
    }

    @Test
    public void testNumberOfAdults() {
        // Arrange
        List<String> datesOfBirth = List.of("01/01/1980", "01/01/2010", "01/01/1990");
        // Act
        long numberOfAdults = AgeCalculator.numberOfAdults(datesOfBirth);
        // Assert
        assertEquals(2, numberOfAdults); // Two dates correspond to adults
    }

    @Test
    public void testNumberOfAdultsWithEmptyList() {
        // Arrange
        List<String> datesOfBirth = List.of();
        // Act
        long numberOfAdults = AgeCalculator.numberOfAdults(datesOfBirth);
        // Assert
        assertEquals(0, numberOfAdults); // No dates provided
    }

    @Test
    public void testNullBirthdate() {
        // Arrange

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> AgeCalculator.calculateAge(null));
        assertEquals("Birthdate cannot be null or empty.", exception.getMessage());
    }

    @Test
    public void testEmptyBirthdate() {
        // Arrange
        String birthdate = "";

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> AgeCalculator.calculateAge(birthdate));
        assertEquals("Birthdate cannot be null or empty.", exception.getMessage());
    }

    private int calculateExpectedAge(String birthdate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDate birthDate = LocalDate.parse(birthdate, formatter);
        LocalDate currentDate = LocalDate.now();
        return Period.between(birthDate, currentDate).getYears();
    }

    @Test
    public void testNumberOfChildren() {
        // Arrange
        List<String> datesOfBirth = List.of("01/01/2010", "01/01/2020", "01/01/2000");
        // Act
        long numberOfChildren = AgeCalculator.numberOfChildren(datesOfBirth);
        // Assert
        assertEquals(2, numberOfChildren);
    }

    @Test
    public void testNumberOfChildrenWithEmptyList() {
        // Arrange
        List<String> datesOfBirth = List.of();
        // Act
        long numberOfChildren = AgeCalculator.numberOfChildren(datesOfBirth);
        // Assert
        assertEquals(0, numberOfChildren); // No dates provided
    }
}
