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

    /*
    @Operation(summary = "Create a new shopping list")
    @PostMapping
    public ResponseEntity<ShoppingListDTO> addShoppingList(@RequestBody ShoppingListDTO shoppingListDTO) {
        ShoppingListDTO createdList = shoppingListService.addShoppingList(shoppingListDTO);
        return new ResponseEntity<>(createdList, HttpStatus.CREATED);
    }*/

    @Operation(summary = "Get a shopping list by id")
    @GetMapping("/{id}")
    public ResponseEntity<List<ShoppingListDTO>> getShoppingList(@PathVariable Long id)
    {
        //Optional<ShoppingListDTO> shoppingListDTO = shoppingListService.getShoppingListById(id);
        //System.out.println(shoppingListDTO);

        List<ShoppingListDTO> lists = shoppingListService.getShoppingListsByUserId(id);
        return new ResponseEntity<>(lists, HttpStatus.OK);
        //return shoppingListDTO.map(ResponseEntity::ok).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
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

    @Operation(summary = "Create a shopping list")
    @PostMapping()
    public ResponseEntity<ShoppingListDTO> createShoppingList(@RequestBody Long userId) {
        List<ShoppingListDTO> userLists = shoppingListService.getShoppingListsByUserId(userId);

        Long newId = userLists.isEmpty()
                ? 1L
                : userLists.get(userLists.size() - 1).getId() + 1;

        ShoppingListDTO shoppingList = new ShoppingListDTO(
                null,
                userId,
                new ArrayList<>(),
                LocalDate.now()
        );        //return new ResponseEntity<>(shoppingList, HttpStatus.CREATED);
        ShoppingListDTO list = shoppingListService.addShoppingList(shoppingList);
        return new ResponseEntity<>(list, HttpStatus.CREATED);
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