package pl.barmate.cocktails.unitTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import pl.barmate.cocktails.model.RequestedIngredientDto;
import pl.barmate.cocktails.service.CocktailService;
import pl.barmate.cocktails.service.TranslationService;

import java.lang.reflect.Method;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class CocktailServiceTest {

    // Mockujemy zależności, chociaż w tym teście ich nie użyjemy
    @Mock private WebClient mockCocktailDbClient;
    @Mock private WebClient mockInventoryClient;
    @Mock private TranslationService mockTranslationService;

    private CocktailService cocktailService;

    @BeforeEach
    void setUp() {
        cocktailService = new CocktailService(mockCocktailDbClient, mockInventoryClient, mockTranslationService);
    }

    // Używamy refleksji, aby przetestować prywatną metodę
    private List<RequestedIngredientDto> invokeParseIngredients(List<String> ingredients, List<String> measures) throws Exception {
        Method method = CocktailService.class.getDeclaredMethod("parseIngredients", List.class, List.class);
        method.setAccessible(true);
        return (List<RequestedIngredientDto>) method.invoke(cocktailService, ingredients, measures);
    }

    @Test
    void parseIngredients_shouldCorrectlyParseValues() throws Exception {
        // Given
        List<String> ingredients = List.of("Tequila", "Sok z limonki", "Syrop cukrowy");
        List<String> measures = List.of("60 ml", "Sok z połówki", "15 ml");

        // When
        List<RequestedIngredientDto> result = invokeParseIngredients(ingredients, measures);

        // Then
        assertThat(result).hasSize(3);
        assertThat(result).containsExactlyInAnyOrder(
                new RequestedIngredientDto("Tequila", 60.0),
                new RequestedIngredientDto("Sok z limonki", 1.0),
                new RequestedIngredientDto("Syrop cukrowy", 15.0)
        );
    }
}
