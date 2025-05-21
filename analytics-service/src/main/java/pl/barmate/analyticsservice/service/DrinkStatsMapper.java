package pl.barmate.analyticsservice.service;

import org.springframework.stereotype.Component;
import pl.barmate.analyticsservice.dto.DrinkStatsDTO;

@Component
public class DrinkStatsMapper {

    public DrinkStatsDTO toDto(DrinkStats entity) {
        if (entity == null) return null;

        return DrinkStatsDTO.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .recipeId(entity.getRecipeId())
                .preparedAt(entity.getPreparedAt())
                .context(entity.getContext())
                .servings(entity.getServings())
                .preparationMethod(entity.getPreparationMethod())
                .customRecipe(entity.getCustomRecipe())
                .location(entity.getLocation())
                .build();
    }

    public DrinkStats toEntity(DrinkStatsDTO dto) {
        if (dto == null) return null;

        return DrinkStats.builder()
                .id(dto.getId())
                .userId(dto.getUserId())
                .recipeId(dto.getRecipeId())
                .preparedAt(dto.getPreparedAt())
                .context(dto.getContext())
                .servings(dto.getServings())
                .preparationMethod(dto.getPreparationMethod())
                .customRecipe(dto.getCustomRecipe())
                .location(dto.getLocation())
                .build();
    }
}