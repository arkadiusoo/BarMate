package pl.barMate.inventory.service;

import pl.barMate.inventory.model.Ingredient;
import pl.barMate.inventory.repository.IngredientRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

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

    public Ingredient getIngredientByName(String name) {
        return ingredientRepository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException("Ingredient not found with name: " + name));
    }

    public Ingredient subtractIngredientAmount(String name, double amountToSubtract) {
        Ingredient existingIngredient = ingredientRepository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException("Ingredient not found with name: " + name));

        if (existingIngredient.getAmount() < amountToSubtract) {
            throw new IllegalArgumentException("Insufficient amount of ingredient: " + name);
        }

        existingIngredient.setAmount(existingIngredient.getAmount() - amountToSubtract);
        return ingredientRepository.save(existingIngredient);
    }

    public Ingredient addIngredientAmount(String name, double amountToSubtract) {
        Ingredient existingIngredient = ingredientRepository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException("Ingredient not found with name: " + name));

        if (existingIngredient.getAmount() < amountToSubtract) {
            throw new IllegalArgumentException("Insufficient amount of ingredient: " + name);
        }

        existingIngredient.setAmount(existingIngredient.getAmount() + amountToSubtract);
        return ingredientRepository.save(existingIngredient);
    }

    public List<Ingredient> checkIngredientShortages(List<Ingredient> requestedIngredients) {
        return requestedIngredients.stream()
                .map(requested -> {
                    Ingredient stored = ingredientRepository.findByName(requested.getName())
                            .orElseThrow(() -> new EntityNotFoundException("Ingredient not found: " + requested.getName()));
                    double shortage = requested.getAmount() - stored.getAmount();
                    if (shortage > 0) {
                        return Ingredient.builder()
                                .name(stored.getName())
                                .amount(shortage)
                                .unit(stored.getUnit())
                                .category(stored.getCategory())
                                .build();
                    } else {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .toList();
    }


}