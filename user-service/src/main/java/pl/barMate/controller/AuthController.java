package pl.barMate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import pl.barMate.model.AuthRequest;
import pl.barMate.repository.UserProfileRepository;
import pl.barMate.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Base64;

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

    final private UserProfileRepository userProfileRepository;

    final private UserService userService;

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

        try {
            var response = restTemplate.postForEntity(tokenUrl, entity, Map.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                String accessToken = (String) response.getBody().get("access_token");
                Map<String, Object> claims = decodeJwtPayload(accessToken);
                String email = (String) claims.get("email");
                String username = (String) claims.get("preferred_username");
                if (!userProfileRepository.existsByUsername(username)) {
                    userService.addUserProfile(username, email);
                }
                return ResponseEntity.ok(response.getBody());
            } else {
                return ResponseEntity.status(response.getStatusCode()).body("Błąd logowania");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Map<String, Object> decodeJwtPayload(String jwtToken) throws IOException {
        String[] chunks = jwtToken.split("\\.");
        Base64.Decoder decoder = Base64.getUrlDecoder();
        String payload = new String(decoder.decode(chunks[1]));
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(payload, Map.class);
    }
}
