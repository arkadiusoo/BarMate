package pl.barMate.inventory.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name must not be blank")
    private String name;

    @NotNull(message = "Category must not be null")
    @Enumerated(EnumType.STRING)
    private IngredientCategory category;

    @Min(value = 0, message = "Amount must be greater than or equal to 0")
    private double amount;

    @NotBlank(message = "Unit must not be blank")
    private String unit;
}

