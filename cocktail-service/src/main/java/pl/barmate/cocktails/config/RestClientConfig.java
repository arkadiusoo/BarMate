package pl.barmate.cocktails.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class RestClientConfig {

    @Value("${cocktaildb.api.base-url}")
    private String baseUrl;

    @Bean
    public WebClient cocktailWebClient(WebClient.Builder builder) {
        ExchangeStrategies strategies = ExchangeStrategies.builder()
                .codecs(configurer -> configurer
                        .defaultCodecs()
                        .maxInMemorySize(16 * 1024 * 1024))
                .build();

        return builder
                .baseUrl(baseUrl)
                .exchangeStrategies(strategies)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }

    @Bean
    @Qualifier("groqWebClient")
    public WebClient groqWebClient(
            WebClient.Builder builder,
            @Value("${groq.api.base-url}") String baseUrl,
            @Value("${groq.api.token}") String apiToken
    ) {
        return builder
                .baseUrl(baseUrl)
                .defaultHeader("Authorization", "Bearer " + apiToken)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }

    @Bean
    @LoadBalanced
    public WebClient.Builder loadBalancedWebClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    @Qualifier("inventoryWebClient")
    public WebClient inventoryWebClient(
            @Qualifier("loadBalancedWebClientBuilder") WebClient.Builder builder
    ) {
        return builder
                .baseUrl("http://inventory-service/api/inventory")
                .defaultHeader("Content-Type", "application/json")
                .build();
    }
}

