package pl.barmate.cocktails.unitTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import pl.barmate.cocktails.model.GroqResponse;
import pl.barmate.cocktails.service.TranslationService;
import reactor.core.publisher.Mono;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TranslationServiceTest {

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private WebClient mockWebClient;

    private TranslationService translationService;

    @BeforeEach
    void setUp() {
        // Tworzymy instancję serwisu, wstrzykując mock WebClienta
        translationService = new TranslationService(mockWebClient, "test-model");
    }

    @Test
    void translateBatch_shouldReturnCorrectlyParsedList() {
        List<String> inputList = List.of("1 oz", "Tequila");
        String mockApiResponse = "30 ml\nTequila";

        GroqResponse.Message message = new GroqResponse.Message();
        message.setContent(mockApiResponse);
        GroqResponse.Choice choice = new GroqResponse.Choice();
        choice.setMessage(message);
        GroqResponse groqResponse = new GroqResponse();
        groqResponse.setChoices(List.of(choice));

        when(mockWebClient.post()
                .uri(any(String.class))
                .bodyValue(any())
                .retrieve()
                .bodyToMono(GroqResponse.class))
                .thenReturn(Mono.just(groqResponse));

        List<String> result = translationService.translateBatch(inputList).block();
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result).containsExactly("30 ml", "Tequila");
    }

    @Test
    void translate_shouldReturnTranslatedText() {
        String inputText = "Ordinary Drink";
        String mockApiResponse = "Klasyczny drink";

        GroqResponse.Message message = new GroqResponse.Message();
        message.setContent(mockApiResponse);
        GroqResponse.Choice choice = new GroqResponse.Choice();
        choice.setMessage(message);
        GroqResponse groqResponse = new GroqResponse();
        groqResponse.setChoices(List.of(choice));

        when(mockWebClient.post().uri(any(String.class)).bodyValue(any()).retrieve().bodyToMono(GroqResponse.class))
                .thenReturn(Mono.just(groqResponse));

        String result = translationService.translate(inputText).block();

        assertThat(result).isEqualTo("Klasyczny drink");
    }
}
