package ro.unibuc.hello.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import ro.unibuc.hello.data.MessageEntity;
import ro.unibuc.hello.data.UserEntity;
import ro.unibuc.hello.data.MessageRepository;
import ro.unibuc.hello.data.UserRepository;
import ro.unibuc.hello.dto.Message;
import ro.unibuc.hello.exception.EntityNotFoundException;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.management.RuntimeErrorException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class MessageServiceTest {
    @Mock
    private MessageRepository messageRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private MessageService messageService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void test_findMessagesBetweenUsers() {
        Date sentDate = new Date();
        List<MessageEntity> messages = Arrays.asList(
                                    new MessageEntity("1", "Hello!", "10", "11", sentDate),
                                    new MessageEntity("2", "Hi!", "11", "10", sentDate)
                                );
        
        when(messageRepository.findMessagesBetweenUsers("10", "11")).thenReturn(messages);

        List<Message> msgs = messageService.findMessagesBetweenUsers("10", "11");

        assertEquals(2, msgs.size());
        assertEquals("1", msgs.get(0).getId());
        assertEquals("2", msgs.get(1).getId());
        assertEquals("Hello!", msgs.get(0).getContent());
        assertEquals("Hi!", msgs.get(1).getContent());
        assertEquals("10", msgs.get(0).getSenderId());
        assertEquals("11", msgs.get(1).getSenderId());
        assertEquals("11", msgs.get(0).getReceiverId());
        assertEquals("10", msgs.get(1).getReceiverId());
        assertEquals(sentDate, msgs.get(0).getSentDate());
        assertEquals(sentDate, msgs.get(1).getSentDate());
    }

    @Test
    void test_findMessagesBetweenUsersWithSubstring() {
        Date sentDate = new Date();
        List<MessageEntity> messages = Arrays.asList(
                                    new MessageEntity("1", "Hello!", "10", "11", sentDate)
                                );
        
        when(messageRepository.findMessagesBetweenUsersWithSubstring("10", "11", "l")).thenReturn(messages);

        List<Message> msgs = messageService.findMessagesBetweenUsersWithSubstring("10", "11", "l");

        assertEquals(1, msgs.size());
        assertEquals("1", msgs.get(0).getId());
        assertEquals("Hello!", msgs.get(0).getContent());
        assertEquals("10", msgs.get(0).getSenderId());
        assertEquals("11", msgs.get(0).getReceiverId());
        assertEquals(sentDate, msgs.get(0).getSentDate());
    }

    @Test
    void test_saveMessage_NoError() {
        Date sentDate = new Date();
        Message msg = new Message("1", "Hello!", "10", "11", sentDate);
        MessageEntity msgEntity = new MessageEntity("1", "Hello!", "10", "11", sentDate);

        when(userRepository.findById(anyString())).thenReturn(Optional.of(mock(UserEntity.class)));
        when(messageRepository.save(any(MessageEntity.class))).thenReturn(msgEntity);
        
        Message savedMessage = messageService.saveMessage(msg);

        assertNotNull(savedMessage);
        assertEquals("1", savedMessage.getId());
        assertEquals("Hello!", savedMessage.getContent());
        assertEquals("10", savedMessage.getSenderId());
        assertEquals("11", savedMessage.getReceiverId());
        assertEquals(sentDate, savedMessage.getSentDate());
    }

    @Test
    void test_saveMesage_invalidMessage() {
        Date sentDate = new Date();
        Message msg = new Message("1", "", "10", "11", sentDate);
        
        assertThrows( RuntimeException.class, () -> messageService.saveMessage(msg));
    }

    @Test
    void test_saveMessageSender_NotFound() {
        Date sentDate = new Date();
        Message msg = new Message("1", "Hello!", "10", "11", sentDate);

        when(userRepository.findById("10")).thenReturn(Optional.empty());
        
        assertThrows(EntityNotFoundException.class, () -> messageService.saveMessage(msg));

        verify(userRepository).findById("10");
        verify(userRepository, never()).findById("11");
        verify(messageRepository, never()).save(any(MessageEntity.class));
    }

    @Test
    void test_saveMessage_ReceiverNotFound() {
        Date sentDate = new Date();
        Message msg = new Message("1", "Hello!", "10", "11", sentDate);

        when(userRepository.findById("10")).thenReturn(Optional.of(mock(UserEntity.class)));
        when(userRepository.findById("11")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> messageService.saveMessage(msg));

        verify(userRepository).findById("10");
        verify(userRepository).findById("11");
        verify(messageRepository, never()).save(any(MessageEntity.class));
    }

    @Test
    void test_deleteMessage_NoError() {
        String id = "1";
        Date sentDate = new Date();
        MessageEntity msg = new MessageEntity(id, "Hello!", "10", "11", sentDate);
        when(messageRepository.findById(id)).thenReturn(Optional.of(msg));

        messageService.deleteMessage(id);
        
        verify(messageRepository, times(1)).delete(msg);
    }

    @Test
    void test_deleteMessage_NotFound() {
        String id = "100";
        when(messageRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> messageService.deleteMessage(id));
        verify(messageRepository, never()).delete(any(MessageEntity.class));
    }
}
