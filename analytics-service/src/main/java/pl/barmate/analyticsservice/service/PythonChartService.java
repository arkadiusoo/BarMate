package pl.barmate.analyticsservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class PythonChartService {

    private final RestTemplate restTemplate;

    private static final String CHART_SERVICE_URL = "http://chart-service/generate?type={chartType}";

    /**
     * Sends JSON data to Python chart generator and receives chart as PNG.
     *
     * @param chartType type of chart: "activity", "top5", etc.
     * @param data      JSON data required to generate the chart
     * @return PNG image as byte array
     */
    public byte[] generateChart(String chartType, Map<String, Object> data) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(data, headers);

        ResponseEntity<byte[]> response = restTemplate.exchange(
                CHART_SERVICE_URL,
                HttpMethod.POST,
                request,
                byte[].class,
                chartType
        );

        return response.getBody();
    }
}