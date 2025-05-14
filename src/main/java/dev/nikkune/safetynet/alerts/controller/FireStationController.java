package dev.nikkune.safetynet.alerts.controller;

import dev.nikkune.safetynet.alerts.dto.FireStationDTO;
import dev.nikkune.safetynet.alerts.mapper.FireStationMapper;
import dev.nikkune.safetynet.alerts.model.FireStation;
import dev.nikkune.safetynet.alerts.service.FireStationService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The FireStationController class is responsible for managing the fire station data in the database.
 * <p>
 * It provides methods to get all the fire stations, get a fire station by its station number, create a new fire station, update an existing fire station, and delete a fire station.
 */
@RestController
@RequestMapping("/firestations")
@Validated
public class FireStationController {
    private final FireStationService service;
    private final FireStationMapper mapper;

    private static final Logger logger = LogManager.getLogger(FireStationController.class);

    public FireStationController(FireStationService service, FireStationMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    /**
     * Retrieves all fire stations from the database.
     * <p>
     * This endpoint returns a list of all fire stations in the system as FireStationDTO objects.
     *
     * @return a ResponseEntity containing a list of all fire station details
     */
    @GetMapping("/all")
    public ResponseEntity<List<FireStationDTO>> getAllFireStations() {
        logger.debug("Received request for all fire stations");
        List<FireStation> fireStations = service.getAll();
        List<FireStationDTO> fireStationsDTO = fireStations.stream().map(mapper::toDTO).toList();
        logger.info("Retrieved {} fire stations", fireStations.size());
        return ResponseEntity.ok(fireStationsDTO);
    }

    /**
     * Retrieves a fire station by its station number.
     * <p>
     * This endpoint receives the station number as a path variable and returns the associated fire station details.
     * <p>
     * If the fire station is not found, a RuntimeException is thrown with the message "Fire station not found".
     *
     * @param number the station number of the fire station to retrieve
     * @return the associated fire station details
     * @throws RuntimeException if the fire station is not found
     */
    @GetMapping
    public ResponseEntity<List<FireStationDTO>> getFireStationByStationNumber(@RequestParam @NotBlank(message = "Station Number is required") String number) {
        logger.debug("Received request for fire station by station number : {}", number);
        List<FireStation> fireStations = service.get(number);
        List<FireStationDTO> fireStationDTOS = fireStations.stream().map(mapper::toDTO).toList();
        if (fireStations.isEmpty()) {
            logger.error("No fire station found for station number : {}", number);
            return new ResponseEntity<>(fireStationDTOS, HttpStatus.NOT_FOUND);
        }
        logger.info("Retrieved {} fire stations by station number", fireStations.size());
        return ResponseEntity.ok(fireStationDTOS);
    }

    /**
     * Retrieves a fire station by its address.
     * <p>
     * This endpoint receives the address as a path variable and returns the associated fire station details.
     * <p>
     * If the fire station is not found, a RuntimeException is thrown with the message "Fire station not found".
     *
     * @param address the address of the fire station to retrieve
     * @return a ResponseEntity containing the fire station details
     * @throws RuntimeException if the fire station is not found
     */
    @GetMapping("/address")
    public ResponseEntity<List<FireStationDTO>> getFireStationByAddress(@RequestParam @NotBlank(message = "Address is required") String address) {
        logger.debug("Received request for fire station by address : {}", address);
        List<FireStation> fireStations = service.getByAddress(address);
        List<FireStationDTO> fireStationDTOS = fireStations.stream().map(mapper::toDTO).toList();
        if (fireStations.isEmpty()) {
            logger.error("No fire station found for address : {}", address);
            return new ResponseEntity<>(fireStationDTOS, HttpStatus.NOT_FOUND);
        }
        logger.info("Retrieved fire station by address");
        return ResponseEntity.ok(fireStationDTOS);
    }

    /**
     * Creates a new fire station with the given information.
     * <p>
     * The request body contains the fire station information and the response is an HTTP 200 (OK) status
     * and the created fire station is returned in the response body.
     * <p>
     * If a fire station with the same address already exists, a Runtime exception is thrown with the message
     * "Fire station already exists".
     *
     * @param fireStationDTO the fire station information
     * @return the created fire station
     * @throws RuntimeException if a fire station with the same address already exists
     */
    @PostMapping
    public ResponseEntity<FireStationDTO> createFireStation(@Valid @RequestBody FireStationDTO fireStationDTO) {
        logger.debug("Received request to create fire station");
        FireStation entity = mapper.toEntity(fireStationDTO);
        service.create(entity);
        logger.info("Created fire station");
        return ResponseEntity.ok(fireStationDTO);
    }

    /**
     * Updates an existing fire station in the database.
     * <p>
     * The fire station is identified by its address and the request body contains the updated information.
     * <p>
     * The response is an HTTP 200 (OK) status and the updated fire station is returned in the response body.
     * <p>
     * If the fire station is not found, a Runtime exception is thrown with the message "Fire station not found".
     *
     * @param fireStationDTO the fire station with updated details
     * @return a ResponseEntity with the updated fire station in its body
     * @throws RuntimeException if the fire station is not found
     */
    @PutMapping
    public ResponseEntity<FireStationDTO> updateFireStation(@Valid @RequestBody FireStationDTO fireStationDTO) {
        logger.debug("Received request to update fire station");
        FireStation entity = mapper.toEntity(fireStationDTO);
        service.update(entity);
        logger.info("Updated fire station");
        return ResponseEntity.ok(fireStationDTO);
    }

    /**
     * Deletes a fire station by its address.
     * <p>
     * This endpoint receives the address as a path variable and deletes the associated fire station.
     * <p>
     * If the fire station is not found, a Runtime exception is thrown with the message "Fire station not found".
     * <p>
     * The response is an HTTP 204 (No Content) status.
     *
     * @param address the address of the fire station to delete
     * @return a ResponseEntity with an empty body
     * @throws RuntimeException if the fire station is not found
     */
    @DeleteMapping
    public ResponseEntity<Void> deleteFireStationByAddress(@RequestParam @NotBlank(message = "Address is required") String address) {
        logger.debug("Received request to delete fire station by address : {}", address);
        service.deleteByAddress(address);
        logger.info("Deleted fire station by address");
        return ResponseEntity.noContent().build();
    }

    /**
     * Deletes a fire station by its station number.
     * <p>
     * This endpoint receives the station number as a path variable and deletes the associated fire station.
     * <p>
     * If the fire station is not found, a Runtime exception is thrown with the message "Fire station not found".
     * <p>
     * The response is an HTTP 204 (No Content) status.
     *
     * @param number the station number of the fire station to delete
     * @return a ResponseEntity with an empty body
     * @throws RuntimeException if the fire station is not found
     */
    @DeleteMapping("/station")
    public ResponseEntity<Void> deleteFireStation(@RequestParam @NotBlank(message = "Station Number is required") String number) {
        logger.debug("Received request to delete fire station by station number : {}", number);
        service.deleteByStation(number);
        logger.info("Deleted fire station by station number");
        return ResponseEntity.noContent().build();
    }
}
