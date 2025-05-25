package pl.barMate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.barMate.dto.ShoppingItemDTO;
import pl.barMate.dto.ShoppingListDTO;
import pl.barMate.model.ShoppingItem;
import pl.barMate.repository.ShoppingItemRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShoppingItemService {

    private final ShoppingItemRepository shoppingItemRepository;
    private final ShoppingListService shoppingListService;

    public ShoppingItemDTO addShoppingItem(ShoppingItemDTO shoppingItemDTO) {

        try
        {
            ShoppingItem shoppingItem = ShoppingItemMapper.toEntity(shoppingItemDTO);
            System.out.println(shoppingItem);
            ShoppingItem savedItem = shoppingItemRepository.save(shoppingItem);
            return ShoppingItemMapper.toDTO(savedItem);

        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
        /*
        Long shoppingListId = shoppingItemDTO.getShoppingListId();
        Optional<ShoppingListDTO> list = shoppingListService.getShoppingListById(shoppingListId);
        List<ShoppingItemDTO> itemList = getItemsByShoppingListId(shoppingListId);
        itemList.add(shoppingItemDTO);
        ShoppingListDTO newShoppingList = shoppingListService.updateShoppingList()

        shoppingListService.updateShoppingList()*/
    }

    public ShoppingItemDTO updateShoppingItem(ShoppingItemDTO shoppingItemDTO) {
        ShoppingItem shoppingItem = ShoppingItemMapper.toEntity(shoppingItemDTO);
        ShoppingItem updatedItem = shoppingItemRepository.save(shoppingItem);
        return ShoppingItemMapper.toDTO(updatedItem);
    }

    public void deleteShoppingItem(Long id) {
        shoppingItemRepository.deleteById(id);
    }

    public List<ShoppingItemDTO> getItemsByShoppingListId(Long shoppingListId) {
        return shoppingItemRepository.findByShoppingListId(shoppingListId)
                .stream()
                .map(ShoppingItemMapper::toDTO)
                .collect(Collectors.toList());
    }
    /*
    public List<ShoppingItemDTO> getItemsByUserId(Long userId) {
        return shoppingItemRepository.findByUserId(userId)
                .stream()
                .map(ShoppingItemMapper::toDTO)
                .collect(Collectors.toList());
    }
    */
    public Optional<ShoppingItemDTO> getShoppingItemById(Long id) {
        return shoppingItemRepository.findById(id)
                .map(ShoppingItemMapper::toDTO);
    }

    public List<ShoppingItemDTO> getItemsByIngredientName(String ingredientName) {
        return shoppingItemRepository.findByIngredientName(ingredientName)
                .stream()
                .map(ShoppingItemMapper::toDTO)
                .collect(Collectors.toList());
    }
}


