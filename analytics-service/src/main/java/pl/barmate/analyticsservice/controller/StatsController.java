package pl.barmate.analyticsservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;
import pl.barmate.analyticsservice.model.DrinkStats;
import pl.barmate.analyticsservice.model.IngredientType;
import pl.barmate.analyticsservice.service.AnalyticsService;
import pl.barmate.analyticsservice.service.PythonChartService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/stats")
@RequiredArgsConstructor
@Tag(name = "Statistics", description = "Drink and ingredient usage analytics")
public class StatsController {

    private final AnalyticsService analyticsService;
    private final PythonChartService pythonChartService;

    @Operation(summary = "Get total drinks in time period")
    @GetMapping("/overview")
    public ResponseEntity<Map<String, Object>> getGlobalOverview(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {

        long totalDrinks = analyticsService.getDrinksPreparedBetween(from, to);

        return ResponseEntity.ok(Map.of(
                "totalDrinks", totalDrinks,
                "from", from,
                "to", to
        ));
    }

    @Operation(summary = "Get summary statistics for a specific user")
    @GetMapping("/user/{userId}")
    public ResponseEntity<Map<String, Object>> getUserStats(
            @PathVariable Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {

        long totalDrinks = analyticsService.getTotalDrinksByUser(userId);
        BigDecimal totalCost = analyticsService.getTotalIngredientCostByUser(userId, from, to);
        Map<IngredientType, BigDecimal> costsByType = analyticsService.getCostsByIngredientType(userId, from, to);

        return ResponseEntity.ok(Map.of(
                "userId", userId,
                "totalDrinks", totalDrinks,
                "totalIngredientCost", totalCost,
                "costsByIngredientType", costsByType,
                "period", Map.of("from", from, "to", to)
        ));
    }

    @Operation(summary = "Get detailed ingredient cost breakdown by type")
    @GetMapping("/user/{userId}/costs")
    public ResponseEntity<Map<IngredientType, BigDecimal>> getIngredientCosts(
            @PathVariable Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {

        return ResponseEntity.ok(analyticsService.getCostsByIngredientType(userId, from, to));
    }

    @Operation(summary = "Get recent drinks prepared by user")
    @GetMapping("/user/{userId}/recent")
    public ResponseEntity<List<DrinkStats>> getRecentDrinks(@PathVariable Long userId,
                                                            @RequestParam(defaultValue = "5") int limit) {
        return ResponseEntity.ok(analyticsService.getRecentDrinks(userId, limit));
    }
    @Operation(summary = "Generate a chart based on the selected type")
    @GetMapping(value = "/charts", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> getChart(
            @RequestParam String type,
            @RequestParam Long userId) {

        // TODO: pobierz dane z recipe-service i przygotuj dane wejściowe
        Map<String, Object> chartInput = analyticsService.buildChartInput(type, userId);

        byte[] image = pythonChartService.generateChart(type, chartInput);
        return ResponseEntity.ok(image);
    }
}