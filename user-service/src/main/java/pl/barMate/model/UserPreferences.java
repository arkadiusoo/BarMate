package pl.barMate.model;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Embeddable
public class UserPreferences {
    private List<Long> favoriteIngredients;
    // List of recipes
    private List<Long> favoriteRecipes;
    private boolean nonAlcoholic;
}
