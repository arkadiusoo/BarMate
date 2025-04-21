package pl.barMate.inventory.service;

import pl.barMate.inventory.model.Ingredient;
import pl.barMate.inventory.model.IngredientCategory;
import pl.barMate.inventory.repository.IngredientRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

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
}
