package pl.barMate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pl.barMate.dto.ShoppingItemDTO;
import pl.barMate.dto.ShoppingListDTO;
import pl.barMate.model.ShoppingItem;
import pl.barMate.repository.ShoppingItemRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service("ShoppingItemService")
@RequiredArgsConstructor
public class ShoppingItemService {

    private final ShoppingItemRepository shoppingItemRepository;
    private final ShoppingItemMapper shoppingItemMapper;
    private final ShoppingListService shoppingListService;
    //private final ShoppingItemMapper shoppingItemMapper;


    public ShoppingItemDTO addShoppingItem(ShoppingItemDTO shoppingItemDTO) throws Exception {

        try
        {
            ShoppingItem shoppingItem = shoppingItemMapper.toEntity(shoppingItemDTO);
            ShoppingItem savedItem = shoppingItemRepository.save(shoppingItem);
            return shoppingItemMapper.toDTO(savedItem);

        } catch (Exception e) {
            throw new Exception("Failed to add a shopping item");
        }
    }

    public ShoppingItemDTO updateShoppingItem(ShoppingItemDTO shoppingItemDTO) throws Exception {
        try {
            ShoppingItem shoppingItem = shoppingItemMapper.toEntity(shoppingItemDTO);
            ShoppingItem updatedItem = shoppingItemRepository.save(shoppingItem);
            return shoppingItemMapper.toDTO(updatedItem);
        } catch (Exception e) {
            throw new Exception("Failed to update the shopping list");
        }
    }

    public void deleteShoppingItem(Long id) throws Exception {
        try {
            shoppingItemRepository.deleteById(id);
        } catch (Exception e) {
            throw new Exception("Failed to remove a shopping item");
        }
    }

    public List<ShoppingItemDTO> getItemsByShoppingListId(Long shoppingListId) throws Exception {
        try {
            return shoppingItemRepository.findByShoppingListId(shoppingListId)
                    .stream()
                    .map(shoppingItemMapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new Exception("Shopping list not found");
        }
    }
    public Optional<ShoppingItemDTO> getShoppingItemById(Long id) throws Exception {
        try {
            return shoppingItemRepository.findById(id)
                    .map(shoppingItemMapper::toDTO);
        } catch (Exception e) {
            throw new Exception("Shopping item not found");
        }
    }

    public List<ShoppingItemDTO> getItemsByIngredientName(String ingredientName) throws Exception{
        try {
            return shoppingItemRepository.findByIngredientName(ingredientName)
                    .stream()
                    .map(shoppingItemMapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new Exception("Ingredient name does not exist");
        }
    }

    public Integer getMaxShoppingItemtId() {
        Integer id = shoppingItemRepository.getMaxId();
        if (id == null) {
            return 0;
        }
        return id;
    }
}


