package pl.barMate.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.barMate.dto.ShoppingItemDTO;
import pl.barMate.dto.ShoppingListDTO;
import pl.barMate.dto.ShoppingItemDTO;
import pl.barMate.model.ShoppingList;
import pl.barMate.service.ShoppingItemService;
import pl.barMate.service.ShoppingListService;

import java.util.Optional;

@RestController
@RequestMapping("/shopping-list")
@RequiredArgsConstructor
public class ShoppingListController {
    private final ShoppingListService shoppingListService;
    private final ShoppingItemService shoppingItemService;


    @Operation(summary = "Create a new shopping list")
    @PostMapping
    public ResponseEntity<ShoppingListDTO> addShoppingList(@RequestBody ShoppingListDTO shoppingListDTO) {
        ShoppingListDTO createdList = shoppingListService.addShoppingList(shoppingListDTO);
        return new ResponseEntity<>(createdList, HttpStatus.CREATED);
    }

    @Operation(summary = "Get a shopping list by id")
    @GetMapping("/{id}")
    public ResponseEntity<ShoppingListDTO> createShoppingList(@PathVariable Long id)
    {
        Optional<ShoppingListDTO> shoppingListDTO = shoppingListService.getShoppingListById(id);
        return shoppingListDTO.map(ResponseEntity::ok).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "Update a shopping list")
    @PutMapping("/{id}")
    public ResponseEntity<ShoppingListDTO> updateShoppingList(@RequestBody ShoppingListDTO shoppingListDTO, @PathVariable Long id)
    {
        shoppingListDTO.setId(id);
        ShoppingListDTO updatedList = shoppingListService.updateShoppingList(shoppingListDTO);
        return new ResponseEntity<>(updatedList, HttpStatus.OK);
    }

    @Operation(summary = "Delete a shopping list")
    @DeleteMapping("/{id}")
    public ResponseEntity<ShoppingListDTO> deleteShoppingList(@PathVariable Long id) {
        shoppingListService.deleteShoppingList(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Add an item to a shopping list")
    @PostMapping("/{id}/items")
    public ResponseEntity<ShoppingItemDTO> addItemToShoppingList(@PathVariable Long id, @RequestBody ShoppingItemDTO shoppingItemDTO) {
        // shoppingItem.setId(id);
        ShoppingItemDTO createdItem = shoppingItemService.addShoppingItem(shoppingItemDTO);

        return new ResponseEntity<>(createdItem, HttpStatus.CREATED);
    }

    @Operation(summary = "Delete an item from a shopping list")
    @DeleteMapping("/{id}/items/{itemId}")
    public ResponseEntity<Void> removeItemFromShoppingList(@PathVariable Long id, @PathVariable Long itemId) {
        shoppingItemService.deleteShoppingItem(itemId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}