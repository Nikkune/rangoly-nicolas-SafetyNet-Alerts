package dev.nikkune.safetynet.alerts.dto;

import lombok.Data;

import java.util.List;

@Data
public class FireAddressDTO {
    private String stationNumber;
    private List<FirePersonDTO> residents;
}
