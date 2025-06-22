package pl.barmate.analyticsservice.service;

public interface RecipeServiceClient {
    Object getMostPopularRecipies();
    Object getMostPopularIngredients();
    Object getConsuptionInTime();
}
