package ro.unibuc.hello.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ro.unibuc.hello.dto.Message;
import ro.unibuc.hello.service.MessageService;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.http.MediaType;

class MessageControllerTest {
    @Mock
    private MessageService messageService;

    @InjectMocks 
    private MessageController messageController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(messageController).build();
    }

    @Test
    void test_between() throws Exception {
        Date sentDate = new Date();
        List<Message> messages = Arrays.asList(
                                    new Message("1", "Hello!", "10", "11", sentDate),
                                    new Message("2", "Hi!", "11", "10", sentDate)
                                );
        
        when(messageService.findMessagesBetweenUsers("10", "11")).thenReturn(messages);

        mockMvc.perform(get("/between/10/11"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].id").value("1"))
                    .andExpect(jsonPath("$[0].content").value("Hello!"))
                    .andExpect(jsonPath("$[0].senderId").value("10"))
                    .andExpect(jsonPath("$[0].receiverId").value("11"))
                    .andExpect(jsonPath("$[0].sentDate").value(sentDate))
                    .andExpect(jsonPath("$[1].id").value("2"))
                    .andExpect(jsonPath("$[1].content").value("Hi!"))
                    .andExpect(jsonPath("$[1].senderId").value("11"))
                    .andExpect(jsonPath("$[1].receiverId").value("10"))
                    .andExpect(jsonPath("$[1].sentDate").value(sentDate));
        
    }

    @Test
    void test_betweenWithSubString() throws Exception {
        Date sentDate = new Date();
        List<Message> messages = Arrays.asList(
                                    new Message("1", "Hello!", "10", "11", sentDate)
                                );
        
        when(messageService.findMessagesBetweenUsersWithSubstring("10", "11", "l")).thenReturn(messages);
        mockMvc.perform(get("/between/10/11/l"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].id").value("1"))
                    .andExpect(jsonPath("$[0].content").value("Hello!"))
                    .andExpect(jsonPath("$[0].senderId").value("10"))
                    .andExpect(jsonPath("$[0].receiverId").value("11"))
                    .andExpect(jsonPath("$[0].sentDate").value(sentDate));
    }

    @Test
    void test_createMessage() throws Exception {
        Date sentDate = new Date();
        Message msg = new Message("1", "Hello!", "10", "11", sentDate);

        when(messageService.saveMessage(any(Message.class))).thenReturn(msg);

        mockMvc.perform(post("/message")
                .content("{\"id\":\"1\",\"content\":\"Hello!\",\"senderId\":\"10\",\"receiverId\":\"11\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.content").value("Hello!"))
                .andExpect(jsonPath("$.senderId").value("10"))
                .andExpect(jsonPath("$.receiverId").value("11"));
    }

    @Test
    void test_deleteMessage() throws Exception {
        String id = "1";
        doNothing().when(messageService).deleteMessage(id);
    
        mockMvc.perform(delete("/message/1"))
               .andExpect(status().isOk());
    
        verify(messageService, times(1)).deleteMessage(id);
    }

}