package dev.nikkune.safetynet.alerts.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ChildAlertChildDTO extends PersonBaseDTO {
    private int age;
}
