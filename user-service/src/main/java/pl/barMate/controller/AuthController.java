package pl.barMate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import pl.barMate.model.AuthRequest;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    @Value("${keycloak.client-id}")
    private String clientId;

    @Value("${keycloak.client-secret}")
    private String clientSecret;

    @Value("${keycloak.token-uri}")
    private String tokenUrl;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
        RestTemplate restTemplate = new RestTemplate();
        var headers = new org.springframework.http.HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED);
        var body = new org.springframework.util.LinkedMultiValueMap<String, String>();
        body.add("grant_type", "password");
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("username", authRequest.getUsername());
        body.add("password", authRequest.getPassword());
        System.out.println(body);
        var entity = new org.springframework.http.HttpEntity(body, headers);
        var response = restTemplate.postForEntity(tokenUrl, entity, Map.class);

        return ResponseEntity.ok(response.getBody());
    }
}
