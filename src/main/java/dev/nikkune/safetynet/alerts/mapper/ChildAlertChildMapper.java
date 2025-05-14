package dev.nikkune.safetynet.alerts.mapper;

import dev.nikkune.safetynet.alerts.dto.ChildAlertChildDTO;
import dev.nikkune.safetynet.alerts.model.Person;
import dev.nikkune.safetynet.alerts.utils.AgeCalculator;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface ChildAlertChildMapper {
    @Mapping(target = "age", source = "medicalRecord.birthdate", qualifiedByName = "birthdateToAge")
    ChildAlertChildDTO toDTO(Person person);

    @Named("birthdateToAge")
    static int birthdateToAge(String birthdate) {
        return AgeCalculator.calculateAge(birthdate);
    }
}
