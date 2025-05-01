package dev.nikkune.safetynet.alerts.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * A FireStationDTO is an object that represents a fire station in the system.
 * <p>
 * It contains information about the station's address and its identifier (station number/name).
 */
@Data
public class FireStationDTO {
    @NotBlank(message = "Address is required")
    private String address;
    @NotBlank(message = "Station is required")
    private String station;
}
