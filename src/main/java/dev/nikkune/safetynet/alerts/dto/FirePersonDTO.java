package dev.nikkune.safetynet.alerts.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class FirePersonDTO extends PersonWithMedicalDTO {
    private String phone;
    private int age;
}
