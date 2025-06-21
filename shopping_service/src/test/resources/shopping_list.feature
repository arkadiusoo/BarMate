Feature: Shopping list item management

  Scenario: User checks off an unchecked item
    Given user creates a shopping list with an unchecked item
    When the user checks off the item on the list
    Then the response should be 200 OK
    And the item should be marked as checked
