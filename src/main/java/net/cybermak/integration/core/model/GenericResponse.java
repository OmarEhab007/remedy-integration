package net.cybermak.integration.core.model;

import java.util.Map;

/**
 * Generic response model for all module operations
 * TDD: Minimal implementation to satisfy ModuleTest requirements
 */
public class GenericResponse {
    
    private String status;
    private Map<String, Object> data;
    private String message;
    private String timestamp;
    
    private GenericResponse() {}
    
    private GenericResponse(Builder builder) {
        this.status = builder.status;
        this.data = builder.data;
        this.message = builder.message;
        this.timestamp = builder.timestamp;
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    public String getStatus() {
        return status;
    }
    
    public Map<String, Object> getData() {
        return data;
    }
    
    public String getMessage() {
        return message;
    }
    
    public String getTimestamp() {
        return timestamp;
    }
    
    public static class Builder {
        private String status;
        private Map<String, Object> data;
        private String message;
        private String timestamp;
        
        public Builder status(String status) {
            this.status = status;
            return this;
        }
        
        public Builder data(Map<String, Object> data) {
            this.data = data;
            return this;
        }
        
        public Builder message(String message) {
            this.message = message;
            return this;
        }
        
        public Builder timestamp(String timestamp) {
            this.timestamp = timestamp;
            return this;
        }
        
        public GenericResponse build() {
            return new GenericResponse(this);
        }
    }
}