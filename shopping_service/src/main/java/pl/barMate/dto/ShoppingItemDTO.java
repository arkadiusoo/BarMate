package pl.barMate.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Data transfer object representing a single object on a shopping list")
public class ShoppingItemDTO {

    @Schema(description = "Unique identifier of the shopping item", example = "1")
    private Long id;

    @Schema(description = "Name of the ingredient to buy", example = "Flour")
    private String ingredientName;

    @Schema(description = "Amount of the ingredient needed", example = "2.5")
    private Double amount;

    @Schema(description = "Unit of measurement for the amount", example = "kg")
    private String unit;

    @Schema(description = "Flag indicating whether the item has been purchased", example = "false")
    private Boolean checked;

    @Schema(description = "Identifier of the user who owns the shopping item", example = "42")
    private Long userId;
}
