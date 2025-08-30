package net.cybermak.integration.api.controller;

import net.cybermak.integration.api.model.requests.IncidentDetails;
import net.cybermak.integration.api.service.CreateIncidentService;
import net.cybermak.integration.config.SecurityConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * CreateIncidentController handles REST API requests for incident creation
 * Provides backward-compatible endpoint matching original WAR file
 * 
 * Original API Contract:
 * - Endpoint: POST /api/remedyITSM/createIncident
 * - Content-Type: application/json
 * - Authentication: Key-based (key field in request body must equal "10")
 * 
 * Recreated from original compiled WAR file to maintain API compatibility
 * 
 * @author Reverse Engineered from WAR file
 * @version 1.0
 */
@RestController
@RequestMapping("api/remedyITSM")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CreateIncidentController {

    static final Logger logger = LoggerFactory.getLogger(CreateIncidentController.class);
    
    private final CreateIncidentService createIncidentService;
    private final SecurityConfig securityConfig;

    /**
     * Constructor for dependency injection
     * 
     * @param createIncidentService Service for incident creation
     * @param securityConfig Security configuration
     */
    public CreateIncidentController(CreateIncidentService createIncidentService, SecurityConfig securityConfig) {
        this.createIncidentService = createIncidentService;
        this.securityConfig = securityConfig;
    }

    /**
     * Creates incident in BMC Remedy system from SolarWinds alert data
     * 
     * API Contract from original system:
     * - URL: POST /api/remedyITSM/createIncident
     * - Content-Type: application/json
     * - Authentication: key field must equal "10"
     * - Response: "Success", "Failed", or error message
     * 
     * @param incidentDetails The incident details from SolarWinds
     * @return ResponseEntity with success/failure message
     */
    @PostMapping(value = "/createIncident", produces = "application/json")
    public ResponseEntity<String> createIncident(@RequestBody IncidentDetails incidentDetails) {
        logger.info("Inside the createIncident method");
        logger.info(incidentDetails.toString());
        
        String response = null;
        
        // Validate API key using configurable security settings
        String expectedKey = securityConfig.getApiKey().getLegacyKey();
        if (securityConfig.getApiKey().isEnabled() && expectedKey.equals(incidentDetails.getKey())) {
            response = createIncidentService.createIncident(incidentDetails);
            
            if (response.isEmpty()) {
                return new ResponseEntity<>("Failed", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Success", HttpStatus.OK);
            }
        } else {
            response = "Invalid Key is Used, You are not allowed create the Incident.";
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }
}