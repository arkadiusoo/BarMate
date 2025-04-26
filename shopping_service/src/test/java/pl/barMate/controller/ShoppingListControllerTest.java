package pl.barMate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.barMate.dto.ShoppingItemDTO;
import pl.barMate.dto.ShoppingListDTO;
import pl.barMate.model.ShoppingItem;
import pl.barMate.model.ShoppingList;
import pl.barMate.service.ShoppingItemMapper;
import pl.barMate.service.ShoppingItemService;
import pl.barMate.service.ShoppingListMapper;
import pl.barMate.service.ShoppingListService;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ShoppingListControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ShoppingListService shoppingListService;

    @Mock
    private ShoppingItemService shoppingItemService;

    @InjectMocks
    private ShoppingListController shoppingListController;

    private ShoppingList shoppingList;
    private ShoppingItem shoppingItem;

    private ShoppingListDTO shoppingListDTO;
    private ShoppingItemDTO shoppingItemDTO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(shoppingListController).build();

        // Initializing test data
        shoppingList = new ShoppingList(1L, 2L, null);
        shoppingItem = new ShoppingItem(1L, "Sugar", 1.5, "kg", false, 1L, shoppingList);

        shoppingListDTO = ShoppingListMapper.toDTO(shoppingList);
        shoppingItemDTO = ShoppingItemMapper.toDTO(shoppingItem);
    }

    @Test
    public void shouldCreateShoppingList() throws Exception {
        when(shoppingListService.addShoppingList(any(ShoppingListDTO.class))).thenReturn(shoppingListDTO);

        mockMvc.perform(post("/shopping-list")
                        .contentType("application/json")
                        .content("{\"userId\": \"2\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId").value(2));
    }

    @Test
    public void shouldReturnShoppingListById() throws Exception {
        when(shoppingListService.getShoppingListById(1L)).thenReturn(java.util.Optional.of(shoppingListDTO));

        mockMvc.perform(get("/shopping-list/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    public void shouldReturnNotFoundForNonExistingShoppingList() throws Exception {
        when(shoppingListService.getShoppingListById(2L)).thenReturn(java.util.Optional.empty());

        mockMvc.perform(get("/shopping-list/{id}", 2L))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldUpdateShoppingList() throws Exception {
        when(shoppingListService.updateShoppingList(any(pl.barMate.dto.ShoppingListDTO.class))).thenReturn(shoppingListDTO);

        mockMvc.perform(put("/shopping-list/{id}", 1L)
                        .contentType("application/json")
                        .content("{\"userId\": \"2\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(2));
    }

    @Test
    public void shouldDeleteShoppingList() throws Exception {
        doNothing().when(shoppingListService).deleteShoppingList(1L);

        mockMvc.perform(delete("/shopping-list/{id}", 1L))
                .andExpect(status().isNoContent());

        verify(shoppingListService, times(1)).deleteShoppingList(1L);
    }

    @Test
    public void shouldAddItemToShoppingList() throws Exception {
        when(shoppingItemService.addShoppingItem(any(pl.barMate.dto.ShoppingItemDTO.class))).thenReturn(shoppingItemDTO);

        mockMvc.perform(post("/shopping-list/{id}/items", 1L)
                        .contentType("application/json")
                        .content("{\"ingredientName\": \"Sugar\", \"amount\": 1.5, \"unit\": \"kg\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.ingredientName").value("Sugar"));
    }

    @Test
    public void shouldRemoveItemFromShoppingList() throws Exception {
        doNothing().when(shoppingItemService).deleteShoppingItem(1L);

        mockMvc.perform(delete("/shopping-list/{id}/items/{itemId}", 1L, 1L))
                .andExpect(status().isNoContent());

        verify(shoppingItemService, times(1)).deleteShoppingItem(1L);
    }
}
