package pl.barMate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.barMate.model.ShoppingItem;

import java.util.List;

@Repository
public interface ShoppingItemRepository extends JpaRepository<ShoppingItem, Long> {
    List<ShoppingItem> findByShoppingListId(Long shoppingListId);
    //List<ShoppingItem> findByUserId(Long userId);
    List<ShoppingItem> findByIngredientName(String ingredientName);

}
