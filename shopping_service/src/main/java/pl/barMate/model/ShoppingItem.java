package pl.barMate.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "shopping_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShoppingItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String ingredientName;

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = true)
    private String unit;

    @Column(nullable = true)
    private Boolean checked = false;

    @Column(nullable = true)
    private Long userId;

    @ManyToOne
    @JoinColumn(name = "shopping_list_id")
    private ShoppingList shoppingList;
}
