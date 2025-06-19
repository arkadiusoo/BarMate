package pl.barMate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.barMate.dto.ShoppingItemDTO;
import pl.barMate.dto.ShoppingListDTO;
import pl.barMate.model.ShoppingItem;
import pl.barMate.model.ShoppingList;
import pl.barMate.repository.ShoppingItemRepository;
import pl.barMate.repository.ShoppingListRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

@Component
public class ShoppingItemMapper {

    private ShoppingListRepository shoppingListRepository;
    public ShoppingItemMapper(ShoppingListRepository shoppingListRepository) {
        this.shoppingListRepository = shoppingListRepository;
    }

    public ShoppingItemDTO toDTO(ShoppingItem item) {
        if (item == null) {
            return null;
        }


        return new ShoppingItemDTO(
                item.getId(),
                item.getIngredientName(),
                item.getAmount(),
                item.getUnit(),
                item.getChecked(),
                item.getShoppingList().getId()

        );
    }

    public ShoppingItem toEntity(ShoppingItemDTO dto) {
        if (dto == null) {
            return null;
        }
        //System.out.println("dto" + dto);

        ShoppingList shoppingList = shoppingListRepository
                .findById(dto.getShoppingListId())
                .orElseThrow(() -> new RuntimeException("Shopping list not found"));

        return ShoppingItem.builder()
                .id(dto.getId())
                .ingredientName(dto.getIngredientName())
                .amount(dto.getAmount())
                .unit(dto.getUnit())
                .checked(dto.getChecked())
                .shoppingList(shoppingList)
                .build();

    }
}
