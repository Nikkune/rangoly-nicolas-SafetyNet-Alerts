package dev.nikkune.safetynet.alerts.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class PersonWithMedicalDTO extends PersonBaseDTO {
    private List<String> medications;
    private List<String> allergies;
}
