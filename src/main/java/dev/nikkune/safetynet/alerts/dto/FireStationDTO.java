package dev.nikkune.safetynet.alerts.dto;

import lombok.Data;

/**
 * A FireStationDTO is an object that represents a fire station in the system.
 * <p>
 * It contains information about the station's address and its identifier (station number/name).
 */
@Data
public class FireStationDTO {
    private String address;
    private String station;
}
