package pl.barmate.analyticsservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "drink_stats")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DrinkStats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long recipeId;

    @Column(nullable = false)
    private LocalDateTime preparedAt;

    private String context; // e.g. "party", "dinner", "alone", optional

    private Integer servings; // number of portions with one preparation

    private String preparationMethod; // shaken, stirred, blended, optional

    private Boolean customRecipe; // if custom

    private String location; // e.g. "kitchen", "garden" optional
}