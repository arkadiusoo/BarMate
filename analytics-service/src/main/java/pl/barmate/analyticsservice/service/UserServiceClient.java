package pl.barmate.analyticsservice.service;

public interface UserServiceClient {
    Object getMostPopularRecipies();
    Object getMostPopularIngredients();
    Object getConsuptionInTime();
}
