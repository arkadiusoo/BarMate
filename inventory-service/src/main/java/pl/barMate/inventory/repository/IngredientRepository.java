package pl.barMate.inventory.repository;

import pl.barMate.inventory.model.Ingredient;
import pl.barMate.inventory.model.IngredientCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
    List<Ingredient> findByCategory(IngredientCategory category);
    List<Ingredient> findByNameContainingIgnoreCase(String name);
}