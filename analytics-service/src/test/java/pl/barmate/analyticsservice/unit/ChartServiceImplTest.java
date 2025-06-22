package pl.barmate.analyticsservice.unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.barmate.analyticsservice.model.Chart;
import pl.barmate.analyticsservice.model.ChartType;
import pl.barmate.analyticsservice.repository.ChartRepository;
import pl.barmate.analyticsservice.service.ChartServiceImpl;
import pl.barmate.analyticsservice.service.PythonChartService;
import pl.barmate.analyticsservice.service.RecipeServiceClient;
import pl.barmate.analyticsservice.dto.ChartHistoryDTO;
import pl.barmate.analyticsservice.dto.ChartMapper;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

public class ChartServiceImplTest {

    @Mock
    private RecipeServiceClient recipeServiceClient;

    @Mock
    private PythonChartService pythonChartService;

    @Mock
    private ChartRepository chartRepository;

    @InjectMocks
    private ChartServiceImpl chartService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldGenerateChartAndSave1() {
        Long userId = 1L;
        ChartType type = ChartType.TheMostPopularRecipies;
        List<String> mockData = List.of("drink1", "drink2");
        byte[] expectedChart = new byte[]{1, 2, 3};

        when(recipeServiceClient.getMostPopularRecipies()).thenReturn(mockData);
        when(pythonChartService.generateChart(eq(type), eq(mockData))).thenReturn(expectedChart);

        byte[] result = chartService.generateChart(type, userId);

        assertThat(result).isEqualTo(expectedChart);
        verify(chartRepository).save(any(Chart.class));
    }

    @Test
    void shouldGenerateChartAndSave2() {
        Long userId = 1L;
        ChartType type = ChartType.TheMostPopularIngredients;
        List<String> mockData = List.of("drink1", "drink2");
        byte[] expectedChart = new byte[]{1, 2, 3};

        when(recipeServiceClient.getMostPopularRecipies()).thenReturn(mockData);
        when(pythonChartService.generateChart(eq(type), eq(mockData))).thenReturn(expectedChart);

        byte[] result = chartService.generateChart(type, userId);

        assertThat(result).isEqualTo(null);
        verify(chartRepository).save(any(Chart.class));
    }

    @Test
    void shouldGenerateChartAndSave3() {
        Long userId = 1L;
        ChartType type = ChartType.ConsuptionInTime;
        List<String> mockData = List.of("drink1", "drink2");
        byte[] expectedChart = new byte[]{1, 2, 3};

        when(recipeServiceClient.getMostPopularRecipies()).thenReturn(mockData);
        when(pythonChartService.generateChart(eq(type), eq(mockData))).thenReturn(expectedChart);

        byte[] result = chartService.generateChart(type, userId);

        assertThat(result).isEqualTo(null);
        verify(chartRepository).save(any(Chart.class));
    }

    @Test
    void shouldThrowExceptionForUnsupportedChartType() {
        assertThatThrownBy(() -> chartService.generateChart(null, 1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Chart type must be provided");
    }

    @Test
    void shouldRegenerateChartFromHistory() {
        Long chartId = 123L;
        Chart chart = Chart.builder()
                .id(chartId)
                .userId(1L)
                .chartType(ChartType.TheMostPopularIngredients)
                .chartData("[\"Sugar\",\"Lemon\"]")
                .build();

        when(chartRepository.findById(chartId)).thenReturn(Optional.of(chart));
        when(pythonChartService.generateChart(eq(chart.getChartType()), any()))
                .thenReturn(new byte[]{42});

        byte[] result = chartService.regenerateChartFromHistory(chartId);

        assertThat(result).isEqualTo(new byte[]{42});
    }

    @Test
    void shouldThrowExceptionWhenChartNotFound() {
        when(chartRepository.findById(404L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> chartService.regenerateChartFromHistory(404L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Chart not found");
    }

    @Test
    void shouldReturnUserChartHistory() {
        Long userId = 1L;
        Chart chart = Chart.builder()
                .id(10L)
                .userId(userId)
                .chartName("name")
                .chartType(ChartType.ConsuptionInTime)
                .created(new Date())
                .build();

        when(chartRepository.findAllByUserId(userId)).thenReturn(List.of(chart));

        List<ChartHistoryDTO> result = chartService.getUserChartHistory(userId);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).id()).isEqualTo(10L);
    }

    @Test
    void shouldThrowExceptionWhenUserHasNoCharts() {
        when(chartRepository.findAllByUserId(77L)).thenReturn(Collections.emptyList());

        assertThatThrownBy(() -> chartService.getUserChartHistory(77L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("User with id 77 not found or has no charts");
    }

    @Test
    void shouldDeserializeConsumptionInTimeChart() {
        Long chartId = 456L;
        Chart chart = Chart.builder()
                .id(chartId)
                .userId(1L)
                .chartType(ChartType.ConsuptionInTime)
                .chartData("{\"2024-01-01\": \"5\", \"2024-01-02\": \"6\"}")
                .build();

        when(chartRepository.findById(chartId)).thenReturn(Optional.of(chart));
        when(pythonChartService.generateChart(eq(chart.getChartType()), any()))
                .thenReturn(new byte[]{10, 20, 30});

        byte[] result = chartService.regenerateChartFromHistory(chartId);

        assertThat(result).isEqualTo(new byte[]{10, 20, 30});
    }
}
