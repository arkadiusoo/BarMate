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

    @Column(nullable = false)
    private String unit;

    @Column(nullable = false)
    private Boolean checked = false;

    @Column(nullable = false)
    private Long userId;

    @ManyToOne
    @JoinColumn(name = "shopping_list_id")
    private ShoppingList shoppingList;
}
