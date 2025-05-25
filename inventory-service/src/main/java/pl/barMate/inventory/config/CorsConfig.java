// filepath: src/main/java/pl/barMate/inventory/config/CorsConfig.java
package pl.barMate.inventory.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // Zezwól na wszystkie endpointy
                        .allowedOrigins("http://localhost:3000") // Zezwól na frontend
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Zezwól na określone metody
                        .allowedHeaders("*") // Zezwól na wszystkie nagłówki
                        .allowCredentials(true); // Zezwól na uwierzytelnianie
            }
        };
    }
}