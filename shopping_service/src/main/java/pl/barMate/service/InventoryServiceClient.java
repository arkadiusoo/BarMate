package pl.barMate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pl.barMate.dto.ShoppingItemDTO;
import pl.barMate.model.ShoppingItem;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class InventoryServiceClient {
    public final RestTemplate restTemplate;

    public void updateAmount(ShoppingItemDTO item) {
        String name = item.getIngredientName();
        double amount = item.getAmount();
        /*
        String url_check = "http://localhost:8082/ingredients/name/{name}";

        Map<String, Object> ingredient = restTemplate.getForObject(url_check, Map.class, name);

        if (ingredient != null) {
            String unit = (String) ingredient.get("unit");
            if (!unit.equals(item.getUnit())) {
                return;
            }
        }
        */
        String url = "http://inventory-service/ingredients/update-add-by-name?name={name}&amount={amount}";

        restTemplate.put(url, null, name, amount);
    }
}
