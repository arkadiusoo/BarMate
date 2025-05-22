package pl.barmate.analyticsservice.service;

public interface RecipeServiceClient {
    Object getMostPopularRecipies(Long userId);
    Object getMostPopularIngredients(Long userId);
    Object getConsuptionInTime(Long userId);
}
