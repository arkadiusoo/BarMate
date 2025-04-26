package pl.barMate.service;

import pl.barMate.dto.ShoppingItemDTO;
import pl.barMate.model.ShoppingItem;

public class ShoppingItemMapper {

    public static ShoppingItemDTO toDTO(ShoppingItem item) {
        if (item == null) {
            return null;
        }
        return new ShoppingItemDTO(
                item.getId(),
                item.getIngredientName(),
                item.getAmount(),
                item.getUnit(),
                item.getChecked(),
                item.getUserId()
        );
    }

    public static ShoppingItem toEntity(ShoppingItemDTO dto) {
        if (dto == null) {
            return null;
        }
        return ShoppingItem.builder()
                .id(dto.getId())
                .ingredientName(dto.getIngredientName())
                .amount(dto.getAmount())
                .unit(dto.getUnit())
                .checked(dto.getChecked())
                .userId(dto.getUserId())
                .build();
    }
}
