package pl.barmate.cocktails.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import pl.barmate.cocktails.model.GroqRequest;
import pl.barmate.cocktails.model.GroqResponse;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TranslationService {
    private static final Logger log = LoggerFactory.getLogger(TranslationService.class);

    private final WebClient webClient;
    private final String modelName;

    private static final String SYSTEM_PROMPT_BARTENDER = "You are a professional, helpful bartender." +
            " Your task is to translate the given English text to Polish. Use natural, clear language." +
            " Just provide the translation, without any additional comments or text.";

    public TranslationService(
            @Qualifier("groqWebClient") WebClient webClient,
            @Value("${groq.api.model}") String modelName
    ) {
        this.webClient = webClient;
        this.modelName = modelName;
    }

    @Cacheable(cacheNames = "groq_translations_v1", key = "#textToTranslate")
    public Mono<String> translate(String textToTranslate) {
        if (textToTranslate == null || textToTranslate.isBlank()) {
            return Mono.just("");
        }
        log.info("Tłumaczenie (Groq): {}", textToTranslate);
        GroqRequest request = new GroqRequest(modelName, SYSTEM_PROMPT_BARTENDER, textToTranslate);
        return callGroqApi(request);
    }

    @Cacheable(cacheNames = "groq_batch_translations_v2", key = "#textsToTranslate.toString()")
    public Mono<List<String>> translateBatch(List<String> textsToTranslate) {
        if (textsToTranslate == null || textsToTranslate.isEmpty()) {
            return Mono.just(List.of());
        }

        String userPrompt = "You are a highly precise translation bot for a bartending application. " +
                "Your task is to translate a list of English terms into Polish, following these strict rules:\n" +
                "1.  Convert imperial and European units to metric (ml): 1 oz = 30 ml, 1 cl = 10 ml. Round to the nearest whole number.\n" +
                "2.  Keep proper names (like 'Triple Sec', 'Grand Marnier') unchanged.\n" +
                "3.  Translate descriptive terms (like 'Juice of 1/2', 'dash') into natural Polish phrases.\n" +
                "4.  Your output MUST be a plain list. Each translated term on a new line.\n" +
                "5.  The number of output lines MUST EXACTLY match the number of input lines.\n" +
                "6.  Do NOT add any comments, explanations, or text outside of the translated terms.\n\n" +
                "--- EXAMPLES OF CORRECT BEHAVIOR ---\n" +
                "INPUT LIST:\n" +
                "```\n" +
                "3 cl\n" +
                "1 oz\n" +
                "1/2 tsp\n" +
                "Juice of 1/2\n" +
                "dash\n" +
                "1 tsp\n" +
                "1 tbsp\n" +
                "Triple Sec\n" +
                "```\n" +
                "CORRECT POLISH TRANSLATION:\n" +
                "```\n" +
                "30 ml\n" +
                "30 ml\n" +
                "2 ml (pół łyżeczki)\n" +
                "Sok z połówki\n" +
                "kilka kropli\n" +
                "jedna łyżeczka\n" +
                "jedna łyżka\n" +
                "Triple Sec\n" +
                "```\n" +
                "--- END OF EXAMPLES ---\n\n" +
                "Now, process the following input list according to all the rules above.\n\n" +
                "INPUT LIST:\n" +
                "```\n" +
                String.join("\n", textsToTranslate) + "\n" +
                "```\n\n" +
                "POLISH TRANSLATION:\n";

        GroqRequest request = new GroqRequest(modelName, SYSTEM_PROMPT_BARTENDER, userPrompt);

        log.info("Tłumaczenie wsadowe (Groq) dla {} elementów z konwersją jednostek.", textsToTranslate.size());

        return callGroqApi(request)
                .map(response -> {
                    String rawResult = response.trim();
                    log.info("--- SUROWA ODPOWIEDŹ Z GROQ API (BATCH) ---\n{}\n--------------------------------------------", rawResult);

                    String cleanedResult = rawResult.replace("```", "");

                    List<String> translatedList = Arrays.stream(cleanedResult.split("\n"))
                            .map(String::trim)
                            .filter(line -> !line.isEmpty())
                            .collect(Collectors.toList());

                    if (translatedList.size() != textsToTranslate.size()) {
                        log.warn("Liczba linii w odpowiedzi modelu ({}) po oczyszczeniu nie zgadza się z liczbą linii na wejściu ({}). Zwracam oryginalną listę.", translatedList.size(), textsToTranslate.size());
                        return textsToTranslate;
                    }

                    log.info("Sukces! Poprawnie przetworzono odpowiedź wsadową.");
                    return translatedList;
                });
    }

    private Mono<String> callGroqApi(GroqRequest request) {
        return webClient.post()
                .uri("/chat/completions")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(GroqResponse.class)
                .map(response -> {
                    if (response != null && response.getChoices() != null && !response.getChoices().isEmpty()) {
                        String content = response.getChoices().getFirst().getMessage().getContent();
                        log.info("Sukces! Groq API zwróciło odpowiedź.");
                        return content.trim();
                    }
                    log.warn("Groq API zwróciło pustą odpowiedź.");
                    return "";
                })
                .doOnError(error -> log.error("Błąd podczas wywołania Groq API: {}", error.getMessage()))
                .onErrorReturn("");
    }
}
