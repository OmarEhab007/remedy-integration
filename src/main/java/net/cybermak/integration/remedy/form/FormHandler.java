package net.cybermak.integration.remedy.form;

import java.util.Map;

/**
 * Interface for BMC Remedy form operations
 * TDD: Interface to enable easy mocking in tests
 */
public interface FormHandler {
    
    /**
     * Creates a new entry in the specified form
     * @param formName the BMC Remedy form name
     * @param fields the field values for the new entry
     * @return the generated entry ID
     */
    String createEntry(String formName, Map<String, Object> fields);
    
    /**
     * Retrieves an entry by ID
     * @param formName the BMC Remedy form name
     * @param entryId the entry ID to retrieve
     * @return the entry data
     */
    Map<String, Object> getEntry(String formName, String entryId);
    
    /**
     * Updates an existing entry
     * @param formName the BMC Remedy form name
     * @param entryId the entry ID to update
     * @param updates the field updates to apply
     */
    void updateEntry(String formName, String entryId, Map<String, Object> updates);
    
    /**
     * Deletes an entry
     * @param formName the BMC Remedy form name
     * @param entryId the entry ID to delete
     */
    void deleteEntry(String formName, String entryId);
}