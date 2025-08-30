package net.cybermak.integration.remedy.connection;

/**
 * BMC Remedy connection abstraction
 * TDD: Minimal implementation to satisfy RemedyConnectionManagerTest
 */
public class RemedyConnection {
    
    private final String host;
    private final int port;
    private final String username;
    private boolean connected;
    
    public RemedyConnection(String host, int port, String username) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.connected = false;
    }
    
    public void connect() {
        // Simulate connection establishment
        // In real implementation, this would connect to BMC Remedy server
        this.connected = true;
    }
    
    public void disconnect() {
        // Simulate connection closure
        this.connected = false;
    }
    
    public boolean isConnected() {
        return connected;
    }
    
    public String getHost() {
        return host;
    }
    
    public int getPort() {
        return port;
    }
    
    public String getUsername() {
        return username;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RemedyConnection that = (RemedyConnection) o;
        return port == that.port && 
               host.equals(that.host) && 
               username.equals(that.username);
    }
    
    @Override
    public int hashCode() {
        return java.util.Objects.hash(host, port, username);
    }
}