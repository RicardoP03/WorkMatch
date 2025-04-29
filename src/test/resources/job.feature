Feature: Job operations

  Scenario: Client retrieves all jobs
    Given the client calls /job/jobs
    Then the client receives status code of 200
    And the client receives a list of jobs

  Scenario: Client creates a new job
    Given the client calls /job/create with job data
    And the client receives response containing job id "3"
    Then the client receives status code of 200

  Scenario: Client updates an existing job
    Given the client calls /job/update/1 with job data
    And the client receives updated job with position "Senior Developer"
    Then the client receives status code of 200

  Scenario: Client deletes a job
    Given the client calls /job/delete/1
    And the client receives confirmation of deletion
    Then the client receives status code of 200

  Scenario: Client searches for jobs by position name
    Given the client calls /job/search with positionName "Developer"
    And the client receives jobs matching position "Developer"
    Then the client receives status code of 200
