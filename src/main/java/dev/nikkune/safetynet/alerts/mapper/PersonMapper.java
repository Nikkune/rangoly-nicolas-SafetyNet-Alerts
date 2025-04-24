package dev.nikkune.safetynet.alerts.mapper;

import dev.nikkune.safetynet.alerts.dto.PersonDTO;
import dev.nikkune.safetynet.alerts.model.Person;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * The PersonMapper interface provides methods for converting between
 * Person and PersonDTO objects.
 * <p>
 * This is a MapStruct mapper and is used for mapping between the
 * domain model and data transfer objects.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PersonMapper {
    /**
     * Converts a Person to a PersonDTO.
     *
     * @param person the person to convert
     * @return a PersonDTO instance
     */
    @Mapping(source = "firstName", target = "firstName")
    @Mapping(source = "lastName", target = "lastName")
    @Mapping(source = "address", target = "address")
    @Mapping(source = "city", target = "city")
    @Mapping(source = "zip", target = "zip")
    @Mapping(source = "phone", target = "phone")
    @Mapping(source = "email", target = "email")
    PersonDTO toDTO(Person person);

    /**
     * Converts a PersonDTO to a Person.
     *
     * @param personDTO the person DTO to convert
     * @return a Person instance
     */
    @Mapping(source = "firstName", target = "firstName")
    @Mapping(source = "lastName", target = "lastName")
    @Mapping(source = "address", target = "address")
    @Mapping(source = "city", target = "city")
    @Mapping(source = "zip", target = "zip")
    @Mapping(source = "phone", target = "phone")
    @Mapping(source = "email", target = "email")
    Person toEntity(PersonDTO personDTO);
}
