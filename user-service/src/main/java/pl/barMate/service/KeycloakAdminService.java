package pl.barMate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pl.barMate.model.AuthRequest;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class KeycloakAdminService {

    public String registerUser(AuthRequest authRequest) {
        String url = "http://localhost:8082/admin/realms/barmate/users";
        String adminToken = getAdminAccessToken();
        Map<String, Object> user = Map.of(
                "username", authRequest.getUsername(),
                "email", authRequest.getEmail(),
                "enabled", true,
                "credentials", new Object[] {
                        Map.of(
                                "type", "password",
                                "value", authRequest.getPassword(),
                                "temporary", false
                        )
                }
        );

        RestTemplate restTemplate = new RestTemplate();
        var headers = new org.springframework.http.HttpHeaders();
        headers.setBearerAuth(adminToken);
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);

        var entity = new org.springframework.http.HttpEntity<>(user, headers);

        ResponseEntity<Void> response = restTemplate.postForEntity(url, entity, Void.class);
        String locationHeader = response.getHeaders().getFirst("Location");
        String keycloakUserId = locationHeader.substring(locationHeader.lastIndexOf('/') + 1);
        return keycloakUserId;
    }

    private String getAdminAccessToken() {
        String tokenUrl = "http://localhost:8082/realms/master/protocol/openid-connect/token";

        RestTemplate restTemplate = new RestTemplate();
        var headers = new org.springframework.http.HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED);

        var body = new org.springframework.util.LinkedMultiValueMap<String, String>();
        body.add("grant_type", "password");
        body.add("client_id", "admin-cli");
        body.add("username", "admin");
        body.add("password", "admin");

        var entity = new org.springframework.http.HttpEntity<>(body, headers);
        var response = restTemplate.postForEntity(tokenUrl, entity, Map.class);
        return (String) response.getBody().get("access_token");
    }
}
