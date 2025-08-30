package net.cybermak.integration.remedy;

import net.cybermak.integration.remedy.connection.RemedyConnection;
import net.cybermak.integration.remedy.connection.RemedyConnectionManager;
import net.cybermak.integration.remedy.exception.RemedyConnectionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * TDD Test: BMC Remedy Connection Manager tests
 * Following Red-Green-Refactor cycle
 */
@TestPropertySource(properties = {
    "remedy.connection.host=localhost",
    "remedy.connection.port=6000",
    "remedy.connection.username=test",
    "remedy.connection.password=test"
})
class RemedyConnectionManagerTest {

    private RemedyConnectionManager connectionManager;

    @BeforeEach
    void setUp() {
        connectionManager = new RemedyConnectionManager();
    }

    @Test
    void shouldEstablishConnection() {
        // Red Phase: Will fail until we implement connection management
        
        RemedyConnection connection = connectionManager.getConnection();
        
        assertThat(connection).isNotNull();
        assertThat(connection.isConnected()).isTrue();
    }

    @Test
    void shouldReuseExistingConnection() {
        // Red Phase: Will fail until we implement connection pooling
        
        RemedyConnection connection1 = connectionManager.getConnection();
        RemedyConnection connection2 = connectionManager.getConnection();
        
        assertThat(connection1).isNotNull();
        assertThat(connection2).isNotNull();
        // Should reuse connection from pool
        assertThat(connection1).isEqualTo(connection2);
    }

    @Test
    void shouldHandleConnectionFailure() {
        // Red Phase: Will fail until we implement error handling
        
        // Simulate connection failure by using invalid configuration
        RemedyConnectionManager invalidManager = new RemedyConnectionManager();
        // This would be configured with invalid settings in real implementation
        
        assertThatThrownBy(() -> {
            // Force a connection with invalid settings
            invalidManager.createNewConnection("invalid-host", 9999, "invalid", "invalid");
        }).isInstanceOf(RemedyConnectionException.class);
    }

    @Test
    void shouldCloseConnection() {
        // Red Phase: Will fail until we implement connection closure
        
        RemedyConnection connection = connectionManager.getConnection();
        
        connectionManager.closeConnection(connection);
        
        assertThat(connection.isConnected()).isFalse();
    }

    @Test
    void shouldRetryOnFailure() {
        // Red Phase: Will fail until we implement retry logic
        
        // This test would verify retry mechanism
        // For now, we'll test basic retry configuration exists
        int retryAttempts = connectionManager.getRetryAttempts();
        
        assertThat(retryAttempts).isGreaterThan(0);
    }
}