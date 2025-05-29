package pl.barMate.inventory.service;

import jakarta.persistence.EntityNotFoundException;
import pl.barMate.inventory.model.Ingredient;
import pl.barMate.inventory.model.IngredientCategory;
import pl.barMate.inventory.repository.IngredientRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class IngredientServiceTest {

    @Mock
    private IngredientRepository repository;

    @InjectMocks
    private IngredientService service;

    @Test
    void shouldCreateIngredient() {
        Ingredient ingredient = new Ingredient(null, "Rum", IngredientCategory.ALCOHOL, 1.0, "L");
        Ingredient saved = new Ingredient(1L, "Rum", IngredientCategory.ALCOHOL, 1.0, "L");

        when(repository.save(ingredient)).thenReturn(saved);

        Ingredient result = service.createIngredient(ingredient);

        // Asercje za pomocą AssertJ
        assertThat(result)
                .isNotNull()
                .extracting(Ingredient::getId, Ingredient::getName)
                .containsExactly(1L, "Rum");
    }

    @Test
    void shouldReturnIngredientById() {
        Ingredient ingredient = new Ingredient(1L, "Cola", IngredientCategory.MIXER, 2.0, "L");
        when(repository.findById(1L)).thenReturn(Optional.of(ingredient));

        Ingredient found = service.getIngredientById(1L);

        // Asercje za pomocą AssertJ
        assertThat(found)
                .isNotNull()
                .extracting(Ingredient::getName)
                .isEqualTo("Cola");
    }

    @Test
    void shouldThrowWhenIngredientNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // Sprawdzamy, czy rzucony jest wyjątek EntityNotFoundException
        assertThatThrownBy(() -> service.getIngredientById(99L))
                .isInstanceOf(jakarta.persistence.EntityNotFoundException.class)
                .hasMessageContaining("Ingredient not found");
    }

    @Test
    void shouldUpdateIngredient() {
        Ingredient existing = new Ingredient(1L, "Lime", IngredientCategory.FRUIT, 5.0, "pieces");
        Ingredient update = new Ingredient(null, "Lime", IngredientCategory.FRUIT, 10.0, "pieces");

        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Ingredient result = service.updateIngredient(1L, update);

        // Asercje za pomocą AssertJ
        assertThat(result)
                .isNotNull()
                .extracting(Ingredient::getAmount)
                .isEqualTo(10.0);
    }

    @Test
    void shouldDeleteIngredient() {
        when(repository.existsById(1L)).thenReturn(true);

        service.deleteIngredient(1L);

        // Weryfikujemy, czy metoda deleteById została wywołana
        verify(repository).deleteById(1L);
    }

    @Test
    void shouldGetIngredientByName() {
        String name = "Salt";
        Ingredient ingredient = new Ingredient(1L, "Salt", IngredientCategory.OTHER, 1.0, "kg");

        when(repository.findByName(name)).thenReturn(Optional.of(ingredient));

        Ingredient result = service.getIngredientByName(name);

        assertNotNull(result);
        assertEquals(name, result.getName());
    }

    @Test
    void shouldSubtractIngredientAmountSuccessfully() {
        // Given
        String ingredientName = "Sugar";
        double initialAmount = 5.0;
        double amountToSubtract = 2.0;

        Ingredient ingredient = new Ingredient(1L, ingredientName, IngredientCategory.OTHER, initialAmount, "kg");

        when(repository.findByName(ingredientName)).thenReturn(Optional.of(ingredient));
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Ingredient updatedIngredient = service.subtractIngredientAmount(ingredientName, amountToSubtract);

        // Then
        assertThat(updatedIngredient.getAmount()).isEqualTo(3.0);
        verify(repository, times(1)).findByName(ingredientName);
        verify(repository, times(1)).save(ingredient);
    }

    @Test
    void shouldThrowEntityNotFoundExceptionWhenIngredientNotFound() {
        // Given
        String ingredientName = "Unknown";
        double amountToSubtract = 1.0;

        when(repository.findByName(ingredientName)).thenReturn(Optional.empty());

        // Then
        assertThatThrownBy(() -> service.subtractIngredientAmount(ingredientName, amountToSubtract))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Ingredient not found with name: " + ingredientName);
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenSubtractedAmountExceedsCurrentAmount() {
        // Given
        String ingredientName = "Sugar";
        double initialAmount = 2.0;
        double amountToSubtract = 5.0;

        Ingredient ingredient = new Ingredient(1L, ingredientName, IngredientCategory.OTHER, initialAmount, "kg");

        when(repository.findByName(ingredientName)).thenReturn(Optional.of(ingredient));

        // Then
        assertThatThrownBy(() -> service.subtractIngredientAmount(ingredientName, amountToSubtract))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Insufficient amount of ingredient: " + ingredientName);

        verify(repository, times(1)).findByName(ingredientName);
        verify(repository, times(0)).save(any());
    }

    @Test
    void shouldReturnOnlyIngredientsWithShortages() {
        // Given
        Ingredient vodkaFromDb = new Ingredient(1L, "Vodka", IngredientCategory.ALCOHOL, 10.0, "ml");
        Ingredient limeFromDb = new Ingredient(2L, "Lime", IngredientCategory.FRUIT, 5.0, "pcs");

        Ingredient vodkaRequest = new Ingredient(null, "Vodka", null, 5.0, null); // wystarczy
        Ingredient limeRequest = new Ingredient(null, "Lime", null, 10.0, null); // brakuje

        when(repository.findByName("Vodka")).thenReturn(Optional.of(vodkaFromDb));
        when(repository.findByName("Lime")).thenReturn(Optional.of(limeFromDb));

        // When
        List<Ingredient> result = service.checkIngredientShortages(List.of(vodkaRequest, limeRequest));

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Lime");
        assertThat(result.get(0).getAmount()).isEqualTo(5.0); // brakująca ilość
        assertThat(result.get(0).getUnit()).isEqualTo("pcs");
    }

}