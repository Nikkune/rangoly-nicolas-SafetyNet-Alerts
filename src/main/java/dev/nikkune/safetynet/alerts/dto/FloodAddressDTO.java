package dev.nikkune.safetynet.alerts.dto;

import lombok.Data;

@Data
public class FloodAddressDTO {
    private String address;
    private FireStationCoverageDTO coverage;
}
