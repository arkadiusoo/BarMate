package pl.barmate.analyticsservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RecipeServiceClientImpl implements RecipeServiceClient {
//TODO: dodać try do komunikacji z user-service
    private final RestTemplate restTemplate;

    private final String baseUrl = "http://recipe-service/api";

    @Override
    public Object getMostPopularRecipies(Long userId) {
//        return restTemplate.getForObject(baseUrl + "/recipes/popularDrinks?userId=" + userId, Object.class);
        return List.of("Mojito", "Margarita", "Mojito", "Martini", "Margarita");
    }

    @Override
    public Object getMostPopularIngredients(Long userId) {
//        return restTemplate.getForObject(baseUrl + "/recipes/popularIngredients?userId=" + userId, Object.class);
        return List.of("Lime", "Mint", "Lime", "Rum", "Mint");
    }

    @Override
    public Object getConsuptionInTime(Long userId) {
//        return restTemplate.getForObject(baseUrl + "/recipes/consumption?userId=" + userId, Object.class);
        return Map.of(
                "Mojito", "2024-06-01",
                "Margarita", "2024-06-02",
                "Martini", "2024-06-03"
        );
    }
}