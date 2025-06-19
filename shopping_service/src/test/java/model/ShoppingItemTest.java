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
    void shouldCreateShoppingItemWithNoArguments() {
        ShoppingItem shoppingItem = new ShoppingItem().builder().build();
        assertThat(shoppingItem).isNotNull();
    }

    @Test
    void shouldSetAndGetId() {
        ShoppingItem shoppingItem = new ShoppingItem().builder().id(1L).build();
        assertThat(shoppingItem.getId()).isEqualTo(1L);
        shoppingItem.setId(2L);
        assertThat(shoppingItem.getId()).isEqualTo(2L);
    }

    @Test
    void shouldSetAndGetIngredientName() {
        ShoppingItem shoppingItem = new ShoppingItem().builder().id(1L).ingredientName("water").build();
        assertThat(shoppingItem.getIngredientName()).isEqualTo("water");
        shoppingItem.setIngredientName("sparkling water");
        assertThat(shoppingItem.getIngredientName()).isEqualTo("sparkling water");
    }

    @Test
    void shouldSetAndGetAmount() {
        ShoppingItem shoppingItem = new ShoppingItem().builder().id(1L).amount(2.0).build();
        assertThat(shoppingItem.getAmount()).isEqualTo(2.0);
        shoppingItem.setAmount(3.0);
        assertThat(shoppingItem.getAmount()).isEqualTo(3.0);
    }

    @Test
    void shouldSetAndGetUnit() {
        ShoppingItem shoppingItem = new ShoppingItem().builder().id(1L).unit("l").build();
        assertThat(shoppingItem.getUnit()).isEqualTo("l");
        shoppingItem.setUnit("ml");
        assertThat(shoppingItem.getUnit()).isEqualTo("ml");
    }

    @Test
    void shouldSetAndGetChecked() {
        ShoppingItem shoppingItem = new ShoppingItem().builder().id(1L).checked(true).build();
        assertThat(shoppingItem.getChecked()).isTrue();
        shoppingItem.setChecked(false);
        assertThat(shoppingItem.getChecked()).isFalse();
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
