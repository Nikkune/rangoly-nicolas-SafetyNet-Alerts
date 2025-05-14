package dev.nikkune.safetynet.alerts.mapper;

import dev.nikkune.safetynet.alerts.dto.PersonDTO;
import dev.nikkune.safetynet.alerts.model.Person;
import org.mapstruct.Mapper;

/**
 * The PersonMapper interface provides methods for converting between
 * Person and PersonDTO objects.
 * <p>
 * This is a MapStruct mapper and is used for mapping between the
 * domain model and data transfer objects.
 */
@Mapper(componentModel = "spring")
public interface PersonMapper {
    /**
     * Converts a Person to a PersonDTO.
     *
     * @param person the person to convert
     * @return a PersonDTO instance
     */
    PersonDTO toDTO(Person person);

    /**
     * Converts a PersonDTO to a Person.
     *
     * @param personDTO the person DTO to convert
     * @return a Person instance
     */
    Person toEntity(PersonDTO personDTO);
}
