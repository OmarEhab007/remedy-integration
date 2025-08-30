package net.cybermak.integration.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * ReadProperties - Singleton configuration manager for monitoring tool integration
 * Loads configuration from remedy.properties file
 * 
 * Original properties file location: E:\Program Files\BMC Software\Integrations\monitoring\remedy.properties
 * Contains server connection details for BMC Remedy integration
 * 
 * Recreated from original compiled WAR file to maintain compatibility
 *
 * @author Reverse Engineered from WAR file
 * @version 1.0
 */
public class ReadProperties {

    final Logger logger = LoggerFactory.getLogger(ReadProperties.class);
    
    private static ReadProperties instance;
    private Properties properties;
    
    public String serverName;
    public String userName;
    public String password;
    public String generateTokenURL;
    public String port;

    /**
     * Protected constructor implementing singleton pattern
     * Loads properties from file during construction
     */
    protected ReadProperties() {
        logger.info("*********************Start Getting Properties****************************");
        
        properties = new Properties();
        
        try {
            // Try original hardcoded path first for backward compatibility
            String filePath = "E:\\Program Files\\BMC Software\\Integrations\\monitoring\\remedy.properties";
            InputStream file = null;
            
            try {
                file = Files.newInputStream(Paths.get(filePath));
            } catch (IOException e) {
                // Fallback to classpath for Spring Boot compatibility
                logger.warn("Cannot load from original path, trying classpath: " + e.getMessage());
                file = getClass().getClassLoader().getResourceAsStream("remedy.properties");
                if (file == null) {
                    file = getClass().getClassLoader().getResourceAsStream("remedy.properties");
                }
            }
            
            if (file != null) {
                properties.load(file);
                
                serverName = properties.getProperty("serverName");
                userName = properties.getProperty("userName");
                password = properties.getProperty("userPassword");
                port = properties.getProperty("port");
                
                logger.info("Loaded properties - Server: " + serverName + ", Port: " + port);
            } else {
                logger.error("Could not load properties file from any location");
            }
            
        } catch (IOException e) {
            logger.error("Error loading properties: " + e.getMessage());
        }
    }

    /**
     * Gets singleton instance of ReadProperties
     * 
     * @return ReadProperties instance
     */
    public static synchronized ReadProperties getInstance() {
        if (instance == null) {
            instance = new ReadProperties();
        }
        return instance;
    }

    /**
     * Gets property value by key
     * 
     * @param key Property key to retrieve
     * @return Property value or error message if key doesn't exist
     */
    public String getValue(String key) {
        return properties.getProperty(key, String.format("The key %s does not exists!", key));
    }
}