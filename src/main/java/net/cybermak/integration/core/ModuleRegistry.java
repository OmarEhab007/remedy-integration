package net.cybermak.integration.core;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Module registry for managing and discovering available modules
 * TDD: Minimal implementation to satisfy ModuleRegistryTest requirements
 */
@Component
public class ModuleRegistry {
    
    private final Map<String, Module> modules = new ConcurrentHashMap<>();
    
    /**
     * Registers a module in the registry
     * @param module the module to register
     * @throws IllegalStateException if a module with the same type is already registered
     */
    public void registerModule(Module module) {
        String moduleType = module.getModuleType();
        
        if (modules.containsKey(moduleType)) {
            throw new IllegalStateException("Module with type '" + moduleType + "' is already registered");
        }
        
        modules.put(moduleType, module);
    }
    
    /**
     * Retrieves a module by its type
     * @param moduleType the type of module to retrieve
     * @return optional containing the module if found, empty otherwise
     */
    public Optional<Module> getModule(String moduleType) {
        return Optional.ofNullable(modules.get(moduleType));
    }
    
    /**
     * Unregisters a module from the registry
     * @param moduleType the type of module to unregister
     */
    public void unregisterModule(String moduleType) {
        modules.remove(moduleType);
    }
    
    /**
     * Returns all registered modules
     * @return list of all registered modules
     */
    public List<Module> getAllModules() {
        return new ArrayList<>(modules.values());
    }
    
    /**
     * Returns all registered module types
     * @return list of all registered module type identifiers
     */
    public List<String> getAllModuleTypes() {
        return new ArrayList<>(modules.keySet());
    }
    
    /**
     * Checks if a module type is registered
     * @param moduleType the module type to check
     * @return true if registered, false otherwise
     */
    public boolean isModuleRegistered(String moduleType) {
        return modules.containsKey(moduleType);
    }
    
    /**
     * Returns the number of registered modules
     * @return count of registered modules
     */
    public int getModuleCount() {
        return modules.size();
    }
}