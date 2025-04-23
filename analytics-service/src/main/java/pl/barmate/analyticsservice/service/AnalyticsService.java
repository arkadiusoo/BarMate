package pl.barmate.analyticsservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.barmate.analyticsservice.model.DrinkStats;
import pl.barmate.analyticsservice.model.IngredientUsage;
import pl.barmate.analyticsservice.model.IngredientType;
import pl.barmate.analyticsservice.repository.DrinkStatsRepository;
import pl.barmate.analyticsservice.repository.IngredientUsageRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final DrinkStatsRepository drinkStatsRepository;
    private final IngredientUsageRepository ingredientUsageRepository;

    
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
}