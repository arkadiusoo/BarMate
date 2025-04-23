package pl.barmate.analyticsservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Data transfer object representing drink preparation statistics")
public class DrinkStatsDTO {

    @Schema(description = "Unique identifier of the statistics record", example = "1001", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotNull
    @Schema(description = "Identifier of the user who prepared the drink", example = "42")
    private Long userId;

    @NotNull
    @Schema(description = "Identifier of the recipe used", example = "101")
    private Long recipeId;

    @NotNull
    @Schema(description = "Date and time when the drink was prepared", example = "2025-04-23T18:30:00")
    private LocalDateTime preparedAt;

    @Schema(description = "Context in which the drink was prepared (e.g. party, dinner, alone)", example = "party")
    private String context;

    @Schema(description = "Number of servings prepared", example = "2")
    private Integer servings;

    @Schema(description = "Drink preparation method (e.g. shaken, stirred, blended)", example = "shaken")
    private String preparationMethod;

    @Schema(description = "Whether the recipe was custom-created by the user", example = "true")
    private Boolean customRecipe;

    @Schema(description = "Location where the drink was prepared (e.g. kitchen, garden)", example = "garden")
    private String location;
}