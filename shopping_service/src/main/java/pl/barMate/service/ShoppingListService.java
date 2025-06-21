package pl.barMate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.barMate.dto.ShoppingItemDTO;
import pl.barMate.dto.ShoppingListDTO;
import pl.barMate.model.ShoppingList;
import pl.barMate.repository.ShoppingListRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service("ShoppingListService")
@RequiredArgsConstructor
public class ShoppingListService {

    private final ShoppingListRepository shoppingListRepository;
    private final ShoppingListMapper shoppingListMapper;

    public ShoppingListDTO addShoppingList(ShoppingListDTO shoppingListDTO) throws Exception {
        try
        {
            ShoppingList shoppingList = shoppingListMapper.toEntity(shoppingListDTO);
            ShoppingList savedList = shoppingListRepository.save(shoppingList);
            return shoppingListMapper.toDTO(savedList);
        } catch (Exception ex) {
            throw new Exception("Failed to add a shopping list");
        }
    }

    public ShoppingListDTO updateShoppingList(ShoppingListDTO shoppingListDTO) throws Exception {
        try {
            ShoppingList shoppingList = shoppingListMapper.toEntity(shoppingListDTO);
            ShoppingList updatedList = shoppingListRepository.save(shoppingList);
            return shoppingListMapper.toDTO(updatedList);
        } catch (Exception e) {
            throw new Exception("Failed to update a shopping list");
        }
    }

    public void deleteShoppingList(Long id) throws Exception {
        try {
            shoppingListRepository.deleteById(id);
        } catch (Exception e) {
            throw new Exception("Failed to delete a shopping list");
        }
    }

    public List<ShoppingListDTO> getShoppingListsByUserId(Long userId) throws Exception {
        try {
            List<ShoppingList> list = shoppingListRepository.findByUserId(userId);
            return shoppingListRepository.findByUserId(userId)
                    .stream()
                    .map(shoppingListMapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new Exception("Failed to get shopping lists for an id");
        }

    }

    public Optional<ShoppingListDTO> getShoppingListById(Long id) throws Exception {
        try {
            return shoppingListRepository.findById(id)
                    .map(shoppingListMapper::toDTO);
        } catch (Exception e) {
            throw new Exception("Failed to get a shopping list");
        }
    }

    public Integer getMaxShoppingListId() {
        Integer id = shoppingListRepository.getMaxId();
        if (id == null) {
            return 0;
        }
        return id;
    }
}

