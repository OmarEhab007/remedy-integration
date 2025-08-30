package net.cybermak.integration.core;

import net.cybermak.integration.core.model.GenericRequest;
import net.cybermak.integration.core.model.GenericResponse;
import net.cybermak.integration.core.model.ValidationResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * TDD Test: ModuleRegistry tests
 * Following Red-Green-Refactor cycle
 */
class ModuleRegistryTest {

    private ModuleRegistry moduleRegistry;
    private TestModule testModule;
    private TestModule anotherTestModule;

    @BeforeEach
    void setUp() {
        moduleRegistry = new ModuleRegistry();
        testModule = new TestModule("test");
        anotherTestModule = new TestModule("another-test");
    }

    @Test
    void shouldRegisterModule() {
        // Red Phase: Will fail until we implement ModuleRegistry
        
        moduleRegistry.registerModule(testModule);
        
        Optional<Module> registeredModule = moduleRegistry.getModule("test");
        assertThat(registeredModule).isPresent();
        assertThat(registeredModule.get()).isEqualTo(testModule);
    }

    @Test
    void shouldRegisterMultipleModules() {
        // Red Phase: Will fail until we implement multiple module support
        
        moduleRegistry.registerModule(testModule);
        moduleRegistry.registerModule(anotherTestModule);
        
        assertThat(moduleRegistry.getModule("test")).isPresent();
        assertThat(moduleRegistry.getModule("another-test")).isPresent();
        assertThat(moduleRegistry.getAllModules()).hasSize(2);
    }

    @Test
    void shouldReturnEmptyForUnknownModule() {
        // Red Phase: Will fail until we implement getModule
        
        Optional<Module> unknownModule = moduleRegistry.getModule("unknown");
        
        assertThat(unknownModule).isEmpty();
    }

    @Test
    void shouldThrowExceptionForDuplicateModuleType() {
        // Red Phase: Will fail until we implement duplicate checking
        TestModule duplicateModule = new TestModule("test");
        
        moduleRegistry.registerModule(testModule);
        
        assertThatThrownBy(() -> moduleRegistry.registerModule(duplicateModule))
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("Module with type 'test' is already registered");
    }

    @Test
    void shouldReturnAllRegisteredModuleTypes() {
        // Red Phase: Will fail until we implement getAllModuleTypes
        
        moduleRegistry.registerModule(testModule);
        moduleRegistry.registerModule(anotherTestModule);
        
        List<String> moduleTypes = moduleRegistry.getAllModuleTypes();
        
        assertThat(moduleTypes).containsExactlyInAnyOrder("test", "another-test");
    }

    @Test
    void shouldReturnEmptyListWhenNoModulesRegistered() {
        // Red Phase: Will fail until we implement getAllModules
        
        List<Module> modules = moduleRegistry.getAllModules();
        List<String> moduleTypes = moduleRegistry.getAllModuleTypes();
        
        assertThat(modules).isEmpty();
        assertThat(moduleTypes).isEmpty();
    }

    @Test
    void shouldUnregisterModule() {
        // Red Phase: Will fail until we implement unregisterModule
        
        moduleRegistry.registerModule(testModule);
        assertThat(moduleRegistry.getModule("test")).isPresent();
        
        moduleRegistry.unregisterModule("test");
        
        assertThat(moduleRegistry.getModule("test")).isEmpty();
    }

    @Test
    void shouldHandleUnregisteringNonExistentModule() {
        // Should not throw exception when unregistering non-existent module
        
        moduleRegistry.unregisterModule("non-existent");
        
        // Should complete without exception
        assertThat(moduleRegistry.getAllModules()).isEmpty();
    }

    // Test implementation of Module interface for testing
    private static class TestModule implements Module {
        private final String moduleType;
        
        public TestModule(String moduleType) {
            this.moduleType = moduleType;
        }
        
        @Override
        public String getModuleType() {
            return moduleType;
        }

        @Override
        public ValidationResult validate(GenericRequest request) {
            return ValidationResult.valid();
        }

        @Override
        public GenericResponse process(GenericRequest request) {
            return GenericResponse.builder()
                .status("SUCCESS")
                .data(Map.of("id", moduleType + "-123"))
                .build();
        }

        @Override
        public Map<String, String> getFieldMappings() {
            return Map.of("field1", "remedy_field_1");
        }
    }
}