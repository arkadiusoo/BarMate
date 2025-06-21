package pl.barmate.cocktails.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Reprezentuje podstawowe informacje o drinku:
 * – używane przy filtrowaniu (filter.php) i listach.
 */
@Data
public class BriefCocktail {

    /** Unikalny identyfikator drinka */
    @JsonProperty("idDrink")
    private String id;

    /** Nazwa drinka */
    @JsonProperty("strDrink")
    private String name;

    /** URL miniaturki obrazka drinka */
    @JsonProperty("strDrinkThumb")
    private String thumbnail;

}

