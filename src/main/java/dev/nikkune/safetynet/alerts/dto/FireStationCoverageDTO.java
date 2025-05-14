package dev.nikkune.safetynet.alerts.dto;

import lombok.Data;

import java.util.List;

@Data
public class FireStationCoverageDTO {
    private List<FireStationCoveragePersonDTO> persons;
    private int numberOfAdults;
    private int numberOfChildren;
}
