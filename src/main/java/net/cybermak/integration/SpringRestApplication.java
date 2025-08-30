package net.cybermak.integration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * Main Spring Boot application for Remedy Integration Platform
 * Based on original SpringRestApplication with WAR deployment support
 * Compatible with existing deployment infrastructure
 */
@SpringBootApplication(scanBasePackages = "net.cybermak.integration")
public class SpringRestApplication extends SpringBootServletInitializer {

    /**
     * Main method for standalone JAR execution
     */
    public static void main(String[] args) {
        SpringApplication.run(SpringRestApplication.class, args);
    }

    /**
     * Configure method for WAR deployment
     * Required for deployment to external servlet containers
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(SpringRestApplication.class);
    }
}