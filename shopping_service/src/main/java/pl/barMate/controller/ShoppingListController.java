package pl.barMate.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.barMate.dto.ShoppingItemDTO;
import pl.barMate.dto.ShoppingListDTO;
import pl.barMate.dto.ShoppingItemDTO;
import pl.barMate.model.ShoppingItem;
import pl.barMate.model.ShoppingList;
import pl.barMate.service.InventoryServiceClient;
import pl.barMate.service.ShoppingItemMapper;
import pl.barMate.service.ShoppingItemService;
import pl.barMate.service.ShoppingListService;
import reactor.core.Exceptions;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/shopping-list")
@RequiredArgsConstructor
public class ShoppingListController {
    private final ShoppingListService shoppingListService;
    private final ShoppingItemService shoppingItemService;
    private final InventoryServiceClient inventoryServiceClient;
    private final ShoppingItemMapper shoppingItemMapper;

    @Operation(summary = "Get a shopping list by id")
    @GetMapping("/{id}")
    public ResponseEntity<ShoppingListDTO> getShoppingList(@PathVariable Long id)
    {

        try {
            Optional<ShoppingListDTO> list = shoppingListService.getShoppingListById(id);
            return list.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(summary = "Get a shopping list by user id")
    @GetMapping("/user/{id}")
    public ResponseEntity<List<ShoppingListDTO>> getShoppingListByUser(@PathVariable Long id)
    {
        try {
            List<ShoppingListDTO> list = shoppingListService.getShoppingListsByUserId(id);
            return new ResponseEntity<>(list, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Update a shopping list")
    @PutMapping("/{id}")
    public ResponseEntity<ShoppingListDTO> updateShoppingList(@RequestBody ShoppingListDTO shoppingListDTO, @PathVariable Long id)
    {
        try {
            shoppingListDTO.setId(id);
            ShoppingListDTO updatedList = shoppingListService.updateShoppingList(shoppingListDTO);
            return new ResponseEntity<>(updatedList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Check off a shopping item")
    @PutMapping("/{list_id}/item/{id}")
    public ResponseEntity<ShoppingItem> checkOffShoppingItem(@PathVariable Long list_id, @PathVariable Long id)
    {
        try {
            shoppingListService.getShoppingListById(list_id).ifPresent(shoppingList -> {
                try {
                    shoppingItemService.getShoppingItemById(id).ifPresent(shoppingItem -> {
                        if (!shoppingItem.getChecked()) {
                            shoppingItem.setChecked(Boolean.TRUE);
                            try {
                                shoppingItemService.updateShoppingItem(shoppingItem);
                                inventoryServiceClient.updateAmount(shoppingItemService.getShoppingItemById(id).get());
                            } catch (Exception e) {
                                throw Exceptions.propagate(e);
                            }
                        }
                    });
                } catch (Exception e) {
                    throw Exceptions.propagate(e);
                }
            });
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            throw Exceptions.propagate(e);
        }
    }

    @Operation(summary = "Create a list with shoppingItems")
    @PostMapping("/with-items")
    public ResponseEntity<ShoppingListDTO> createShoppingListWithItems(@RequestBody Long userId, @RequestBody List<ShoppingItemDTO> shoppingItemsDTO) {
        ShoppingListDTO shoppingList = new ShoppingListDTO(
                null,
                userId,
                new ArrayList<>(),
                LocalDate.now()
        );
        ShoppingListDTO list = null;
        try {
            list = shoppingListService.addShoppingList(shoppingList);
        } catch (Exception e) {
            throw Exceptions.propagate(e);
        }
        for (ShoppingItemDTO shoppingItemDTO : shoppingItemsDTO) {
            ShoppingItemDTO createdItem = null;
            try {
                createdItem = shoppingItemService.addShoppingItem(shoppingItemDTO);
            } catch (Exception e) {
                throw Exceptions.propagate(e);
            }
            createdItem.setShoppingListId(list.getId());
        }
        return new ResponseEntity<>(list, HttpStatus.CREATED);
    }

    @Operation(summary = "Delete a shopping list")
    @DeleteMapping("/{id}")
    public ResponseEntity<ShoppingListDTO> deleteShoppingList(@PathVariable Long id) {
        try {
            shoppingListService.deleteShoppingList(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Operation(summary = "Create a shopping list")
    @PostMapping()
    public ResponseEntity<ShoppingListDTO> createShoppingList(@RequestBody Long userId) {

        ShoppingListDTO shoppingList = new ShoppingListDTO(
                null,
                userId,
                new ArrayList<>(),
                LocalDate.now()
        );
        ShoppingListDTO list = null;
        try {
            list = shoppingListService.addShoppingList(shoppingList);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return new ResponseEntity<>(list, HttpStatus.CREATED);
    }

    @Operation(summary = "Add an item to a shopping list")
    @PostMapping("/{id}/items")
    public ResponseEntity<ShoppingItemDTO> addItemToShoppingList(@PathVariable Long id, @RequestBody ShoppingItemDTO shoppingItemDTO) {
        ShoppingItemDTO createdItem;
        try {
            createdItem = shoppingItemService.addShoppingItem(shoppingItemDTO);
        } catch (Exception e) {
            throw Exceptions.propagate(e);
        }

        return new ResponseEntity<>(createdItem, HttpStatus.CREATED);
    }

    @Operation(summary = "Delete an item from a shopping list")
    @DeleteMapping("/{id}/items/{itemId}")
    public ResponseEntity<Void> removeItemFromShoppingList(@PathVariable Long id, @PathVariable Long itemId) {
        try {
            shoppingItemService.deleteShoppingItem(itemId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            throw Exceptions.propagate(e);
        }
    }

    @Operation(summary = "Get items from a specific shopping list")
    @GetMapping("/{listId}/items")
    public ResponseEntity<List<ShoppingItemDTO>> getItemsFromShoppingList(@PathVariable Long listId) {
        List<ShoppingItemDTO> items;
        try {
            items = shoppingItemService.getItemsByShoppingListId(listId);
        } catch (Exception e) {
            throw Exceptions.propagate(e);
        }
        return new ResponseEntity<>(items, HttpStatus.OK);
    }
}