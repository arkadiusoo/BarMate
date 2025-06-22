package pl.barMate.service;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import pl.barMate.dto.ShoppingItemDTO;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class InventoryServiceClient {

    @Qualifier("inventoryWebClient")
    private final WebClient inventoryWebClient;

    public Mono<Void> updateAmount(ShoppingItemDTO item) {
        String name = item.getIngredientName();
        String unit = item.getUnit();
        double amount = item.getAmount();

        // Budujemy URI z parametrami query
        return inventoryWebClient.put()
                .uri(uriBuilder -> uriBuilder
                        .path("/ingredients/update-add-by-name")
                        .queryParam("name", name)
                        .queryParam("unit", unit)
                        .queryParam("amount", amount)
                        .build())
                .retrieve()
                .bodyToMono(Void.class) // Oczekujemy pustej odpowiedzi
                .doOnError(e -> {
                    // Tu możesz dodać logowanie błędów
                    System.err.println("Błąd przy updateAmount: " + e.getMessage());
                });
    }
}
