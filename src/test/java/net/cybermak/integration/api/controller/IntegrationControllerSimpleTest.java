package net.cybermak.integration.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.cybermak.integration.core.ModuleRegistry;
import net.cybermak.integration.modules.incident.IncidentModule;
import net.cybermak.integration.remedy.form.FormHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
 * Simplified TDD Test for Generic Integration Controller
 */
@SpringBootTest(classes = {GenericIntegrationController.class})
@AutoConfigureWebMvc
class IntegrationControllerSimpleTest {

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
        // TDD Red-Green-Refactor: Test modules endpoint
        
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
        // TDD Red-Green-Refactor: Test incident creation
        
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
                .andExpect(jsonPath("$.data.incidentId", is("INC000000000123")));
    }

    @Test
    void shouldReturnNotFoundForUnknownModule() throws Exception {
        // TDD Red-Green-Refactor: Test error handling
        
        when(moduleRegistry.getModule("unknown")).thenReturn(Optional.empty());

        Map<String, Object> requestData = Map.of("field1", "value1");

        mockMvc.perform(post("/api/v1/integration/unknown")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestData)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is("ERROR")))
                .andExpect(jsonPath("$.message", containsString("Module not found")));
    }

    @Test
    void shouldHandleValidationErrors() throws Exception {
        // TDD Red-Green-Refactor: Test validation
        
        IncidentModule mockIncidentModule = new IncidentModule(formHandler);
        when(moduleRegistry.getModule("incident")).thenReturn(Optional.of(mockIncidentModule));

        // Missing required fields
        Map<String, Object> invalidData = Map.of("summary", "Test incident");

        mockMvc.perform(post("/api/v1/integration/incident")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidData)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is("ERROR")))
                .andExpect(jsonPath("$.message", containsString("Invalid request")));
    }
}