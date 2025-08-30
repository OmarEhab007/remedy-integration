package net.cybermak.integration.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Security configuration properties
 * Centralizes security-related configuration
 */
@Configuration
@ConfigurationProperties(prefix = "security")
public class SecurityConfig {
    
    private ApiKey apiKey = new ApiKey();
    private Jwt jwt = new Jwt();
    
    public ApiKey getApiKey() { return apiKey; }
    public void setApiKey(ApiKey apiKey) { this.apiKey = apiKey; }
    
    public Jwt getJwt() { return jwt; }
    public void setJwt(Jwt jwt) { this.jwt = jwt; }
    
    public static class ApiKey {
        private String legacyKey = "${LEGACY_API_KEY:10}";
        private boolean enabled = true;
        
        public String getLegacyKey() { return legacyKey; }
        public void setLegacyKey(String legacyKey) { this.legacyKey = legacyKey; }
        
        public boolean isEnabled() { return enabled; }
        public void setEnabled(boolean enabled) { this.enabled = enabled; }
    }
    
    public static class Jwt {
        private String secret = "${JWT_SECRET:defaultSecretKey}";
        private long expiration = 86400000;
        
        public String getSecret() { return secret; }
        public void setSecret(String secret) { this.secret = secret; }
        
        public long getExpiration() { return expiration; }
        public void setExpiration(long expiration) { this.expiration = expiration; }
    }
}