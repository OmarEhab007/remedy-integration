package net.cybermak.integration.core;

import net.cybermak.integration.core.model.GenericRequest;
import net.cybermak.integration.core.model.GenericResponse;
import net.cybermak.integration.core.model.ValidationResult;

import java.util.Map;

/**
 * Core Module interface that all integration modules must implement
 * TDD: Minimal interface to satisfy ModuleTest requirements
 */
public interface Module {
    
    /**
     * Returns the type/name of this module
     * @return module type identifier (e.g., "incident", "workorder", "change")
     */
    String getModuleType();
    
    /**
     * Validates an incoming request
     * @param request the request to validate
     * @return validation result with success/failure and error messages
     */
    ValidationResult validate(GenericRequest request);
    
    /**
     * Processes a valid request and returns a response
     * @param request the request to process
     * @return response containing result data
     * @throws IllegalArgumentException if request is invalid
     */
    GenericResponse process(GenericRequest request);
    
    /**
     * Returns field mappings for BMC Remedy integration
     * @return map of logical field names to BMC Remedy field IDs
     */
    Map<String, String> getFieldMappings();
}