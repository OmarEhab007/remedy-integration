package net.cybermak.integration.api.controller;

import net.cybermak.integration.api.service.modern.ModuleService;
import net.cybermak.integration.core.model.GenericResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Generic REST API controller for BMC Remedy integrations
 * TDD: Implementation to satisfy GenericIntegrationControllerTest requirements
 */
@RestController
@RequestMapping("/api/v1/integration")
@CrossOrigin(origins = "*", maxAge = 3600)
public class GenericIntegrationController {

    private final ModuleService moduleService;

    public GenericIntegrationController(ModuleService moduleService) {
        this.moduleService = moduleService;
    }

    /**
     * Get all available modules
     */
    @GetMapping("/modules")
    public ResponseEntity<ApiResponse> getAvailableModules() {
        try {
            List<String> moduleTypes = moduleService.getAvailableModules();
            
            ApiResponse response = ApiResponse.builder()
                .status("SUCCESS")
                .data(Map.of("modules", moduleTypes))
                .message("Available modules retrieved successfully")
                .timestamp(LocalDateTime.now().toString())
                .build();
                
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                "Internal server error: " + e.getMessage());
        }
    }

    /**
     * Create a new entry in the specified module
     */
    @PostMapping("/{moduleType}")
    public ResponseEntity<ApiResponse> createEntry(
            @PathVariable String moduleType,
            @RequestBody Map<String, Object> data) {
        
        try {
            if (!moduleService.moduleExists(moduleType)) {
                return createErrorResponse(HttpStatus.NOT_FOUND, 
                    "Module not found: " + moduleType);
            }

            GenericResponse moduleResponse = moduleService.createEntry(moduleType, data);
            
            ApiResponse response = ApiResponse.builder()
                .status(moduleResponse.getStatus())
                .data(moduleResponse.getData())
                .message(moduleResponse.getMessage())
                .timestamp(LocalDateTime.now().toString())
                .build();

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (IllegalArgumentException e) {
            return createErrorResponse(HttpStatus.BAD_REQUEST, 
                "Invalid request: " + e.getMessage());
        } catch (Exception e) {
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, 
                "Internal server error: " + e.getMessage());
        }
    }

    /**
     * Get an entry by ID from the specified module
     */
    @GetMapping("/{moduleType}/{entryId}")
    public ResponseEntity<ApiResponse> getEntry(
            @PathVariable String moduleType,
            @PathVariable String entryId) {
        
        try {
            if (!moduleService.moduleExists(moduleType)) {
                return createErrorResponse(HttpStatus.NOT_FOUND, 
                    "Module not found: " + moduleType);
            }

            GenericResponse moduleResponse = moduleService.getEntry(moduleType, entryId);
            
            ApiResponse response = ApiResponse.builder()
                .status(moduleResponse.getStatus())
                .data(moduleResponse.getData())
                .message(moduleResponse.getMessage())
                .timestamp(LocalDateTime.now().toString())
                .build();

            return ResponseEntity.ok(response);
            
        } catch (IllegalArgumentException e) {
            return createErrorResponse(HttpStatus.BAD_REQUEST, 
                "Invalid request: " + e.getMessage());
        } catch (Exception e) {
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, 
                "Internal server error: " + e.getMessage());
        }
    }

    /**
     * Update an entry by ID in the specified module
     */
    @PutMapping("/{moduleType}/{entryId}")
    public ResponseEntity<ApiResponse> updateEntry(
            @PathVariable String moduleType,
            @PathVariable String entryId,
            @RequestBody Map<String, Object> data) {
        
        try {
            if (!moduleService.moduleExists(moduleType)) {
                return createErrorResponse(HttpStatus.NOT_FOUND, 
                    "Module not found: " + moduleType);
            }

            GenericResponse moduleResponse = moduleService.updateEntry(moduleType, entryId, data);
            
            ApiResponse response = ApiResponse.builder()
                .status(moduleResponse.getStatus())
                .data(moduleResponse.getData())
                .message(moduleResponse.getMessage())
                .timestamp(LocalDateTime.now().toString())
                .build();

            return ResponseEntity.ok(response);
            
        } catch (IllegalArgumentException e) {
            return createErrorResponse(HttpStatus.BAD_REQUEST, 
                "Invalid request: " + e.getMessage());
        } catch (Exception e) {
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, 
                "Internal server error: " + e.getMessage());
        }
    }

    /**
     * Batch create entries in the specified module
     */
    @PostMapping("/{moduleType}/batch")
    public ResponseEntity<ApiResponse> batchCreateEntries(
            @PathVariable String moduleType,
            @RequestBody List<Map<String, Object>> batchData) {
        
        try {
            if (!moduleService.moduleExists(moduleType)) {
                return createErrorResponse(HttpStatus.NOT_FOUND, 
                    "Module not found: " + moduleType);
            }

            List<Map<String, Object>> results = new ArrayList<>();

            for (Map<String, Object> data : batchData) {
                try {
                    GenericResponse moduleResponse = moduleService.createEntry(moduleType, data);
                    results.add(moduleResponse.getData());
                    
                } catch (Exception e) {
                    results.add(Map.of(
                        "error", e.getMessage(),
                        "data", data
                    ));
                }
            }

            ApiResponse response = ApiResponse.builder()
                .status("SUCCESS")
                .data(Map.of("results", results))
                .message("Batch operation completed")
                .timestamp(LocalDateTime.now().toString())
                .build();

            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, 
                "Internal server error: " + e.getMessage());
        }
    }

    /**
     * Search entries in the specified module
     */
    @GetMapping("/{moduleType}/search")
    public ResponseEntity<ApiResponse> searchEntries(
            @PathVariable String moduleType,
            @RequestParam Map<String, String> searchParams) {
        
        try {
            if (!moduleService.moduleExists(moduleType)) {
                return createErrorResponse(HttpStatus.NOT_FOUND, 
                    "Module not found: " + moduleType);
            }

            GenericResponse moduleResponse = moduleService.searchEntries(moduleType, searchParams);
            
            ApiResponse response = ApiResponse.builder()
                .status(moduleResponse.getStatus())
                .data(moduleResponse.getData())
                .message(moduleResponse.getMessage())
                .timestamp(LocalDateTime.now().toString())
                .build();

            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, 
                "Internal server error: " + e.getMessage());
        }
    }

    /**
     * Create error response
     */
    private ResponseEntity<ApiResponse> createErrorResponse(HttpStatus status, String message) {
        ApiResponse response = ApiResponse.builder()
            .status("ERROR")
            .message(message)
            .timestamp(LocalDateTime.now().toString())
            .build();
            
        return ResponseEntity.status(status).body(response);
    }


    /**
     * API Response wrapper
     */
    public static class ApiResponse {
        private String status;
        private Map<String, Object> data;
        private String message;
        private String timestamp;

        private ApiResponse() {}

        private ApiResponse(Builder builder) {
            this.status = builder.status;
            this.data = builder.data;
            this.message = builder.message;
            this.timestamp = builder.timestamp;
        }

        public static Builder builder() {
            return new Builder();
        }

        public String getStatus() { return status; }
        public Map<String, Object> getData() { return data; }
        public String getMessage() { return message; }
        public String getTimestamp() { return timestamp; }

        public static class Builder {
            private String status;
            private Map<String, Object> data;
            private String message;
            private String timestamp;

            public Builder status(String status) { this.status = status; return this; }
            public Builder data(Map<String, Object> data) { this.data = data; return this; }
            public Builder message(String message) { this.message = message; return this; }
            public Builder timestamp(String timestamp) { this.timestamp = timestamp; return this; }

            public ApiResponse build() { return new ApiResponse(this); }
        }
    }
}