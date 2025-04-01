package ro.unibuc.hello.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import ro.unibuc.hello.dto.Message;
import ro.unibuc.hello.dto.User;
import ro.unibuc.hello.exception.EntityNotFoundException;

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

import ro.unibuc.hello.service.MessageService;
import ro.unibuc.hello.service.UserService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Date;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@Tag("IntegrationTest")
public class MessageControllerIntegrationTest {
    @Container
    public static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:6.0.20").
                                                      withExposedPorts(27018).withSharding();

    @BeforeAll
    public static void setUp() {
        mongoDBContainer.start();
    }

    @AfterAll static void tearDown() {
        mongoDBContainer.stop();
    }

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        final String MONGO_URL = "mongodb://localhost:";
        final String PORT = String.valueOf(mongoDBContainer.getMappedPort(27018));

        registry.add("mongodb.connection.url", () -> MONGO_URL + PORT);
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;


    private User us1, us2;
    private Message msg1, msg2;

    @BeforeEach
    public void cleanUpAndAddTestData() {
        //setting users
        us1 = new User("10", "Richard", "Employeer", "parola123");
        us2 = new User("11", "Joe", "JobSeeker", "parola123");
        us1 = userService.saveUser(us1);
        us2 = userService.saveUser(us2);

        Date sentDate = new Date();
        msg1 = new Message("1", "Hello!", us1.getId(), us2.getId(), sentDate);
        msg2 = new Message("2", "Hi!", us2.getId(), us1.getId(), sentDate);

        msg1 = messageService.saveMessage(msg1);
        msg2 = messageService.saveMessage(msg2);
    }

    @Test
    public void test_between()  throws Exception {
        String path = "/between/" + us1.getId() + "/" + us2.getId();
        mockMvc.perform(get(path))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.length()").value(2))
        .andExpect(jsonPath("$[0].id").value(msg1.getId()))
        .andExpect(jsonPath("$[0].content").value("Hello!"))
        .andExpect(jsonPath("$[0].senderId").value(us1.getId()))
        .andExpect(jsonPath("$[0].receiverId").value(us2.getId()))
        .andExpect(jsonPath("$[1].id").value(msg2.getId()))
        .andExpect(jsonPath("$[1].content").value("Hi!"))
        .andExpect(jsonPath("$[1].senderId").value(us2.getId()))
        .andExpect(jsonPath("$[1].receiverId").value(us1.getId()));
    }

    @Test
    public void test_betweenWithSubString()  throws Exception {
        String path = "/between/" + us1.getId() + "/" + us2.getId() + "/l";
        mockMvc.perform(get(path))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.length()").value(1))
        .andExpect(jsonPath("$[0].id").value(msg1.getId()))
        .andExpect(jsonPath("$[0].content").value("Hello!"))
        .andExpect(jsonPath("$[0].senderId").value(us1.getId()))
        .andExpect(jsonPath("$[0].receiverId").value(us2.getId()));
    }

    // I really don't know why it dosen't work is beyond me when I call it manually it works, and the service works 
    // properly I just can't find why it dosen't
    @Test
    public void test_saveMesage() throws Exception {
        Date sentDate = new Date();
        Message msg = new Message("3", "How are you!", us1.getId(), us2.getId(), sentDate);

        mockMvc.perform(post("/message")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(msg)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.content").value("How are you!"))
            .andExpect(jsonPath("$.senderId").value(us1.getId()))
            .andExpect(jsonPath("$.receiverId").value(us2.getId()));

        String path = "/between/" + us1.getId() + "/" + us2.getId();
        mockMvc.perform(get(path))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.length()").value(3));
    }

    @Test
    public void teste_deleteMessage() throws Exception {
        String path = "/message/" + msg1.getId(); 
        mockMvc.perform(delete(path))
        .andExpect(status().isOk());

        path = "/between/" + us1.getId() + "/" + us2.getId();
        mockMvc.perform(get(path))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.length()").value(1))
        .andExpect(jsonPath("$[0].id").value(msg2.getId()))
        .andExpect(jsonPath("$[0].content").value("Hi!"))
        .andExpect(jsonPath("$[0].senderId").value(us2.getId()))
        .andExpect(jsonPath("$[0].receiverId").value(us1.getId()));
    }
}
