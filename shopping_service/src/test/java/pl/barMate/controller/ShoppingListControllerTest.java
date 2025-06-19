package pl.barMate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pl.barMate.dto.ShoppingItemDTO;
import pl.barMate.dto.ShoppingListDTO;
import pl.barMate.service.ShoppingItemService;
import pl.barMate.service.ShoppingListService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ShoppingListControllerTest {

    @Mock
    private ShoppingListService shoppingListService;

    @Mock
    private ShoppingItemService shoppingItemService;

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
    void addItemToShoppingList_ShouldReturnCreatedItem() {
        Long listId = 1L;
        ShoppingItemDTO inputItem = new ShoppingItemDTO();
        ShoppingItemDTO savedItem = new ShoppingItemDTO();

        when(shoppingItemService.addShoppingItem(inputItem)).thenReturn(savedItem);

        ResponseEntity<ShoppingItemDTO> response = shoppingListController.addItemToShoppingList(listId, inputItem);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(savedItem);
    }

    @Test
    void removeItemFromShoppingList_ShouldReturnNoContent() {
        Long listId = 1L;
        Long itemId = 2L;

        doNothing().when(shoppingItemService).deleteShoppingItem(itemId);

        ResponseEntity<Void> response = shoppingListController.removeItemFromShoppingList(listId, itemId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(shoppingItemService).deleteShoppingItem(itemId);
    }

    @Test
    void getItemsFromShoppingList_ShouldReturnItems() {
        Long listId = 3L;
        List<ShoppingItemDTO> items = List.of(new ShoppingItemDTO(), new ShoppingItemDTO());

        when(shoppingItemService.getItemsByShoppingListId(listId)).thenReturn(items);

        ResponseEntity<List<ShoppingItemDTO>> response = shoppingListController.getItemsFromShoppingList(listId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(items);
    }
}
