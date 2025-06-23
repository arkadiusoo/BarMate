package pl.barmate.analyticsservice.service;

public interface UserServiceClient {
    Object getMostPopularRecipies(String token);
    Object getMostPopularIngredients(String token);
    Object getConsuptionInTime(String token);
}
