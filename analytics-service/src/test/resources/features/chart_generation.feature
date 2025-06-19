# Created by arkadiusoo at 20/06/2025
Feature: Chart generation

  Scenario: User generates a chart of most popular recipes
    Given a user with ID 1 exists
    When the user requests a chart for the most popular recipes
    Then the chart is generated successfully