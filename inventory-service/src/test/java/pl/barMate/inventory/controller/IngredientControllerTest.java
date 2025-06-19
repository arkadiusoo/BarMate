package pl.barMate.inventory.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.when;

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

    @Test
    void shouldReturnIngredientByName() throws Exception {
        String name = "Sugar";
        Ingredient ingredient = new Ingredient(1L, "Sugar", IngredientCategory.OTHER, 2.0, "kg");

        when(ingredientService.getIngredientByName(name)).thenReturn(ingredient);

        mockMvc.perform(get("/ingredients/name/{name}", name))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(name));
    }

    @Test
    void shouldSubtractIngredientAmountSuccessfully() throws Exception {
        // Given
        String name = "Sugar";
        double updatedAmount = 3.0;
        Ingredient ingredient = new Ingredient(1L, name, IngredientCategory.OTHER, updatedAmount, "kg");

        when(ingredientService.subtractIngredientAmount(name, 2.0)).thenReturn(Optional.of(ingredient));

        // When + Then
        mockMvc.perform(put("/ingredients/update-by-name")
                        .param("name", name)
                        .param("amount", "2.0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Sugar"))
                .andExpect(jsonPath("$.amount").value(3.0));
    }

    @Test
    void shouldReturnNotFoundWhenIngredientDoesNotExist() throws Exception {
        // Given
        String name = "Unknown";

        when(ingredientService.subtractIngredientAmount(name, 1.0))
                .thenThrow(new EntityNotFoundException("Ingredient not found with name: " + name));

        // When + Then
        mockMvc.perform(put("/ingredients/update-by-name")
                        .param("name", name)
                        .param("amount", "1.0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnBadRequestWhenSubtractedAmountExceedsCurrentAmount() throws Exception {
        // Given
        String name = "Sugar";

        when(ingredientService.subtractIngredientAmount(name, 5.0))
                .thenThrow(new IllegalArgumentException("Insufficient amount of ingredient: " + name));

        // When + Then
        mockMvc.perform(put("/ingredients/update-by-name")
                        .param("name", name)
                        .param("amount", "5.0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnShortagesCorrectly() throws Exception {
        // Request list (doesn't matter if unit/category is null)
        Ingredient vodkaRequest = new Ingredient(null, "Vodka", null, 5.0, null);
        Ingredient limeRequest = new Ingredient(null, "Lime", null, 10.0, null);
        List<Ingredient> requestList = List.of(vodkaRequest, limeRequest);

        // Expected shortage
        Ingredient limeShortage = new Ingredient(null, "Lime", IngredientCategory.FRUIT, 5.0, "pcs");
        List<Ingredient> shortageList = List.of(limeShortage);

        when(ingredientService.checkIngredientShortages(Mockito.anyList())).thenReturn(shortageList);

        var response = mockMvc.perform(post("/ingredients/check-shortages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestList)))
                .andExpect(status().isOk())
                .andReturn();

        String content = response.getResponse().getContentAsString();

        assertThat(content).contains("\"name\":\"Lime\"");
        assertThat(content).contains("\"amount\":5.0");
        assertThat(content).doesNotContain("\"name\":\"Vodka\"");
    }


}