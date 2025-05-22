package service;

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
import static org.mockito.Mockito.*;

class ShoppingListServiceTest {

    @Mock
    private ShoppingListRepository shoppingListRepository;

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
            mockedMapper.when(() -> ShoppingListMapper.toEntity(dto)).thenReturn(entity);
            mockedMapper.when(() -> ShoppingListMapper.toDTO(entity)).thenReturn(dto);

            // when
            ShoppingListDTO result = shoppingListService.addShoppingList(dto);

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

        try (MockedStatic<ShoppingListMapper> mockedMapper = mockStatic(ShoppingListMapper.class)) {
            mockedMapper.when(() -> ShoppingListMapper.toEntity(dto)).thenReturn(entity);
            mockedMapper.when(() -> ShoppingListMapper.toDTO(entity)).thenReturn(dto);

            ShoppingListDTO result = shoppingListService.updateShoppingList(dto);

            assertThat(result).isEqualTo(dto);
            verify(shoppingListRepository).save(entity);
        }
    }

    @Test
    void shouldDeleteShoppingList() {
        Long id = 5L;

        shoppingListService.deleteShoppingList(id);

        verify(shoppingListRepository).deleteById(id);
    }

    @Test
    void shouldGetShoppingListsByUserId() {
        Long userId = 15L;
        ShoppingList entity = ShoppingList.builder().id(1L).userId(userId).items(null).build();
        ShoppingListDTO dto = new ShoppingListDTO(1L, userId, null, null);

        when(shoppingListRepository.findByUserId(userId)).thenReturn(List.of(entity));

        try (MockedStatic<ShoppingListMapper> mockedMapper = mockStatic(ShoppingListMapper.class)) {
            mockedMapper.when(() -> ShoppingListMapper.toDTO(entity)).thenReturn(dto);

            List<ShoppingListDTO> result = shoppingListService.getShoppingListsByUserId(userId);

            assertThat(result).containsExactly(dto);
        }
    }

    @Test
    void shouldGetShoppingListById() {
        Long id = 99L;
        ShoppingList entity = ShoppingList.builder().id(id).userId(999L).items(null).build();
        ShoppingListDTO dto = new ShoppingListDTO(id, 999L, null, null);

        when(shoppingListRepository.findById(id)).thenReturn(Optional.of(entity));

        try (MockedStatic<ShoppingListMapper> mockedMapper = mockStatic(ShoppingListMapper.class)) {
            mockedMapper.when(() -> ShoppingListMapper.toDTO(entity)).thenReturn(dto);

            Optional<ShoppingListDTO> result = shoppingListService.getShoppingListById(id);

            assertThat(result).isPresent().contains(dto);
        }
    }
}
