package pl.barmate.cocktails.model;

import lombok.Data;

import java.util.List;

/**
 * Data Transfer Object reprezentujący pełne informacje o drinku,
 * przekazywane do klienta API.
 */
@Data
public class CocktailDto {

    /** Unikalny identyfikator drinka */
    private String id;

    /** Nazwa drinka */
    private String name;

    /** Kategoria drinka (np. “Ordinary Drink”) */
    private String category;

    /** Czy alkoholowy (np. “Alcoholic” / “Non alcoholic”) */
    private String alcoholic;

    /** Typ szkła (np. “Cocktail glass”) */
    private String glass;

    /** Instrukcje przygotowania */
    private String instructions;

    /** URL obrazka */
    private String thumbnail;

    /** Lista składników (np. “Gin”, “Tonic Water”…) */
    private List<String> ingredients;

    /** Lista ilości/miar odpowiadających składnikom */
    private List<String> measures;

    public CocktailDto(String id, String name, String category, String alcoholic, String glass, String instructions, String thumbnail, List<String> ingredients, List<String> measures) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.alcoholic = alcoholic;
        this.glass = glass;
        this.instructions = instructions;
        this.thumbnail = thumbnail;
        this.ingredients = ingredients;
        this.measures = measures;
    }

}

