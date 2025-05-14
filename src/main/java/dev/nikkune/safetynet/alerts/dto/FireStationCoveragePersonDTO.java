package dev.nikkune.safetynet.alerts.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class FireStationCoveragePersonDTO extends PersonBaseDTO {
    private String address;
    private String phone;
}
