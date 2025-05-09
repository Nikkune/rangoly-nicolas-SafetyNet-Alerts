package dev.nikkune.safetynet.alerts.service;

import dev.nikkune.safetynet.alerts.config.JsonDatabase;
import dev.nikkune.safetynet.alerts.model.FireStation;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The FireStationService class is responsible for managing the fire station data in the database.
 * <p>
 * It provides methods to retrieve all fire stations, retrieve a specific fire station by address,
 * create a new fire station, update an existing fire station, and delete a fire station.
 */
@Service
public class FireStationService {
    private final JsonDatabase jsonDatabase;

    public FireStationService(JsonDatabase jsonDatabase) {
        this.jsonDatabase = jsonDatabase;
    }

    /**
     * Retrieves all fire stations from the database.
     * <p>
     * Returns a list of all fire stations that are stored in the database.
     *
     * @return a list of all fire stations
     */
    public List<FireStation> getAll() {
        return jsonDatabase.fireStations();
    }

    /**
     * Retrieves a list of fire stations associated with the given station number.
     * Filters and returns fire stations from the database that match the specified station number.
     *
     * @param station the station number used to find the associated fire stations
     * @return a list of fire stations associated with the given station number
     * @throws RuntimeException if an error occurs during the retrieval process
     */
    public List<FireStation> get(String station) throws RuntimeException {
        return jsonDatabase.fireStations().stream().filter(f -> f.getStation().equals(station)).toList();
    }

    /**
     * Retrieves a list of fire stations from the database by their associated address.
     * Filters the fire station records based on the given address.
     *
     * @param address the address used to find the associated fire stations
     * @return a list of fire stations associated with the given address
     */
    public List<FireStation> getByAddress(String address) {
        return jsonDatabase.fireStations().stream().filter(f -> f.getAddress().equals(address)).toList();
    }

    /**
     * Creates a new fire station with the given address and station number.
     * <p>
     * The new fire station is associated with all persons that are associated with the given address.
     * <p>
     * The new fire station is added to the collection of fire stations in the database and the database is saved.
     * <p>
     * If a fire station with the given address already exists, a RuntimeException is thrown
     * with the message "Fire station already exists".
     *
     * @param fireStation the fire station to create
     * @throws RuntimeException if a fire station with the given address already exists
     */
    public void create(FireStation fireStation) throws RuntimeException {
        FireStation existingFireStation = jsonDatabase.fireStations().stream().filter(f -> f.getAddress().equals(fireStation.getAddress())).findFirst().orElse(null);
        if (existingFireStation == null) {
            fireStation.setPersons(jsonDatabase.people().stream().filter(p -> p.getAddress().equals(fireStation.getAddress())).toList());
            jsonDatabase.fireStations().add(fireStation);
            jsonDatabase.saveData();
        } else {
            throw new RuntimeException("Fire station already exists");
        }
    }

    /**
     * Updates an existing fire station in the database.
     * <p>
     * Finds the fire station by its address, updates its details including station number,
     * address, and associated persons, and persists the changes to the database.
     * <p>
     * If no fire station is found with the given address, a RuntimeException is thrown
     * with the message "Fire station not found".
     *
     * @param fireStation the fire station with updated details
     * @throws RuntimeException if no fire station is found with the given address
     */
    public void update(FireStation fireStation) throws RuntimeException {
        FireStation existingFireStation = jsonDatabase.fireStations().stream().filter(f -> f.getAddress().equals(fireStation.getAddress())).findFirst().orElse(null);
        if (existingFireStation != null) {
            existingFireStation.setStation(fireStation.getStation());
            existingFireStation.setPersons(jsonDatabase.people().stream().filter(p -> p.getAddress().equals(fireStation.getAddress())).toList());
            jsonDatabase.saveData();
        } else {
            throw new RuntimeException("Fire station not found");
        }
    }

    /**
     * Deletes a fire station by its address.
     * <p>
     * Finds the fire station with the given address, removes it from the collection of fire stations in the database,
     * and persists the changes to the database.
     * <p>
     * If no fire station is found with the given address, a RuntimeException is thrown with the message "Fire station not found".
     *
     * @param address the address of the fire station to delete
     * @throws RuntimeException if no fire station is found with the given address
     */
    public void deleteByAddress(String address) throws RuntimeException {
        FireStation existingFireStation = jsonDatabase.fireStations().stream().filter(f -> f.getAddress().equals(address)).findFirst().orElse(null);
        if (existingFireStation != null) {
            jsonDatabase.fireStations().remove(existingFireStation);
            jsonDatabase.saveData();
        } else {
            throw new RuntimeException("Fire station not found");
        }
    }

    /**
     * Deletes a fire station by its station number.
     * <p>
     * Finds the fire station with the given station number, removes it from the collection of fire stations in the database,
     * and persists the changes to the database.
     * <p>
     * If no fire station is found with the given station number, a RuntimeException is thrown with the message "Fire station not found".
     *
     * @param station the station number of the fire station to delete
     * @throws RuntimeException if no fire station is found with the given station number
     */
    public void deleteByStation(String station) throws RuntimeException {
        FireStation existingFireStation = jsonDatabase.fireStations().stream().filter(f -> f.getStation().equals(station)).findFirst().orElse(null);
        if (existingFireStation != null) {
            jsonDatabase.fireStations().remove(existingFireStation);
            jsonDatabase.saveData();
        } else {
            throw new RuntimeException("Fire station not found");
        }
    }
}
