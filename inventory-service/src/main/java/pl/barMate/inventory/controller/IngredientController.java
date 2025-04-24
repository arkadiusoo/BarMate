package pl.barMate.inventory.controller;

import pl.barMate.inventory.model.Ingredient;
import pl.barMate.inventory.service.IngredientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ingredients")
@RequiredArgsConstructor
@Tag(name = "Ingredients", description = "Ingredient management API")
public class IngredientController {
    private final IngredientService ingredientService;


    @PostMapping
    @Operation(summary = "Create a new ingredient")
    public ResponseEntity<Ingredient> createIngredient(
            @Valid @RequestBody Ingredient ingredient) {
        return ResponseEntity.ok(ingredientService.createIngredient(ingredient));
    }

    @GetMapping
    @Operation(summary = "Get all ingredients")
    public ResponseEntity<List<Ingredient>> getAllIngredients() {
        return ResponseEntity.ok(ingredientService.getAllIngredients());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get ingredient by ID")
    public ResponseEntity<Ingredient> getIngredientById(
            @Parameter(description = "ID of the ingredient to retrieve") @PathVariable Long id) {
        return ResponseEntity.ok(ingredientService.getIngredientById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing ingredient")
    public ResponseEntity<Ingredient> updateIngredient(
            @Parameter(description = "ID of the ingredient to update") @PathVariable Long id,
            @Valid @RequestBody Ingredient ingredient) {
        return ResponseEntity.ok(ingredientService.updateIngredient(id, ingredient));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete ingredient by ID")
    public ResponseEntity<Void> deleteIngredient(
            @Parameter(description = "ID of the ingredient to delete") @PathVariable Long id) {
        ingredientService.deleteIngredient(id);
        return ResponseEntity.noContent().build();
    }
}
