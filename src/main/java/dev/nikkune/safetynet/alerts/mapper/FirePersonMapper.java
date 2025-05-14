package dev.nikkune.safetynet.alerts.mapper;

import dev.nikkune.safetynet.alerts.dto.FirePersonDTO;
import dev.nikkune.safetynet.alerts.model.Person;
import dev.nikkune.safetynet.alerts.utils.AgeCalculator;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface FirePersonMapper {
    @Mapping(target = "age", source = "medicalRecord.birthdate", qualifiedByName = "birthdateToAge")
    @Mapping(target = "medications", source = "medicalRecord.medications")
    @Mapping(target = "allergies", source = "medicalRecord.allergies")
    FirePersonDTO toDTO (Person person);

    @Named("birthdateToAge")
    static int birthdateToAge(String birthdate) {
        return AgeCalculator.calculateAge(birthdate);
    }
}
