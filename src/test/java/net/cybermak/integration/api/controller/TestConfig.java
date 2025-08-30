package net.cybermak.integration.api.controller;

import net.cybermak.integration.core.ModuleRegistry;
import net.cybermak.integration.remedy.form.FormHandler;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.mockito.Mockito.mock;

/**
 * Test configuration for API controller tests
 */
@TestConfiguration
@EnableWebSecurity
public class TestConfig {
    
    @Bean
    @Primary
    public ModuleRegistry moduleRegistry() {
        return mock(ModuleRegistry.class);
    }
    
    @Bean
    @Primary
    public FormHandler formHandler() {
        return mock(FormHandler.class);
    }
    
    @Bean
    public SecurityFilterChain testFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                .anyRequest().permitAll() // Allow all for tests
            );
            
        return http.build();
    }
}