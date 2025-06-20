// plik: src/main/java/pl/barmate/cocktails/service/TranslationService.java
// Zastąp całą zawartość tego pliku poniższym kodem:
package pl.barmate.cocktails.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import pl.barmate.cocktails.model.HuggingFaceTranslationRequest;
import pl.barmate.cocktails.model.HuggingFaceTranslationResponse;
import reactor.core.publisher.Mono;

@Service
public class TranslationService {
    private static final Logger log = LoggerFactory.getLogger(TranslationService.class);

    private final WebClient webClient;
    private final String modelPath;

    public TranslationService(
            @Qualifier("huggingFaceWebClient") WebClient webClient,
            @Value("${huggingface.api.model}") String model
    ) {
        this.webClient = webClient;
        this.modelPath = "/models/" + model;
    }

    // Upewnij się, że nazwa cache jest unikalna, żeby nie wczytać starych, błędnych wyników
    @Cacheable(cacheNames = "translations_v3", key = "#textToTranslate")
    public Mono<String> translate(String textToTranslate) {
        if (textToTranslate == null || textToTranslate.isBlank()) {
            return Mono.just("");
        }

        log.info("Próba tłumaczenia tekstu: '{}'", textToTranslate);

        // Przygotowujemy request dla modelu mBART
        // Kody języków: "en_XX" dla angielskiego, "pl_PL" dla polskiego
        HuggingFaceTranslationRequest request = new HuggingFaceTranslationRequest(textToTranslate, "en_XX", "pl_PL");

        return webClient.post()
                .uri(modelPath)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(HuggingFaceTranslationResponse[].class)
                .map(responseArray -> {
                    if (responseArray != null && responseArray.length > 0 && responseArray[0].getTranslationText() != null) {
                        String translated = responseArray[0].getTranslationText();
                        log.info("Sukces! Przetłumaczono na: '{}'", translated);
                        return translated;
                    }
                    log.warn("Odpowiedź z API pusta lub niepoprawna dla tekstu: '{}'", textToTranslate);
                    return textToTranslate;
                })
                .doOnError(error -> log.error("Błąd podczas wywołania API tłumacza: {}", error.getMessage()))
                .onErrorReturn(textToTranslate);
    }
}