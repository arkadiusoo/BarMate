package pl.barmate.cocktails.controller;

import pl.barmate.cocktails.model.BriefCocktail;
import pl.barmate.cocktails.model.CocktailDto;
import pl.barmate.cocktails.model.RequestedIngredientDto;
import pl.barmate.cocktails.service.CocktailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * REST controller exposing endpoints for searching, filtering and listing cocktails
 * via TheCocktailDB API.
 */
@RestController
@RequestMapping("/api/cocktails")
public class CocktailController {

    private final CocktailService cocktailService;

    @Autowired
    public CocktailController(CocktailService cocktailService) {
        this.cocktailService = cocktailService;
    }

    @Operation(summary = "Search cocktails by full name")
    @GetMapping("/search")
    public Mono<List<CocktailDto>> searchByName(
            @Parameter(description = "Full or partial name of the cocktail", required = true)
            @RequestParam("name") String name) {
        return cocktailService.searchByName(name);
    }

    @Operation(summary = "Search cocktails by first letter")
    @GetMapping("/search/letter")
    public Mono<List<CocktailDto>> searchByFirstLetter(
            @Parameter(description = "First letter of the cocktail name", required = true, example = "a")
            @RequestParam("letter") char letter) {
        return cocktailService.searchByFirstLetter(letter);
    }

    @Operation(summary = "Lookup a single cocktail by its ID")
    @GetMapping("/lookup")
    public Mono<CocktailDto> lookupById(
            @Parameter(description = "Unique ID of the cocktail", required = true)
            @RequestParam("id") String id) {
        return cocktailService.lookupById(id);
    }

    @Operation(summary = "Get a random cocktail")
    @GetMapping("/random")
    public Mono<CocktailDto> getRandom() {
        return cocktailService.getRandom();
    }

    @Operation(summary = "Filter cocktails by ingredient (brief info)")
    @GetMapping("/filter/ingredient")
    public Mono<List<BriefCocktail>> filterByIngredient(
            @Parameter(description = "Ingredient to filter by", required = true)
            @RequestParam("ingredient") String ingredient) {
        return cocktailService.filterByIngredient(ingredient);
    }

    @Operation(summary = "Filter cocktails by category (brief info)")
    @GetMapping("/filter/category")
    public Mono<List<BriefCocktail>> filterByCategory(
            @Parameter(description = "Category to filter by", required = true)
            @RequestParam("category") String category) {
        return cocktailService.filterByCategory(category);
    }

    @Operation(summary = "Filter cocktails by glass type (brief info)")
    @GetMapping("/filter/glass")
    public Mono<List<BriefCocktail>> filterByGlass(
            @Parameter(description = "Glass type to filter by", required = true)
            @RequestParam("glass") String glass) {
        return cocktailService.filterByGlass(glass);
    }

    @Operation(summary = "List all available cocktail categories")
    @GetMapping("/list/categories")
    public Mono<List<String>> listCategories() {
        return cocktailService.listCategories();
    }

    @Operation(summary = "List all available glass types")
    @GetMapping("/list/glasses")
    public Mono<List<String>> listGlasses() {
        return cocktailService.listGlasses();
    }

    @Operation(summary = "List all available ingredients")
    @GetMapping("/list/ingredients")
    public Mono<List<String>> listIngredients() {
        return cocktailService.listIngredients();
    }

    @Operation(summary = "List all alcoholic filters")
    @GetMapping("/list/alcoholic")
    public Mono<List<String>> listAlcoholic() {
        return cocktailService.listAlcoholic();
    }

    @Operation(summary = "Get 10 random cocktails")
    @GetMapping("/random/selection")
    public Mono<List<CocktailDto>> getRandomSelection() {
        return cocktailService.getRandomSelection();
    }

    @Operation(summary = "List popular cocktails")
    @GetMapping("/popular")
    public Mono<List<CocktailDto>> listPopular() {
        return cocktailService.listPopular();
    }

    @Operation(summary = "List latest cocktails")
    @GetMapping("/latest")
    public Mono<List<CocktailDto>> listLatest() {
        return cocktailService.listLatest();
    }

    @Operation(summary = "Filter by multiple ingredients")
    @GetMapping("/filter/multiple")
    public Mono<List<BriefCocktail>> filterByMultipleIngredients(
            @Parameter(description = "Comma-separated list of ingredients", example = "Dry_Vermouth,Gin,Anis")
            @RequestParam("ingredients") List<String> ingredients) {
        return cocktailService.filterByMultipleIngredients(ingredients);
    }

    @Operation(summary = "Check availability of ingredients for a specific cocktail")
    @GetMapping("/check-availability/{cocktailId}")
    public Mono<List<RequestedIngredientDto>> checkAvailability(
            @Parameter(description = "ID of the cocktail to check", required = true)
            @PathVariable String cocktailId) {
        return cocktailService.checkAvailability(cocktailId);
    }
}

