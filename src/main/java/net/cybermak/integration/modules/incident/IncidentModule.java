package net.cybermak.integration.modules.incident;

import net.cybermak.integration.core.Module;
import net.cybermak.integration.core.model.GenericRequest;
import net.cybermak.integration.core.model.GenericResponse;
import net.cybermak.integration.core.model.ValidationResult;
import net.cybermak.integration.remedy.form.FormHandler;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * Incident module implementation for BMC Remedy integration
 * TDD: Implementation to satisfy IncidentModuleTest requirements
 */
@Component
public class IncidentModule implements Module {
    
    private static final String MODULE_TYPE = "incident";
    private static final String REMEDY_FORM = "HPD:Help Desk";
    
    private final FormHandler formHandler;
    
    // Field mappings from generic fields to BMC Remedy fields
    private static final Map<String, String> FIELD_MAPPINGS = Map.of(
        "summary", "Short_Description",
        "description", "Detailed_Decription",
        "priority", "Priority",
        "status", "Status",
        "submitter", "Submitter",
        "impact", "Impact",
        "urgency", "Urgency"
    );
    
    // Required fields for incident creation
    private static final List<String> REQUIRED_FIELDS = List.of(
        "summary", "description", "priority", "submitter"
    );
    
    public IncidentModule(FormHandler formHandler) {
        this.formHandler = formHandler;
    }
    
    @Override
    public String getModuleType() {
        return MODULE_TYPE;
    }
    
    @Override
    public ValidationResult validate(GenericRequest request) {
        List<String> errors = new ArrayList<>();
        Map<String, Object> data = request.getData();
        
        if (data == null || data.isEmpty()) {
            errors.add("Request data cannot be empty");
            return ValidationResult.invalid(errors);
        }
        
        // Validate required fields for create operation
        if ("create".equals(request.getOperation())) {
            for (String requiredField : REQUIRED_FIELDS) {
                if (!data.containsKey(requiredField) || 
                    data.get(requiredField) == null || 
                    data.get(requiredField).toString().trim().isEmpty()) {
                    errors.add(getFieldDisplayName(requiredField) + " is required");
                }
            }
        }
        
        // Validate incident ID for get/update operations
        if (("get".equals(request.getOperation()) || "update".equals(request.getOperation())) &&
            (!data.containsKey("incidentId") || data.get("incidentId") == null)) {
            errors.add("Incident ID is required for " + request.getOperation() + " operation");
        }
        
        // Validate priority values if provided
        if (data.containsKey("priority")) {
            String priority = data.get("priority").toString();
            if (!isValidPriority(priority)) {
                errors.add("Invalid priority value. Must be: Critical, High, Medium, Low");
            }
        }
        
        return errors.isEmpty() ? ValidationResult.valid() : ValidationResult.invalid(errors);
    }
    
    @Override
    public GenericResponse process(GenericRequest request) {
        ValidationResult validation = validate(request);
        if (!validation.isValid()) {
            throw new IllegalArgumentException("Invalid request: " + validation.getErrorMessage());
        }
        
        String operation = request.getOperation();
        
        switch (operation) {
            case "create":
                return processCreateIncident(request);
            case "get":
                return processGetIncident(request);
            case "update":
                return processUpdateIncident(request);
            default:
                throw new IllegalArgumentException("Unsupported operation: " + operation);
        }
    }
    
    @Override
    public Map<String, String> getFieldMappings() {
        return new HashMap<>(FIELD_MAPPINGS);
    }
    
    private GenericResponse processCreateIncident(GenericRequest request) {
        Map<String, Object> remedyFields = mapToRemedyFields(request.getData());
        
        // Set default values for BMC Remedy
        remedyFields.put("Status", "New");
        remedyFields.put("Source", "Integration");
        
        String incidentId = formHandler.createEntry(REMEDY_FORM, remedyFields);
        
        return GenericResponse.builder()
            .status("SUCCESS")
            .data(Map.of("incidentId", incidentId))
            .message("Incident created successfully")
            .timestamp(LocalDateTime.now().toString())
            .build();
    }
    
    private GenericResponse processGetIncident(GenericRequest request) {
        String incidentId = request.getData().get("incidentId").toString();
        
        Map<String, Object> remedyData = formHandler.getEntry(REMEDY_FORM, incidentId);
        Map<String, Object> genericData = mapFromRemedyFields(remedyData);
        
        return GenericResponse.builder()
            .status("SUCCESS")
            .data(Map.of("incident", genericData))
            .message("Incident retrieved successfully")
            .timestamp(LocalDateTime.now().toString())
            .build();
    }
    
    private GenericResponse processUpdateIncident(GenericRequest request) {
        String incidentId = request.getData().get("incidentId").toString();
        
        // Remove incidentId from update data
        Map<String, Object> updateData = new HashMap<>(request.getData());
        updateData.remove("incidentId");
        
        Map<String, Object> remedyFields = mapToRemedyFields(updateData);
        
        formHandler.updateEntry(REMEDY_FORM, incidentId, remedyFields);
        
        return GenericResponse.builder()
            .status("SUCCESS")
            .data(Map.of("incidentId", incidentId))
            .message("Incident updated successfully")
            .timestamp(LocalDateTime.now().toString())
            .build();
    }
    
    private Map<String, Object> mapToRemedyFields(Map<String, Object> genericFields) {
        Map<String, Object> remedyFields = new HashMap<>();
        
        for (Map.Entry<String, Object> entry : genericFields.entrySet()) {
            String genericFieldName = entry.getKey();
            String remedyFieldName = FIELD_MAPPINGS.get(genericFieldName);
            
            if (remedyFieldName != null) {
                remedyFields.put(remedyFieldName, entry.getValue());
            }
        }
        
        return remedyFields;
    }
    
    private Map<String, Object> mapFromRemedyFields(Map<String, Object> remedyFields) {
        Map<String, Object> genericFields = new HashMap<>();
        
        // Reverse mapping
        for (Map.Entry<String, String> mapping : FIELD_MAPPINGS.entrySet()) {
            String genericFieldName = mapping.getKey();
            String remedyFieldName = mapping.getValue();
            
            if (remedyFields.containsKey(remedyFieldName)) {
                genericFields.put(genericFieldName, remedyFields.get(remedyFieldName));
            }
        }
        
        // Add special fields
        if (remedyFields.containsKey("Incident_Number")) {
            genericFields.put("incidentId", remedyFields.get("Incident_Number"));
        }
        
        return genericFields;
    }
    
    private boolean isValidPriority(String priority) {
        return List.of("Critical", "High", "Medium", "Low").contains(priority);
    }
    
    private String getFieldDisplayName(String fieldName) {
        switch (fieldName) {
            case "summary": return "Summary";
            case "description": return "Description";
            case "priority": return "Priority";
            case "submitter": return "Submitter";
            default: return fieldName;
        }
    }
}