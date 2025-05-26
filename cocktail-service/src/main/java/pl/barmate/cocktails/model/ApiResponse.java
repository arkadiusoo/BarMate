package pl.barmate.cocktails.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

/**
 * Ogólna struktura odpowiedzi z TheCocktailDB:
 * {
 *   "drinks": [ ... ]
 * }
 *
 * @param <T> typ elementu w liście drinks (np. Cocktail lub BriefCocktail)
 */
@Data
public class ApiResponse<T> {

    /** Lista obiektów z odpowiedzi */
    @JsonProperty("drinks")
    private List<T> drinks;

    public List<T> getDrinks() {
        return drinks;
    }

    public void setDrinks(List<T> drinks) {
        this.drinks = drinks;
    }

}