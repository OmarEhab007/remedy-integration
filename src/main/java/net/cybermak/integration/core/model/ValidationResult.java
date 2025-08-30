package net.cybermak.integration.core.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Validation result model for request validation
 * TDD: Minimal implementation to satisfy ModuleTest requirements
 */
public class ValidationResult {
    
    private final boolean valid;
    private final List<String> errors;
    
    private ValidationResult(boolean valid, List<String> errors) {
        this.valid = valid;
        this.errors = errors != null ? new ArrayList<>(errors) : new ArrayList<>();
    }
    
    public static ValidationResult valid() {
        return new ValidationResult(true, null);
    }
    
    public static ValidationResult invalid(String error) {
        List<String> errors = new ArrayList<>();
        errors.add(error);
        return new ValidationResult(false, errors);
    }
    
    public static ValidationResult invalid(List<String> errors) {
        return new ValidationResult(false, errors);
    }
    
    public boolean isValid() {
        return valid;
    }
    
    public List<String> getErrors() {
        return new ArrayList<>(errors);
    }
    
    public String getErrorMessage() {
        if (errors.isEmpty()) {
            return null;
        }
        return String.join(", ", errors);
    }
}