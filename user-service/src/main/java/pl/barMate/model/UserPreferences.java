package pl.barMate.model;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Embeddable
public class UserPreferences {
    @ElementCollection
    private List<String> favoriteIngredients = new ArrayList<>();
    // List of recipes
    @ElementCollection
    private List<Long> favoriteRecipes = new ArrayList<>();
    private boolean nonAlcoholic;
}
