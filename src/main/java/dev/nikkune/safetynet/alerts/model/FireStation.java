package dev.nikkune.safetynet.alerts.model;

import lombok.Data;

import java.util.List;

@Data
public class FireStation {
    private String address;
    private String station;
    private List<Person> persons;
}
