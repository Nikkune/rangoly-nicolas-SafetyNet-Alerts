package dev.nikkune.safetynet.alerts.controller;

import dev.nikkune.safetynet.alerts.dto.*;
import dev.nikkune.safetynet.alerts.service.URLService;
import jakarta.validation.constraints.NotBlank;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
@Validated
public class URLController {
    private final URLService service;

    private static final Logger logger = LogManager.getLogger(URLController.class);

    public URLController(URLService service) {
        this.service = service;
    }

    @GetMapping("/firestation")
    public ResponseEntity<List<FireStationCoverageDTO>> getPersonCoveredByStation(@RequestParam @NotBlank(message = "Station Number is required") String stationNumber) {
        logger.debug("Received request to get person covered by station {}", stationNumber);
        FireStationCoverageDTO fireStationCoverageDTO = service.getPersonCoveredByStation(stationNumber);
        if (fireStationCoverageDTO.getPersons().isEmpty()) {
            logger.error("No person found for station number : {}", stationNumber);
            return new ResponseEntity<>(List.of(), HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(List.of(fireStationCoverageDTO));
    }

    @GetMapping("/childAlert")
    public ResponseEntity<List<ChildAlertDTO>> getPersonWithChildAlert(@RequestParam @NotBlank(message = "Address is required") String address) {
        logger.debug("Received request to get child alert for address {}", address);
        List<ChildAlertDTO> persons = service.getChildAlert(address);
        if (persons.isEmpty()) {
            logger.error("No child alert found for address : {}", address);
            return new ResponseEntity<>(persons, HttpStatus.NOT_FOUND);
        }
        logger.info("Returning {} persons with child alert for address {}", persons.size(), address);
        return ResponseEntity.ok(persons);
    }

    /**
     * Retrieves a list of phone numbers of all persons covered by the specified fire station.
     *
     * @param firestation the identifier of the fire station whose associated persons' phone numbers
     *                    are to be retrieved; must not be blank
     * @return a ResponseEntity containing a list of phone numbers of persons covered by the specified
     * fire station
     */
    @GetMapping("/phoneAlert")
    public ResponseEntity<Set<String>> getPhoneAlert(@RequestParam @NotBlank(message = "Station Number is required") String firestation) {
        logger.debug("Received request to get phone alert for firestation {}", firestation);
        Set<String> phones = service.getPhoneAlert(firestation);
        if (phones.isEmpty()) {
            logger.error("No phone number found for station number : {}", firestation);
            return new ResponseEntity<>(phones, HttpStatus.NOT_FOUND);
        }
        logger.info("Returning {} phones for firestation {}", phones.size(), firestation);
        return ResponseEntity.ok(phones);
    }

    @GetMapping("/fire")
    public ResponseEntity<List<FireAddressDTO>> getFire(@RequestParam @NotBlank(message = "Address is required") String address) {
        logger.debug("Received request to get fire for address {}", address);
        List<FireAddressDTO> fireDTOList = service.getFire(address);
        if (fireDTOList.isEmpty()) {
            logger.error("No fire found for address : {}", address);
            return new ResponseEntity<>(fireDTOList, HttpStatus.NOT_FOUND);
        }
        logger.info("Returning {} fires for address {}", fireDTOList.size(), address);
        return ResponseEntity.ok(fireDTOList);
    }

    @GetMapping("/flood/stations")
    public ResponseEntity<List<FloodAddressDTO>> getFloodPersonCoveredByStation(@RequestParam @NotBlank(message = "Fire stations is required") String stations) {
        logger.debug("Received request to get person in flood by stations {}", stations);
        List<FloodAddressDTO> floodPersonCoveredByStation = service.getFloodPersonCoveredByStation(stations);
        if (floodPersonCoveredByStation.isEmpty()) {
            logger.error("No person found for stations : {}", stations);
            return new ResponseEntity<>(floodPersonCoveredByStation, HttpStatus.NOT_FOUND);
        }
        logger.info("Returning {} persons in flood by stations {}", floodPersonCoveredByStation.size(), stations);
        return ResponseEntity.ok(floodPersonCoveredByStation);
    }

    @GetMapping("/personInfo")
    public ResponseEntity<List<PersonInfoDTO>> getPersonInfo(@RequestParam @NotBlank(message = "Last Name is required") String lastName) {
        logger.debug("Received request to get person info for last name {}", lastName);
        List<PersonInfoDTO> persons = service.getPersonInfo(lastName);
        if (persons.isEmpty()) {
            logger.error("No person found for last name : {}", lastName);
            return new ResponseEntity<>(persons, HttpStatus.NOT_FOUND);
        }
        logger.info("Returning {} persons with last name {}", persons.size(), lastName);
        return ResponseEntity.ok(persons);
    }

    /**
     * Retrieves a list of email addresses of all persons residing in the specified city.
     *
     * @param city the name of the city for which the community email addresses are to be retrieved;
     *             must not be blank
     * @return a ResponseEntity containing a list of email addresses of persons residing in the specified city
     */
    @GetMapping("/communityEmail")
    public ResponseEntity<Set<String>> getCommunityEmail(@RequestParam @NotBlank(message = "City is required") String city) {
        logger.debug("Received request to get community email for city {}", city);
        Set<String> emails = service.getCommunityEmail(city);
        if (emails.isEmpty()) {
            logger.error("No email found for city : {}", city);
            return new ResponseEntity<>(emails, HttpStatus.NOT_FOUND);
        }
        logger.info("Returning {} emails for address {}", emails.size(), city);
        return ResponseEntity.ok(emails);
    }
}
