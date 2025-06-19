package pl.barmate.cocktails.service;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.ChatModel;
import com.openai.models.chat.completions.ChatCompletionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Serwis tłumaczeń i ulepszania opisów drinków za pomocą OpenAI.
 */
@Service
public class TranslationService {

    private final OpenAIClient client;

    public TranslationService(@Value("${openai.api.key}") String apiKey) {
        this.client = OpenAIOkHttpClient.builder()
                .apiKey(apiKey)
                .build();
    }

    /**
     * Wrappuje dowolny tekst w ChatCompletion i zwraca przetłumaczoną zawartość.
     */
    public Mono<String> translate(String prompt) {
        ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
                .addUserMessage(prompt)
                .model(ChatModel.O4_MINI)
                .build();

        return Mono.fromCallable(() -> client.chat()
                        .completions()
                        .create(params))
                .map(completion ->
                        completion.choices()
                                .getFirst()
                                .message()
                                .content()
                                .orElse("")
                                .trim()
                );
    }

    public Mono<String> translateIngredients(List<String> ingredients, String lang) {
        // budujemy prompt dla całej listy
        String joined = String.join("\n- ", ingredients);
        String prompt = String.format(
                "Przetłumacz na %s listę składników drinka:\n- %s",
                lang, joined);
        return translate(prompt);
    }

    public Mono<String> translateInstructions(String instructions, String lang) {
        String prompt = String.format(
                "Przetłumacz na %s instrukcje przygotowania drinka, zachowując numerację kroków:\n%s",
                lang, instructions);
        return translate(prompt);
    }

    public Mono<String> translateName(String name, String lang) {
        String prompt = String.format(
                "Przetłumacz na %s nazwę drinka: \"%s\"",
                lang, name);
        return translate(prompt);
    }
}
