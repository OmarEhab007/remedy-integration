package net.cybermak.integration.api.service.legacy;

import net.cybermak.integration.api.model.requests.IncidentDetails;
import net.cybermak.integration.api.service.CreateIncidentService;
import org.springframework.stereotype.Service;

/**
 * Legacy service wrapper for backward compatibility
 * Bridges old CreateIncidentService with new modular architecture
 */
@Service
public class LegacyIncidentService {
    
    private final CreateIncidentService createIncidentService;
    
    public LegacyIncidentService(CreateIncidentService createIncidentService) {
        this.createIncidentService = createIncidentService;
    }
    
    /**
     * Process legacy incident creation request
     * Maintains exact same behavior as original implementation
     */
    public String processLegacyIncident(IncidentDetails incidentDetails) {
        return createIncidentService.createIncident(incidentDetails);
    }
}