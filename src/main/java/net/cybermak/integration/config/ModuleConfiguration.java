package net.cybermak.integration.config;

import net.cybermak.integration.api.bridge.IncidentModuleBridge;
import net.cybermak.integration.core.ModuleRegistry;
import net.cybermak.integration.modules.incident.IncidentModule;
import net.cybermak.integration.remedy.form.FormHandler;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for automatically registering modules
 * TDD: Configuration to make integration tests pass
 */
@Configuration
public class ModuleConfiguration {

    @Bean
    public CommandLineRunner registerModules(
            ModuleRegistry moduleRegistry, 
            FormHandler formHandler,
            IncidentModuleBridge incidentModuleBridge) {
        return args -> {
            // Register the legacy-compatible incident bridge module
            moduleRegistry.registerModule(incidentModuleBridge);
            
            // Optionally register the original generic incident module for comparison
            // IncidentModule incidentModule = new IncidentModule(formHandler);
            // moduleRegistry.registerModule("incident-generic", incidentModule);
        };
    }
}