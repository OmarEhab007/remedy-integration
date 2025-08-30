package net.cybermak.integration.remedy.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * Configuration properties for BMC Remedy connections
 * Based on original remedy.properties configuration
 */
@Configuration
@ConfigurationProperties(prefix = "remedy")
public class RemedyConnectionProperties {

    private Connection connection = new Connection();
    private Map<String, ServerConfig> servers;
    private Map<String, String> forms;
    private Map<String, Map<String, String>> fields;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public Map<String, ServerConfig> getServers() {
        return servers;
    }

    public void setServers(Map<String, ServerConfig> servers) {
        this.servers = servers;
    }

    public Map<String, String> getForms() {
        return forms;
    }

    public void setForms(Map<String, String> forms) {
        this.forms = forms;
    }

    public Map<String, Map<String, String>> getFields() {
        return fields;
    }

    public void setFields(Map<String, Map<String, String>> fields) {
        this.fields = fields;
    }

    public static class Connection {
        private String serverName;
        private String username;
        private String password;
        private int port = 6000;
        private Pool pool = new Pool();
        private Retry retry = new Retry();

        public String getServerName() {
            return serverName;
        }

        public void setServerName(String serverName) {
            this.serverName = serverName;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }

        public Pool getPool() {
            return pool;
        }

        public void setPool(Pool pool) {
            this.pool = pool;
        }

        public Retry getRetry() {
            return retry;
        }

        public void setRetry(Retry retry) {
            this.retry = retry;
        }
    }

    public static class Pool {
        private int maxSize = 10;
        private int minIdle = 2;
        private long maxWaitTime = 30000;
        private long validationTimeout = 5000;

        public int getMaxSize() {
            return maxSize;
        }

        public void setMaxSize(int maxSize) {
            this.maxSize = maxSize;
        }

        public int getMinIdle() {
            return minIdle;
        }

        public void setMinIdle(int minIdle) {
            this.minIdle = minIdle;
        }

        public long getMaxWaitTime() {
            return maxWaitTime;
        }

        public void setMaxWaitTime(long maxWaitTime) {
            this.maxWaitTime = maxWaitTime;
        }

        public long getValidationTimeout() {
            return validationTimeout;
        }

        public void setValidationTimeout(long validationTimeout) {
            this.validationTimeout = validationTimeout;
        }
    }

    public static class Retry {
        private int maxAttempts = 3;
        private long delay = 1000;

        public int getMaxAttempts() {
            return maxAttempts;
        }

        public void setMaxAttempts(int maxAttempts) {
            this.maxAttempts = maxAttempts;
        }

        public long getDelay() {
            return delay;
        }

        public void setDelay(long delay) {
            this.delay = delay;
        }
    }

    public static class ServerConfig {
        private String name;
        private String username;
        private String password;
        private int port = 6000;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }
    }
}