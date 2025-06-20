package pl.barmate.cocktails.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
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
    @Qualifier("huggingFaceWebClient") // Identyfikator dla tego konkretnego beana
    public WebClient huggingFaceWebClient(
            WebClient.Builder builder,
            @Value("${huggingface.api.base-url}") String hfBaseUrl,
            @Value("${huggingface.api.token}") String apiToken
    ) {
        return builder
                .baseUrl(hfBaseUrl)
                .defaultHeader("Authorization", "Bearer " + apiToken)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }
}

