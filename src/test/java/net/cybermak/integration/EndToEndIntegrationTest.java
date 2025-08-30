package net.cybermak.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.cybermak.integration.core.ModuleRegistry;
import net.cybermak.integration.modules.incident.IncidentModule;
import net.cybermak.integration.remedy.form.FormHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * End-to-End Integration Test for the complete application
 * TDD: Final integration test to verify all components work together
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebMvc
class EndToEndIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ModuleRegistry moduleRegistry;

    @Autowired
    private FormHandler formHandler;

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void shouldRegisterIncidentModuleAutomatically() throws Exception {
        // TDD: Test that the incident module is automatically registered
        
        mockMvc.perform(get("/api/v1/integration/modules")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("SUCCESS")))
                .andExpect(jsonPath("$.data.modules", hasItem("incident")));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void shouldCreateIncidentEndToEnd() throws Exception {
        // TDD: Test complete incident creation flow
        
        Map<String, Object> incidentData = Map.of(
            "summary", "End-to-end test incident",
            "description", "This is a test incident created by integration test",
            "priority", "High",
            "submitter", "integration.test@example.com"
        );

        mockMvc.perform(post("/api/v1/integration/incident")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(incidentData)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status", is("SUCCESS")))
                .andExpect(jsonPath("$.data.incidentId", isA(String.class)))
                .andExpect(jsonPath("$.data.incidentId", startsWith("INC")))
                .andExpect(jsonPath("$.message", containsString("created successfully")));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void shouldValidateIncidentRequiredFields() throws Exception {
        // TDD: Test validation works end-to-end
        
        Map<String, Object> invalidIncident = Map.of(
            "summary", "Incomplete incident"
            // Missing required fields: description, priority, submitter
        );

        mockMvc.perform(post("/api/v1/integration/incident")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidIncident)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is("ERROR")))
                .andExpect(jsonPath("$.message", containsString("Invalid request")));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void shouldRetrieveIncidentAfterCreation() throws Exception {
        // TDD: Test full CRUD cycle
        
        // First create an incident
        Map<String, Object> incidentData = Map.of(
            "summary", "Retrievable incident",
            "description", "Test incident for retrieval",
            "priority", "Medium",
            "submitter", "test@example.com"
        );

        String createResponse = mockMvc.perform(post("/api/v1/integration/incident")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(incidentData)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // Extract incident ID from response (simplified - in real test would parse JSON properly)
        Map<String, Object> responseMap = objectMapper.readValue(createResponse, Map.class);
        Map<String, Object> data = (Map<String, Object>) responseMap.get("data");
        String incidentId = (String) data.get("incidentId");

        // Then retrieve the incident
        mockMvc.perform(get("/api/v1/integration/incident/" + incidentId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("SUCCESS")))
                .andExpect(jsonPath("$.data.incident.incidentId", is(incidentId)))
                .andExpect(jsonPath("$.data.incident.summary", is("Retrievable incident")));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void shouldHandleModuleNotFound() throws Exception {
        // TDD: Test error handling for unknown modules
        
        Map<String, Object> requestData = Map.of(
            "field1", "value1",
            "field2", "value2"
        );

        mockMvc.perform(post("/api/v1/integration/nonexistent")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestData)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is("ERROR")))
                .andExpect(jsonPath("$.message", containsString("Module not found: nonexistent")));
    }

    @Test
    void shouldRequireAuthenticationForSecuredEndpoints() throws Exception {
        // TDD: Test security is working
        
        mockMvc.perform(get("/api/v1/integration/modules"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void shouldProvideHealthEndpoint() throws Exception {
        // TDD: Test that application health endpoint works
        
        mockMvc.perform(get("/actuator/health"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldAllowPublicAccessToSwaggerDocs() throws Exception {
        // TDD: Test that API documentation is publicly accessible
        
        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk());
    }
}