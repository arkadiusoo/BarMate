package pl.barmate.analyticsservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.barmate.analyticsservice.dto.ChartHistoryDTO;
import pl.barmate.analyticsservice.dto.ChartMapper;
import pl.barmate.analyticsservice.model.Chart;
import pl.barmate.analyticsservice.model.ChartType;
import pl.barmate.analyticsservice.repository.ChartRepository;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ChartServiceImpl implements ChartService {

    private final RecipeServiceClient recipeServiceClient;
    private final PythonChartService pythonChartService;
    private final ChartRepository chartRepository;

    @Override
    public byte[] generateChart(ChartType chartType, Long userId) {
        // 1. get data from recipe-service
        Object chartInputData = switch (chartType) {
            case TheMostPopularRecipies -> recipeServiceClient.getMostPopularRecipies(userId);
            case TheMostPopularIngredients -> recipeServiceClient.getMostPopularIngredients(userId);
            case ConsuptionInTime -> recipeServiceClient.getConsuptionInTime(userId);
            default -> throw new IllegalArgumentException("Unsupported chart type: " + chartType);
        };

        // 2. serialize data to json
        String jsonData = serializeToJson(chartInputData);

        // 3. save chart to db
        Chart chart = Chart.builder()
                .userId(userId)
                .chartType(chartType)
                .chartName("Wykres " + chartType.name())
                .chartData(jsonData)
                .created(new Date())
                .build();

        chartRepository.save(chart);

        // 4. generate chart
        return pythonChartService.generateChart(chartType, chartInputData);
    }

    @Override
    public byte[] regenerateChartFromHistory(Long chartId) {
        Chart chart = chartRepository.findById(chartId)
                .orElseThrow(() -> new IllegalArgumentException("Chart not found: " + chartId));

        // 1. pars json
        Object chartData = deserializeFromJson(chart.getChartData(), chart.getChartType());

        // 2. upload to python chart service
        return pythonChartService.generateChart(chart.getChartType(), chartData);
    }

    // ObjectkMapper
    private String serializeToJson(Object data) {
        try {
            return new ObjectMapper().writeValueAsString(data);
        } catch (Exception e) {
            throw new RuntimeException("JSON serialization error", e);
        }
    }

private Object deserializeFromJson(String json, ChartType chartType) {
    try {
        ObjectMapper mapper = new ObjectMapper();
        return switch (chartType) {
            case TheMostPopularRecipies, TheMostPopularIngredients ->
                mapper.readValue(json, new com.fasterxml.jackson.core.type.TypeReference<List<String>>() {});
            case ConsuptionInTime ->
                mapper.readValue(json, new com.fasterxml.jackson.core.type.TypeReference<Map<String, String>>() {});
        };
    } catch (Exception e) {
        throw new RuntimeException("JSON deserialization error", e);
    }
}

    public List<ChartHistoryDTO> getUserChartHistory(Long userId) {
    return chartRepository.findAllByUserId(userId).stream()
            .map(ChartMapper::toHistoryDTO)
            .toList();
}
}