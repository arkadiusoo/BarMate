package pl.barmate.analyticsservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnalyticsService {

    private final DrinkStatsRepository drinkStatsRepository;
    private final IngredientUsageRepository ingredientUsageRepository;
    private final RestTemplate restTemplate;
    @Value("${services.recipe.url}")
    private String recipeServiceUrl;


     // Returns the total number of drinks prepared by the given user
    public long getTotalDrinksByUser(Long userId) {
        return drinkStatsRepository.findByUserId(userId).size();
    }


     // Returns the total number of drinks prepared globally in the given time period
    public long getDrinksPreparedBetween(LocalDateTime from, LocalDateTime to) {
        return drinkStatsRepository.findByPreparedAtBetween(from, to).size();
    }


     // Calculates total ingredient expenses by user in a time range
    public BigDecimal getTotalIngredientCostByUser(Long userId, LocalDate from, LocalDate to) {
        return ingredientUsageRepository.findByUserIdAndDateBetween(userId, from, to).stream()
                .map(IngredientUsage::getTotalCost)
                .filter(cost -> cost != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }


     // Returns total expenses grouped by ingredient type
    public Map<IngredientType, BigDecimal> getCostsByIngredientType(Long userId, LocalDate from, LocalDate to) {
        List<IngredientUsage> usageList = ingredientUsageRepository.findByUserIdAndDateBetween(userId, from, to);
        Map<IngredientType, BigDecimal> costMap = new EnumMap<>(IngredientType.class);

        for (IngredientUsage usage : usageList) {
            IngredientType type = usage.getIngredientType();
            BigDecimal cost = usage.getTotalCost() != null ? usage.getTotalCost() : BigDecimal.ZERO;
            costMap.merge(type, cost, BigDecimal::add);
        }

        return costMap;
    }


     // Returns the most recent drink preparations by user
    public List<DrinkStats> getRecentDrinks(Long userId, int limit) {
        return drinkStatsRepository.findByUserId(userId).stream()
                .sorted((a, b) -> b.getPreparedAt().compareTo(a.getPreparedAt()))
                .limit(limit)
                .toList();
    }
    // Prepares JSON input for Python chart generator
    public Map<String, Object> buildChartInput(String chartType, Long userId) {
        return switch (chartType) {
            case "activity" -> buildActivityChartInput(userId);
            case "top5" -> buildTop5ChartInput(userId);
            default -> throw new IllegalArgumentException("Unsupported chart type: " + chartType);
        };
    }

    private Map<String, Object> buildActivityChartInput(Long userId) {
        List<DrinkStats> stats = drinkStatsRepository.findByUserId(userId);

        Map<LocalDate, Long> grouped = stats.stream()
                .collect(Collectors.groupingBy(
                        s -> s.getPreparedAt().toLocalDate(),
                        TreeMap::new,
                        Collectors.counting()
                ));

        List<String> dates = grouped.keySet().stream()
                .map(LocalDate::toString)
                .toList();

        List<Long> counts = new ArrayList<>(grouped.values());

        return Map.of(
                "dates", dates,
                "counts", counts
        );
    }

    private Map<String, Object> buildTop5ChartInput(Long userId) {
        List<DrinkStats> stats = drinkStatsRepository.findByUserId(userId);

        Map<Long, Long> recipeCounts = stats.stream()
                .collect(Collectors.groupingBy(DrinkStats::getRecipeId, Collectors.counting()));

        List<Map.Entry<Long, Long>> top5 = recipeCounts.entrySet().stream()
                .sorted(Map.Entry.<Long, Long>comparingByValue().reversed())
                .limit(5)
                .toList();

        List<String> recipeNames = top5.stream()
                .map(entry -> getRecipeName(entry.getKey()))
                .toList();

        List<Long> preparationCounts = top5.stream()
                .map(Map.Entry::getValue)
                .toList();

        return Map.of(
                "recipeNames", recipeNames,
                "preparationCounts", preparationCounts
        );
    }

    private String getRecipeName(Long recipeId) {
        try {

            String url = recipeServiceUrl + "/recipes/" + recipeId;
            Map<?, ?> response = restTemplate.getForObject(url, Map.class);
            return response != null && response.containsKey("name")
                    ? response.get("name").toString()
                    : "Recipe #" + recipeId;
        } catch (Exception ex) {
            log.warn("Failed to fetch recipe {}: {}", recipeId, ex.getMessage());
            return "Recipe #" + recipeId;
        }
    }
}