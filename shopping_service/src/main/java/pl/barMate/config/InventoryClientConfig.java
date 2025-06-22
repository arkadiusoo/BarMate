package pl.barMate.config;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class InventoryClientConfig {

    @Bean
    @Qualifier("inventoryWebClient")
    public WebClient inventoryWebClient() {
        return WebClient.builder()
                .baseUrl("http://localhost:8080/inventory_service") // bazowy URL do inventory service
                .defaultHeader("Content-Type", "application/json")
                .build();
    }
}
