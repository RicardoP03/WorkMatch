package ro.unibuc.hello.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ro.unibuc.hello.data.MessageEntity;
import ro.unibuc.hello.data.MessageRepository;
import ro.unibuc.hello.dto.Message;
import ro.unibuc.hello.data.UserRepository;
import ro.unibuc.hello.exception.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.management.RuntimeErrorException;

import java.util.Date;


@Component
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    private boolean messageContentCheck(String content) {
        return content != null && !content.isEmpty();
    }

    public List<Message> findMessagesBetweenUsers(String id1, String id2) {
        return messageRepository.findMessagesBetweenUsers(id1, id2).stream()
                .map(message -> new Message(message.getId(), 
                                            message.getContent(), 
                                            message.getSenderId(), 
                                            message.getReceiverId(),
                                            message.getSentDate()))
                .collect(Collectors.toList());
    }

    public List<Message> findMessagesBetweenUsersWithSubstring(String id1, String id2, String substring) {
        return messageRepository.findMessagesBetweenUsersWithSubstring(id1, id2, substring).stream()
                .map(message -> new Message(message.getId(), 
                                            message.getContent(), 
                                            message.getSenderId(), 
                                            message.getReceiverId(),
                                            message.getSentDate()))
                .collect(Collectors.toList());
    }
    
    public Message findMessageById(String id) {
        Optional<MessageEntity> msgEnt = messageRepository.findById(id);

        MessageEntity messageEntity = msgEnt.orElse(null);

        if (messageEntity == null) {
            return null;
        }


        return new Message(
                messageEntity.getId(),
                messageEntity.getContent(),
                messageEntity.getSenderId(),
                messageEntity.getReceiverId(),
                messageEntity.getSentDate()
        );
    }

    public Message saveMessage(Message message) throws EntityNotFoundException,  RuntimeException {
        if(!messageContentCheck(message.getContent())) {
            throw new RuntimeException("The content of the message must be non empty.");
        }

        userRepository.findById(message.getSenderId())
            .orElseThrow(() -> new EntityNotFoundException("Sender with ID " + message.getSenderId() + " not found"));

        userRepository.findById(message.getReceiverId())
            .orElseThrow(() -> new EntityNotFoundException("Receiver with ID " + message.getReceiverId() + " not found"));

        Date sentDate = new Date();
        MessageEntity messageEntity = new MessageEntity(
            message.getContent(),
            message.getSenderId(),
            message.getReceiverId(),
            sentDate    
        );

        messageEntity = messageRepository.save(messageEntity);

        return new Message(
            messageEntity.getId(),
            messageEntity.getContent(),
            messageEntity.getSenderId(),
            messageEntity.getReceiverId(),
            messageEntity.getSentDate()
        );
    }

    public void deleteMessage(String id) throws EntityNotFoundException {
        MessageEntity messageEntity = messageRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id));
        messageRepository.delete(messageEntity);
    }
       
}