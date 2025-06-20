package pl.barmate.cocktails.model;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @JsonIgnore
    private final Map<String,String> rawIngredients = new LinkedHashMap<>();

    @JsonIgnore
    private final Map<String,String> rawMeasures = new LinkedHashMap<>();

    @JsonAnySetter
    public void handleUnknown(String key, Object value) {
        if (value == null) return;
        String v = value.toString().trim();
        if (key.startsWith("strIngredient") && !v.isBlank()) {
            rawIngredients.put(key, v);
        } else if (key.startsWith("strMeasure") && !v.isBlank()) {
            rawMeasures.put(key, v);
        }
    }

    @JsonIgnore
    public List<String> getIngredients() {
        return rawIngredients.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }

    @JsonIgnore
    public List<String> getMeasures() {
        return rawMeasures.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }
}
