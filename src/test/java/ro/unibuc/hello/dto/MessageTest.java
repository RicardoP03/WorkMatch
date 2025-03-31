package ro.unibuc.hello.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.Date;

class MessageTest {
    Date sentDate = new Date();
    Message msg =  new Message("67d7139fd390b106badfd9f5", "Hello!", "67d7139fd390b106badfd9f3", "67d7139fd390b106badfd9f4", sentDate);

    @Test
    void test_id() {
        Assertions.assertEquals("67d7139fd390b106badfd9f5", msg.getId());
    }

    @Test
    void test_content() {
        Assertions.assertEquals("Hello!", msg.getContent());
    }

    @Test
    void test_senderId() {
        Assertions.assertEquals("67d7139fd390b106badfd9f3", msg.getSenderId());
    }

    @Test
    void test_receiverId() {
        Assertions.assertEquals("67d7139fd390b106badfd9f4", msg.getReceiverId());
    }

    @Test
    void test_sentDate() {
        Assertions.assertEquals(sentDate, msg.getSentDate());
    }

    @Test
    void test_contentSetter() {
        Message testMsg =  new Message("67d7139fd390b106badfd9f5", "Hello!", "67d7139fd390b106badfd9f3", "67d7139fd390b106badfd9f4", sentDate);
        testMsg.setContent("Hello! How are you?");
        Assertions.assertEquals("Hello! How are you?", testMsg.getContent());
    }
}
