package pl.barmate.cocktails.controller;

import pl.barmate.cocktails.model.BriefCocktail;
import pl.barmate.cocktails.model.CocktailDto;
import pl.barmate.cocktails.service.CocktailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.barmate.cocktails.service.TranslationService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

/**
 * REST controller exposing endpoints for searching, filtering and listing cocktails
 * via TheCocktailDB API.
 */
@RestController
@RequestMapping("/api/cocktails")
public class CocktailController {

    private final CocktailService cocktailService;
    private final TranslationService translationService;

    @Autowired
    public CocktailController(CocktailService cocktailService, TranslationService translationService) {
        this.cocktailService = cocktailService;
        this.translationService = translationService;
    }

    @Operation(summary = "Search cocktails by full name")
    @GetMapping("/search")
    public Mono<List<CocktailDto>> searchByName(
            @RequestParam("name") String name,
            @RequestParam(defaultValue = "polish") String lang) {

        Mono<List<CocktailDto>> base = cocktailService.searchByName(name);

        return translateList(base, lang);
    }

    @Operation(summary = "Search cocktails by first letter")
    @GetMapping("/search/letter")
    public Mono<List<CocktailDto>> searchByFirstLetter(
            @RequestParam("letter") char letter,
            @RequestParam(defaultValue = "polish") String lang) {

        return translateList(cocktailService.searchByFirstLetter(letter), lang);
    }

    @Operation(summary = "Lookup a single cocktail by its ID")
    @GetMapping("/lookup")
    public Mono<CocktailDto> lookupById(
            @RequestParam("id") String id,
            @RequestParam(defaultValue = "polish") String lang) {

        return translateOne(cocktailService.lookupById(id), lang);
    }

    @Operation(summary = "Get a random cocktail")
    @GetMapping("/random")
    public Mono<CocktailDto> getRandom(
            @RequestParam(defaultValue = "polish") String lang) {

        return translateOne(cocktailService.getRandom(), lang);
    }

    @Operation(summary = "Get 10 random cocktails")
    @GetMapping("/random/selection")
    public Mono<List<CocktailDto>> getRandomSelection(
            @RequestParam(defaultValue = "polish") String lang) {

        return translateList(cocktailService.getRandomSelection(), lang);
    }

    @Operation(summary = "Filter cocktails by ingredient (brief info)")
    @GetMapping("/filter/ingredient")
    public Mono<List<BriefCocktail>> filterByIngredient(
            @RequestParam("ingredient") String ingredient,
            @RequestParam(defaultValue = "polish") String lang
    ) {
        return cocktailService.filterByIngredient(ingredient)
                .flatMapMany(Flux::fromIterable)
                .flatMap(bc ->
                        translationService.translateName(bc.getName(), lang)
                                .map(translated -> {
                                    bc.setName(translated);
                                    return bc;
                                })
                )
                .collectList();
    }

    @Operation(summary = "Filter cocktails by category (brief info)")
    @GetMapping("/filter/category")
    public Mono<List<BriefCocktail>> filterByCategory(
            @RequestParam("category") String category,
            @RequestParam(defaultValue = "polish") String lang
    ) {
        return cocktailService.filterByCategory(category)
                .flatMapMany(Flux::fromIterable)
                .flatMap(bc ->
                        translationService.translateName(bc.getName(), lang)
                                .map(translated -> {
                                    bc.setName(translated);
                                    return bc;
                                })
                )
                .collectList();
    }

    @Operation(summary = "Filter cocktails by glass type (brief info)")
    @GetMapping("/filter/glass")
    public Mono<List<BriefCocktail>> filterByGlass(
            @RequestParam("glass") String glass,
            @RequestParam(defaultValue = "polish") String lang
    ) {
        return cocktailService.filterByGlass(glass)
                .flatMapMany(Flux::fromIterable)
                .flatMap(bc ->
                        translationService.translateName(bc.getName(), lang)
                                .map(translated -> {
                                    bc.setName(translated);
                                    return bc;
                                })
                )
                .collectList();
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



    @Operation(summary = "List popular cocktails")
    @GetMapping("/popular")
    public Mono<List<CocktailDto>> listPopular(
            @RequestParam(defaultValue = "polish") String lang) {

        return translateList(cocktailService.listPopular(), lang);
    }

    @Operation(summary = "List latest cocktails")
    @GetMapping("/latest")
    public Mono<List<CocktailDto>> listLatest(
            @RequestParam(defaultValue = "polish") String lang) {

        return translateList(cocktailService.listLatest(), lang);
    }

    @Operation(summary = "Filter by multiple ingredients")
    @GetMapping("/filter/multiple")
    public Mono<List<BriefCocktail>> filterByMultipleIngredients(
            @Parameter(description = "Comma-separated list of ingredients", example = "Dry_Vermouth,Gin,Anis")
            @RequestParam("ingredients") List<String> ingredients) {
        return cocktailService.filterByMultipleIngredients(ingredients);
    }

    /**
     * Uniwersalny wrapper dla Mono<List<CocktailDto>>> + tłumaczenie
     */
    private Mono<List<CocktailDto>> translateList(
            Mono<List<CocktailDto>> source,
            String lang) {

        return source
                .flatMapMany(Flux::fromIterable)
                .flatMap(dto -> {
                    Mono<String> nameT    = translationService.translateName(dto.getName(), lang);
                    Mono<String> instrT   = translationService.translateInstructions(dto.getInstructions(), lang);
                    Mono<String> ingsT    = translationService.translateIngredients(dto.getIngredients(), lang);

                    return Mono.zip(nameT, instrT, ingsT)
                            .map(tuple -> {
                                dto.setName(tuple.getT1());
                                dto.setInstructions(tuple.getT2());
                                // ChatGPT zwróci wszystkie składniki jako jeden tekst – jeśli wolisz listę, możesz
                                // rozbić po nowej linii:
                                List<String> translatedIngredients = Arrays.stream(tuple.getT3().split("\\R"))
                                        .map(String::trim)
                                        .filter(s -> !s.isEmpty())
                                        .toList();
                                dto.setIngredients(translatedIngredients);
                                return dto;
                            });
                })
                .collectList();
    }

    /**
     * Wrapper dla pojedynczego CocktailDto
     */
    private Mono<CocktailDto> translateOne(
            Mono<CocktailDto> source,
            String lang) {
        return source.flatMap(dto -> translateList(Mono.just(List.of(dto)), lang)
                .map(list -> list.get(0)));
    }

}

