package dev.nikkune.safetynet.alerts.controller;

import dev.nikkune.safetynet.alerts.dto.FireStationDTO;
import dev.nikkune.safetynet.alerts.mapper.FireStationMapper;
import dev.nikkune.safetynet.alerts.model.FireStation;
import dev.nikkune.safetynet.alerts.service.FireStationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The FireStationController class is responsible for managing the fire station data in the database.
 * <p>
 * It provides methods to get all the fire stations, get a fire station by its station number, create a new fire station, update an existing fire station, and delete a fire station.
 */
@RestController
@RequestMapping("/firestation")
public class FireStationController {
    private final FireStationService service;
    private final FireStationMapper mapper;

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
    @GetMapping
    public ResponseEntity<List<FireStationDTO>> getAllFireStations() {
        List<FireStation> fireStations = service.getAll();
        List<FireStationDTO> fireStationsDTO = fireStations.stream().map(fireStation -> mapper.toDTO(fireStation)).toList();
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
    @GetMapping("/{number}")
    public ResponseEntity<List<FireStationDTO>> getFireStationByStationNumber(@PathVariable String number) {
        try {
            List<FireStation> fireStations = service.get(number);
            List<FireStationDTO> fireStationDTOS = fireStations.stream().map(fireStation -> mapper.toDTO(fireStation)).toList();
            return ResponseEntity.ok(fireStationDTOS);
        } catch (RuntimeException e) {
            throw new RuntimeException("Fire station not found");
        }
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
    @GetMapping("/address/{address}")
    public ResponseEntity<FireStationDTO> getFireStationByAddress(@PathVariable String address) {
        try {
            FireStation fireStation = service.getByAddress(address);
            FireStationDTO fireStationDTO = mapper.toDTO(fireStation);
            return ResponseEntity.ok(fireStationDTO);
        } catch (RuntimeException e) {
            throw new RuntimeException("Fire station not found");
        }
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
    public ResponseEntity<FireStationDTO> createFireStation(@RequestBody FireStationDTO fireStationDTO) {
        FireStation entity = mapper.toEntity(fireStationDTO);
        try {
            service.create(entity);
            return ResponseEntity.ok(fireStationDTO);
        } catch (RuntimeException e) {
            throw new RuntimeException("Fire station already exists");
        }
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
    public ResponseEntity<FireStationDTO> updateFireStation(@RequestBody FireStationDTO fireStationDTO) {
        FireStation entity = mapper.toEntity(fireStationDTO);
        try {
            service.update(entity);
            return ResponseEntity.ok(fireStationDTO);
        } catch (RuntimeException e) {
            throw new RuntimeException("Fire station not found");
        }
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
    @DeleteMapping("/address/{address}")
    public ResponseEntity<Void> deleteFireStationByAddress(@PathVariable String address) {
        try {
            service.deleteByAddress(address);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            throw new RuntimeException("Fire station not found");
        }
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
     * @param station the station number of the fire station to delete
     * @return a ResponseEntity with an empty body
     * @throws RuntimeException if the fire station is not found
     */
    @DeleteMapping("/station/{station}")
    public ResponseEntity<Void> deleteFireStation(@PathVariable String station) {
        try {
            service.deleteByStation(station);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            throw new RuntimeException("Fire station not found");
        }
    }
}
