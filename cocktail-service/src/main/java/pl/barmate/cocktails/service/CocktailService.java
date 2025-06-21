package pl.barmate.cocktails.service;

import org.springframework.beans.factory.annotation.Qualifier;
import pl.barmate.cocktails.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class CocktailService {

    private static final Logger log = LoggerFactory.getLogger(CocktailService.class);

    private final WebClient cocktailDbWebClient;
    private final WebClient inventoryWebClient;
    private final TranslationService translationService;

    @Autowired
    public CocktailService(
            @Qualifier("cocktailWebClient") WebClient cocktailDbWebClient,
            @Qualifier("inventoryWebClient") WebClient inventoryWebClient, // <-- DODAJ
            TranslationService translationService
    ) {
        this.cocktailDbWebClient = cocktailDbWebClient;
        this.inventoryWebClient = inventoryWebClient; // <-- DODAJ
        this.translationService = translationService;
    }
    // --- Public API methods ---

    /** Search cocktails by full name */
    public Mono<List<CocktailDto>> searchByName(String name) {
        return fetchFull("/search.php", "s", name);
    }

    /** Search cocktails by their first letter */
    public Mono<List<CocktailDto>> searchByFirstLetter(char letter) {
        return fetchFull("/search.php", "f", String.valueOf(letter));
    }

    /** Lookup a single cocktail by its ID */
    public Mono<CocktailDto> lookupById(String id) {
        return fetchFull("/lookup.php", "i", id)
                .flatMapMany(Flux::fromIterable)
                .singleOrEmpty();
    }

    /** Get a random cocktail */
    public Mono<CocktailDto> getRandom() {
        return fetchFull("/random.php", null, null)
                .flatMapMany(Flux::fromIterable)
                .next();
    }

    /** Filter cocktails by ingredient (returns only id, name, thumbnail) */
    public Mono<List<BriefCocktail>> filterByIngredient(String ingredient) {
        return fetchBrief("/filter.php", "i", ingredient);
    }

    /** Filter cocktails by category (returns only id, name, thumbnail) */
    public Mono<List<BriefCocktail>> filterByCategory(String category) {
        return fetchBrief("/filter.php", "c", category);
    }

    /** Filter cocktails by glass (returns only id, name, thumbnail) */
    public Mono<List<BriefCocktail>> filterByGlass(String glass) {
        return fetchBrief("/filter.php", "g", glass);
    }

    /** List all available categories */
    public Mono<List<String>> listCategories() {
        return fetchList("/list.php", "c", "list");
    }

    /** List all available glasses */
    public Mono<List<String>> listGlasses() {
        return fetchList("/list.php", "g", "list");
    }

    /** List all available ingredients */
    public Mono<List<String>> listIngredients() {
        return fetchList("/list.php", "i", "list");
    }

    /** List all alcoholic filters */
    public Mono<List<String>> listAlcoholic() {
        return fetchList("/list.php", "a", "list");
    }

    /** 10 losowych koktajli (Premium API) */
    public Mono<List<CocktailDto>> getRandomSelection() {
        return fetchFull("/randomselection.php", null, null);
    }

    /** Lista popularnych koktajli (Premium API) */
    public Mono<List<CocktailDto>> listPopular() {
        return fetchFull("/popular.php", null, null);
    }

    /** Lista najnowszych koktajli (Premium API) */
    public Mono<List<CocktailDto>> listLatest() {
        return fetchFull("/latest.php", null, null);
    }

    /**
     * Filtrowanie po wielu składnikach (Premium API).
     * @param ingredients lista składników (np. ["Gin","Vermouth"])
     */
    public Mono<List<BriefCocktail>> filterByMultipleIngredients(List<String> ingredients) {
        String joined = String.join(",", ingredients);
        return fetchBrief("/filter.php", "i", joined);
    }

    private Mono<List<IngredientConsumptionDto>> performAvailabilityCheck(Mono<CocktailDto> cocktailDtoMono) {
        return cocktailDtoMono
                .flatMap(cocktailDto -> {
                    // Używamy naszego ulepszonego parsera, który zwraca listę z jednostkami
                    List<IngredientConsumptionDto> requestedIngredients = parseIngredients(
                            cocktailDto.getIngredients(),
                            cocktailDto.getMeasures()
                    );

                    // Wysyłamy do InventoryService listę składników z jednostkami
                    return inventoryWebClient.post()
                            .uri("/ingredients/check-shortages")
                            .bodyValue(requestedIngredients)
                            .retrieve()
                            // ZMIANA: Oczekujemy odpowiedzi w formacie naszego nowego, pełnego DTO
                            .bodyToMono(new ParameterizedTypeReference<List<IngredientConsumptionDto>>() {})
                            .onErrorResume(e -> {
                                log.error("Błąd komunikacji z InventoryService: {}", e.getMessage());
                                return Mono.error(new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Inventory service is currently unavailable."));
                            });
                });
    }

    public Mono<List<IngredientConsumptionDto>> checkAvailability(String cocktailId) {
        return performAvailabilityCheck(lookupById(cocktailId));
    }

    public Mono<List<IngredientConsumptionDto>> checkAvailabilityByName(String cocktailName) {
        Mono<CocktailDto> firstCocktailMono = searchByName(cocktailName)
                .flatMap(cocktailList -> {
                    if (cocktailList.isEmpty()) {
                        return Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Cocktail not found with name: " + cocktailName));
                    }
                    return Mono.just(cocktailList.get(0));
                });
        return performAvailabilityCheck(firstCocktailMono);
    }

    public Mono<String> makeCocktailByName(String cocktailName) {
        Mono<CocktailDto> cocktailDtoMono = searchByName(cocktailName)
                .flatMap(cocktailList -> {
                    if (cocktailList.isEmpty()) {
                        return Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Cocktail not found: " + cocktailName));
                    }
                    return Mono.just(cocktailList.get(0));
                });

        return cocktailDtoMono.flatMap(cocktailDto -> {
            List<IngredientConsumptionDto> ingredientsToSubtract = parseIngredients(
                    cocktailDto.getIngredients(),
                    cocktailDto.getMeasures()
            );

            return Flux.fromIterable(ingredientsToSubtract)
                    .concatMap(ingredient ->
                            inventoryWebClient.post()
                                    .uri("/ingredients/subtract-by-name/" + ingredient.getName() + "?amount=" + ingredient.getAmount())
                                    .retrieve()
                                    .bodyToMono(Void.class) // Oczekujemy pustej odpowiedzi (status 200 OK)
                                    .onErrorResume(e -> {
                                        log.error("Błąd podczas odejmowania składnika '{}': {}", ingredient.getName(), e.getMessage());
                                        // Przekształcamy błąd z InventoryService na błąd zrozumiały dla klienta
                                        return Mono.error(new ResponseStatusException(HttpStatus.CONFLICT, "Failed to subtract ingredient: " + ingredient.getName() + ". Reason: " + e.getMessage()));
                                    })
                    )
                    .then(Mono.just("Successfully made cocktail: " + cocktailName)); // Po zakończeniu wszystkich operacji, zwróć sukces
        });
    }


    // --- Internal helper methods ---

    private List<IngredientConsumptionDto> parseIngredients(List<String> ingredients, List<String> measures) {
        List<IngredientConsumptionDto> result = new ArrayList<>();
        Pattern pattern = Pattern.compile("(\\d*\\.?\\d+)\\s*(\\w+)?");

        for (int i = 0; i < ingredients.size(); i++) {
            String ingredientName = ingredients.get(i);
            String measureText = (i < measures.size()) ? measures.get(i) : "";

            Matcher matcher = pattern.matcher(measureText);
            double amount = 0;
            String unit = "szt.";

            if (matcher.find()) {
                try {
                    amount = Double.parseDouble(matcher.group(1));
                    if (matcher.group(2) != null) {
                        unit = matcher.group(2);
                    }
                } catch (NumberFormatException e) { /* ignoruj błąd parsowania */ }
            } else {
                if (measureText.toLowerCase().contains("sok z połówki")) {
                    amount = 0.5;
                    unit = "szt.";
                } else if (measureText.toLowerCase().contains("kilka kropli")) {
                    amount = 2;
                    unit = "krople";
                } else if (!measureText.isBlank()) {
                    amount = 1;
                }
            }

            if (amount > 0) {
                result.add(new IngredientConsumptionDto(ingredientName, amount, unit));
            }
        }
        return result;
    }

    private Mono<List<String>> translateList(List<String> list) {
        if (list == null || list.isEmpty()) {
            return Mono.just(new ArrayList<>());
        }
        return translationService.translateBatch(list);
    }

    private Mono<CocktailDto> translateCocktailDto(CocktailDto dto) {
        return Mono.just(dto) // Zaczynamy od DTO
                .flatMap(d -> translationService.translate(d.getCategory())
                        .map(translated -> {
                            d.setCategory(translated);
                            return d;
                        }))
                .flatMap(d -> translationService.translate(d.getGlass())
                        .map(translated -> {
                            d.setGlass(translated);
                            return d;
                        }))
                .flatMap(d -> translationService.translate(d.getInstructions())
                        .map(translated -> {
                            d.setInstructions(translated);
                            return d;
                        }))
                .flatMap(d -> translateList(d.getIngredients())
                        .map(translated -> {
                            d.setIngredients(translated);
                            return d;
                        }))
                .flatMap(d -> translateList(d.getMeasures())
                        .map(translated -> {
                            d.setMeasures(translated);
                            return d;
                        }));
    }

    private Mono<List<CocktailDto>> fetchFull(String path, String param, String value) {
        return cocktailDbWebClient.get()
                .uri(uriBuilder -> {
                    var b = uriBuilder.path(path);
                    if (param != null) b.queryParam(param, value);
                    return b.build();
                })
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ApiResponse<Cocktail>>() {})
                .flatMap(resp -> {
                    if (resp.getDrinks() == null || resp.getDrinks().isEmpty()) {
                        return Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Brak wyników"));
                    }

                    List<CocktailDto> dtos = resp.getDrinks().stream()
                            .map(this::mapToDto)
                            .toList();

                    return Flux.fromIterable(dtos)
                            .flatMap(this::translateCocktailDto)
                            .collectList();
                });
    }

    @SuppressWarnings({})
    private Mono<List<BriefCocktail>> fetchBrief(String path, String param, String value) {
        return cocktailDbWebClient.get()
                .uri(uriBuilder -> {
                    var b = uriBuilder.path(path);
                    if (param != null) {
                        b.queryParam(param, value);
                    }
                    return b.build();
                })
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ApiResponse<BriefCocktail>>() {})
                .flatMap(resp -> {
                    if (resp.getDrinks() == null || resp.getDrinks().isEmpty()) {
                        return Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Nie znaleziono drinków"));
                    }
                    return Mono.just(resp.getDrinks());
                });
    }

    @SuppressWarnings({})
    private Mono<List<String>> fetchList(String path, String param, String value) {
        return cocktailDbWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(path)
                        .queryParam(param, value)
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ApiResponse<Map<String, String>>>() {})
                .map(ApiResponse::getDrinks)
                .map(list -> {
                    List<String> result = new ArrayList<>(list.size());
                    for (Map<String, String> m : list) {
                        result.add(m.values().stream().findFirst().orElse(""));
                    }
                    return result;
                })
                .flatMap(this::translateList);
    }

    private CocktailDto mapToDto(Cocktail c) {
        return new CocktailDto(
                c.getId(),
                c.getName(),
                c.getCategory(),
                c.getAlcoholic(),
                c.getGlass(),
                c.getInstructions(),
                c.getThumbnail(),
                c.getIngredients(),
                c.getMeasures()
        );
    }
}

