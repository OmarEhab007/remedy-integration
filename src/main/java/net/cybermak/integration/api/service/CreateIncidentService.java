package net.cybermak.integration.api.service;

import com.bmc.arsys.api.ARException;
import com.bmc.arsys.api.ARServerUser;
import com.bmc.arsys.api.Entry;
import com.bmc.arsys.api.Value;
import net.cybermak.integration.api.model.requests.IncidentDetails;
import net.cybermak.integration.config.ReadProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * CreateIncidentService handles BMC Remedy integration for incident creation
 * Manages authentication, connection, and incident creation in BMC Remedy system
 * 
 * Key Features:
 * - BMC AR System API integration using arAPI 91.9
 * - Automated login to Remedy server
 * - Creates incidents in INTG-ITSM-SOLARWIND-INCIDENT-CREATE-STAGING-FORM
 * - Field mapping from SolarWinds alerts to Remedy incident fields
 * 
 * BMC Remedy Field Mappings:
 * - Field ID 536870943: sourceOfCreation
 * - Field ID 536870913: alertName  
 * - Field ID 536870914: alertDescription
 * - Field ID 536870915: severity
 * - Field ID 536870916: alertLimitCategory
 * - Field ID 536870917: add1
 * - Field ID 536870918: add2
 * 
 * Recreated from original compiled WAR file to maintain compatibility
 * 
 * @author Reverse Engineered from WAR file
 * @version 1.0
 */
@Service
public class CreateIncidentService {

    static final Logger logger = LoggerFactory.getLogger(CreateIncidentService.class);
    
    ReadProperties readProperties = ReadProperties.getInstance();
    String servername = null;
    String arServerUserName = null;
    String arServerPassword = null;
    String generateTokenURL = null;
    String port = null;

    /**
     * Creates incident in BMC Remedy from SolarWinds incident details
     * 
     * @param incidentDetails Incident data from SolarWinds
     * @return Generated incident ID or empty string if failed
     */
    public String createIncident(IncidentDetails incidentDetails) {
        logger.info(incidentDetails.toString());
        
        loadProperties();
        ARServerUser arServerUser = loginToRemedy(servername, arServerUserName, arServerPassword, Integer.parseInt(port));
        String requestID = createInBoundEntry(arServerUser, incidentDetails);
        return requestID;
    }

    /**
     * Loads configuration properties from ReadProperties singleton
     */
    public void loadProperties() {
        ReadProperties readProperties = ReadProperties.getInstance();
        logger.info("*********************Start Getting Properties****************************");
        
        try {
            servername = readProperties.serverName;
            logger.info("ServerName :" + servername);
            arServerUserName = readProperties.userName;
            arServerPassword = readProperties.password;
            port = readProperties.port;
            logger.info("Get properties successfully");
            
        } catch (Exception ex) {
            logger.error("ERROR READING CONF PROPERTIES" + ex);
        }
    }

    /**
     * Authenticates and creates connection to BMC Remedy server
     * 
     * @param servername Remedy server hostname
     * @param userName Username for authentication
     * @param userPassword Password for authentication
     * @param port Server port number
     * @return Authenticated ARServerUser instance
     */
    public static ARServerUser loginToRemedy(String servername, String userName, String userPassword, int port) {
        // Base64 encode the password (from original implementation)
        byte[] bytesEncoded = com.bmc.thirdparty.org.apache.commons.codec.binary.Base64.encodeBase64(userPassword.getBytes());
        
        ARServerUser serverUser = new ARServerUser();
        serverUser.setServer(servername);
        serverUser.setUser(userName);
        serverUser.setPassword(userPassword);
        serverUser.setPort(port);
        
        logger.info("Server Name: {} ----User Name: {} ----Port: {}", servername, userName, port);
        logger.debug("Password configured: {}", userPassword != null && !userPassword.isEmpty() ? "[CONFIGURED]" : "[MISSING]");
        
        logger.info("Connected------User verified");
        
        try {
            logger.info("----Start verifying the AR User----");
            serverUser.verifyUser();
            logger.info("Verified the AR user successfully.");
            
        } catch (ARException e) {
            logger.error("Error while verifying the AR user :" + e.getMessage());
        }
        
        return serverUser;
    }

    /**
     * Creates incident entry in BMC Remedy staging form
     * Maps SolarWinds incident fields to BMC Remedy form fields
     * 
     * @param arServerUser Authenticated AR Server user
     * @param incidentDetails Incident details from SolarWinds
     * @return Generated incident ID or empty string if failed
     */
    public String createInBoundEntry(ARServerUser arServerUser, IncidentDetails incidentDetails) {
        logger.info("******create Solar winds Entry starts************************");
        String generatedID = "";
        
        try {
            Entry coreValues = new Entry();
            
            // Map incident fields to BMC Remedy form fields (using original field IDs)
            coreValues.put(Integer.valueOf(536870943), new Value(incidentDetails.getSourceOfCreation()));
            coreValues.put(Integer.valueOf(536870913), new Value(incidentDetails.getAlertName()));
            coreValues.put(Integer.valueOf(536870914), new Value(incidentDetails.getAlertDescription()));
            coreValues.put(Integer.valueOf(536870915), new Value(incidentDetails.getSeverity()));
            coreValues.put(Integer.valueOf(536870916), new Value(incidentDetails.getAlertLimitCategory()));
            coreValues.put(Integer.valueOf(536870917), new Value(incidentDetails.getAdd1()));
            coreValues.put(Integer.valueOf(536870918), new Value(incidentDetails.getAdd2()));
            
            // Create entry in BMC Remedy staging form
            generatedID = arServerUser.createEntry("INTG-ITSM-SOLARWIND-INCIDENT-CREATE-STAGING-FORM", coreValues);
            
            logger.info("******create Solar Winds Entry End s************************");
            
        } catch (Exception var9) {
            logger.error("Exception..." + var9);
        }
        
        return generatedID;
    }
}