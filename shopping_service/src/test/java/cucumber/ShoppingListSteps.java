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

    @Given("user creates a shopping list with an unchecked item")
    public void createUncheckedItem() {
        //ShoppingList shoppingList = new ShoppingList(listId, 1L, new ArrayList<>());
        ResponseEntity<ShoppingListDTO> list = shoppingListController.createShoppingList(1L);
        shoppingListId = list.getBody().getId();
        ShoppingItemDTO item = new ShoppingItemDTO();
        item.setIngredientName("water");
        item.setAmount(100.0);
        item.setUnit("l");
        item.setChecked(false);
        ResponseEntity<ShoppingItemDTO> shoppingItem = shoppingListController.addItemToShoppingList(shoppingListId, item);
        shoppingItemId = shoppingItem.getBody().getId();
        System.out.println("Saved item ID: " + shoppingItem.getBody().getId());
    }

    @When("the user checks off the item on the list")
    public void checkOffItem() {
        response = shoppingListController.checkOffShoppingItem(shoppingListId, shoppingItemId);
    }

    @Then("the response should be 200 OK")
    public void assertResponseOK() {
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
    }

    @And("the item should be marked as checked")
    public void assertItemChecked() {
        ShoppingItem item = shoppingItemRepository.findById(shoppingItemId).orElseThrow();
        assertThat(item.getChecked()).isTrue();
    }
}
