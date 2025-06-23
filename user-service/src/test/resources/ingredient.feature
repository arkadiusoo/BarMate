Feature: Managing user preferences

  Scenario: Adding a favorite ingredient
    Given a user profile with username "kamil" exists
    When the user adds the ingredient "rum" to favorites
    Then the favorite ingredients of user "kamil" should contain "rum"
