package ro.unibuc.hello.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ro.unibuc.hello.dto.Job;
import ro.unibuc.hello.data.JobEntity;
import ro.unibuc.hello.service.JobService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@Tag("IntegrationTest")
public class JobControllerIntegrationTest {

    @Container
    public static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:6.0.20")
            .withExposedPorts(27017)
            .withEnv("MONGO_INITDB_ROOT_USERNAME", "root")
            .withEnv("MONGO_INITDB_ROOT_PASSWORD", "example")
            .withEnv("MONGO_INITDB_DATABASE", "testdb")
            .withCommand("--auth");

    @BeforeAll
    public static void setUp() {
        mongoDBContainer.start();
    }

    @AfterAll
    public static void tearDown() {
        mongoDBContainer.stop();
    }

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        final String MONGO_URL = "mongodb://root:example@localhost:";
        final String PORT = String.valueOf(mongoDBContainer.getMappedPort(27017));
        registry.add("mongodb.connection.url", () -> MONGO_URL + PORT);
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JobService jobService;

    private Date postDate = new Date();

    @BeforeEach
    public void cleanUpAndAddTestData() {
        jobService.deleteJob("1");
        jobService.deleteJob("2");
        postDate = new Date();
        JobEntity job1 = new JobEntity("1", "Developer", "Build applications", 5, postDate);
        JobEntity job2 = new JobEntity("2", "Designer", "Design interfaces", 4, postDate);

        jobService.create(job1);
        jobService.create(job2);
    }

    @Test
    public void testGetAllJobs() throws Exception {
        mockMvc.perform(get("/job/jobs"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[0].positionName").value("Developer"))
            .andExpect(jsonPath("$[1].positionName").value("Designer"));
    }

    @Test
    public void testCreateJob() throws Exception {
        postDate= new Date();
        JobEntity newJob = new JobEntity("3", "Manager", "Manage teams", 5, postDate);

        mockMvc.perform(post("/job/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(newJob)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("3"))
                .andExpect(jsonPath("$.positionName").value("Manager"))
                .andExpect(jsonPath("$.companyName").value("Corporation"));

        mockMvc.perform(get("/job/jobs"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(3));
    }

    @Test
    public void testUpdateJob() throws Exception {
        postDate= new Date();
        JobEntity updatedJob = new JobEntity("1", "Senior Developer", "Build and mentor", 20, postDate);

        mockMvc.perform(put("/job/update/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(updatedJob)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.positionName").value("Senior Developer"))
                .andExpect(jsonPath("$.companyName").value("Tech Company"));

        mockMvc.perform(get("/job/jobs"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].positionName").value("Senior Developer"))
                .andExpect(jsonPath("$[1].positionName").value("Designer"));
    }

    @Test
    public void testDeleteJob() throws Exception {
        mockMvc.perform(delete("/job/delete/1"))
            .andExpect(status().isOk());

        mockMvc.perform(get("/job/jobs"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].positionName").value("Designer"));
    }

    @Test
    public void testFindJobByPositionName() throws Exception {
        mockMvc.perform(get("/job/search")
                .param("positionName", "Developer"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.length()").value(1))
            .andExpect(jsonPath("$[0].positionName").value("Developer"));
    }

    @Test
    public void testGetJobById() throws Exception {
        mockMvc.perform(get("/job/getJob")
                .param("id", "1"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.positionName").value("Developer"))
            .andExpect(jsonPath("$.companyName").value("Tech Company"));
    }
}
