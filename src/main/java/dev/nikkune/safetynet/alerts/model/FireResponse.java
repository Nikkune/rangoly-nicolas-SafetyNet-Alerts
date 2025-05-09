package dev.nikkune.safetynet.alerts.model;

import lombok.Data;

import java.util.List;

@Data
public class FireResponse {
    private List<FireStation> fireStations;
    private List<Person> people;
}
