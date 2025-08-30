package net.cybermak.integration.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.cybermak.integration.core.ModuleRegistry;
import net.cybermak.integration.core.model.GenericRequest;
import net.cybermak.integration.core.model.GenericResponse;
import net.cybermak.integration.modules.incident.IncidentModule;
import net.cybermak.integration.remedy.form.FormHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * TDD Test: Generic Integration Controller tests
 * Following Red-Green-Refactor cycle for REST API
 */
@WebMvcTest(controllers = GenericIntegrationController.class, 
            useDefaultFilters = false)
@Import({TestConfig.class})
class GenericIntegrationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ModuleRegistry moduleRegistry;

    @MockBean
    private FormHandler formHandler;

    @Test
    void shouldReturnAvailableModules() throws Exception {
        // Red Phase: Will fail until we implement the controller and endpoint
        
        when(moduleRegistry.getAllModuleTypes())
            .thenReturn(List.of("incident", "workorder", "change"));

        mockMvc.perform(get("/api/v1/integration/modules")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("SUCCESS")))
                .andExpect(jsonPath("$.data.modules", hasSize(3)))
                .andExpect(jsonPath("$.data.modules", containsInAnyOrder("incident", "workorder", "change")));
    }

    @Test
    void shouldCreateIncidentSuccessfully() throws Exception {
        // Red Phase: Will fail until we implement the create endpoint
        
        IncidentModule mockIncidentModule = new IncidentModule(formHandler);
        when(moduleRegistry.getModule("incident")).thenReturn(Optional.of(mockIncidentModule));
        when(formHandler.createEntry(any(), any())).thenReturn("INC000000000123");

        Map<String, Object> requestData = Map.of(
            "summary", "Test incident",
            "description", "Test description",
            "priority", "High",
            "submitter", "test.user@example.com"
        );

        mockMvc.perform(post("/api/v1/integration/incident")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestData)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status", is("SUCCESS")))
                .andExpect(jsonPath("$.data.incidentId", is("INC000000000123")))
                .andExpect(jsonPath("$.message", containsString("created successfully")));
    }

    @Test
    void shouldGetIncidentById() throws Exception {
        // Red Phase: Will fail until we implement the get endpoint
        
        IncidentModule mockIncidentModule = new IncidentModule(formHandler);
        when(moduleRegistry.getModule("incident")).thenReturn(Optional.of(mockIncidentModule));
        when(formHandler.getEntry(any(), any())).thenReturn(Map.of(
            "Incident_Number", "INC000000000123",
            "Short_Description", "Test incident",
            "Priority", "High",
            "Status", "New"
        ));

        mockMvc.perform(get("/api/v1/integration/incident/INC000000000123")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("SUCCESS")))
                .andExpect(jsonPath("$.data.incident.incidentId", is("INC000000000123")))
                .andExpect(jsonPath("$.data.incident.summary", is("Test incident")));
    }

    @Test
    void shouldUpdateIncidentSuccessfully() throws Exception {
        // Red Phase: Will fail until we implement the update endpoint
        
        IncidentModule mockIncidentModule = new IncidentModule(formHandler);
        when(moduleRegistry.getModule("incident")).thenReturn(Optional.of(mockIncidentModule));

        Map<String, Object> updateData = Map.of(
            "status", "In Progress",
            "priority", "Critical"
        );

        mockMvc.perform(put("/api/v1/integration/incident/INC000000000123")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateData)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("SUCCESS")))
                .andExpect(jsonPath("$.data.incidentId", is("INC000000000123")))
                .andExpect(jsonPath("$.message", containsString("updated successfully")));
    }

    @Test
    void shouldReturnBadRequestForInvalidData() throws Exception {
        // Red Phase: Will fail until we implement validation
        
        IncidentModule mockIncidentModule = new IncidentModule(formHandler);
        when(moduleRegistry.getModule("incident")).thenReturn(Optional.of(mockIncidentModule));

        Map<String, Object> invalidData = Map.of(
            "summary", "Test incident"
            // Missing required fields
        );

        mockMvc.perform(post("/api/v1/integration/incident")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidData)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is("ERROR")))
                .andExpect(jsonPath("$.message", containsString("Invalid request")));
    }

    @Test
    void shouldReturnNotFoundForUnknownModule() throws Exception {
        // Red Phase: Will fail until we implement error handling
        
        when(moduleRegistry.getModule("unknown")).thenReturn(Optional.empty());

        Map<String, Object> requestData = Map.of(
            "field1", "value1"
        );

        mockMvc.perform(post("/api/v1/integration/unknown")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestData)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is("ERROR")))
                .andExpect(jsonPath("$.message", containsString("Module not found")));
    }

    @Test
    void shouldHandleBatchOperations() throws Exception {
        // Red Phase: Will fail until we implement batch endpoint
        
        IncidentModule mockIncidentModule = new IncidentModule(formHandler);
        when(moduleRegistry.getModule("incident")).thenReturn(Optional.of(mockIncidentModule));
        when(formHandler.createEntry(any(), any()))
            .thenReturn("INC000000000123", "INC000000000124");

        List<Map<String, Object>> batchData = List.of(
            Map.of("summary", "Incident 1", "description", "Desc 1", "priority", "High", "submitter", "user1@test.com"),
            Map.of("summary", "Incident 2", "description", "Desc 2", "priority", "Medium", "submitter", "user2@test.com")
        );

        mockMvc.perform(post("/api/v1/integration/incident/batch")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(batchData)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("SUCCESS")))
                .andExpect(jsonPath("$.data.results", hasSize(2)))
                .andExpect(jsonPath("$.data.results[0].incidentId", is("INC000000000123")))
                .andExpect(jsonPath("$.data.results[1].incidentId", is("INC000000000124")));
    }

    @Test
    void shouldHandleInternalServerError() throws Exception {
        // Red Phase: Will fail until we implement error handling
        
        when(moduleRegistry.getAllModuleTypes()).thenThrow(new RuntimeException("Database error"));

        mockMvc.perform(get("/api/v1/integration/modules")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status", is("ERROR")))
                .andExpect(jsonPath("$.message", containsString("Internal server error")));
    }

    @Test
    void shouldReturnApiDocumentation() throws Exception {
        // Red Phase: Will fail until we implement OpenAPI integration
        
        mockMvc.perform(get("/swagger-ui/index.html"))
                .andExpect(status().isOk());
        
        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void shouldSupportSearchOperations() throws Exception {
        // Red Phase: Will fail until we implement search endpoint
        
        IncidentModule mockIncidentModule = new IncidentModule(formHandler);
        when(moduleRegistry.getModule("incident")).thenReturn(Optional.of(mockIncidentModule));

        mockMvc.perform(get("/api/v1/integration/incident/search")
                .param("status", "New")
                .param("priority", "High")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("SUCCESS")))
                .andExpect(jsonPath("$.data.incidents", isA(List.class)));
    }
}