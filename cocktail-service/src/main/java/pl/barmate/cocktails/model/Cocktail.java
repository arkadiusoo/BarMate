package pl.barmate.cocktails.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Reprezentuje pełne dane drinka pobrane z TheCocktailDB.
 */
@Getter
@Data
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Cocktail {

    /** Unikalny identyfikator drinka */
    @JsonProperty("idDrink")
    private String id;

    /** Nazwa drinka */
    @JsonProperty("strDrink")
    private String name;

    /** Kategoria (np. „Ordinary Drink”) */
    @JsonProperty("strCategory")
    private String category;

    /** Typ alkoholowy (np. „Alcoholic”/„Non alcoholic”) */
    @JsonProperty("strAlcoholic")
    private String alcoholic;

    /** Rodzaj szkła (np. „Cocktail glass”) */
    @JsonProperty("strGlass")
    private String glass;

    /** Instrukcje przygotowania drinka */
    @JsonProperty("strInstructions")
    private String instructions;

    /** URL obrazka drinka */
    @JsonProperty("strDrinkThumb")
    private String thumbnail;

    private List<String> ingredients;

    private List<String> measures;

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
