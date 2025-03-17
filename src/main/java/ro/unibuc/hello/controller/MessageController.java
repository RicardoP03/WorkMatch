package ro.unibuc.hello.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ro.unibuc.hello.dto.Message;
import ro.unibuc.hello.dto.User;
import ro.unibuc.hello.exception.EntityNotFoundException;
import ro.unibuc.hello.service.MessageService;
import ro.unibuc.hello.service.UserService;
import jakarta.servlet.http.HttpSession;
import java.util.List;


@Controller
public class MessageController {
    @Autowired
    private MessageService messageService;

    @GetMapping("/between/{id1}")
    @ResponseBody
    public String get(@PathVariable String id1) {
        return "Mapping works";
    }

    @GetMapping("/between/{id1}/{id2}")
    @ResponseBody
    public List<Message> getMessagesBetweenUsers(
        @PathVariable String id1,
        @PathVariable String id2) {
        
        return messageService.findMessagesBetweenUsersById(id1, id2);
    }

    @PostMapping("/message")
    @ResponseBody
    public Message createMessage(@RequestBody Message message) {
        try {
            Message savedMessage = messageService.saveMessage(message);
            return savedMessage;
        } 
        catch (Exception e) {
            System.err.println("Error saving message: " + e.getMessage());
            return null;
        }
    }

    @DeleteMapping("/message/{id}")
    @ResponseBody
    public void deleteMessage(@PathVariable String id) throws EntityNotFoundException {
        messageService.deleteMessage(id);
    }

}