package service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import pl.barMate.dto.ShoppingItemDTO;
import pl.barMate.model.ShoppingItem;
import pl.barMate.repository.ShoppingItemRepository;
import pl.barMate.service.ShoppingItemService;
import pl.barMate.service.ShoppingListService;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ShoppingItemServiceTest {

    @Mock
    private ShoppingItemRepository shoppingItemRepository;

    @Mock
    private ShoppingListService shoppingListService;

    @InjectMocks
    private ShoppingItemService shoppingItemService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldAddShoppingItem() {
        // given
        ShoppingItemDTO dto = new ShoppingItemDTO(1L, "Milk", 2.0, "liters", false, 1L);
        ShoppingItem entity = new ShoppingItem(1L, "Milk", 2.0, "liters", false, null); // Zakładamy konstruktor
        when(shoppingItemRepository.save(any(ShoppingItem.class))).thenReturn(entity);

        try (MockedStatic<pl.barMate.service.ShoppingItemMapper> mockedMapper = mockStatic(pl.barMate.service.ShoppingItemMapper.class)) {
            mockedMapper.when(() -> pl.barMate.service.ShoppingItemMapper.toEntity(dto)).thenReturn(entity);
            mockedMapper.when(() -> pl.barMate.service.ShoppingItemMapper.toDTO(entity)).thenReturn(dto);

            // when
            ShoppingItemDTO result = shoppingItemService.addShoppingItem(dto);

            // then
            assertThat(result).isEqualTo(dto);
            verify(shoppingItemRepository).save(entity);
        }
    }

    @Test
    void shouldUpdateShoppingItem() {
        ShoppingItemDTO dto = new ShoppingItemDTO(1L, "Sugar", 1.0, "kg", true, 2L);
        ShoppingItem entity = new ShoppingItem(1L, "Sugar", 1.0, "kg", true, null);

        when(shoppingItemRepository.save(any())).thenReturn(entity);

        try (MockedStatic<pl.barMate.service.ShoppingItemMapper> mockedMapper = mockStatic(pl.barMate.service.ShoppingItemMapper.class)) {
            mockedMapper.when(() -> pl.barMate.service.ShoppingItemMapper.toEntity(dto)).thenReturn(entity);
            mockedMapper.when(() -> pl.barMate.service.ShoppingItemMapper.toDTO(entity)).thenReturn(dto);

            ShoppingItemDTO result = shoppingItemService.updateShoppingItem(dto);

            assertThat(result).isEqualTo(dto);
            verify(shoppingItemRepository).save(entity);
        }
    }

    @Test
    void shouldDeleteShoppingItem() {
        Long id = 5L;

        shoppingItemService.deleteShoppingItem(id);

        verify(shoppingItemRepository).deleteById(id);
    }

    @Test
    void shouldGetShoppingItemById() {
        Long id = 3L;
        ShoppingItem entity = new ShoppingItem(3L, "Bread", 1.0, "pcs", false, null);
        ShoppingItemDTO dto = new ShoppingItemDTO(3L, "Bread", 1.0, "pcs", false, 1L);

        when(shoppingItemRepository.findById(id)).thenReturn(Optional.of(entity));

        try (MockedStatic<pl.barMate.service.ShoppingItemMapper> mockedMapper = mockStatic(pl.barMate.service.ShoppingItemMapper.class)) {
            mockedMapper.when(() -> pl.barMate.service.ShoppingItemMapper.toDTO(entity)).thenReturn(dto);

            Optional<ShoppingItemDTO> result = shoppingItemService.getShoppingItemById(id);

            assertThat(result).isPresent().contains(dto);
        }
    }

    @Test
    void shouldGetItemsByShoppingListId() {
        Long listId = 10L;
        ShoppingItem entity = new ShoppingItem(1L, "Eggs", 12.0, "pcs", false, null);
        ShoppingItemDTO dto = new ShoppingItemDTO(1L, "Eggs", 12.0, "pcs", false, listId);

        when(shoppingItemRepository.findByShoppingListId(listId)).thenReturn(List.of(entity));

        try (MockedStatic<pl.barMate.service.ShoppingItemMapper> mockedMapper = mockStatic(pl.barMate.service.ShoppingItemMapper.class)) {
            mockedMapper.when(() -> pl.barMate.service.ShoppingItemMapper.toDTO(entity)).thenReturn(dto);

            List<ShoppingItemDTO> result = shoppingItemService.getItemsByShoppingListId(listId);

            assertThat(result).containsExactly(dto);
        }
    }

    @Test
    void shouldGetItemsByIngredientName() {
        String name = "Butter";
        ShoppingItem entity = new ShoppingItem(2L, "Butter", 1.0, "pack", true, null);
        ShoppingItemDTO dto = new ShoppingItemDTO(2L, "Butter", 1.0, "pack", true, 2L);

        when(shoppingItemRepository.findByIngredientName(name)).thenReturn(List.of(entity));

        try (MockedStatic<pl.barMate.service.ShoppingItemMapper> mockedMapper = mockStatic(pl.barMate.service.ShoppingItemMapper.class)) {
            mockedMapper.when(() -> pl.barMate.service.ShoppingItemMapper.toDTO(entity)).thenReturn(dto);

            List<ShoppingItemDTO> result = shoppingItemService.getItemsByIngredientName(name);

            assertThat(result).containsExactly(dto);
        }
    }
}
