Feature: Ingredient model validation

  Scenario: Create valid ingredient
    Given I have an ingredient with name "Whiskey", category "ALCOHOL", amount 1.5 and unit "l"
    When I validate the ingredient
    Then the ingredient should be valid

  Scenario: Create invalid ingredient with blank name
    Given I have an ingredient with name "", category "ALCOHOL", amount 1.5 and unit "l"
    When I validate the ingredient
    Then the ingredient should be invalid
