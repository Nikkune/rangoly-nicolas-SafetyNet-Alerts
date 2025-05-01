package dev.nikkune.safetynet.alerts.mapper;

import dev.nikkune.safetynet.alerts.dto.MedicalRecordDTO;
import dev.nikkune.safetynet.alerts.model.MedicalRecord;
import org.mapstruct.Mapper;

/**
 * The MedicalRecordMapper interface provides methods for converting between
 * MedicalRecord and MedicalRecordDTO objects.
 * <p>
 * This is a MapStruct mapper and is used for mapping between the
 * domain model and data transfer objects.
 */
@Mapper(componentModel = "spring")
public interface MedicalRecordMapper {
    /**
     * Converts a MedicalRecord object to a MedicalRecordDTO.
     *
     * @param medicalRecord the medical record to convert
     * @return a MedicalRecordDTO instance representing the given medical record
     */
    MedicalRecordDTO toDTO(MedicalRecord medicalRecord);

    /**
     * Converts a MedicalRecordDTO object to a MedicalRecord.
     *
     * @param medicalRecordDTO the medical record DTO to convert
     * @return a MedicalRecord instance representing the given medical record DTO
     */
    MedicalRecord toEntity(MedicalRecordDTO medicalRecordDTO);
}
