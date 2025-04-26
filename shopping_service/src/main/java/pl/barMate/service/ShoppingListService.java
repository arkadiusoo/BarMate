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

@Service
@RequiredArgsConstructor
public class ShoppingListService {

    private final ShoppingListRepository shoppingListRepository;

    public ShoppingListDTO addShoppingList(ShoppingListDTO shoppingListDTO) {
        ShoppingList shoppingList = ShoppingListMapper.toEntity(shoppingListDTO);
        ShoppingList savedList = shoppingListRepository.save(shoppingList);
        return ShoppingListMapper.toDTO(savedList);
    }

    public ShoppingListDTO updateShoppingList(ShoppingListDTO shoppingListDTO) {
        ShoppingList shoppingList = ShoppingListMapper.toEntity(shoppingListDTO);
        ShoppingList updatedList = shoppingListRepository.save(shoppingList);
        return ShoppingListMapper.toDTO(updatedList);
    }

    public void deleteShoppingList(Long id) {
        shoppingListRepository.deleteById(id);
    }

    public List<ShoppingListDTO> getShoppingListsByUserId(Long userId) {
        return shoppingListRepository.findByUserId(userId)
                .stream()
                .map(ShoppingListMapper::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<ShoppingListDTO> getShoppingListById(Long id) {
        return shoppingListRepository.findById(id)
                .map(ShoppingListMapper::toDTO);
    }
}

