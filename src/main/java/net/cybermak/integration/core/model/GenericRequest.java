package net.cybermak.integration.core.model;

import java.util.Map;

/**
 * Generic request model for all module operations
 * TDD: Minimal implementation to satisfy ModuleTest requirements
 */
public class GenericRequest {
    
    private String moduleType;
    private String operation;
    private Map<String, Object> data;
    private Map<String, String> metadata;
    
    private GenericRequest() {}
    
    private GenericRequest(Builder builder) {
        this.moduleType = builder.moduleType;
        this.operation = builder.operation;
        this.data = builder.data;
        this.metadata = builder.metadata;
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    public String getModuleType() {
        return moduleType;
    }
    
    public String getOperation() {
        return operation;
    }
    
    public Map<String, Object> getData() {
        return data;
    }
    
    public Map<String, String> getMetadata() {
        return metadata;
    }
    
    public static class Builder {
        private String moduleType;
        private String operation;
        private Map<String, Object> data;
        private Map<String, String> metadata;
        
        public Builder moduleType(String moduleType) {
            this.moduleType = moduleType;
            return this;
        }
        
        public Builder operation(String operation) {
            this.operation = operation;
            return this;
        }
        
        public Builder data(Map<String, Object> data) {
            this.data = data;
            return this;
        }
        
        public Builder metadata(Map<String, String> metadata) {
            this.metadata = metadata;
            return this;
        }
        
        public GenericRequest build() {
            return new GenericRequest(this);
        }
    }
}