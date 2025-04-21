package pl.barMate.inventory.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.barMate.inventory.service.IngredientService;

import static org.mockito.Mockito.mock;

@Configuration
public class IngredientTestConfig {

    @Bean
    public IngredientService ingredientService() {
        return mock(IngredientService.class);
    }
}
