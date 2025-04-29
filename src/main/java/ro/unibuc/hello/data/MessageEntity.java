package ro.unibuc.hello.data;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;

@Document(collection = "messages")
public class MessageEntity {
    @Id
    private String id;
    private String content;
    private String senderId;
    private String receiverId; 
    private Date sentDate;

    public MessageEntity() {}

    public MessageEntity(String content, String senderId, String receiverId, Date sentDate) {
        this.content = content;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.sentDate = sentDate;
    }

    public String getId() { return id; }
    public String getContent() { return content; }
    public String getSenderId() { return senderId; }
    public String getReceiverId() { return receiverId; } 
    public Date getSentDate() { return sentDate; }

    public void setContent(String content) { this.content = content; }

    public String toString() {
        return String.format("Message[id='%s', content='%s', senderId='%s', receiverId='%s', sentDate='%s']",
            id, content, senderId, receiverId, sentDate); 
    }
}
