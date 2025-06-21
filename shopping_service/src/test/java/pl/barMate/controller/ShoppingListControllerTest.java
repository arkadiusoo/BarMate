package pl.barMate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pl.barMate.dto.ShoppingItemDTO;
import pl.barMate.dto.ShoppingListDTO;
import pl.barMate.model.ShoppingItem;
import pl.barMate.model.ShoppingList;
import pl.barMate.service.InventoryServiceClient;
import pl.barMate.service.ShoppingItemService;
import pl.barMate.service.ShoppingListService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ShoppingListControllerTest {

    @Mock
    private ShoppingListService shoppingListService;

    @Mock
    private ShoppingItemService shoppingItemService;

    @Mock
    private InventoryServiceClient inventoryServiceClient;

    @InjectMocks
    private ShoppingListController shoppingListController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getShoppingList_ShouldReturnList_WhenFound() {
        Long id = 1L;
        ShoppingListDTO dto = new ShoppingListDTO(id, 10L, new ArrayList<>(), LocalDate.now());
        try {
            when(shoppingListService.getShoppingListById(id)).thenReturn(Optional.of(dto));
        } catch (Exception e) {
            Assertions.fail();
        }

        ResponseEntity<ShoppingListDTO> response = shoppingListController.getShoppingList(id);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(dto);
    }

    @Test
    void getShoppingList_ShouldReturnNotFound_WhenNotFound() {
        Long id = 1L;
        try {
            when(shoppingListService.getShoppingListById(id)).thenReturn(Optional.empty());
        } catch (Exception e) {
            Assertions.fail();
        }

        ResponseEntity<ShoppingListDTO> response = shoppingListController.getShoppingList(id);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNull();
    }

    @Test
    void shouldThrowRuntimeExceptionWhenServiceFails() throws Exception {
        // Given
        Long id = 3L;
        Mockito.when(shoppingListService.getShoppingListById(id))
                .thenThrow(new IllegalStateException("Shopping list not found"));

        ResponseEntity<ShoppingListDTO> response = shoppingListController.getShoppingList(id);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

    }

    @Test
    void getShoppingListByUser_ShouldReturnLists() {
        Long userId = 5L;
        List<ShoppingListDTO> lists = List.of(
                new ShoppingListDTO(1L, userId, new ArrayList<>(), LocalDate.now()),
                new ShoppingListDTO(2L, userId, new ArrayList<>(), LocalDate.now())
        );
        try {
            when(shoppingListService.getShoppingListsByUserId(userId)).thenReturn(lists);
        } catch (Exception e) {
            Assertions.fail();
        }

        ResponseEntity<List<ShoppingListDTO>> response = null;
        try {
            response = shoppingListController.getShoppingListByUser(userId);
        } catch (Exception e) {
            Assertions.fail();
        }

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(2);
        assertThat(response.getBody()).isEqualTo(lists);
    }

    @Test
    void getShoppingListByUser_ShouldReturnNotFound_WhenUserNotFound() throws Exception {
        Long userId = 5L;
        when(shoppingListService.getShoppingListsByUserId(userId)).thenThrow(new Exception("User not found"));

        ResponseEntity<List<ShoppingListDTO>> response = shoppingListController.getShoppingListByUser(userId);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNull();
    }

    @Test
    void updateShoppingList_ShouldReturnUpdatedList() {
        Long id = 3L;
        ShoppingListDTO inputDto = new ShoppingListDTO(null, 10L, new ArrayList<>(), LocalDate.now());
        ShoppingListDTO updatedDto = new ShoppingListDTO(id, 10L, new ArrayList<>(), LocalDate.now());

        try {
            when(shoppingListService.updateShoppingList(any())).thenReturn(updatedDto);
        } catch (Exception e) {
            Assertions.fail();
        }

        ResponseEntity<ShoppingListDTO> response = null;
        try {
            response = shoppingListController.updateShoppingList(inputDto, id);
        } catch (Exception e) {
            Assertions.fail();
        }

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(updatedDto);
        assertThat(inputDto.getId()).isEqualTo(id);  // id powinno być ustawione w kontrolerze
    }

    @Test
    void updateShoppingList_ShouldFail_WhenShoppingListNotFound() throws Exception {
        ShoppingListDTO dto = new ShoppingListDTO(null, 10L, new ArrayList<>(), LocalDate.now());
        when(shoppingListService.updateShoppingList(dto)).thenThrow(new Exception("Shopping list not found"));

        ResponseEntity<ShoppingListDTO> response = shoppingListController.updateShoppingList(dto, 1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNull();

    }

    @Test
    void deleteShoppingList_ShouldReturnNoContent() {
        Long id = 10L;

        try {
            doNothing().when(shoppingListService).deleteShoppingList(id);
        } catch (Exception e) {
            Assertions.fail();
        }

        ResponseEntity<ShoppingListDTO> response = shoppingListController.deleteShoppingList(id);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        try {
            verify(shoppingListService).deleteShoppingList(id);
        } catch (Exception e) {
            Assertions.fail();
        }
    }

    @Test
    void deleteShoppingList_ShouldFail_WhenShoppingListNotFound() throws Exception {
        Long id = 10L;
        doThrow(new Exception("Shopping list not found")).when(shoppingListService).deleteShoppingList(id);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                shoppingListController.deleteShoppingList(id));

        assertEquals("java.lang.Exception: Shopping list not found", exception.getMessage());
    }

    @Test
    void createShoppingList_ShouldReturnCreatedList() {
        Long userId = 15L;
        ShoppingListDTO newList = new ShoppingListDTO(null, userId, new ArrayList<>(), LocalDate.now());
        ShoppingListDTO savedList = new ShoppingListDTO(1L, userId, new ArrayList<>(), LocalDate.now());

        try {
            when(shoppingListService.getShoppingListsByUserId(userId)).thenReturn(new ArrayList<>());
        } catch (Exception e) {
            Assertions.fail();
        }
        try {
            when(shoppingListService.addShoppingList(any())).thenReturn(savedList);
        } catch (Exception e) {
            Assertions.fail();
        }

        ResponseEntity<ShoppingListDTO> response = shoppingListController.createShoppingList(userId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(savedList);
    }

    @Test
    void createShoppingList_ShouldFail() throws Exception {
        Long userId = 10L;
        doThrow(new Exception("Failed to create a shopping list")).when(shoppingListService).addShoppingList(new ShoppingListDTO(null, userId, new ArrayList<>(), LocalDate.now()));

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                shoppingListController.createShoppingList(userId));

        assertEquals("java.lang.Exception: Failed to create a shopping list", exception.getMessage());
    }

    @Test
    void addItemToShoppingList_ShouldReturnCreatedItem() throws Exception {
        Long listId = 1L;
        ShoppingItemDTO inputItem = new ShoppingItemDTO();
        ShoppingItemDTO savedItem = new ShoppingItemDTO();

        when(shoppingItemService.addShoppingItem(inputItem)).thenReturn(savedItem);

        ResponseEntity<ShoppingItemDTO> response = shoppingListController.addItemToShoppingList(listId, inputItem);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(savedItem);
    }

    @Test
    void addItemToShoppingList_ShouldFail() throws Exception {
        Long id = 10L;
        String name = "water";
        double amount = 100.0;
        String unit = "l";
        Long shoppingListId = 1L;
        doThrow(new Exception("Failed to add a shopping item")).when(shoppingItemService).addShoppingItem(new ShoppingItemDTO(id, name, amount, unit, false, shoppingListId));

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                shoppingListController.addItemToShoppingList(shoppingListId, new ShoppingItemDTO(id, name, amount, unit, false, shoppingListId)));

        assertEquals("java.lang.Exception: Failed to add a shopping item", exception.getMessage());
    }

    @Test
    void removeItemFromShoppingList_ShouldReturnNoContent() throws Exception {
        Long listId = 1L;
        Long itemId = 2L;

        doNothing().when(shoppingItemService).deleteShoppingItem(itemId);

        ResponseEntity<Void> response = shoppingListController.removeItemFromShoppingList(listId, itemId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(shoppingItemService).deleteShoppingItem(itemId);
    }

    @Test
    void removeItemFromShoppingList_ShouldFail() throws Exception {
        Long id = 10L;
        Long shoppingListId = 1L;
        doThrow(new Exception("Failed to add a shopping item")).when(shoppingItemService).deleteShoppingItem(id);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                shoppingListController.removeItemFromShoppingList(shoppingListId, id));

        assertEquals("java.lang.Exception: Failed to add a shopping item", exception.getMessage());
    }

    @Test
    void getItemsFromShoppingList_ShouldReturnItems() throws Exception {
        Long listId = 3L;
        List<ShoppingItemDTO> items = List.of(new ShoppingItemDTO(), new ShoppingItemDTO());

        when(shoppingItemService.getItemsByShoppingListId(listId)).thenReturn(items);

        ResponseEntity<List<ShoppingItemDTO>> response = shoppingListController.getItemsFromShoppingList(listId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(items);
    }

    @Test
    void getItemsFromShoppingList_ShouldFail() throws Exception {
        Long shoppingListId = 1L;
        doThrow(new Exception("Failed to get shopping items")).when(shoppingItemService).getItemsByShoppingListId(shoppingListId);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                shoppingListController.getItemsFromShoppingList(shoppingListId));

        assertEquals("java.lang.Exception: Failed to get shopping items", exception.getMessage());
    }

    @Test
    void createShoppingListWithItems_ShouldReturnCreatedList(){
        Long shoppingItemId1 = 1L;
        Long shoppingItemId2 = 2L;
        Long shoppingListId = 1L;
        Long userId = 1L;
        try {
            when(shoppingListService.addShoppingList(any(ShoppingListDTO.class)))
                    .thenReturn(new ShoppingListDTO(shoppingListId, userId, new ArrayList<>(), LocalDate.now()));
        } catch (Exception e) {
            Assertions.fail();
        }

        try {
            when(shoppingItemService.addShoppingItem(any(ShoppingItemDTO.class)))
                    .thenReturn(new ShoppingItemDTO(shoppingItemId1, "water", 100.0, "l", false, shoppingListId));
        } catch (Exception e) {
            Assertions.fail();
        }
        ShoppingItemDTO shoppingItemDTO = new ShoppingItemDTO(shoppingItemId1, "water", 100.0, "l", false, shoppingListId);
        ResponseEntity<ShoppingListDTO> response = shoppingListController.createShoppingListWithItems(userId, Arrays.asList(shoppingItemDTO));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody().getUserId()).isEqualTo(userId);
    }

    @Test
    void createShoppingListWithItems_ShouldFailToAddItems() throws Exception {
        Long shoppingItemId1 = 1L;
        Long shoppingItemId2 = 2L;
        Long shoppingListId = 1L;
        Long userId = 1L;
        doThrow(new Exception("Failed to add shopping items")).when(shoppingItemService).addShoppingItem(new ShoppingItemDTO(shoppingItemId1, "water", 100.0, "l", false, shoppingListId));

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                shoppingListController.createShoppingListWithItems(userId, Arrays.asList(new ShoppingItemDTO(shoppingItemId1, "water", 100.0, "l", false, shoppingListId))));

        assertEquals("java.lang.Exception: Failed to add shopping items", exception.getMessage());

    }

    @Test
    void createShoppingListWithItems_ShouldFailToCreateAList() throws Exception {
        Long shoppingItemId1 = 1L;
        Long shoppingItemId2 = 2L;
        Long shoppingListId = 1L;
        Long userId = 1L;
        doThrow(new Exception("Failed to create a list")).when(shoppingListService).addShoppingList(any());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                shoppingListController.createShoppingListWithItems(userId, Arrays.asList(new ShoppingItemDTO(shoppingItemId1, "water", 100.0, "l", false, shoppingListId))));

        assertEquals("java.lang.Exception: Failed to create a list", exception.getMessage());
    }

    @Test
    void checkOffShoppingItem_ShouldReturnOK() {
        Long shoppingItemId = 1L;
        Long shoppingListId = 1L;

        // Given: shopping item is initially not checked
        ShoppingItemDTO uncheckedItem = new ShoppingItemDTO();
        uncheckedItem.setId(shoppingItemId);
        uncheckedItem.setChecked(false);

        // After update, the item is marked as checked
        ShoppingItemDTO checkedItem = new ShoppingItemDTO();
        checkedItem.setId(shoppingItemId);
        checkedItem.setChecked(true);

        try {
            when(shoppingListService.getShoppingListById(shoppingListId)).thenReturn(Optional.of(new ShoppingListDTO()));
            // Mock: get by ID returns unchecked item
            when(shoppingItemService.getShoppingItemById(shoppingItemId)).thenReturn(Optional.of(uncheckedItem));

            // Mock: update returns checked item
            when(shoppingItemService.updateShoppingItem(any(ShoppingItemDTO.class))).thenReturn(checkedItem);

            // Mock: inventory service (you can verify later if it was called)
            doNothing().when(inventoryServiceClient).updateAmount(any(ShoppingItemDTO.class));
        } catch (Exception e) {
            Assertions.fail("Mocking failed: " + e.getMessage());
        }

        // When
        ResponseEntity<ShoppingItemDTO> response = shoppingListController.checkOffShoppingItem(shoppingListId, shoppingItemId);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getChecked()).isTrue();

        // Optional: verify calls
        verify(inventoryServiceClient, times(1)).updateAmount(any(ShoppingItemDTO.class));
    }


    @Test
    void checkOffShoppingItem_ShouldFail() throws Exception {
        Long shoppingItemId = 1L;
        Long shoppingListId = 1L;

        ShoppingListDTO mockList = new ShoppingListDTO(shoppingListId, 1L, new ArrayList<>(), LocalDate.now());
        ShoppingItemDTO mockItem = new ShoppingItemDTO(shoppingItemId, "milk", 1.0, "l", false, shoppingListId);

        when(shoppingListService.getShoppingListById(shoppingListId)).thenReturn(Optional.of(mockList));
        when(shoppingItemService.getShoppingItemById(shoppingItemId)).thenReturn(Optional.of(mockItem));

        doThrow(new Exception("Failed to check off a shopping item"))
                .when(shoppingItemService).updateShoppingItem(any(ShoppingItemDTO.class));

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                shoppingListController.checkOffShoppingItem(shoppingListId, shoppingItemId)
        );

        assertEquals("java.lang.Exception: Failed to check off a shopping item", exception.getMessage());
    }

    @Test
    void checkOffShoppingItem_ShouldThrowException_WhenGetShoppingListFails() throws Exception {
        Long shoppingListId = 1L;
        Long shoppingItemId = 1L;

        when(shoppingListService.getShoppingListById(shoppingListId))
                .thenThrow(new RuntimeException("Failed to fetch shopping list"));

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                shoppingListController.checkOffShoppingItem(shoppingListId, shoppingItemId)
        );

        assertEquals("Failed to fetch shopping list", exception.getMessage());
    }

    @Test
    void checkOffShoppingItem_ShouldThrowException_WhenGetShoppingItemFails() throws Exception {
        Long shoppingListId = 1L;
        Long shoppingItemId = 1L;

        when(shoppingListService.getShoppingListById(shoppingListId))
                .thenReturn(Optional.of(new ShoppingListDTO(shoppingListId, 1L, new ArrayList<>(), LocalDate.now())));

        when(shoppingItemService.getShoppingItemById(shoppingItemId))
                .thenThrow(new RuntimeException("Failed to fetch shopping item"));

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                shoppingListController.checkOffShoppingItem(shoppingListId, shoppingItemId)
        );

        assertEquals("Failed to fetch shopping item", exception.getMessage());
    }

    @Test
    void checkOffShoppingItem_ShouldDoNothing_WhenItemIsAlreadyChecked() throws Exception {
        Long shoppingListId = 1L;
        Long shoppingItemId = 1L;

        ShoppingListDTO shoppingList = new ShoppingListDTO(shoppingListId, 1L, new ArrayList<>(), LocalDate.now());
        ShoppingItemDTO checkedItem = new ShoppingItemDTO(shoppingItemId, "bread", 1.0, "szt", true, shoppingListId);

        when(shoppingListService.getShoppingListById(shoppingListId)).thenReturn(Optional.of(shoppingList));
        when(shoppingItemService.getShoppingItemById(shoppingItemId)).thenReturn(Optional.of(checkedItem));

        ResponseEntity<ShoppingItemDTO> response = shoppingListController.checkOffShoppingItem(shoppingListId, shoppingItemId);

        // Nie powinno dojść do żadnych aktualizacji
        verify(shoppingItemService, never()).updateShoppingItem(any());
        verify(inventoryServiceClient, never()).updateAmount(any());

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }


}
