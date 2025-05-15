package dev.nikkune.safetynet.alerts.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@JsonPropertyOrder({"firstName", "lastName", "phone", "age", "medications", "allergies"})
public class FirePersonDTO extends PersonWithMedicalDTO {
    private String phone;
    private int age;
}
