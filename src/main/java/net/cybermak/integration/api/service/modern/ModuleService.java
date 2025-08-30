package net.cybermak.integration.api.service.modern;

import net.cybermak.integration.core.Module;
import net.cybermak.integration.core.ModuleRegistry;
import net.cybermak.integration.core.model.GenericRequest;
import net.cybermak.integration.core.model.GenericResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Modern service layer for generic module operations
 * Handles all BMC Remedy module interactions through the modular architecture
 */
@Service
public class ModuleService {
    
    private static final Logger logger = LoggerFactory.getLogger(ModuleService.class);
    private final ModuleRegistry moduleRegistry;
    
    public ModuleService(ModuleRegistry moduleRegistry) {
        this.moduleRegistry = moduleRegistry;
    }
    
    /**
     * Get all available module types
     */
    public List<String> getAvailableModules() {
        return moduleRegistry.getAllModuleTypes();
    }
    
    /**
     * Check if a module exists
     */
    public boolean moduleExists(String moduleType) {
        return moduleRegistry.getModule(moduleType).isPresent();
    }
    
    /**
     * Create entry in specified module
     */
    public GenericResponse createEntry(String moduleType, Map<String, Object> data) {
        logger.info("Creating entry in module: {} with data: {}", moduleType, data);
        
        Optional<Module> moduleOpt = moduleRegistry.getModule(moduleType);
        if (moduleOpt.isEmpty()) {
            throw new IllegalArgumentException("Module not found: " + moduleType);
        }
        
        GenericRequest request = GenericRequest.builder()
            .moduleType(moduleType)
            .operation("create")
            .data(data)
            .build();
            
        return moduleOpt.get().process(request);
    }
    
    /**
     * Get entry by ID from specified module
     */
    public GenericResponse getEntry(String moduleType, String entryId) {
        logger.info("Getting entry {} from module: {}", entryId, moduleType);
        
        Optional<Module> moduleOpt = moduleRegistry.getModule(moduleType);
        if (moduleOpt.isEmpty()) {
            throw new IllegalArgumentException("Module not found: " + moduleType);
        }
        
        GenericRequest request = GenericRequest.builder()
            .moduleType(moduleType)
            .operation("get")
            .data(Map.of(getIdFieldName(moduleType), entryId))
            .build();
            
        return moduleOpt.get().process(request);
    }
    
    /**
     * Update entry in specified module
     */
    public GenericResponse updateEntry(String moduleType, String entryId, Map<String, Object> data) {
        logger.info("Updating entry {} in module: {} with data: {}", entryId, moduleType, data);
        
        Optional<Module> moduleOpt = moduleRegistry.getModule(moduleType);
        if (moduleOpt.isEmpty()) {
            throw new IllegalArgumentException("Module not found: " + moduleType);
        }
        
        // Add entry ID to data
        data.put(getIdFieldName(moduleType), entryId);
        
        GenericRequest request = GenericRequest.builder()
            .moduleType(moduleType)
            .operation("update")
            .data(data)
            .build();
            
        return moduleOpt.get().process(request);
    }
    
    /**
     * Search entries in specified module
     */
    public GenericResponse searchEntries(String moduleType, Map<String, String> searchParams) {
        logger.info("Searching in module: {} with params: {}", moduleType, searchParams);
        
        Optional<Module> moduleOpt = moduleRegistry.getModule(moduleType);
        if (moduleOpt.isEmpty()) {
            throw new IllegalArgumentException("Module not found: " + moduleType);
        }
        
        GenericRequest request = GenericRequest.builder()
            .moduleType(moduleType)
            .operation("search")
            .data(Map.copyOf(searchParams))
            .build();
            
        return moduleOpt.get().process(request);
    }
    
    /**
     * Get the appropriate ID field name for different module types
     */
    private String getIdFieldName(String moduleType) {
        switch (moduleType.toLowerCase()) {
            case "incident":
                return "incidentId";
            case "workorder":
                return "workOrderId";
            case "change":
                return "changeId";
            default:
                return "id";
        }
    }
}