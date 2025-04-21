package pl.barMate.inventory.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class IngredientTest {

    @Test
    void ingredientBuilderShouldCreateCorrectObject() {
        // given
        Ingredient ingredient = Ingredient.builder()
                .id(1L)
                .name("Vodka")
                .category(IngredientCategory.ALCOHOL)
                .amount(500)
                .unit("ml")
                .build();

        // then
        assertThat(ingredient)
                .isNotNull()
                .extracting("id", "name", "category", "amount", "unit")
                .containsExactly(1L, "Vodka", IngredientCategory.ALCOHOL, 500.0, "ml");
    }
}
