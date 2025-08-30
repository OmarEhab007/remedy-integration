package net.cybermak.integration.modules.incident;

import net.cybermak.integration.core.Module;
import net.cybermak.integration.core.model.GenericRequest;
import net.cybermak.integration.core.model.GenericResponse;
import net.cybermak.integration.core.model.ValidationResult;
import net.cybermak.integration.remedy.form.FormHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * TDD Test: Incident Module tests
 * Following Red-Green-Refactor cycle
 */
class IncidentModuleTest {

    @Mock
    private FormHandler formHandler;
    
    private IncidentModule incidentModule;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        incidentModule = new IncidentModule(formHandler);
    }

    @Test
    void shouldReturnCorrectModuleType() {
        // Red Phase: Will fail until we implement IncidentModule
        
        String moduleType = incidentModule.getModuleType();
        
        assertThat(moduleType).isEqualTo("incident");
    }

    @Test
    void shouldValidateValidIncidentRequest() {
        // Red Phase: Will fail until we implement validation logic
        
        GenericRequest validRequest = GenericRequest.builder()
            .moduleType("incident")
            .operation("create")
            .data(Map.of(
                "summary", "Test incident summary",
                "description", "Test incident description",
                "priority", "Medium",
                "impact", "Low",
                "urgency", "Medium",
                "submitter", "test.user@example.com"
            ))
            .build();
        
        ValidationResult result = incidentModule.validate(validRequest);
        
        assertThat(result.isValid()).isTrue();
        assertThat(result.getErrors()).isEmpty();
    }

    @Test
    void shouldValidateInvalidIncidentRequest() {
        // Red Phase: Will fail until we implement validation logic
        
        GenericRequest invalidRequest = GenericRequest.builder()
            .moduleType("incident")
            .operation("create")
            .data(Map.of(
                "summary", "Test incident"
                // Missing required fields: description, priority, etc.
            ))
            .build();
        
        ValidationResult result = incidentModule.validate(invalidRequest);
        
        assertThat(result.isValid()).isFalse();
        assertThat(result.getErrors()).isNotEmpty();
        assertThat(result.getErrors()).contains("Description is required");
        assertThat(result.getErrors()).contains("Priority is required");
    }

    @Test
    void shouldProcessCreateIncidentRequest() {
        // Red Phase: Will fail until we implement process logic
        
        when(formHandler.createEntry(eq("HPD:Help Desk"), any(Map.class)))
            .thenReturn("INC000000000456");
        
        GenericRequest createRequest = GenericRequest.builder()
            .moduleType("incident")
            .operation("create")
            .data(Map.of(
                "summary", "Test incident",
                "description", "Test description",
                "priority", "High",
                "impact", "Medium",
                "urgency", "High",
                "submitter", "test.user@example.com"
            ))
            .build();
        
        GenericResponse response = incidentModule.process(createRequest);
        
        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo("SUCCESS");
        assertThat(response.getData()).containsKey("incidentId");
        assertThat(response.getData().get("incidentId")).isEqualTo("INC000000000456");
        
        // Verify that the remedy form handler was called correctly
        verify(formHandler).createEntry(eq("HPD:Help Desk"), any(Map.class));
    }

    @Test
    void shouldProcessGetIncidentRequest() {
        // Red Phase: Will fail until we implement get operation
        
        when(formHandler.getEntry("HPD:Help Desk", "INC000000000456"))
            .thenReturn(Map.of(
                "Incident_Number", "INC000000000456",
                "Short_Description", "Test incident",
                "Detailed_Decription", "Test description",
                "Priority", "High",
                "Status", "New"
            ));
        
        GenericRequest getRequest = GenericRequest.builder()
            .moduleType("incident")
            .operation("get")
            .data(Map.of("incidentId", "INC000000000456"))
            .build();
        
        GenericResponse response = incidentModule.process(getRequest);
        
        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo("SUCCESS");
        assertThat(response.getData()).containsKey("incident");
        
        verify(formHandler).getEntry("HPD:Help Desk", "INC000000000456");
    }

    @Test
    void shouldThrowExceptionForInvalidRequest() {
        // Red Phase: Will fail until we implement validation
        
        GenericRequest invalidRequest = GenericRequest.builder()
            .moduleType("incident")
            .operation("create")
            .data(Map.of()) // Empty data
            .build();
        
        assertThatThrownBy(() -> incidentModule.process(invalidRequest))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Invalid request");
    }

    @Test
    void shouldProvideCorrectFieldMappings() {
        // Red Phase: Will fail until we implement field mappings
        
        Map<String, String> fieldMappings = incidentModule.getFieldMappings();
        
        assertThat(fieldMappings).isNotEmpty();
        assertThat(fieldMappings).containsEntry("summary", "Short_Description");
        assertThat(fieldMappings).containsEntry("description", "Detailed_Decription");
        assertThat(fieldMappings).containsEntry("priority", "Priority");
        assertThat(fieldMappings).containsEntry("status", "Status");
        assertThat(fieldMappings).containsEntry("submitter", "Submitter");
    }

    @Test
    void shouldSupportUpdateOperation() {
        // Red Phase: Will fail until we implement update operation
        
        GenericRequest updateRequest = GenericRequest.builder()
            .moduleType("incident")
            .operation("update")
            .data(Map.of(
                "incidentId", "INC000000000456",
                "status", "In Progress",
                "priority", "High"
            ))
            .build();
        
        GenericResponse response = incidentModule.process(updateRequest);
        
        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo("SUCCESS");
        
        verify(formHandler).updateEntry(eq("HPD:Help Desk"), eq("INC000000000456"), any(Map.class));
    }

    @Test
    void shouldMapFieldsCorrectlyToRemedy() {
        // Test that field mapping works correctly when creating incidents
        
        when(formHandler.createEntry(eq("HPD:Help Desk"), any(Map.class)))
            .thenReturn("INC000000000789");
        
        GenericRequest request = GenericRequest.builder()
            .moduleType("incident")
            .operation("create")
            .data(Map.of(
                "summary", "Brief summary",
                "description", "Detailed description",
                "priority", "Critical",
                "submitter", "user@company.com"
            ))
            .build();
        
        incidentModule.process(request);
        
        // Verify the mapped fields are passed to Remedy
        verify(formHandler).createEntry(eq("HPD:Help Desk"), any(Map.class));
    }
}