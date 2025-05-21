package pl.barmate.analyticsservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.barmate.analyticsservice.model.ChartType;

@Service
@RequiredArgsConstructor
public class ChartServiceImpl implements ChartService {

    private final RecipeServiceClient recipeServiceClient;
    private final PythonChartService pythonChartService;

    @Override
    public byte[] generateChart(ChartType chartType, Long userId) {

        Object chartInputData = switch (chartType) {
            case TheMostPopularRecipies -> recipeServiceClient.getMostPopularRecipies(userId);
            case TheMostPopularIngredients -> recipeServiceClient.getMostPopularIngredients(userId);
            case ConsuptionInTime -> recipeServiceClient.getConsuptionInTime(userId);
            default -> throw new IllegalArgumentException("Unsupported chart type: " + chartType);
        };

        return pythonChartService.generateChart(chartType, chartInputData);
    }
}
