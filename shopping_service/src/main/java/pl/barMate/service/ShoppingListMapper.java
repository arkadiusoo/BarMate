package pl.barMate.service;

import pl.barMate.dto.ShoppingItemDTO;
import pl.barMate.dto.ShoppingListDTO;
import pl.barMate.model.ShoppingItem;
import pl.barMate.model.ShoppingList;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class ShoppingListMapper {

    public static ShoppingListDTO toDTO(ShoppingList shoppingList) {
        if (shoppingList == null) {
            return null;
        }
        return new ShoppingListDTO(
                shoppingList.getId(),
                shoppingList.getUserId(),
                shoppingList.getItems() != null
                        ? shoppingList.getItems().stream()
                        .map(ShoppingItemMapper::toDTO)
                        .collect(Collectors.toList())
                        : null,
                LocalDate.now()
        );
    }

    public static ShoppingList toEntity(ShoppingListDTO dto) {
        if (dto == null) {
            return null;
        }
        return ShoppingList.builder()
                .id(dto.getId())
                .userId(dto.getUserId())
                .items(dto.getItems() != null
                        ? dto.getItems().stream()
                        .map(ShoppingItemMapper::toEntity)
                        .collect(Collectors.toList())
                        : null)
                .build();
    }
}
