package pl.barmate.analyticsservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class UserServiceClientImpl implements UserServiceClient {
    private final RestTemplate restTemplate;

    private final String baseUrl = "http://localhost:8080/user-service/me";

    @Override
    public Object getMostPopularRecipies(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<Object> response = restTemplate.exchange(
                baseUrl + "/getDrinkNames",
                HttpMethod.GET,
                entity,
                Object.class
        );

        return response.getBody();
    }

    @Override
    public Object getMostPopularIngredients(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<Object> response = restTemplate.exchange(
                baseUrl + "/getDateList",
                HttpMethod.GET,
                entity,
                Object.class
        );
        return restTemplate.getForObject(baseUrl + "/getIngredients", Object.class);
//        return List.of("Lime", "Mint", "Lime", "Rum", "Mint");
    }

    @Override
    public Object getConsuptionInTime(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token); // Authorization: Bearer <token>

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<Object> response = restTemplate.exchange(
                baseUrl + "/getDateList",
                HttpMethod.GET,
                entity,
                Object.class
        );

        return response.getBody();
//        return Map.of(
//                "Mojito", "2024-06-01",
//                "Margarita", "2024-06-02",
//                "Martini", "2024-06-03"
//        );
    }
}