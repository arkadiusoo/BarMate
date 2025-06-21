package service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import pl.barMate.dto.ShoppingItemDTO;
import pl.barMate.model.ShoppingItem;
import pl.barMate.repository.ShoppingItemRepository;
import pl.barMate.repository.ShoppingListRepository;
import pl.barMate.service.ShoppingItemMapper;
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
    private ShoppingListRepository shoppingListRepository;

    @Mock
    private ShoppingListService shoppingListService;

    @Mock
    private ShoppingItemMapper shoppingItemMapper;

    @InjectMocks
    private ShoppingItemService shoppingItemService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldAddShoppingItem() throws Exception {
        // given
        ShoppingItemDTO inputDto = new ShoppingItemDTO(1L, "Milk", 2.0, "liters", false, 1L);
        ShoppingItem entityToSave = new ShoppingItem(1L, "Milk", 2.0, "liters", false, null);
        ShoppingItem savedEntity = new ShoppingItem(1L, "Milk", 2.0, "liters", false, null);
        ShoppingItemDTO expectedDto = new ShoppingItemDTO(1L, "Milk", 2.0, "liters", false, 1L);

        when(shoppingItemMapper.toEntity(inputDto)).thenReturn(entityToSave);
        when(shoppingItemRepository.save(entityToSave)).thenReturn(savedEntity);
        when(shoppingItemMapper.toDTO(savedEntity)).thenReturn(expectedDto);

        // when
        ShoppingItemDTO result = shoppingItemService.addShoppingItem(inputDto);

        // then
        assertThat(result).isEqualTo(expectedDto);
        verify(shoppingItemMapper).toEntity(inputDto);
        verify(shoppingItemRepository).save(entityToSave);
        verify(shoppingItemMapper).toDTO(savedEntity);
    }


    @Test
    void shouldThrowWhenToEntityFails() throws Exception {
        ShoppingItemDTO dto = new ShoppingItemDTO(); // przykładowy DTO

        // Zwracanie wyjątku
        when(shoppingItemMapper.toEntity(dto)).thenThrow(new RuntimeException("Mapping failed"));

        try {
            shoppingItemService.addShoppingItem(dto);
        } catch (Exception e) {
            assertThat(e).hasMessage("Failed to add a shopping item");
        }

    }

    @Test
    void shouldThrowWhenSaveFails() throws Exception {
        ShoppingItemDTO dto = new ShoppingItemDTO();
        ShoppingItem entity = new ShoppingItem();

        when(shoppingItemMapper.toEntity(dto)).thenReturn(entity);
        when(shoppingItemRepository.save(entity)).thenThrow(new RuntimeException("DB error"));

        ShoppingItemDTO result = null;
        try {
            shoppingItemService.addShoppingItem(dto);
        } catch (Exception e) {
            assertThat(e.getMessage()).isEqualTo("Failed to add a shopping item");
        }
    }

    @Test
    void shouldUpdateShoppingItem() throws Exception {
        // given
        ShoppingItemDTO dto = new ShoppingItemDTO(1L, "Milk", 2.0, "liters", false, 1L);
        ShoppingItem entity = new ShoppingItem(1L, "Milk", 2.0, "liters", false, null);
        ShoppingItem savedEntity = new ShoppingItem(1L, "Milk", 2.0, "liters", false, null);

        when(shoppingItemMapper.toEntity(dto)).thenReturn(entity);
        when(shoppingItemRepository.save(entity)).thenReturn(savedEntity);
        when(shoppingItemMapper.toDTO(savedEntity)).thenReturn(dto);

        // when
        ShoppingItemDTO result = shoppingItemService.updateShoppingItem(dto);

            assertThat(result).isEqualTo(dto);
            verify(shoppingItemRepository).save(entity);

    }

    @Test
    void shouldDeleteShoppingItem() throws Exception {
        Long id = 5L;

        shoppingItemService.deleteShoppingItem(id);

        verify(shoppingItemRepository).deleteById(id);
    }

    @Test
    void shouldGetShoppingItemById() throws Exception {
        Long id = 3L;
        ShoppingItem entity = new ShoppingItem(3L, "Bread", 1.0, "pcs", false, null);
        ShoppingItemDTO dto = new ShoppingItemDTO(3L, "Bread", 1.0, "pcs", false, 1L);

        when(shoppingItemRepository.findById(id)).thenReturn(Optional.of(entity));

        when(shoppingItemMapper.toDTO(entity)).thenReturn(dto);

            Optional<ShoppingItemDTO> result = shoppingItemService.getShoppingItemById(id);

            assertThat(result).isPresent().contains(dto);
    }

    @Test
    void shouldGetItemsByShoppingListId() throws Exception {
        Long listId = 10L;
        ShoppingItem entity = new ShoppingItem(1L, "Eggs", 12.0, "pcs", false, null);
        ShoppingItemDTO dto = new ShoppingItemDTO(1L, "Eggs", 12.0, "pcs", false, listId);

        when(shoppingItemRepository.findByShoppingListId(listId)).thenReturn(List.of(entity));

        when(shoppingItemMapper.toDTO(entity)).thenReturn(dto);

            List<ShoppingItemDTO> result = shoppingItemService.getItemsByShoppingListId(listId);

            assertThat(result).containsExactly(dto);

    }

    @Test
    void shouldGetItemsByIngredientName() throws Exception {
        String name = "Butter";
        ShoppingItem entity = new ShoppingItem(2L, "Butter", 1.0, "pack", true, null);
        ShoppingItemDTO dto = new ShoppingItemDTO(2L, "Butter", 1.0, "pack", true, 2L);

        when(shoppingItemRepository.findByIngredientName(name)).thenReturn(List.of(entity));

        when(shoppingItemMapper.toDTO(entity)).thenReturn(dto);

            List<ShoppingItemDTO> result = shoppingItemService.getItemsByIngredientName(name);

            assertThat(result).containsExactly(dto);
    }
}
