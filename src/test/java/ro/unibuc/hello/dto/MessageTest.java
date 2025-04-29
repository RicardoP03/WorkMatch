package ro.unibuc.hello.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.Date;

class MessageTest {
    Date sentDate = new Date();
    Message msg =  new Message("1", "Hello!", "11", "10", sentDate);

    @Test
    void test_id() {
        Assertions.assertEquals("1", msg.getId());
    }

    @Test
    void test_content() {
        Assertions.assertEquals("Hello!", msg.getContent());
    }

    @Test
    void test_senderId() {
        Assertions.assertEquals("11", msg.getSenderId());
    }

    @Test
    void test_receiverId() {
        Assertions.assertEquals("10", msg.getReceiverId());
    }

    @Test
    void test_sentDate() {
        Assertions.assertEquals(sentDate, msg.getSentDate());
    }

    @Test
    void test_idSetter() {
        Message testMsg =  new Message("1", "Hello!", "11", "10", sentDate);
        testMsg.setId("2");
        Assertions.assertEquals("2", testMsg.getId());
    }

    @Test
    void test_contentSetter() {
        Message testMsg =  new Message("1", "Hello!", "11", "10", sentDate);
        testMsg.setContent("Hello! How are you?");
        Assertions.assertEquals("Hello! How are you?", testMsg.getContent());
    }

    @Test
    void test_senderIdSetter() {
        Message testMsg =  new Message("1", "Hello!", "11", "10", sentDate);
        testMsg.setSenderId("20");
        Assertions.assertEquals("20", testMsg.getSenderId());
    }

    @Test
    void test_receiverIdSetter() {
        Message testMsg =  new Message("1", "Hello!", "11", "10", sentDate);
        testMsg.setReceiverId("20");
        Assertions.assertEquals("20", testMsg.getReceiverId());
    }

    @Test
    void test_sentDateSetter() {
        Message testMsg =  new Message("1", "Hello!", "11", "10", sentDate);
        Date newDate = new Date();
        testMsg.setSentDate(newDate);
        Assertions.assertEquals(newDate, testMsg.getSentDate());
    }
}
