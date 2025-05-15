package dev.nikkune.safetynet.alerts.dto;

import lombok.Data;

import java.util.List;

@Data
public class FloodAddressDTO {
    private String address;
    private List<FirePersonDTO> residents;
}
