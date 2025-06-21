package pl.barmate.cocktails.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestedIngredientDto {
    private String name;
    private double amount;
}
