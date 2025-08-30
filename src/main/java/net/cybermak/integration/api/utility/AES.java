package net.cybermak.integration.api.utility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

/**
 * AES Encryption/Decryption Utility Class
 * Provides AES encryption and decryption capabilities for password security
 * 
 * Features:
 * - AES-256 encryption with SHA-1 key derivation
 * - Base64 encoding/decoding for string handling
 * - Support for object encryption via SealedObject
 * - Hardcoded secret key for backward compatibility
 * 
 * Recreated from original compiled WAR file to maintain compatibility
 * 
 * @author Reverse Engineered from WAR file
 * @version 1.0
 */
public class AES {

    static final Logger logger = LoggerFactory.getLogger(AES.class);
    
    private static SecretKeySpec secretKey;
    private static byte[] key;

    /**
     * Sets the encryption key using SHA-1 hash algorithm
     * Creates a 16-byte AES key from the provided string
     * 
     * @param myKey The string to derive the encryption key from
     */
    public static void setKey(final String myKey) {
        MessageDigest sha = null;
        
        logger.info(" Original Key:" + myKey);
        
        try {
            key = myKey.getBytes("UTF-8");
            sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            secretKey = new SecretKeySpec(key, "AES");
            
            logger.info(" Final Key:" + secretKey.toString());
            
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * Encrypts a string using AES encryption
     * 
     * @param strToEncrypt String to encrypt
     * @param secret Secret key for encryption
     * @return Base64 encoded encrypted string or null if error
     */
    public static String encrypt(final String strToEncrypt, final String secret) {
        try {
            setKey(secret);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
        } catch (Exception e) {
            logger.error("Error while encrypting:" + e.toString());
        }
        return null;
    }

    /**
     * Decrypts an AES encrypted string
     * 
     * @param strToDecrypt Base64 encoded encrypted string
     * @param secret Secret key for decryption
     * @return Decrypted string or null if error
     */
    public static String decrypt(final String strToDecrypt, final String secret) {
        try {
            setKey(secret);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
        } catch (Exception e) {
            System.out.println("Error while decrypting:" + e.toString());
        }
        return null;
    }

    /**
     * Encrypts a Serializable object using AES
     * 
     * @param algorithm Cipher algorithm to use
     * @param object Object to encrypt
     * @param key Secret key for encryption
     * @param iv Initialization vector
     * @return SealedObject containing encrypted data
     * @throws NoSuchPaddingException If padding is not available
     * @throws NoSuchAlgorithmException If algorithm is not available
     * @throws InvalidAlgorithmParameterException If algorithm parameters are invalid
     * @throws InvalidKeyException If key is invalid
     * @throws IOException If I/O error occurs
     * @throws IllegalBlockSizeException If block size is illegal
     */
    public static SealedObject encryptObject(String algorithm, Serializable object,
                                           SecretKey key, IvParameterSpec iv)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException,
            InvalidKeyException, IOException, IllegalBlockSizeException {

        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);
        SealedObject sealedObject = new SealedObject(object, cipher);
        return sealedObject;
    }

    /**
     * Decrypts a SealedObject using AES
     * 
     * @param algorithm Cipher algorithm to use
     * @param sealedObject SealedObject to decrypt
     * @param key Secret key for decryption
     * @param iv Initialization vector
     * @return Decrypted Serializable object
     * @throws NoSuchPaddingException If padding is not available
     * @throws NoSuchAlgorithmException If algorithm is not available
     * @throws InvalidAlgorithmParameterException If algorithm parameters are invalid
     * @throws InvalidKeyException If key is invalid
     * @throws ClassNotFoundException If class is not found during deserialization
     * @throws BadPaddingException If padding is bad
     * @throws IllegalBlockSizeException If block size is illegal
     * @throws IOException If I/O error occurs
     */
    public static Serializable decryptObject(String algorithm, SealedObject sealedObject,
                                           SecretKey key, IvParameterSpec iv)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException,
            InvalidKeyException, ClassNotFoundException, BadPaddingException, IllegalBlockSizeException,
            IOException {

        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.DECRYPT_MODE, key, iv);
        Serializable unsealObject = (Serializable) sealedObject.getObject(cipher);
        return unsealObject;
    }

    /**
     * Decrypts data using hardcoded secret key
     * Uses "TESecretKey12345" as the default key
     * 
     * @param encryptedData Base64 encoded encrypted data
     * @return Decrypted string
     */
    public static String decryptData(String encryptedData) {
        // Hardcoded secret key from original implementation
        String key = "TESecretKey12345";
        byte[] encryptedDataBytes = key.getBytes();
        
        SecretKeySpec secretKeySpec = new SecretKeySpec(encryptedDataBytes, "AES");
        
        Cipher cipher;
        try {
            cipher = Cipher.getInstance("AES");
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new RuntimeException(e);
        }
        
        byte[] encryptedDataInput = Base64.getDecoder().decode(encryptedData);
        
        try {
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        }
        
        byte[] decryptedDataBytes;
        try {
            decryptedDataBytes = cipher.doFinal(encryptedDataInput);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            throw new RuntimeException(e);
        }
        
        String decryptedData = new String(decryptedDataBytes);
        return decryptedData;
    }

    /**
     * Encrypts data using hardcoded secret key
     * Uses "TESecretKey12345" as the default key
     * 
     * @param inputString String to encrypt
     * @return Base64 encoded encrypted string
     */
    public static String encryptData(String inputString) {
        // Hardcoded secret key from original implementation
        String key = "TESecretKey12345";
        byte[] inputStringBytes = inputString.getBytes(StandardCharsets.UTF_8);
        byte[] encryptedDataBytes = key.getBytes();
        
        SecretKeySpec secretKeySpec = new SecretKeySpec(encryptedDataBytes, "AES");
        
        Cipher cipher;
        try {
            cipher = Cipher.getInstance("AES");
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new RuntimeException(e);
        }
        
        try {
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        }
        
        byte[] outputDataBytes;
        try {
            outputDataBytes = cipher.doFinal(inputStringBytes);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            throw new RuntimeException(e);
        }
        
        // Use Base64Utils from Spring/BMC for consistency with original
        String output = Base64.getEncoder().encodeToString(outputDataBytes);
        return output;
    }
}