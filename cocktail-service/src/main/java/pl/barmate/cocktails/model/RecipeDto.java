package pl.barmate.cocktails.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
public class RecipeDto {

    /** Nazwa drinka */
    private String name;

    /** Rodzaj szkła */
    private String glass;

    /** Lista składników wraz z miarami */
    private List<IngredientMeasure> ingredients;

    /** Szczegółowe instrukcje przygotowania */
    private String instructions;


    public RecipeDto() { }

    public RecipeDto(String name, String glass, List<IngredientMeasure> ingredients, String instructions) {
        this.name = name;
        this.glass = glass;
        this.ingredients = ingredients;
        this.instructions = instructions;
    }

    @Setter
    @Getter
    public static class IngredientMeasure {
        private String name;
        private String measure;

        public IngredientMeasure() { }

        public IngredientMeasure(String name, String measure) {
            this.name = name;
            this.measure = measure;
        }

    }
}
