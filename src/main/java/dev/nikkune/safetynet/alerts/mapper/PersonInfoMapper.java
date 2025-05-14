package dev.nikkune.safetynet.alerts.mapper;

import dev.nikkune.safetynet.alerts.dto.PersonInfoDTO;
import dev.nikkune.safetynet.alerts.model.Person;
import dev.nikkune.safetynet.alerts.utils.AgeCalculator;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface PersonInfoMapper {
    @Mapping(target = "age", source = "medicalRecord.birthdate", qualifiedByName = "birthdateToAge")
    @Mapping(target = "medications", source = "medicalRecord.medications")
    @Mapping(target = "allergies", source = "medicalRecord.allergies")
    PersonInfoDTO toDTO(Person person);

    @Named("birthdateToAge")
    static int birthdateToAge(String birthdate) {
        return AgeCalculator.calculateAge(birthdate);
    }
}
