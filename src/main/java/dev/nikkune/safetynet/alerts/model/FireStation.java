package dev.nikkune.safetynet.alerts.model;

import lombok.Data;

import java.util.List;

/**
 * The FireStation class represents a fire station in the system.
 * <p>
 * It contains information about the station's address, its identifier (station number/name),
 * and a list of persons associated with that fire station.
 */
@Data
public class FireStation {
    private String address;
    private String station;
    private List<Person> persons;
}
