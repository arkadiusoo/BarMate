package pl.barMate.inventory.cucumber;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import pl.barMate.inventory.model.Ingredient;
import pl.barMate.inventory.model.IngredientCategory;
import io.cucumber.java.en.*;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class IngredientValidationSteps {

    private Ingredient ingredient;
    private Set<ConstraintViolation<Ingredient>> violations;

    @Given("I have an ingredient with name {string}, category {string}, amount {double} and unit {string}")
    public void i_have_an_ingredient(String name, String category, double amount, String unit) {
        this.ingredient = Ingredient.builder()
                .name(name)
                .category(IngredientCategory.valueOf(category))
                .amount(amount)
                .unit(unit)
                .build();
    }

    @When("I validate the ingredient")
    public void i_validate_the_ingredient() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        violations = validator.validate(ingredient);
    }

    @Then("the ingredient should be valid")
    public void the_ingredient_should_be_valid() {
        assertTrue(violations.isEmpty(), "Expected no validation violations, but found: " + violations);
    }

    @Then("the ingredient should be invalid")
    public void the_ingredient_should_be_invalid() {
        assertFalse(violations.isEmpty(), "Expected validation violations but none found");
    }
}
