package pl.barMate.inventory.service;

import pl.barMate.inventory.model.Ingredient;
import pl.barMate.inventory.repository.IngredientRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IngredientService {

    private final IngredientRepository ingredientRepository;

    public Ingredient createIngredient(Ingredient ingredient) {
        return ingredientRepository.save(ingredient);
    }

    public List<Ingredient> getAllIngredients() {
        return ingredientRepository.findAll();
    }

    public Ingredient getIngredientById(Long id) {
        return ingredientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ingredient not found with id: " + id));
    }

    public Ingredient updateIngredient(Long id, Ingredient updated) {
        Ingredient existing = getIngredientById(id);
        existing.setName(updated.getName());
        existing.setCategory(updated.getCategory());
        existing.setAmount(updated.getAmount());
        existing.setUnit(updated.getUnit());
        return ingredientRepository.save(existing);
    }

    public void deleteIngredient(Long id) {
        if (!ingredientRepository.existsById(id)) {
            throw new EntityNotFoundException("Ingredient not found with id: " + id);
        }
        ingredientRepository.deleteById(id);
    }
}
