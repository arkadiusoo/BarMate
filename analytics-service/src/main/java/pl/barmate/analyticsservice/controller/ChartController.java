package pl.barmate.analyticsservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.barmate.analyticsservice.dto.ChartHistoryDTO;
import pl.barmate.analyticsservice.model.ChartType;
import pl.barmate.analyticsservice.service.ChartService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/charts")
public class ChartController {

    private final ChartService chartService;

    @GetMapping(value = "/generate", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> generateNewChart(
            @RequestParam ChartType chartType,
            @RequestParam Long userId) {

        byte[] image = chartService.generateChart(chartType, userId);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(image);
    }

    @GetMapping(value = "/regenerate/{chartId}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> regenerateChart(@PathVariable Long chartId) {
        byte[] image = chartService.regenerateChartFromHistory(chartId);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(image);
    }

    @GetMapping("/history")
    public ResponseEntity<List<ChartHistoryDTO>> getUserHistory(@RequestParam Long userId) {
    return ResponseEntity.ok(chartService.getUserChartHistory(userId));
}
}
