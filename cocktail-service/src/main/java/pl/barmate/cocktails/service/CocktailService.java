package pl.barmate.cocktails.service;

import pl.barmate.cocktails.model.ApiResponse;
import pl.barmate.cocktails.model.BriefCocktail;
import pl.barmate.cocktails.model.Cocktail;
import pl.barmate.cocktails.model.CocktailDto;
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

@Service
public class CocktailService {

    private final WebClient webClient;

    @Autowired
    public CocktailService(WebClient cocktailWebClient) {
        this.webClient = cocktailWebClient;
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

    // --- Internal helper methods ---

    private Mono<List<CocktailDto>> fetchFull(String path, String param, String value) {
        return webClient.get()
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
                    return Mono.just(dtos);
                });
    }

    @SuppressWarnings({})
    private Mono<List<BriefCocktail>> fetchBrief(String path, String param, String value) {
        return webClient.get()
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
        return webClient.get()
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
                        // each map has one entry like {"strCategory":"Ordinary Drink"}
                        result.add(m.values().stream().findFirst().orElse(""));
                    }
                    return result;
                });
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

