package pl.barMate.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.barMate.model.ShoppingItem;
import pl.barMate.model.ShoppingList;
import pl.barMate.repository.ShoppingListRepository;
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
    public ResponseEntity<ShoppingList> addShoppingList(@RequestBody ShoppingList shoppingList) {
        ShoppingList createdList = shoppingListService.addShoppingList(shoppingList);
        return new ResponseEntity<>(createdList, HttpStatus.CREATED);
    }

    @Operation(summary = "Get a shopping list by id")
    @GetMapping("/{id}")
    public ResponseEntity<ShoppingList> createShoppingList(@RequestBody Long id)
    {
        Optional<ShoppingList> shoppingList = shoppingListService.getShoppingListById(id);
        return shoppingList.map(ResponseEntity::ok).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "Update a shopping list")
    @PutMapping("/{id}")
    public ResponseEntity<ShoppingList> updateShoppingList(@RequestBody ShoppingList shoppingList, @PathVariable Long id)
    {
        shoppingList.setId(id);
        ShoppingList updatedList = shoppingListService.updateShoppingList(shoppingList);
        return new ResponseEntity<>(updatedList, HttpStatus.OK);
    }

    @Operation(summary = "Delete a shopping list")
    @DeleteMapping("/{id}")
    public ResponseEntity<ShoppingList> deleteShoppingList(@PathVariable Long id) {
        shoppingListService.deleteShoppingList(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Add an item to a shopping list")
    @PostMapping("/{id}/items")
    public ResponseEntity<ShoppingItem> addItemToShoppingList(@PathVariable Long id, @RequestBody ShoppingItem shoppingItem) {
        // shoppingItem.setId(id);
        ShoppingItem createdItem = shoppingItemService.addShoppingItem(shoppingItem);
        return new ResponseEntity<>(createdItem, HttpStatus.CREATED);
    }

    @Operation(summary = "Delete an item from a shopping list")
    @DeleteMapping("/{id}/items/{itemId}")
    public ResponseEntity<Void> removeItemFromShoppingList(@PathVariable Long id, @PathVariable Long itemId) {
        shoppingItemService.deleteShoppingItem(itemId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
