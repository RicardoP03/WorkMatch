package ro.unibuc.hello.e2e.steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.RestTemplate;
import ro.unibuc.hello.dto.Job;
import ro.unibuc.hello.e2e.util.HeaderSetup;
import ro.unibuc.hello.e2e.util.ResponseErrorHandler;
import ro.unibuc.hello.e2e.util.ResponseResults;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@CucumberContextConfiguration
@SpringBootTest()
public class JobSteps {

    public static ResponseResults latestResponse = null;

    @Autowired
    protected RestTemplate restTemplate;

    @Given("^the client calls /job/jobs$")
    public void the_client_issues_GET_all_jobs() {
        executeGet("http://localhost:8080/job/jobs");
    }

    @Then("^the client receives status code of (\\d+)$")
    public void the_client_receives_status_code_of(int statusCode) throws Throwable {
        final HttpStatusCode currentStatusCode = latestResponse.getTheResponse().getStatusCode();
        assertThat("status code is incorrect : " + latestResponse.getBody(), currentStatusCode.value(), is(statusCode));
    }

    @And("^the client receives a list of jobs$")
    public void the_client_receives_job_list() throws JsonProcessingException {
        String latestResponseBody = latestResponse.getBody();
        Job[] jobs = new ObjectMapper().readValue(latestResponseBody, Job[].class);
        assertThat("Response received is incorrect", jobs.length, is(2)); 
    }

    @Given("^the client calls /job/create with job data$")
    public void the_client_issues_POST_create_job() throws JsonProcessingException {
        Date postDate = new Date();
        Job newJob = new Job("3", "Manager", "Manage teams", 29, postDate);
        executePost("http://localhost:8080/job/create", new ObjectMapper().writeValueAsString(newJob));
    }

    @And("^the client receives response containing job id \"(\\d+)\"$")
    public void the_client_receives_response_with_job_id(String jobId) throws JsonProcessingException {
        String latestResponseBody = latestResponse.getBody();
        Job createdJob = new ObjectMapper().readValue(latestResponseBody, Job.class);
        assertThat("Job ID is incorrect", createdJob.getId(), is(jobId));
    }

    @Given("^the client calls /job/update/(\\d+) with job data$")
    public void the_client_issues_PUT_update_job(String jobId) throws JsonProcessingException {
        Date postDate = new Date();
        Job updatedJob = new Job(jobId, "Senior Developer", "Build and mentor", 5, postDate);
        executePut("http://localhost:8080/job/update/" + jobId, new ObjectMapper().writeValueAsString(updatedJob));
    }

    @And("^the client receives updated job with position \"(.*)\"$")
    public void the_client_receives_updated_job(String position) throws JsonProcessingException {
        String latestResponseBody = latestResponse.getBody();
        Job updatedJob = new ObjectMapper().readValue(latestResponseBody, Job.class);
        assertThat("Job position is incorrect", updatedJob.getPoisitonName(), is(position));
    }

    @Given("^the client calls /job/delete/(\\d+)$")
    public void the_client_issues_DELETE_delete_job(String jobId) {
        executeDelete("http://localhost:8080/job/delete/" + jobId);
    }

    @And("^the client receives confirmation of deletion$")
    public void the_client_receives_confirmation_of_deletion() {
        String latestResponseBody = latestResponse.getBody();
        assertThat("Response should indicate success", latestResponseBody, is("succes"));
    }

    @Given("^the client calls /job/search with positionName \"(.*)\"$")
    public void the_client_issues_GET_search_job_by_position(String positionName) {
        executeGet("http://localhost:8080/job/search?positionName=" + positionName);
    }

    @And("^the client receives jobs matching position \"(.*)\"$")
    public void the_client_receives_jobs_matching_position(String positionName) throws JsonProcessingException {
        String latestResponseBody = latestResponse.getBody();
        Job[] jobs = new ObjectMapper().readValue(latestResponseBody, Job[].class);
        for (Job job : jobs) {
            assertThat("Job position doesn't match", job.getPoisitonName(), is(positionName));
        }
    }

    private void executeGet(String url) {
        final Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        final HeaderSetup requestCallback = new HeaderSetup(headers);
        final ResponseErrorHandler errorHandler = new ResponseErrorHandler();

        restTemplate.setErrorHandler(errorHandler);
        latestResponse = restTemplate.execute(url, HttpMethod.GET, requestCallback, response -> {
            if (errorHandler.getHadError()) {
                return (errorHandler.getResults());
            } else {
                return (new ResponseResults(response));
            }
        });
    }

    private void executePost(String url, String body) {
        final Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        headers.put("Content-Type", "application/json");
        final HeaderSetup requestCallback = new HeaderSetup(headers);
        final ResponseErrorHandler errorHandler = new ResponseErrorHandler();

        restTemplate.setErrorHandler(errorHandler);
        latestResponse = restTemplate.execute(url, HttpMethod.POST, requestCallback, response -> {
            if (errorHandler.getHadError()) {
                return (errorHandler.getResults());
            } else {
                return (new ResponseResults(response));
            }
        });
    }

    private void executePut(String url, String body) {
        final Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        headers.put("Content-Type", "application/json");
        final HeaderSetup requestCallback = new HeaderSetup(headers);
        final ResponseErrorHandler errorHandler = new ResponseErrorHandler();

        restTemplate.setErrorHandler(errorHandler);
        latestResponse = restTemplate.execute(url, HttpMethod.PUT, requestCallback, response -> {
            if (errorHandler.getHadError()) {
                return (errorHandler.getResults());
            } else {
                return (new ResponseResults(response));
            }
        });
    }

    private void executeDelete(String url) {
        final Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        final HeaderSetup requestCallback = new HeaderSetup(headers);
        final ResponseErrorHandler errorHandler = new ResponseErrorHandler();

        restTemplate.setErrorHandler(errorHandler);
        latestResponse = restTemplate.execute(url, HttpMethod.DELETE, requestCallback, response -> {
            if (errorHandler.getHadError()) {
                return (errorHandler.getResults());
            } else {
                return (new ResponseResults(response));
            }
        });
    }
}
