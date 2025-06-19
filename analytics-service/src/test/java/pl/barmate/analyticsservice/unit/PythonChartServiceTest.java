package pl.barmate.analyticsservice.unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import pl.barmate.analyticsservice.model.ChartType;
import pl.barmate.analyticsservice.service.PythonChartService;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class PythonChartServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private PythonChartService pythonChartService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // bezpośrednio ustawiamy URL serwisu
        pythonChartService = new PythonChartService(restTemplate);
        // ręcznie ustawiamy wartość pola przez refleksję
        try {
            var field = PythonChartService.class.getDeclaredField("chartServiceBaseUrl");
            field.setAccessible(true);
            field.set(pythonChartService, "http://mock-chart-service");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void shouldReturnChartDataWhenSuccessful() {
        byte[] expectedData = new byte[]{1, 2, 3};
        ResponseEntity<byte[]> responseEntity = new ResponseEntity<>(expectedData, HttpStatus.OK);

        when(restTemplate.exchange(
                eq("http://mock-chart-service/generate"),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(byte[].class))
        ).thenReturn(responseEntity);

        byte[] result = pythonChartService.generateChart(ChartType.ConsuptionInTime, List.of("mock"));

        assertThat(result).isEqualTo(expectedData);
    }

    @Test
    void shouldThrowExceptionWhenNon2xxReturned() {
        ResponseEntity<byte[]> responseEntity = new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);

        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(byte[].class))
        ).thenReturn(responseEntity);

        assertThatThrownBy(() ->
                pythonChartService.generateChart(ChartType.ConsuptionInTime, Map.of())
        ).isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Chart generation failed with status");
    }

    @Test
    void shouldThrowExceptionWhenRestClientFails() {
        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(byte[].class))
        ).thenThrow(new RestClientException("Connection error"));

        assertThatThrownBy(() ->
                pythonChartService.generateChart(ChartType.ConsuptionInTime, Map.of())
        ).isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Python chart service error");
    }
}
