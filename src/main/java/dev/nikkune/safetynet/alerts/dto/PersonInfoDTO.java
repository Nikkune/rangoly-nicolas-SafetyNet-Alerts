package dev.nikkune.safetynet.alerts.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class PersonInfoDTO extends PersonWithMedicalDTO {
    private String address;
    private int age;
    private String email;
}
