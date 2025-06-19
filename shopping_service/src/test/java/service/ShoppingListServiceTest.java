package service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import pl.barMate.dto.ShoppingListDTO;
import pl.barMate.model.ShoppingList;
import pl.barMate.repository.ShoppingListRepository;
import pl.barMate.service.ShoppingListMapper;
import pl.barMate.service.ShoppingListService;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.*;

class ShoppingListServiceTest {

    @Mock
    private ShoppingListRepository shoppingListRepository;

    @Mock
    private ShoppingListMapper shoppingListMapper;

    @InjectMocks
    private ShoppingListService shoppingListService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldAddShoppingList() {
        // given
        ShoppingListDTO dto = new ShoppingListDTO(1L, 10L, null, null);
        ShoppingList entity = ShoppingList.builder().id(1L).userId(10L).items(null).build();

        when(shoppingListRepository.save(any())).thenReturn(entity);

        try (MockedStatic<ShoppingListMapper> mockedMapper = mockStatic(ShoppingListMapper.class)) {
            when(shoppingListMapper.toEntity(dto)).thenReturn(entity);
            when(shoppingListMapper.toDTO(entity)).thenReturn(dto);

            // when
            ShoppingListDTO result = null;
            try {
                result = shoppingListService.addShoppingList(dto);
            } catch (Exception e) {
                fail();
            }

            // then
            assertThat(result).isEqualTo(dto);
            verify(shoppingListRepository).save(entity);
        }
    }

    @Test
    void shouldUpdateShoppingList() {
        ShoppingListDTO dto = new ShoppingListDTO(2L, 20L, null, null);
        ShoppingList entity = ShoppingList.builder().id(2L).userId(20L).items(null).build();

        when(shoppingListRepository.save(any())).thenReturn(entity);

           when(shoppingListMapper.toEntity(dto)).thenReturn(entity);
           when(shoppingListMapper.toDTO(entity)).thenReturn(dto);

        ShoppingListDTO result = null;
        try {
            result = shoppingListService.updateShoppingList(dto);
        } catch (Exception e) {
            Assertions.fail();
        }

        assertThat(result).isEqualTo(dto);
            verify(shoppingListRepository).save(entity);

    }

    @Test
    void shouldDeleteShoppingList() {
        Long id = 5L;

        try {
            shoppingListService.deleteShoppingList(id);
        } catch (Exception e) {
            Assertions.fail();
        }

        verify(shoppingListRepository).deleteById(id);
    }

    @Test
    void shouldGetShoppingListsByUserId() {
        Long userId = 15L;
        ShoppingList entity = ShoppingList.builder().id(1L).userId(userId).items(null).build();
        ShoppingListDTO dto = new ShoppingListDTO(1L, userId, null, null);

        when(shoppingListRepository.findByUserId(userId)).thenReturn(List.of(entity));

        when(shoppingListMapper.toDTO(entity)).thenReturn(dto);

        List<ShoppingListDTO> result = null;
        try {
            result = shoppingListService.getShoppingListsByUserId(userId);
        } catch (Exception e) {
            Assertions.fail();
        }

        assertThat(result).containsExactly(dto);

    }

    @Test
    void shouldGetShoppingListById() {
        Long id = 99L;
        ShoppingList entity = ShoppingList.builder().id(id).userId(999L).items(null).build();
        ShoppingListDTO dto = new ShoppingListDTO(id, 999L, null, null);

        when(shoppingListRepository.findById(id)).thenReturn(Optional.of(entity));

        when(shoppingListMapper.toDTO(entity)).thenReturn(dto);

        Optional<ShoppingListDTO> result = null;
        try {
            result = shoppingListService.getShoppingListById(id);
        } catch (Exception e) {
            Assertions.fail();
        }

        assertThat(result).isPresent().contains(dto);
    }
}
