package dev.nikkune.safetynet.alerts.mapper;

import dev.nikkune.safetynet.alerts.dto.PersonBaseDTO;
import dev.nikkune.safetynet.alerts.model.Person;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PersonBaseMapper {
    PersonBaseDTO toDTO(Person person);
}
