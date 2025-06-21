Feature: Shopping list item management

  Scenario: User checks off an unchecked item
    Given a shopping list with ID 100000 and an unchecked item with ID 100000
    When the user checks off item with ID 10 on list ID 1
    Then the response should be 200 OK
    And the item with ID 10 should be marked as checked
    And the inventory should be updated for item ID 10
