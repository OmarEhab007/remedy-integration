package net.cybermak.integration.remedy.exception;

/**
 * Exception thrown when BMC Remedy connection operations fail
 * TDD: Minimal implementation to satisfy RemedyConnectionManagerTest
 */
public class RemedyConnectionException extends RuntimeException {
    
    public RemedyConnectionException(String message) {
        super(message);
    }
    
    public RemedyConnectionException(String message, Throwable cause) {
        super(message, cause);
    }
}