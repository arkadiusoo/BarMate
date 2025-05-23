package pl.barMate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import pl.barMate.model.AuthRequest;
import pl.barMate.service.KeycloakAdminService;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final KeycloakAdminService keycloakAdminService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody AuthRequest authRequest) {
        String keycloakId = keycloakAdminService.registerUser(authRequest);

        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
        RestTemplate restTemplate = new RestTemplate();
        var headers = new org.springframework.http.HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED);
        var body = new org.springframework.util.LinkedMultiValueMap<String, String>();
        body.add("grant_type", "password");
        body.add("client_id", "barmate-client");
        body.add("client_secret", "cAV1iZKdk25SREzjIdF1QPbtHC7TKVz6");
        body.add("username", authRequest.getUsername());
        body.add("password", authRequest.getPassword());

        var entity = new org.springframework.http.HttpEntity(body, headers);
        var tokenUrl = "http://localhost:8082/realms/barmate/protocol/openid-connect/token";
        var response = restTemplate.postForEntity(tokenUrl, entity, Map.class);

        return ResponseEntity.ok(response.getBody());
    }
}
