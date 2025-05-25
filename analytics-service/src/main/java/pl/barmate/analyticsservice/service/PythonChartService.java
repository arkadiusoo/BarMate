package pl.barmate.analyticsservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import pl.barmate.analyticsservice.model.ChartType;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class PythonChartService {

    private final RestTemplate restTemplate;

    @Value("${services.chart.url}")
    private String chartServiceBaseUrl;

    public byte[] generateChart(ChartType chartType, Object chartData) {
        String url = chartServiceBaseUrl + "/generate";

        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("chartType", chartType.name()); // używamy dokładnej nazwy enuma
        requestMap.put("data", chartData);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestMap, headers);

        try {
            ResponseEntity<byte[]> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    request,
                    byte[].class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            } else {
                log.error("Python chart service returned status: {}", response.getStatusCode());
                throw new RuntimeException("Chart generation failed with status: " + response.getStatusCode());
            }
        } catch (RestClientException ex) {
            log.error("Failed to call Python chart service: {}", ex.getMessage());
            throw new RuntimeException("Python chart service error", ex);
        }
    }
}