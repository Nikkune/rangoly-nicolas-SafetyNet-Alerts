package dev.nikkune.safetynet.alerts.mapper;

import dev.nikkune.safetynet.alerts.dto.FireStationDTO;
import dev.nikkune.safetynet.alerts.model.FireStation;
import org.mapstruct.Mapper;

/**
 * The FireStationMapper interface provides methods for converting between
 * FireStation and FireStationDTO objects.
 * <p>
 * This is a MapStruct mapper and is used for mapping between the
 * domain model and data transfer objects.
 */
@Mapper(componentModel = "spring")
public interface FireStationMapper {

    /**
     * Converts a FireStation to a FireStationDTO.
     *
     * @param fireStation the fire station to convert
     * @return a FireStationDTO instance
     */
    FireStationDTO toDTO(FireStation fireStation);

    /**
     * Converts a FireStationDTO to a FireStation.
     *
     * @param fireStationDTO the fire station DTO to convert
     * @return a FireStation instance
     */
    FireStation toEntity(FireStationDTO fireStationDTO);
}
