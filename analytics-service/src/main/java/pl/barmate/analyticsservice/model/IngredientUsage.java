package pl.barmate.analyticsservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "ingredient_usage")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IngredientUsage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String ingredientName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private IngredientType ingredientType;

    @Column(nullable = false)
    private double amountUsed; // in milliliters or grams

    @Column(nullable = false)
    private BigDecimal cost;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private LocalDate date;

    private BigDecimal unitCost; // unit cost (e.g. for 100ml)

    private BigDecimal totalCost; // total cost of this amount
}