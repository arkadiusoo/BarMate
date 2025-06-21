package pl.barmate.cocktails.integrationTests;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import pl.barmate.cocktails.model.ApiResponse;
import pl.barmate.cocktails.model.Cocktail;
import pl.barmate.cocktails.model.CocktailDto;
import pl.barmate.cocktails.service.CocktailService;
import pl.barmate.cocktails.service.TranslationService;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class CocktailServiceIntegrationTest {

    @MockitoBean
    private TranslationService mockTranslationService;

    @Autowired
    private CocktailService cocktailService;

    @MockitoBean(name = "cocktailWebClient")
    private WebClient mockCocktailDbClient;

    @MockitoBean(name = "inventoryWebClient")
    private WebClient mockInventoryClient;


    @Test
    void searchByName_shouldReturnTranslatedCocktail_whenApisAreSuccessful() {

        Cocktail mockApiCocktail = new Cocktail();
        mockApiCocktail.setId("11007");
        mockApiCocktail.setName("Margarita");
        mockApiCocktail.setCategory("Ordinary Drink");
        mockApiCocktail.setGlass("Cocktail glass");
        mockApiCocktail.setInstructions("Shake with ice.");
        mockApiCocktail.setAlcoholic("Alcoholic");
        mockApiCocktail.setThumbnail("http://path.to/image.jpg");

        mockApiCocktail.handleUnknown("strIngredient1", "Tequila");
        mockApiCocktail.handleUnknown("strMeasure1", "2 oz");

        ApiResponse<Cocktail> mockApiResponse = new ApiResponse<>();
        mockApiResponse.setDrinks(List.of(mockApiCocktail));

        WebClient.RequestHeadersUriSpec requestHeadersUriSpec = Mockito.mock(WebClient.RequestHeadersUriSpec.class);
        WebClient.ResponseSpec responseSpec = Mockito.mock(WebClient.ResponseSpec.class);
        when(mockCocktailDbClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(java.util.function.Function.class))).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(any(ParameterizedTypeReference.class))).thenReturn(Mono.just(mockApiResponse));

        when(mockTranslationService.translate("Ordinary Drink")).thenReturn(Mono.just("Zwykły drink"));
        when(mockTranslationService.translate("Cocktail glass")).thenReturn(Mono.just("Szklanka koktajlowa"));
        when(mockTranslationService.translate("Shake with ice.")).thenReturn(Mono.just("Wstrząśnij z lodem."));

        when(mockTranslationService.translateBatch(List.of("Tequila"))).thenReturn(Mono.just(List.of("Przetłumaczona Tequila")));
        when(mockTranslationService.translateBatch(List.of("2 oz"))).thenReturn(Mono.just(List.of("Przetłumaczone 60 ml")));

        Mono<List<CocktailDto>> resultMono = cocktailService.searchByName("Margarita");

        StepVerifier.create(resultMono)
                .expectNextMatches(cocktailList -> {
                    assertThat(cocktailList).hasSize(1);

                    CocktailDto margarita = cocktailList.get(0);

                    assertThat(margarita.getId()).isEqualTo("11007");
                    assertThat(margarita.getName()).isEqualTo("Margarita");
                    assertThat(margarita.getAlcoholic()).isEqualTo("Alcoholic");
                    assertThat(margarita.getThumbnail()).isEqualTo("http://path.to/image.jpg");

                    assertThat(margarita.getCategory()).isEqualTo("Zwykły drink");
                    assertThat(margarita.getGlass()).isEqualTo("Szklanka koktajlowa");
                    assertThat(margarita.getInstructions()).isEqualTo("Wstrząśnij z lodem.");

                    assertThat(margarita.getIngredients()).containsExactly("Przetłumaczona Tequila");
                    assertThat(margarita.getMeasures()).containsExactly("Przetłumaczone 60 ml");

                    return true;
                })
                .verifyComplete();
    }

    @Test
    void searchByName_shouldReturnNotFound_whenCocktailApiReturnsEmpty() {
        ApiResponse<Cocktail> emptyApiResponse = new ApiResponse<>();
        emptyApiResponse.setDrinks(null);

        WebClient.RequestHeadersUriSpec requestHeadersUriSpec = Mockito.mock(WebClient.RequestHeadersUriSpec.class);
        WebClient.ResponseSpec responseSpec = Mockito.mock(WebClient.ResponseSpec.class);

        when(mockCocktailDbClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(java.util.function.Function.class))).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(any(ParameterizedTypeReference.class))).thenReturn(Mono.just(emptyApiResponse));

        Mono<List<CocktailDto>> resultMono = cocktailService.searchByName("NonExistent");

        StepVerifier.create(resultMono)
                .expectErrorSatisfies(error -> {
                    assertThat(error).isInstanceOf(ResponseStatusException.class);
                    assertThat(((ResponseStatusException) error).getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
                })
                .verify();
    }
}