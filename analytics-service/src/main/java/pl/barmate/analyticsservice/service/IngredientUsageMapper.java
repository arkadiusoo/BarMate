package pl.barmate.analyticsservice.service;

import org.springframework.stereotype.Component;

@Component
public class IngredientUsageMapper {

    public IngredientUsageDTO toDto(IngredientUsage entity) {
        if (entity == null) return null;

        return IngredientUsageDTO.builder()
                .id(entity.getId())
                .ingredientName(entity.getIngredientName())
                .ingredientType(entity.getIngredientType())
                .amountUsed(entity.getAmountUsed())
                .cost(entity.getCost())
                .userId(entity.getUserId())
                .date(entity.getDate())
                .unitCost(entity.getUnitCost())
                .totalCost(entity.getTotalCost())
                .build();
    }

    public IngredientUsage toEntity(IngredientUsageDTO dto) {
        if (dto == null) return null;

        return IngredientUsage.builder()
                .id(dto.getId())
                .ingredientName(dto.getIngredientName())
                .ingredientType(dto.getIngredientType())
                .amountUsed(dto.getAmountUsed())
                .cost(dto.getCost())
                .userId(dto.getUserId())
                .date(dto.getDate())
                .unitCost(dto.getUnitCost())
                .totalCost(dto.getTotalCost())
                .build();
    }
}