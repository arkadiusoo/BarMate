package model;


import org.junit.jupiter.api.Test;
import pl.barMate.model.ShoppingItem;
import pl.barMate.model.ShoppingList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.util.Arrays;
import java.util.List;

class ShoppingListTest {

    @Test
    void shouldCreateShoppingListUsingBuilder() {
        ShoppingList list = ShoppingList.builder()
                .id(5L)
                .userId(10L)
                .build();

        assertThat(list.getId()).isEqualTo(5L);
        assertThat(list.getUserId()).isEqualTo(10L);
    }

    @Test
    void shouldCreateShoppingListWithNoArguments() {
        ShoppingList list = ShoppingList.builder().build();
        assertThat(list).isNotNull();
    }

    @Test
    void shouldSetAndGetId() {
        ShoppingList list = ShoppingList.builder().id(1L).build();
        assertThat(list.getId()).isEqualTo(1L);
        list.setId(2L);
        assertThat(list.getId()).isEqualTo(2L);
    }

    @Test
    void shouldSetAndGetUserId() {
        ShoppingList list = ShoppingList.builder().userId(1L).build();
        assertThat(list.getUserId()).isEqualTo(1L);
        list.setUserId(2L);
        assertThat(list.getUserId()).isEqualTo(2L);
    }

    @Test
    void shouldSetAndGetItems() {
        ShoppingItem item1 = ShoppingItem.builder().id(1L).ingredientName("Bread").build();
        ShoppingItem item2 = ShoppingItem.builder().id(2L).ingredientName("Butter").build();

        ShoppingList list = new ShoppingList();
        list.setItems(Arrays.asList(item1, item2));

        List<ShoppingItem> items = list.getItems();
        assertThat(items).hasSize(2);
        assertThat(items).extracting(ShoppingItem::getIngredientName)
                .containsExactlyInAnyOrder("Bread", "Butter");
    }
}
