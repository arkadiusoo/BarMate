package pl.barmate.analyticsservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import pl.barmate.analyticsservice.dto.ChartHistoryDTO;
import pl.barmate.analyticsservice.model.ChartType;
import pl.barmate.analyticsservice.service.ChartService;
import pl.barmate.analyticsservice.service.UserServiceClient;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/charts")
@Tag(name = "ChartService", description = "Operations related to generating charts: creating, listing history and regenerating charts from history.")
public class ChartController {

    private final ChartService chartService;
    private final UserServiceClient userService;

    @Operation(
        summary = "Generate chart from user's data.",
        description = "Returns image with plot based on user's data from user-service."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Returns generated plot successfully.",
            content = @io.swagger.v3.oas.annotations.media.Content(mediaType = MediaType.IMAGE_PNG_VALUE)
        ),
        @ApiResponse(
            responseCode = "404",
            description = "User with such id not found.",
            content = @io.swagger.v3.oas.annotations.media.Content
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @io.swagger.v3.oas.annotations.media.Content
        )
    })

    @GetMapping(value = "/generate", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> generateNewChart(
            @AuthenticationPrincipal Jwt jwt, @RequestParam ChartType chartType) {
        try {
            String token = jwt.getTokenValue();
            String username = jwt.getClaimAsString("preferred_username");
            byte[] image = chartService.generateChart(chartType, token, username);
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
        @ApiResponse(
            responseCode = "200",
            description = "Returns generated plot successfully.",
            content = @io.swagger.v3.oas.annotations.media.Content(mediaType = MediaType.IMAGE_PNG_VALUE)
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Record with such id not found.",
            content = @io.swagger.v3.oas.annotations.media.Content
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @io.swagger.v3.oas.annotations.media.Content
        )
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
        @ApiResponse(
                responseCode = "200",
                description = "List of data retrieved successfully.",
                content = @io.swagger.v3.oas.annotations.media.Content(mediaType = MediaType.IMAGE_PNG_VALUE)
        ),
        @ApiResponse(
                responseCode = "404",
                description = "User with such id not found.",
                content = @io.swagger.v3.oas.annotations.media.Content
        ),
        @ApiResponse(
                responseCode = "500",
                description = "Internal server error",
                content = @io.swagger.v3.oas.annotations.media.Content
        )
    })
    @GetMapping("/history")
    public ResponseEntity<List<ChartHistoryDTO>> getUserHistory(@AuthenticationPrincipal Jwt jwt, @RequestParam Long userId) {
        String userName = jwt.getClaimAsString("preferred_username");
        try {
            return ResponseEntity.ok(chartService.getUserChartHistory(userName));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(404).build();
        } catch (Exception ex) {
            return ResponseEntity.status(500).build();
        }
}

    @GetMapping("/popular")
    public ResponseEntity<Object> getMostPopularRecipies(JwtAuthenticationToken authentication) {
        String token = authentication.getToken().getTokenValue();
        Object data = userService.getMostPopularRecipies(token);
        return ResponseEntity.ok(data);
    }
}
