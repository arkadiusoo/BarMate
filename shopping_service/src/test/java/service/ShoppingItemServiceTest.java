package service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.barMate.dto.ShoppingItemDTO;
import pl.barMate.model.ShoppingItem;
import pl.barMate.repository.ShoppingItemRepository;
import pl.barMate.service.ShoppingItemMapper;
import pl.barMate.service.ShoppingItemService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


class ShoppingItemServiceTest {

    @Mock
    private ShoppingItemRepository shoppingItemRepository;

    @InjectMocks
    private ShoppingItemService shoppingItemService;

    private ShoppingItem shoppingItem;
    private ShoppingItemDTO shoppingItemDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        shoppingItem = ShoppingItem.builder()
                .id(1L)
                .userId(1L)
                .ingredientName("Milk")
                .amount(2.0)
                .unit("l")
                .checked(false)
                .build();

        shoppingItemDTO = ShoppingItemMapper.toDTO(shoppingItem);
    }

    @Test
    void shouldAddShoppingItem() {
        when(shoppingItemRepository.save(any(ShoppingItem.class))).thenReturn(shoppingItem);

        ShoppingItemDTO result = shoppingItemService.addShoppingItem(shoppingItemDTO);

        assertThat(result).usingRecursiveComparison().isEqualTo(shoppingItemDTO);
        verify(shoppingItemRepository, times(1)).save(any(ShoppingItem.class));
    }

    @Test
    void shouldUpdateShoppingItem() {
        when(shoppingItemRepository.save(any(ShoppingItem.class))).thenReturn(shoppingItem);

        ShoppingItemDTO result = shoppingItemService.updateShoppingItem(shoppingItemDTO);

        assertThat(result).usingRecursiveComparison().isEqualTo(shoppingItemDTO);
        verify(shoppingItemRepository, times(1)).save(any(ShoppingItem.class));
    }

    @Test
    void shouldDeleteShoppingItem() {
        Long id = 1L;

        shoppingItemService.deleteShoppingItem(id);

        verify(shoppingItemRepository, times(1)).deleteById(id);
    }

    @Test
    void shouldGetItemsByShoppingListId() {
        Long shoppingListId = 1L;
        List<ShoppingItem> items = Arrays.asList(shoppingItem);

        when(shoppingItemRepository.findByShoppingListId(shoppingListId)).thenReturn(items);

        List<ShoppingItemDTO> result = shoppingItemService.getItemsByShoppingListId(shoppingListId);

        assertThat(result).hasSize(1);
        assertThat(result.get(0)).usingRecursiveComparison().isEqualTo(shoppingItemDTO);
        verify(shoppingItemRepository, times(1)).findByShoppingListId(shoppingListId);
    }

    @Test
    void shouldGetItemsByUserId() {
        Long userId = 1L;
        List<ShoppingItem> items = Arrays.asList(shoppingItem);

        when(shoppingItemRepository.findByUserId(userId)).thenReturn(items);

        List<ShoppingItemDTO> result = shoppingItemService.getItemsByUserId(userId);

        assertThat(result).hasSize(1);
        assertThat(result.get(0)).usingRecursiveComparison().isEqualTo(shoppingItemDTO);
        verify(shoppingItemRepository, times(1)).findByUserId(userId);
    }

    @Test
    void shouldGetShoppingItemById() {
        Long id = 1L;

        when(shoppingItemRepository.findById(id)).thenReturn(Optional.of(shoppingItem));

        Optional<ShoppingItemDTO> result = shoppingItemService.getShoppingItemById(id);

        assertThat(result).usingRecursiveComparison().isEqualTo(shoppingItemDTO);
        verify(shoppingItemRepository, times(1)).findById(id);
    }

    @Test
    void shouldGetItemsByIngredientName() {
        String ingredientName = "Milk";
        List<ShoppingItem> items = Arrays.asList(shoppingItem);

        when(shoppingItemRepository.findByIngredientName(ingredientName)).thenReturn(items);

        List<ShoppingItemDTO> result = shoppingItemService.getItemsByIngredientName(ingredientName);

        assertThat(result).hasSize(1);
        assertThat(result.get(0)).usingRecursiveComparison().isEqualTo(shoppingItemDTO);
        verify(shoppingItemRepository, times(1)).findByIngredientName(ingredientName);
    }
}