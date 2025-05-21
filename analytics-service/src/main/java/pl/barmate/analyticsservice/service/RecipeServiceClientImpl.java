package pl.barmate.analyticsservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class RecipeServiceClientImpl implements RecipeServiceClient {

    private final RestTemplate restTemplate;

    private final String baseUrl = "http://recipe-service/api";

    @Override
    public Object getMostPopularRecipies(Long userId) {
        return restTemplate.getForObject(baseUrl + "/recipes/popular?userId=" + userId, Object.class);
    }

    @Override
    public Object getMostPopularIngredients(Long userId) {
        return restTemplate.getForObject(baseUrl + "/ingredients/popular?userId=" + userId, Object.class);
    }

    @Override
    public Object getConsuptionInTime(Long userId) {
        return restTemplate.getForObject(baseUrl + "/stats/consumption?userId=" + userId, Object.class);
    }
}