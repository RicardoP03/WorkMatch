Feature: User API

  Scenario: Get all users
    Given the client calls /users
    Then the client receives status code of 200
    And the client receives user with name "Alice"

  Scenario: Get user by ID
    Given the client calls /users/1
    Then the client receives status code of 200
    And the client receives user with name "Alice"

  Scenario: Create a new user
    Given the client creates a new user
    Then the client receives status code of 201

  Scenario: Update a user
    Given the client updates a user with id "1"
    Then the client receives status code of 200

  Scenario: Delete a user
    Given the client deletes a user with id "1"
    Then the client receives status code of 200
