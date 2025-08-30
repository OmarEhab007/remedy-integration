package net.cybermak.integration.remedy.form;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * BMC Remedy form handler for CRUD operations
 * TDD: Minimal implementation to satisfy RemedyFormHandlerTest
 */
@Component
public class RemedyFormHandler implements FormHandler {
    
    private final Map<String, Map<String, Object>> dataStore = new ConcurrentHashMap<>();
    private final AtomicLong incidentIdGenerator = new AtomicLong(123);
    
    // Known forms and their required fields
    private static final Map<String, List<String>> FORM_REQUIRED_FIELDS = Map.of(
        "HPD:Help Desk", List.of("Short_Description", "Priority", "Status")
    );
    
    /**
     * Creates a new entry in the specified form
     * @param formName the BMC Remedy form name
     * @param fields the field values for the new entry
     * @return the generated entry ID
     * @throws IllegalArgumentException if form doesn't exist or required fields are missing
     */
    public String createEntry(String formName, Map<String, Object> fields) {
        validateForm(formName);
        validateRequiredFields(formName, fields);
        
        String entryId = generateEntryId();
        
        // Store the entry
        Map<String, Object> entryData = new ConcurrentHashMap<>(fields);
        entryData.put("Incident_Number", entryId);
        entryData.put("Entry_ID", entryId);
        
        dataStore.put(entryId, entryData);
        
        return entryId;
    }
    
    /**
     * Retrieves an entry by ID
     * @param formName the BMC Remedy form name
     * @param entryId the entry ID to retrieve
     * @return the entry data
     * @throws IllegalArgumentException if entry not found
     */
    public Map<String, Object> getEntry(String formName, String entryId) {
        Map<String, Object> entry = dataStore.get(entryId);
        if (entry == null) {
            throw new IllegalArgumentException("Entry not found: " + entryId);
        }
        return new ConcurrentHashMap<>(entry);
    }
    
    /**
     * Updates an existing entry
     * @param formName the BMC Remedy form name
     * @param entryId the entry ID to update
     * @param updates the field updates to apply
     * @throws IllegalArgumentException if entry not found
     */
    public void updateEntry(String formName, String entryId, Map<String, Object> updates) {
        Map<String, Object> entry = dataStore.get(entryId);
        if (entry == null) {
            throw new IllegalArgumentException("Entry not found: " + entryId);
        }
        
        entry.putAll(updates);
    }
    
    /**
     * Deletes an entry
     * @param formName the BMC Remedy form name
     * @param entryId the entry ID to delete
     */
    public void deleteEntry(String formName, String entryId) {
        dataStore.remove(entryId);
    }
    
    private void validateForm(String formName) {
        if (!FORM_REQUIRED_FIELDS.containsKey(formName)) {
            throw new IllegalArgumentException("Form not found: " + formName);
        }
    }
    
    private void validateRequiredFields(String formName, Map<String, Object> fields) {
        List<String> requiredFields = FORM_REQUIRED_FIELDS.get(formName);
        
        for (String requiredField : requiredFields) {
            if (!fields.containsKey(requiredField) || fields.get(requiredField) == null) {
                throw new IllegalArgumentException("Required field missing: " + requiredField);
            }
        }
    }
    
    private String generateEntryId() {
        return String.format("INC%012d", incidentIdGenerator.getAndIncrement());
    }
}