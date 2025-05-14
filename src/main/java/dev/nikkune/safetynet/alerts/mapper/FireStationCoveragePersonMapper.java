package dev.nikkune.safetynet.alerts.mapper;

import dev.nikkune.safetynet.alerts.dto.FireStationCoveragePersonDTO;
import dev.nikkune.safetynet.alerts.model.Person;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FireStationCoveragePersonMapper {
    FireStationCoveragePersonDTO toDTO(Person person);
}
