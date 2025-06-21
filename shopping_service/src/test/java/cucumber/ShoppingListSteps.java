package cucumber;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.java.en.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.transaction.annotation.Transactional;
import pl.barMate.controller.ShoppingListController;
import pl.barMate.dto.ShoppingItemDTO;
import pl.barMate.dto.ShoppingListDTO;
import pl.barMate.model.ShoppingItem;
import pl.barMate.model.ShoppingList;
import pl.barMate.repository.ShoppingItemRepository;
import pl.barMate.repository.ShoppingListRepository;
import pl.barMate.service.ShoppingItemService;
import pl.barMate.service.InventoryServiceClient;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.list;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@Transactional // każda zmiana zostanie wycofana po teście
public class ShoppingListSteps {

    @Autowired
    private ShoppingListController shoppingListController;

    @Autowired
    private ShoppingItemRepository shoppingItemRepository;

    @Autowired
    private ShoppingListRepository shoppingListRepository;

    private ShoppingItem savedItem;
    private ResponseEntity<ShoppingItemDTO> response;
    Long shoppingItemId;
    Long shoppingListId;

    @Given("a shopping list with ID {long} and an unchecked item with ID {long}")
    public void createUncheckedItem(Long listId, Long itemId) {
        ShoppingList shoppingList = new ShoppingList(listId, 1L, new ArrayList<>());
        //shoppingList.setSomeField(1L); // inne pola
        //shoppingList = shoppingListRepository.save(shoppingList); // zapisz listę, by mieć ID wygenerowane przez DB
        ResponseEntity<ShoppingListDTO> list = shoppingListController.createShoppingList(1L);
        shoppingListId = list.getBody().getId();
        ShoppingItemDTO item = new ShoppingItemDTO();
        item.setIngredientName("water");
        item.setAmount(100.0);
        item.setUnit("l");
        item.setChecked(false);
        //item.setShoppingList(list);

        //savedItem = shoppingItemRepository.save(item);
        ResponseEntity<ShoppingItemDTO> shoppingItem = shoppingListController.addItemToShoppingList(shoppingListId, item);
        shoppingItemId = shoppingItem.getBody().getId();
        System.out.println("Saved item ID: " + shoppingItem.getBody().getId());
    }

    @When("the user checks off item with ID {long} on list ID {long}")
    public void checkOffItem(Long itemId, Long listId) {
        response = shoppingListController.checkOffShoppingItem(shoppingListId, shoppingItemId);
    }

    @Then("the response should be 200 OK")
    public void assertResponseOK() {
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
    }

    @And("the item with ID {long} should be marked as checked")
    public void assertItemChecked(Long itemId) {
        ShoppingItem item = shoppingItemRepository.findById(shoppingItemId).orElseThrow();
        assertThat(item.getChecked()).isTrue();
    }

    @And("the inventory should be updated for item ID {long}")
    public void assertInventoryUpdated(Long itemId) {
        // Tu można sprawdzić pośrednio – np. jeśli InventoryServiceClient zapisuje do bazy lub zmienia stan
        // Albo: dodać testowy `TestInventoryServiceClient` jako bean i obserwować czy została wywołana metoda
    }
}
