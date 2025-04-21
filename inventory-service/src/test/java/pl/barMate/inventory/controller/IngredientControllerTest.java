package pl.barMate.inventory.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import pl.barMate.inventory.config.IngredientTestConfig;
import pl.barMate.inventory.model.Ingredient;
import pl.barMate.inventory.model.IngredientCategory;
import pl.barMate.inventory.service.IngredientService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(controllers = IngredientController.class)
@ContextConfiguration(classes = {IngredientController.class, IngredientTestConfig.class})
@AutoConfigureMockMvc(addFilters = false)
class IngredientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private IngredientService ingredientService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateIngredient() throws Exception {
        // given
        Ingredient ingredient = new Ingredient();
        ingredient.setId(1L);
        ingredient.setName("Vodka");
        ingredient.setAmount(5);
        ingredient.setCategory(IngredientCategory.ALCOHOL);
        ingredient.setUnit("ml");

        Mockito.when(ingredientService.createIngredient(Mockito.any(Ingredient.class)))
                .thenReturn(ingredient);

        // when + then
        var result = mockMvc.perform(post("/ingredients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ingredient)))
                .andReturn();

        String content = result.getResponse().getContentAsString();

        // using AssertJ to verify the response content
        assertThat(result.getResponse().getStatus()).isEqualTo(200);
        assertThat(content).contains("\"id\":1");
        assertThat(content).contains("\"name\":\"Vodka\"");
        assertThat(content).contains("\"amount\":5");
    }

    @Test
    void testGetAllIngredients() throws Exception {
        Ingredient ingredient = Ingredient.builder()
                .id(1L)
                .name("Vodka")
                .amount(5)
                .category(IngredientCategory.ALCOHOL)
                .unit("ml")
                .build();

        Mockito.when(ingredientService.getAllIngredients())
                .thenReturn(List.of(ingredient));

        var result = mockMvc.perform(get("/ingredients"))
                .andReturn();

        String content = result.getResponse().getContentAsString();

        // AssertJ assertions
        assertThat(result.getResponse().getStatus()).isEqualTo(200);
        assertThat(content).contains("\"id\":1");
        assertThat(content).contains("\"name\":\"Vodka\"");
    }

    @Test
    void testGetIngredientById() throws Exception {
        Ingredient ingredient = Ingredient.builder()
                .id(1L)
                .name("Vodka")
                .amount(5)
                .category(IngredientCategory.ALCOHOL)
                .unit("ml")
                .build();

        Mockito.when(ingredientService.getIngredientById(1L))
                .thenReturn(ingredient);

        var result = mockMvc.perform(get("/ingredients/1"))
                .andReturn();

        String content = result.getResponse().getContentAsString();

        // AssertJ assertions
        assertThat(result.getResponse().getStatus()).isEqualTo(200);
        assertThat(content).contains("\"id\":1");
        assertThat(content).contains("\"name\":\"Vodka\"");
    }

    @Test
    void testUpdateIngredient() throws Exception {
        Ingredient ingredient = Ingredient.builder()
                .id(1L)
                .name("Vodka")
                .amount(10)
                .category(IngredientCategory.ALCOHOL)
                .unit("ml")
                .build();

        Mockito.when(ingredientService.updateIngredient(Mockito.eq(1L), Mockito.any(Ingredient.class)))
                .thenReturn(ingredient);

        var result = mockMvc.perform(put("/ingredients/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ingredient)))
                .andReturn();

        String content = result.getResponse().getContentAsString();

        // AssertJ assertions
        assertThat(result.getResponse().getStatus()).isEqualTo(200);
        assertThat(content).contains("\"amount\":10");
    }

    @Test
    void testDeleteIngredient() throws Exception {
        Mockito.doNothing().when(ingredientService).deleteIngredient(1L);

        var result = mockMvc.perform(delete("/ingredients/1"))
                .andReturn();

        // AssertJ assertions
        assertThat(result.getResponse().getStatus()).isEqualTo(204); // No Content
    }
}
