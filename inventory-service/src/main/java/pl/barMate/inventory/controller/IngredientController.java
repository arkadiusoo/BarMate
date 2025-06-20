package pl.barMate.inventory.controller;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
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

    @GetMapping("/name/{name}")
    @Operation(summary = "Get ingredient by name")
    public ResponseEntity<Ingredient> getIngredientByName(
            @Parameter(description = "Name of the ingredient to retrieve") @PathVariable String name) {
        return ResponseEntity.ok(ingredientService.getIngredientByName(name));
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

    @PutMapping("/update-by-name")
    @Operation(summary = "Subtract amount from ingredient by name")
    public ResponseEntity<Ingredient> subtractIngredientAmount(
            @RequestParam("name") @Parameter(description = "Name of the ingredient") String name,
            @RequestParam("unit") @Parameter(description = "Name of unit") String unit,
            @RequestParam("amount") @Parameter(description = "Amount to subtract") double amount) {
        try {
            return ingredientService.subtractIngredientAmount(name, unit, amount)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.noContent().build());
        } catch (EntityNotFoundException ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PutMapping("/update-add-by-name")
    @Operation(summary = "Add amount to ingredient by name")
    public ResponseEntity<Ingredient> addIngredientAmount(
            @RequestParam("name") @Parameter(description = "Name of the ingredient") String name,
            @RequestParam("unit") @Parameter(description = "Name of unit") String unit,
            @RequestParam("amount") @Parameter(description = "Amount to add") double amount) {
        try {
            return ResponseEntity.ok(ingredientService.addIngredientAmount(name, unit, amount));
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }


    @DeleteMapping("/{id}")
    @Operation(summary = "Delete ingredient by ID")
    public ResponseEntity<Void> deleteIngredient(
            @Parameter(description = "ID of the ingredient to delete") @PathVariable Long id) {
        ingredientService.deleteIngredient(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/check-shortages")
    @Operation(summary = "Check for ingredient shortages based on required amounts")
    public ResponseEntity<List<Ingredient>> checkIngredientShortages(
            @RequestBody List<Ingredient> requestedIngredients) {
        return ResponseEntity.ok(ingredientService.checkIngredientShortages(requestedIngredients));
    }


}