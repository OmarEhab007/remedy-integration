package net.cybermak.integration.api.utility;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for AES utility methods.
 * Validates encryption/decryption functionality and maintains compatibility with original implementation.
 */
class AESTest {

    @Test
    @DisplayName("Test basic string encryption and decryption")
    void testBasicEncryptDecrypt() {
        // Given
        String originalText = "Hello, World!";
        String secretKey = "mySecretKey123";
        
        // When
        String encrypted = AES.encrypt(originalText, secretKey);
        String decrypted = AES.decrypt(encrypted, secretKey);
        
        // Then
        assertNotNull(encrypted, "Encrypted text should not be null");
        assertNotEquals(originalText, encrypted, "Encrypted text should be different from original");
        assertEquals(originalText, decrypted, "Decrypted text should match original");
    }

    @Test
    @DisplayName("Test encryption with different keys produces different results")
    void testDifferentKeysProduceDifferentResults() {
        // Given
        String originalText = "Test message";
        String key1 = "key1";
        String key2 = "key2";
        
        // When
        String encrypted1 = AES.encrypt(originalText, key1);
        String encrypted2 = AES.encrypt(originalText, key2);
        
        // Then
        assertNotEquals(encrypted1, encrypted2, "Different keys should produce different encrypted results");
    }

    @Test
    @DisplayName("Test decryption with wrong key returns null")
    void testDecryptionWithWrongKey() {
        // Given
        String originalText = "Secret message";
        String correctKey = "correctKey";
        String wrongKey = "wrongKey";
        
        // When
        String encrypted = AES.encrypt(originalText, correctKey);
        String decrypted = AES.decrypt(encrypted, wrongKey);
        
        // Then
        assertNull(decrypted, "Decryption with wrong key should return null");
    }

    @Test
    @DisplayName("Test encryptData and decryptData with hardcoded key")
    void testEncryptDecryptDataWithHardcodedKey() {
        // Given
        String originalText = "Password123";
        
        // When
        String encrypted = AES.encryptData(originalText);
        String decrypted = AES.decryptData(encrypted);
        
        // Then
        assertNotNull(encrypted, "Encrypted data should not be null");
        assertNotEquals(originalText, encrypted, "Encrypted data should be different from original");
        assertEquals(originalText, decrypted, "Decrypted data should match original");
    }

    @Test
    @DisplayName("Test empty string encryption and decryption")
    void testEmptyStringEncryptDecrypt() {
        // Given
        String originalText = "";
        String secretKey = "testKey";
        
        // When
        String encrypted = AES.encrypt(originalText, secretKey);
        String decrypted = AES.decrypt(encrypted, secretKey);
        
        // Then
        assertEquals(originalText, decrypted, "Empty string should encrypt and decrypt correctly");
    }

    @Test
    @DisplayName("Test null input handling")
    void testNullInputHandling() {
        // Given
        String secretKey = "testKey";
        
        // When & Then
        assertDoesNotThrow(() -> {
            String result = AES.encrypt(null, secretKey);
            // Should handle gracefully (may return null or throw exception based on implementation)
        });
    }

    @Test
    @DisplayName("Test special characters encryption and decryption")
    void testSpecialCharactersEncryptDecrypt() {
        // Given
        String originalText = "Special chars: @#$%^&*()_+{}|:<>?[]\\;',./";
        String secretKey = "specialKey";
        
        // When
        String encrypted = AES.encrypt(originalText, secretKey);
        String decrypted = AES.decrypt(encrypted, secretKey);
        
        // Then
        assertEquals(originalText, decrypted, "Special characters should encrypt and decrypt correctly");
    }

    @Test
    @DisplayName("Test Unicode characters encryption and decryption")
    void testUnicodeCharactersEncryptDecrypt() {
        // Given
        String originalText = "Unicode test: 你好世界 🌍 αβγδε";
        String secretKey = "unicodeKey";
        
        // When
        String encrypted = AES.encrypt(originalText, secretKey);
        String decrypted = AES.decrypt(encrypted, secretKey);
        
        // Then
        assertEquals(originalText, decrypted, "Unicode characters should encrypt and decrypt correctly");
    }
}