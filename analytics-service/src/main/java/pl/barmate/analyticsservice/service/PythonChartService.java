package pl.barmate.analyticsservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class PythonChartService {

    private final RestTemplate restTemplate;

    @Value("${services.chart.url}")
    private String chartServiceBaseUrl;

    public byte[] generateChart(String chartType, Map<String, Object> data) {
        String url = chartServiceBaseUrl + "/generate?type=" + chartType;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(data, headers);

        ResponseEntity<byte[]> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                request,
                byte[].class
        );

        return response.getBody();
    }
}