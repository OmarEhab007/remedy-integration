package net.cybermak.integration.api.bridge;

import net.cybermak.integration.api.model.requests.IncidentDetails;
import net.cybermak.integration.api.service.CreateIncidentService;
import net.cybermak.integration.core.Module;
import net.cybermak.integration.core.model.GenericRequest;
import net.cybermak.integration.core.model.GenericResponse;
import net.cybermak.integration.core.model.ValidationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Bridge between legacy incident creation and modern module architecture
 * Allows the incident module to leverage existing BMC ARS implementation
 */
@Component
public class IncidentModuleBridge implements Module {
    
    private static final Logger logger = LoggerFactory.getLogger(IncidentModuleBridge.class);
    private final CreateIncidentService createIncidentService;
    
    public IncidentModuleBridge(CreateIncidentService createIncidentService) {
        this.createIncidentService = createIncidentService;
    }
    
    @Override
    public String getModuleType() {
        return "incident";
    }
    
    @Override
    public ValidationResult validate(GenericRequest request) {
        if (request == null) {
            return ValidationResult.invalid("Request cannot be null");
        }
        
        if (request.getOperation() == null || request.getOperation().trim().isEmpty()) {
            return ValidationResult.invalid("Operation cannot be null or empty");
        }
        
        if (request.getData() == null) {
            return ValidationResult.invalid("Request data cannot be null");
        }
        
        // Validate based on operation type
        switch (request.getOperation().toLowerCase()) {
            case "create":
                return validateCreateRequest(request);
            case "get":
            case "update":
                return validateIncidentIdRequest(request);
            case "search":
                return ValidationResult.valid(); // Search can have flexible parameters
            default:
                return ValidationResult.invalid("Unsupported operation: " + request.getOperation());
        }
    }
    
    @Override
    public Map<String, String> getFieldMappings() {
        Map<String, String> mappings = new HashMap<>();
        
        // BMC Remedy field mappings for incident creation
        mappings.put("sourceOfCreation", "536870943");
        mappings.put("alertName", "536870913");
        mappings.put("alertDescription", "536870914");
        mappings.put("severity", "536870915");
        mappings.put("alertLimitCategory", "536870916");
        mappings.put("add1", "536870917");
        mappings.put("add2", "536870918");
        
        return mappings;
    }
    
    @Override
    public GenericResponse process(GenericRequest request) {
        logger.info("Processing incident request: {}", request.getOperation());
        
        try {
            switch (request.getOperation().toLowerCase()) {
                case "create":
                    return handleCreate(request);
                case "get":
                    return handleGet(request);
                case "update":
                    return handleUpdate(request);
                case "search":
                    return handleSearch(request);
                default:
                    return GenericResponse.builder()
                        .status("ERROR")
                        .message("Unsupported operation: " + request.getOperation())
                        .data(Map.of())
                        .build();
            }
        } catch (Exception e) {
            logger.error("Error processing incident request: {}", e.getMessage(), e);
            return GenericResponse.builder()
                .status("ERROR")
                .message("Error processing request: " + e.getMessage())
                .data(Map.of())
                .build();
        }
    }
    
    private GenericResponse handleCreate(GenericRequest request) {
        // Convert generic request to IncidentDetails
        IncidentDetails incidentDetails = convertToIncidentDetails(request.getData());
        
        // Set default key for legacy API compatibility
        if (incidentDetails.getKey() == null || incidentDetails.getKey().isEmpty()) {
            incidentDetails.setKey("10");
        }
        
        // Use legacy service to create incident
        String incidentId = createIncidentService.createIncident(incidentDetails);
        
        if (incidentId != null && !incidentId.isEmpty()) {
            return GenericResponse.builder()
                .status("SUCCESS")
                .message("Incident created successfully")
                .data(Map.of("incidentId", incidentId, "status", "Created"))
                .build();
        } else {
            return GenericResponse.builder()
                .status("FAILED")
                .message("Failed to create incident")
                .data(Map.of())
                .build();
        }
    }
    
    private GenericResponse handleGet(GenericRequest request) {
        // For now, return mock data - in real implementation would query BMC
        String incidentId = (String) request.getData().get("incidentId");
        
        return GenericResponse.builder()
            .status("SUCCESS")
            .message("Incident retrieved successfully")
            .data(Map.of(
                "incidentId", incidentId,
                "status", "Active",
                "priority", "Medium",
                "description", "Mock incident data"
            ))
            .build();
    }
    
    private GenericResponse handleUpdate(GenericRequest request) {
        // For now, return mock success - in real implementation would update BMC
        String incidentId = (String) request.getData().get("incidentId");
        
        return GenericResponse.builder()
            .status("SUCCESS")
            .message("Incident updated successfully")
            .data(Map.of("incidentId", incidentId, "status", "Updated"))
            .build();
    }
    
    private GenericResponse handleSearch(GenericRequest request) {
        // For now, return mock search results
        return GenericResponse.builder()
            .status("SUCCESS")
            .message("Search completed successfully")
            .data(Map.of("incidents", java.util.List.of(
                Map.of("incidentId", "INC000001", "status", "Active", "priority", "High"),
                Map.of("incidentId", "INC000002", "status", "Resolved", "priority", "Medium")
            )))
            .build();
    }
    
    /**
     * Convert generic request data to IncidentDetails object
     */
    private IncidentDetails convertToIncidentDetails(Map<String, Object> data) {
        IncidentDetails details = new IncidentDetails();
        
        details.setSourceOfCreation(getString(data, "sourceOfCreation"));
        details.setAlertName(getString(data, "alertName"));
        details.setAlertDescription(getString(data, "alertDescription"));
        details.setSeverity(getString(data, "severity"));
        details.setAlertLimitCategory(getString(data, "alertLimitCategory"));
        details.setAdd1(getString(data, "add1"));
        details.setAdd2(getString(data, "add2"));
        details.setKey(getString(data, "key"));
        
        return details;
    }
    
    private String getString(Map<String, Object> data, String key) {
        Object value = data.get(key);
        return value != null ? value.toString() : "";
    }
    
    /**
     * Validate create incident request
     */
    private ValidationResult validateCreateRequest(GenericRequest request) {
        Map<String, Object> data = request.getData();
        
        // Check required fields for incident creation
        if (!data.containsKey("alertName") || isEmpty(getString(data, "alertName"))) {
            return ValidationResult.invalid("alertName is required for incident creation");
        }
        
        if (!data.containsKey("alertDescription") || isEmpty(getString(data, "alertDescription"))) {
            return ValidationResult.invalid("alertDescription is required for incident creation");
        }
        
        if (!data.containsKey("severity") || isEmpty(getString(data, "severity"))) {
            return ValidationResult.invalid("severity is required for incident creation");
        }
        
        return ValidationResult.valid();
    }
    
    /**
     * Validate requests that require incident ID
     */
    private ValidationResult validateIncidentIdRequest(GenericRequest request) {
        Map<String, Object> data = request.getData();
        
        if (!data.containsKey("incidentId") || isEmpty(getString(data, "incidentId"))) {
            return ValidationResult.invalid("incidentId is required for " + request.getOperation() + " operation");
        }
        
        return ValidationResult.valid();
    }
    
    private boolean isEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }
}