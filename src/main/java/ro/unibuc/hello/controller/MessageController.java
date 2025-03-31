package ro.unibuc.hello.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ro.unibuc.hello.dto.Message;
import ro.unibuc.hello.exception.EntityNotFoundException;
import ro.unibuc.hello.service.MessageService;
import java.util.List;


@Controller
public class MessageController {
    @Autowired
    private MessageService messageService;

    @GetMapping("/between/{id1}/{id2}")
    @ResponseBody
    public List<Message> getMessagesBetweenUsers(
        @PathVariable String id1,
        @PathVariable String id2) {
        
        return messageService.findMessagesBetweenUsers(id1, id2);
    }

    @GetMapping("/between/{id1}/{id2}/{searchedString}")
    @ResponseBody
    public List<Message>getMessagesBetweenUsersWithSubstring(
        @PathVariable String id1,
        @PathVariable String id2,
        @PathVariable String searchedString) {
        return messageService.findMessagesBetweenUsersWithSubstring(id1, id2, searchedString);
    }

    @PostMapping("/message")
    public Message createMessage(@RequestBody Message message) throws EntityNotFoundException {
        Message savedMessage = messageService.saveMessage(message);
        return savedMessage;
    }

    @DeleteMapping("/message/{id}")
    @ResponseBody
    public String deleteMessage(@PathVariable String id) throws EntityNotFoundException {
        try {
            messageService.deleteMessage(id);
            return "The message has been deleted\n";
        }
        catch (Exception e) {
            return "Error deleting message: " + e.getMessage();
        }
    }

}