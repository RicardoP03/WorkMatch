package ro.unibuc.hello.dto;
import java.util.Date;

public class Message {
    private String id;
    private String content;
    private String senderId;
    private String receiverId;
    private Date sentDate;

    public Message() {}

    public Message(String id, String content, String senderId, String receiverId, Date sentDate) {
        this.id = id;
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
    
    public void setId(String id) { this.id = id; }
    public void setContent(String content) { this.content = content; }
    public void setSenderId(String senderId) { this.senderId = senderId; }
    public void setReceiverId(String receiverId) { this.receiverId = receiverId; }
    public void setSentDate(Date sentDate) { this.sentDate = sentDate; }
}
