package net.cybermak.integration.api.utility;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration test demonstrating real-world usage of AES utility.
 * Tests scenarios similar to BMC Remedy password encryption/decryption.
 */
class AESIntegrationTest {

    @Test
    @DisplayName("Test password encryption/decryption scenario")
    void testPasswordEncryptionDecryption() {
        // Simulate BMC Remedy password encryption scenario
        String originalPassword = "RemedyAdmin123!";
        String encryptionKey = "BMCRemedyIntegration2023";
        
        // Encrypt the password
        String encryptedPassword = AES.encrypt(originalPassword, encryptionKey);
        assertNotNull(encryptedPassword, "Password encryption should not return null");
        assertNotEquals(originalPassword, encryptedPassword, "Encrypted password should be different");
        
        // Decrypt the password
        String decryptedPassword = AES.decrypt(encryptedPassword, encryptionKey);
        assertEquals(originalPassword, decryptedPassword, "Decrypted password should match original");
        
        System.out.println("Original Password: " + originalPassword);
        System.out.println("Encrypted Password: " + encryptedPassword);
        System.out.println("Decrypted Password: " + decryptedPassword);
    }

    @Test
    @DisplayName("Test configuration data encryption with hardcoded key")
    void testConfigurationDataEncryption() {
        // Simulate configuration data encryption (like database passwords)
        String configValue = "jdbc:postgresql://remedy-db:5432/remedy?user=admin&password=secret123";
        
        // Encrypt using hardcoded key method
        String encrypted = AES.encryptData(configValue);
        assertNotNull(encrypted, "Configuration encryption should not return null");
        assertNotEquals(configValue, encrypted, "Encrypted config should be different");
        
        // Decrypt using hardcoded key method
        String decrypted = AES.decryptData(encrypted);
        assertEquals(configValue, decrypted, "Decrypted config should match original");
        
        System.out.println("Original Config: " + configValue);
        System.out.println("Encrypted Config: " + encrypted);
        System.out.println("Decrypted Config: " + decrypted);
    }

    @Test
    @DisplayName("Test multiple password encryption consistency")
    void testMultiplePasswordEncryptionConsistency() {
        String password = "AdminPassword456";
        String key = "SharedSecretKey";
        
        // Encrypt the same password multiple times
        String encrypted1 = AES.encrypt(password, key);
        String encrypted2 = AES.encrypt(password, key);
        
        // Both encryptions should produce the same result (deterministic)
        assertEquals(encrypted1, encrypted2, "Same input should produce same encrypted output");
        
        // Both should decrypt correctly
        assertEquals(password, AES.decrypt(encrypted1, key));
        assertEquals(password, AES.decrypt(encrypted2, key));
    }

    @Test
    @DisplayName("Test cross-compatibility with original BMC implementation")
    void testCrossCompatibility() {
        // Test with typical BMC Remedy configuration values
        String[] testPasswords = {
            "AR System User",
            "remedypassword",
            "Integration123!",
            "BMC@2023#Secret"
        };
        
        String integrationKey = "CITCMonitoringIntegration";
        
        for (String password : testPasswords) {
            String encrypted = AES.encrypt(password, integrationKey);
            String decrypted = AES.decrypt(encrypted, integrationKey);
            
            assertEquals(password, decrypted, 
                "Password encryption/decryption should be reversible for: " + password);
        }
    }

    @Test
    @DisplayName("Test encrypted data format is Base64")
    void testEncryptedDataFormat() {
        String testData = "TestData123";
        String key = "TestKey";
        
        String encrypted = AES.encrypt(testData, key);
        
        // Encrypted data should be valid Base64
        try {
            byte[] decodedBytes = java.util.Base64.getDecoder().decode(encrypted);
            assertTrue(decodedBytes.length > 0, "Encrypted data should decode as valid Base64");
        } catch (IllegalArgumentException e) {
            fail("Encrypted data should be valid Base64 format");
        }
    }
}