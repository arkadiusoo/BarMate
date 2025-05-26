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

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}

