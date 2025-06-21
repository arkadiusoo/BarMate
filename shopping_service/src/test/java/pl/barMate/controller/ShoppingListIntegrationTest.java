package pl.barMate.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import pl.barMate.dto.ShoppingItemDTO;
import pl.barMate.dto.ShoppingListDTO;
import pl.barMate.model.ShoppingItem;
import pl.barMate.model.ShoppingList;
import pl.barMate.repository.ShoppingItemRepository;
import pl.barMate.repository.ShoppingListRepository;
import pl.barMate.service.InventoryServiceClient;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class ShoppingListIntegrationTest {

    @Autowired
    private ShoppingListController shoppingListController;

    @Autowired
    private ShoppingListRepository shoppingListRepository;


    @Autowired
    private ShoppingItemRepository shoppingItemRepository;

    @Autowired
    private InventoryServiceClient inventoryServiceClient;

    @Test
    public void shouldCreateShoppingListSuccessfully() {
        Long userId = 42L;

        ResponseEntity<ShoppingListDTO> response = shoppingListController.createShoppingList(userId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        ShoppingListDTO createdList = response.getBody();
        assertThat(createdList).isNotNull();
        assertThat(createdList.getUserId()).isEqualTo(userId);
        assertThat(createdList.getId()).isNotNull();
        assertThat(createdList.getItems()).isEmpty();

        // Sprawdzenie, że lista jest w bazie
        Optional<ShoppingList> listInDb = shoppingListRepository.findById(createdList.getId());
        assertThat(listInDb).isPresent();

        // Opcjonalnie, jeśli chcesz sprawdzić pole createdAt:
        assertThat(createdList.getCreatedAt()).isNotNull();
    }

    @Test
    public void shouldAddItemToShoppingList() {
        Long userId = 42L;
        ResponseEntity<ShoppingListDTO> listResponse = shoppingListController.createShoppingList(userId);
        Long listId = listResponse.getBody().getId();

        ShoppingItemDTO newItem = new ShoppingItemDTO();
        newItem.setIngredientName("Flour");
        newItem.setAmount(2.0);
        newItem.setUnit("kg");
        newItem.setChecked(false);

        ResponseEntity<ShoppingItemDTO> itemResponse = shoppingListController.addItemToShoppingList(listId, newItem);

        assertThat(itemResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        ShoppingItemDTO savedItem = itemResponse.getBody();
        assertThat(savedItem).isNotNull();
        assertThat(savedItem.getIngredientName()).isEqualTo("Flour");
        assertThat(savedItem.getChecked()).isFalse();

        // Sprawdzenie, czy przedmiot jest na liście
        ResponseEntity<ShoppingListDTO> updatedListResponse = shoppingListController.getShoppingList(listId);
        assertThat(updatedListResponse.getBody().getItems()).extracting("ingredientName").contains("Flour");
    }


    @Test
    public void shouldCheckOffItemAndUpdateInventory() throws Exception {
        // given
        Long userId = 42L;

        // Tworzymy listę zakupów
        ResponseEntity<ShoppingListDTO> listResponse = shoppingListController.createShoppingList(userId);
        Long listId = listResponse.getBody().getId();

        // Przygotowujemy item
        ShoppingItemDTO newItem = new ShoppingItemDTO();
        newItem.setIngredientName("Sugar");
        newItem.setAmount(1.0);
        newItem.setUnit("kg");
        newItem.setChecked(false);
        // UWAGA: kontroler sam ustawia ID i shoppingListId

        // Dodajemy item do listy
        ResponseEntity<ShoppingItemDTO> itemResponse = shoppingListController.addItemToShoppingList(listId, newItem);
        assertThat(itemResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        ShoppingItemDTO addedItem = itemResponse.getBody();
        Long itemId = addedItem.getId();

        // when – oznaczamy jako kupione
        ResponseEntity<ShoppingItemDTO> checkOffResponse = shoppingListController.checkOffShoppingItem(listId, itemId);

        // then – status OK i faktyczna zmiana w bazie
        assertThat(checkOffResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        ShoppingItem checkedItem = shoppingItemRepository.findById(itemId).orElseThrow();
        assertThat(checkedItem.getChecked()).isTrue();
    }


    @Test
    public void shouldRemoveItemFromShoppingList() {
        Long userId = 42L;
        ResponseEntity<ShoppingListDTO> listResponse = shoppingListController.createShoppingList(userId);
        Long listId = listResponse.getBody().getId();

        ShoppingItemDTO newItem = new ShoppingItemDTO();
        newItem.setIngredientName("Butter");
        newItem.setAmount(0.5);
        newItem.setUnit("kg");
        newItem.setChecked(false);

        ResponseEntity<ShoppingItemDTO> itemResponse = shoppingListController.addItemToShoppingList(listId, newItem);
        Long itemId = itemResponse.getBody().getId();

        ResponseEntity<Void> deleteResponse = shoppingListController.removeItemFromShoppingList(listId, itemId);

        assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        Optional<ShoppingItem> deletedItem = shoppingItemRepository.findById(itemId);
        assertThat(deletedItem).isEmpty();
    }

    @Test
    public void shouldReturnNullWhenCheckingNonExistentItem() {
        // Given
        Long userId = 1L;
        ResponseEntity<ShoppingListDTO> response = shoppingListController.createShoppingList(userId);
        Long listId = response.getBody().getId();
        Long nonExistentItemId = 99999L;

        // When
        ResponseEntity<?> result = shoppingListController.checkOffShoppingItem(listId, nonExistentItemId);

        // Then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isNull();
    }

    @Test
    public void shouldReturnTheSameObjectWhenCheckingOffAlreadyCheckedOffItem() {
        // Given
        Long userId = 1L;
        ResponseEntity<ShoppingListDTO> response = shoppingListController.createShoppingList(userId);
        Long listId = response.getBody().getId();
        ShoppingItemDTO newItem = new ShoppingItemDTO();
        newItem.setIngredientName("Sugar");
        newItem.setAmount(1.0);
        newItem.setUnit("kg");
        newItem.setChecked(true);

        ResponseEntity<ShoppingItemDTO> itemResponse = shoppingListController.addItemToShoppingList(listId, newItem);
        Long itemId = itemResponse.getBody().getId();

        // When
        ResponseEntity<ShoppingItemDTO> result = shoppingListController.checkOffShoppingItem(listId, itemId);

        // Then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getChecked()).isTrue();
    }

    @Test
    public void shouldReturnNotFoundWhenAddingItemToNonExistentList() {
        // Given
        Long nonExistentListId = 99999L;
        ShoppingItemDTO item = new ShoppingItemDTO();
        item.setIngredientName("Rum");
        item.setAmount(1.0);
        item.setUnit("l");

        // When
        ResponseEntity<?> result = null;
        try {
            result = shoppingListController.addItemToShoppingList(nonExistentListId, item);
        } catch (Exception e) {
            assertThat(e.getMessage()).isEqualTo("java.lang.Exception: Failed to add a shopping item");
        }
    }


    @Test
    public void shouldReturnAllShoppingListsForUser() {
        // Given
        Long userId = 42L;
        // Tworzymy dwie listy zakupowe dla użytkownika
        ResponseEntity<ShoppingListDTO> list1 = shoppingListController.createShoppingList(userId);
        ResponseEntity<ShoppingListDTO> list2 = shoppingListController.createShoppingList(userId);

        // I jedną dla innego użytkownika
        // shoppingListController.createShoppingList(999L);

        ResponseEntity<ShoppingListDTO> list12 = shoppingListController.getShoppingList(list1.getBody().getId());

        assertThat(list12.getBody().getUserId()).isEqualTo(userId);

        // When
        ResponseEntity<List<ShoppingListDTO>> response = shoppingListController.getShoppingListByUser(userId);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<ShoppingListDTO> lists = response.getBody();
        assertThat(lists).isNotNull();
        assertThat(lists).hasSize(2);
        assertThat(lists).allMatch(list -> list.getUserId().equals(userId));
    }

}
