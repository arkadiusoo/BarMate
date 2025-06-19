package pl.barmate.analyticsservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
@Tag(name = "ChartService", description = "Operations related to generating charts: creating, listing history and regenerating charts from history.")
public class ChartController {

    private final ChartService chartService;

    @Operation(
        summary = "Generate chart from user's data.",
        description = "Returns image with plot based on user's data from user-service."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Returns generated plot successfully."),
        @ApiResponse(responseCode = "404", description = "User with such id not found."),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })

    @GetMapping(value = "/generate", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> generateNewChart(
            @RequestParam ChartType chartType,
            @RequestParam Long userId) {
        try {
            byte[] image = chartService.generateChart(chartType, userId);
            return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(image);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(404).build();
        } catch (Exception ex) {
            return ResponseEntity.status(500).build();
        }


    }

    @Operation(
        summary = "Regenerate chart from user's data.",
        description = "Returns image with plot based on previously saved user's data from user-service."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Returns generated plot successfully."),
        @ApiResponse(responseCode = "404", description = "Record with such id not found."),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })

    @GetMapping(value = "/regenerate/{chartId}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> regenerateChart(@PathVariable Long chartId) {
        try {
            byte[] image = chartService.regenerateChartFromHistory(chartId);
            return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(image);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(404).build();
        } catch (Exception ex) {
            return ResponseEntity.status(500).build();
        }

    }

    @Operation(
        summary = "Get all user's summaries.",
        description = "Returns array with previously saved user's data from user-service."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "List of data retrieved successfully."),
        @ApiResponse(responseCode = "404", description = "User with such id not found."),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/history")
    public ResponseEntity<List<ChartHistoryDTO>> getUserHistory(@RequestParam Long userId) {
        try {
            return ResponseEntity.ok(chartService.getUserChartHistory(userId));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(404).build();
        } catch (Exception ex) {
            return ResponseEntity.status(500).build();
        }

}
}
