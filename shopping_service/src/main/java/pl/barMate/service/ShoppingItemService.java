package pl.barMate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.barMate.model.ShoppingItem;
import pl.barMate.repository.ShoppingItemRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor()
public class ShoppingItemService {

    private final ShoppingItemRepository shoppingItemRepository;

    public ShoppingItem addShoppingItem(ShoppingItem shoppingItem) {
        return shoppingItemRepository.save(shoppingItem);
    }

    public ShoppingItem updateShoppingItem(ShoppingItem shoppingItem) {
        return shoppingItemRepository.save(shoppingItem);
    }

    public void deleteShoppingItem(Long id) {
        shoppingItemRepository.deleteById(id);
    }

    public List<ShoppingItem> getItemsByShoppingListId(Long shoppingListId) {
        return shoppingItemRepository.findByShoppingListId(shoppingListId);
    }

    public List<ShoppingItem> getItemsByUserId(Long userId) {
        return shoppingItemRepository.findByUserId(userId);
    }

    public Optional<ShoppingItem> getShoppingItemById(Long id) {
        return shoppingItemRepository.findById(id);
    }

    public List<ShoppingItem> getItemsByIngredientName(String ingredientName) {
        return shoppingItemRepository.findByIngredientName(ingredientName);
    }
}

