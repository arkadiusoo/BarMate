package pl.barMate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.barMate.model.ShoppingList;
import pl.barMate.repository.ShoppingListRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor()
public class ShoppingListService {

    private final ShoppingListRepository shoppingListRepository;


    public ShoppingList addShoppingList(ShoppingList shoppingList) {
        return shoppingListRepository.save(shoppingList);
    }

    public ShoppingList updateShoppingList(ShoppingList shoppingList) {
        return shoppingListRepository.save(shoppingList);
    }

    public void deleteShoppingList(Long id) {
        shoppingListRepository.deleteById(id);
    }

    public List<ShoppingList> getShoppingListsByUserId(Long userId) {
        return shoppingListRepository.findByUserId(userId);
    }


    public Optional<ShoppingList> getShoppingListById(Long id) {
        return shoppingListRepository.findById(id);
    }
}
