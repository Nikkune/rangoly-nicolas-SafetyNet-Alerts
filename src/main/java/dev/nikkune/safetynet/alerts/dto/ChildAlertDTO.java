package dev.nikkune.safetynet.alerts.dto;

import lombok.Data;

import java.util.List;

@Data
public class ChildAlertDTO {
    private ChildAlertChildDTO child;
    private List<PersonBaseDTO> otherHouseholdMembers;
}
