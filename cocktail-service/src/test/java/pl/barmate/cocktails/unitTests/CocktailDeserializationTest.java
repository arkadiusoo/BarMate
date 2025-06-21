package pl.barmate.cocktails.unitTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import pl.barmate.cocktails.model.Cocktail;

import static org.assertj.core.api.Assertions.assertThat;

class CocktailDeserializationTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldCorrectlyDeserializeIngredientsAndMeasures() throws Exception {
        // Given
        String jsonResponse = """
        {
          "idDrink": "11007",
          "strDrink": "Margarita",
          "strIngredient1": "Tequila",
          "strMeasure1": "2 oz",
          "strIngredient2": "Triple Sec",
          "strMeasure2": "1 oz",
          "strIngredient3": "Lime Juice",
          "strMeasure3": "1 oz",
          "strIngredient4": null,
          "strMeasure4": ""
        }
        """;

        Cocktail cocktail = objectMapper.readValue(jsonResponse, Cocktail.class);

        assertThat(cocktail.getId()).isEqualTo("11007");
        assertThat(cocktail.getIngredients()).containsExactly("Tequila", "Triple Sec", "Lime Juice");
        assertThat(cocktail.getMeasures()).containsExactly("2 oz", "1 oz", "1 oz");
    }
}
