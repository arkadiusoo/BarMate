package model;

import org.junit.jupiter.api.Test;
import pl.barMate.model.ShoppingItem;
import pl.barMate.model.ShoppingList;

import static org.assertj.core.api.Assertions.assertThat;


class ShoppingItemTest {

    @Test
    void shouldCreateShoppingItemUsingBuilder() {
        ShoppingItem item = ShoppingItem.builder()
                .id(1L)
                .ingredientName("Milk")
                .amount(2.0)
                .unit("liters")
                .checked(false)
                //.userId(10L)
                .shoppingList(new ShoppingList())
                .build();

        assertThat(item.getId()).isEqualTo(1L);
        assertThat(item.getIngredientName()).isEqualTo("Milk");
        assertThat(item.getAmount()).isEqualTo(2.0);
        assertThat(item.getUnit()).isEqualTo("liters");
        assertThat(item.getChecked()).isFalse();
        assertThat(item.getShoppingList()).isNotNull();
    }

    @Test
    void shouldSetAndGetShoppingList() {
        ShoppingList list = ShoppingList.builder()
                .id(5L)
                .userId(10L)
                .build();

        ShoppingItem item = new ShoppingItem();
        item.setShoppingList(list);

        assertThat(item.getShoppingList()).isEqualTo(list);
    }
}
