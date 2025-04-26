package service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.barMate.dto.ShoppingItemDTO;
import pl.barMate.dto.ShoppingListDTO;
import pl.barMate.model.ShoppingItem;
import pl.barMate.model.ShoppingList;
import pl.barMate.repository.ShoppingItemRepository;
import pl.barMate.repository.ShoppingListRepository;
import pl.barMate.service.ShoppingItemMapper;
import pl.barMate.service.ShoppingItemService;
import pl.barMate.service.ShoppingListMapper;
import pl.barMate.service.ShoppingListService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ShoppingListServiceTest {@Mock
private ShoppingListRepository shoppingListRepository;

    private MockMvc mockMvc;

    @InjectMocks
    private ShoppingListService shoppingListService;

    private ShoppingList shoppingList;
    private ShoppingItem shoppingItem;

    private ShoppingListDTO shoppingListDTO;
    private ShoppingItemDTO shoppingItemDTO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initializing test data
        shoppingList = new ShoppingList(1L, 2L, null);
        shoppingItem = new ShoppingItem(1L, "Sugar", 1.5, "kg", false, 1L, shoppingList);

        shoppingItemDTO = ShoppingItemMapper.toDTO(shoppingItem);
        shoppingListDTO = ShoppingListMapper.toDTO(shoppingList);
    }

    @Test
    public void shouldAddShoppingListTest() throws Exception {
        when(shoppingListRepository.save(any(ShoppingList.class))).thenReturn(shoppingList);

        Optional<ShoppingListDTO> result = Optional.ofNullable(shoppingListService.addShoppingList(shoppingListDTO));

        assertThat(result).isEqualTo(shoppingList);
        verify(shoppingListRepository, times(1)).save(shoppingList);
    }

    @Test
    public void shouldUpdateShoppingList() throws Exception {
        when(shoppingListRepository.save(any(ShoppingList.class))).thenReturn(shoppingList);
        Optional<ShoppingListDTO> result = Optional.ofNullable(shoppingListService.updateShoppingList(shoppingListDTO));
        assertThat(result).isEqualTo(shoppingList);
        verify(shoppingListRepository, times(1)).save(shoppingList);
    }

    @Test
    public void shouldDeleteShoppingList() throws Exception {
        when(shoppingListRepository.existsById(shoppingList.getId())).thenReturn(true);
        shoppingListService.deleteShoppingList(shoppingList.getId());
        verify(shoppingListRepository, times(1)).deleteById(shoppingList.getId());
    }

    @Test
    public void shouldGetShoppingListByIdTest() throws Exception {
        Long shoppingListId = 1L;

        when(shoppingListRepository.findById(shoppingListId)).thenReturn(Optional.of(shoppingList));

        Optional<ShoppingListDTO> result = shoppingListService.getShoppingListById(shoppingListId);

        assertThat(result.get()).isEqualTo(shoppingList);
        verify(shoppingListRepository, times(1)).findById(shoppingListId);
    }

}
