package pl.barmate.analyticsservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO representing usage and cost of a specific ingredient")
public class IngredientUsageDTO {

    @Schema(description = "Unique identifier of the usage record", example = "2001", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotNull
    @Schema(description = "Name of the ingredient used", example = "Vodka")
    private String ingredientName;

    @NotNull
    @Schema(description = "Type of the ingredient", example = "ALCOHOL")
    private IngredientType ingredientType;

    @DecimalMin("0.0")
    @Schema(description = "Amount used (in ml or grams)", example = "50.0")
    private double amountUsed;

    @NotNull
    @DecimalMin("0.0")
    @Schema(description = "Cost of the ingredient used", example = "5.50")
    private BigDecimal cost;

    @NotNull
    @Schema(description = "ID of the user who used the ingredient", example = "42")
    private Long userId;

    @NotNull
    @Schema(description = "Date of usage", example = "2025-04-23")
    private LocalDate date;

    @Schema(description = "Unit cost of the ingredient", example = "11.00")
    private BigDecimal unitCost;

    @Schema(description = "Total cost of the used amount", example = "5.50")
    private BigDecimal totalCost;
}