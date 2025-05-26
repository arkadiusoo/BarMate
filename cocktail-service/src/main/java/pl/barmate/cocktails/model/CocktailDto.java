package pl.barmate.cocktails.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * Data Transfer Object reprezentujący pełne informacje o drinku,
 * przekazywane do klienta API.
 */
@Data
@AllArgsConstructor
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAlcoholic() {
        return alcoholic;
    }

    public void setAlcoholic(String alcoholic) {
        this.alcoholic = alcoholic;
    }

    public String getGlass() {
        return glass;
    }

    public void setGlass(String glass) {
        this.glass = glass;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public List<String> getMeasures() {
        return measures;
    }

    public void setMeasures(List<String> measures) {
        this.measures = measures;
    }
}

