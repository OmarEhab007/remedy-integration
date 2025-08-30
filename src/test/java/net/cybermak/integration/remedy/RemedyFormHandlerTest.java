package net.cybermak.integration.remedy;

import net.cybermak.integration.remedy.form.RemedyFormHandler;
import net.cybermak.integration.remedy.model.RemedyForm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * TDD Test: BMC Remedy Form Handler tests
 * Following Red-Green-Refactor cycle
 */
class RemedyFormHandlerTest {

    private RemedyFormHandler formHandler;

    @BeforeEach
    void setUp() {
        formHandler = new RemedyFormHandler();
    }

    @Test
    void shouldCreateEntry() {
        // Red Phase: Will fail until we implement entry creation
        
        String formName = "HPD:Help Desk";
        Map<String, Object> fields = Map.of(
            "Short_Description", "Test incident",
            "Priority", "Medium",
            "Status", "New"
        );
        
        String entryId = formHandler.createEntry(formName, fields);
        
        assertThat(entryId).isNotNull();
        assertThat(entryId).isNotEmpty();
        assertThat(entryId).matches("INC\\d{12}"); // BMC Remedy incident ID pattern
    }

    @Test
    void shouldRetrieveEntry() {
        // Red Phase: Will fail until we implement entry retrieval
        
        String formName = "HPD:Help Desk";
        Map<String, Object> fields = Map.of(
            "Short_Description", "Test incident",
            "Priority", "Medium",
            "Status", "New"
        );
        
        // First create an entry
        String entryId = formHandler.createEntry(formName, fields);
        
        // Then retrieve it
        Map<String, Object> entry = formHandler.getEntry(formName, entryId);
        
        assertThat(entry).isNotNull();
        assertThat(entry).isNotEmpty();
        assertThat(entry.get("Incident_Number")).isEqualTo(entryId);
        assertThat(entry.get("Short_Description")).isEqualTo("Test incident");
    }

    @Test
    void shouldUpdateEntry() {
        // Red Phase: Will fail until we implement entry update
        
        String formName = "HPD:Help Desk";
        Map<String, Object> fields = Map.of(
            "Short_Description", "Test incident",
            "Priority", "Medium",
            "Status", "New"
        );
        
        // First create an entry
        String entryId = formHandler.createEntry(formName, fields);
        
        Map<String, Object> updates = Map.of(
            "Status", "In Progress",
            "Priority", "High"
        );
        
        formHandler.updateEntry(formName, entryId, updates);
        
        // Verify update was successful by retrieving the entry
        Map<String, Object> updatedEntry = formHandler.getEntry(formName, entryId);
        assertThat(updatedEntry.get("Status")).isEqualTo("In Progress");
        assertThat(updatedEntry.get("Priority")).isEqualTo("High");
        assertThat(updatedEntry.get("Short_Description")).isEqualTo("Test incident"); // Original field should remain
    }

    @Test
    void shouldDeleteEntry() {
        // Red Phase: Will fail until we implement entry deletion
        
        String formName = "HPD:Help Desk";
        Map<String, Object> fields = Map.of(
            "Short_Description", "Test incident",
            "Priority", "Medium",
            "Status", "New"
        );
        
        // First create an entry
        String entryId = formHandler.createEntry(formName, fields);
        
        // Verify it exists
        assertThat(formHandler.getEntry(formName, entryId)).isNotNull();
        
        formHandler.deleteEntry(formName, entryId);
        
        // Verify deletion by attempting to retrieve the entry
        assertThatThrownBy(() -> formHandler.getEntry(formName, entryId))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Entry not found");
    }

    @Test
    void shouldHandleInvalidForm() {
        // Red Phase: Will fail until we implement form validation
        
        String invalidFormName = "NonExistent:Form";
        Map<String, Object> fields = Map.of("field1", "value1");
        
        assertThatThrownBy(() -> formHandler.createEntry(invalidFormName, fields))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Form not found");
    }

    @Test
    void shouldValidateRequiredFields() {
        // Red Phase: Will fail until we implement field validation
        
        String formName = "HPD:Help Desk";
        Map<String, Object> incompleteFields = Map.of(
            "Short_Description", "Test"
            // Missing required fields like Priority, Status
        );
        
        assertThatThrownBy(() -> formHandler.createEntry(formName, incompleteFields))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Required field");
    }
}