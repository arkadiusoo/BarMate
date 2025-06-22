package pl.barmate.analyticsservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class UserServiceClientImpl implements UserServiceClient {
    private final RestTemplate restTemplate;

    private final String baseUrl = "http://localhost:8080/user-service/me";

    @Override
    public Object getMostPopularRecipies() {
        return restTemplate.getForObject(baseUrl + "/getDrinkNames", Object.class);
//        return List.of("Mojito", "Margarita", "Mojito", "Martini", "Margarita");
    }

    @Override
    public Object getMostPopularIngredients() {
        return restTemplate.getForObject(baseUrl + "/getIngredients", Object.class);
//        return List.of("Lime", "Mint", "Lime", "Rum", "Mint");
    }

    @Override
    public Object getConsuptionInTime() {
        return restTemplate.getForObject(baseUrl + "/getDateList", Object.class);
//        return Map.of(
//                "Mojito", "2024-06-01",
//                "Margarita", "2024-06-02",
//                "Martini", "2024-06-03"
//        );
    }
}