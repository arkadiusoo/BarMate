package pl.barMate.inventory.service;

import pl.barMate.inventory.model.Ingredient;
import pl.barMate.inventory.model.IngredientCategory;
import pl.barMate.inventory.repository.IngredientRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

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

    public Optional<Ingredient> subtractIngredientAmount(String name, double amountToSubtract) {
        Ingredient existingIngredient = ingredientRepository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException("Ingredient not found with name: " + name));

        double currentAmount = existingIngredient.getAmount();

        if (currentAmount < amountToSubtract) {
            throw new IllegalArgumentException("Insufficient amount of ingredient: " + name);
        }

        if (currentAmount == amountToSubtract) {
            ingredientRepository.delete(existingIngredient);
            return Optional.empty();
        } else {
            existingIngredient.setAmount(currentAmount - amountToSubtract);
            return Optional.of(ingredientRepository.save(existingIngredient));
        }
    }

    public Ingredient addIngredientAmount(String name, String unit, double amountToAdd) {
        Optional<Ingredient> existingOpt = ingredientRepository.findByNameAndUnit(name, unit);

        Ingredient ingredient;
        if (existingOpt.isPresent()) {
            ingredient = existingOpt.get();
            ingredient.setAmount(ingredient.getAmount() + amountToAdd);
        } else {
            ingredient = Ingredient.builder()
                    .name(name)
                    .amount(amountToAdd)
                    .unit(unit)
                    .category(IngredientCategory.OTHER) // możesz to dodać jako parametr, jeśli potrzebujesz
                    .build();
        }

        return ingredientRepository.save(ingredient);
    }


    public List<Ingredient> checkIngredientShortages(List<Ingredient> requestedIngredients) {
        return requestedIngredients.stream()
                .map(requested -> {
                    Optional<Ingredient> storedOpt = ingredientRepository.findByNameAndUnit(
                            requested.getName(),
                            requested.getUnit()
                    );

                    if (storedOpt.isEmpty()) {
                        // Składnik nie istnieje w bazie → cały wymagany jest brakiem
                        return Ingredient.builder()
                                .name(requested.getName())
                                .amount(requested.getAmount())
                                .unit(requested.getUnit())
                                .category(IngredientCategory.OTHER) // lub null, jeśli kategoria nieznana
                                .build();
                    }

                    Ingredient stored = storedOpt.get();
                    double shortage = requested.getAmount() - stored.getAmount();

                    if (shortage > 0) {
                        return Ingredient.builder()
                                .name(stored.getName())
                                .amount(shortage)
                                .unit(stored.getUnit())
                                .category(stored.getCategory())
                                .build();
                    } else {
                        return null; // brak niedoboru
                    }
                })
                .filter(Objects::nonNull)
                .toList();
    }



}