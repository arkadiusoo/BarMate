package pl.barmate.cocktails.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IngredientConsumptionDto {
    private String name;
    private double amount;
    private String unit;
}
