package net.cybermak.integration.core;

import net.cybermak.integration.core.model.GenericRequest;
import net.cybermak.integration.core.model.GenericResponse;
import net.cybermak.integration.core.model.ValidationResult;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * TDD Test: Module interface contract tests
 * Following Red-Green-Refactor cycle
 */
class ModuleTest {

    @Test
    void moduleShouldHaveRequiredMethods() {
        // Red Phase: This test will fail as Module interface doesn't exist yet
        // This test defines the contract that every module must implement
        
        // A module should be able to:
        // 1. Identify itself by name/type
        // 2. Validate incoming requests
        // 3. Process valid requests
        // 4. Support field mappings for BMC Remedy integration
        
        // We'll create a test implementation to verify the interface
        TestModule testModule = new TestModule();
        
        assertThat(testModule.getModuleType()).isNotNull();
        assertThat(testModule.getModuleType()).isNotEmpty();
    }

    @Test
    void moduleShouldValidateRequest() {
        // Red Phase: Will fail until we implement validation logic
        TestModule testModule = new TestModule();
        GenericRequest validRequest = createValidRequest();
        GenericRequest invalidRequest = createInvalidRequest();
        
        ValidationResult validResult = testModule.validate(validRequest);
        ValidationResult invalidResult = testModule.validate(invalidRequest);
        
        assertThat(validResult.isValid()).isTrue();
        assertThat(invalidResult.isValid()).isFalse();
        assertThat(invalidResult.getErrors()).isNotEmpty();
    }

    @Test
    void moduleShouldProcessValidRequest() {
        // Red Phase: Will fail until we implement processing logic
        TestModule testModule = new TestModule();
        GenericRequest request = createValidRequest();
        
        GenericResponse response = testModule.process(request);
        
        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isNotNull();
        assertThat(response.getData()).isNotNull();
    }

    @Test
    void moduleShouldThrowExceptionForInvalidRequest() {
        // Red Phase: Will fail until we implement validation
        TestModule testModule = new TestModule();
        GenericRequest invalidRequest = createInvalidRequest();
        
        assertThatThrownBy(() -> testModule.process(invalidRequest))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Invalid request");
    }

    @Test
    void moduleShouldProvideFieldMappings() {
        // Red Phase: Will fail until we implement field mapping
        TestModule testModule = new TestModule();
        
        Map<String, String> fieldMappings = testModule.getFieldMappings();
        
        assertThat(fieldMappings).isNotNull();
        assertThat(fieldMappings).isNotEmpty();
    }

    // Helper methods to create test data
    private GenericRequest createValidRequest() {
        return GenericRequest.builder()
            .moduleType("test")
            .operation("create")
            .data(Map.of("field1", "value1", "field2", "value2"))
            .build();
    }

    private GenericRequest createInvalidRequest() {
        return GenericRequest.builder()
            .moduleType("test")
            .operation("create")
            .data(Map.of()) // Empty data should be invalid
            .build();
    }

    // Test implementation of Module interface
    private static class TestModule implements Module {
        
        @Override
        public String getModuleType() {
            return "test";
        }

        @Override
        public ValidationResult validate(GenericRequest request) {
            if (request.getData().isEmpty()) {
                return ValidationResult.invalid("Data cannot be empty");
            }
            return ValidationResult.valid();
        }

        @Override
        public GenericResponse process(GenericRequest request) {
            ValidationResult validationResult = validate(request);
            if (!validationResult.isValid()) {
                throw new IllegalArgumentException("Invalid request: " + validationResult.getErrors());
            }
            
            return GenericResponse.builder()
                .status("SUCCESS")
                .data(Map.of("id", "test-123", "created", true))
                .build();
        }

        @Override
        public Map<String, String> getFieldMappings() {
            return Map.of(
                "field1", "remedy_field_1",
                "field2", "remedy_field_2"
            );
        }
    }
}