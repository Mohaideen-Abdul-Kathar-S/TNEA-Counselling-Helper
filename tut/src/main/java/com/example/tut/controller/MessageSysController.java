package com.example.tut.controller;

import com.example.tut.model.MessageSys;
import com.example.tut.repository.MessageSysRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class MessageSysController {

    @Autowired
    private MessageSysRepository messageSysRepository;

    // Send a message
    @PostMapping("/send")
public String sendMessage(@RequestParam String sender, @RequestParam String receiver, @RequestParam String message) {
    String msgId = sender.compareTo(receiver) < 0 ? sender + "_" + receiver : receiver + "_" + sender;
    

    MessageSys chat = messageSysRepository.findByMsgId(msgId);

    // If conversation doesn't exist, create a new one
    if (chat == null) {
        chat = new MessageSys(sender, receiver);
    }

    if (chat.getMsg() == null) {
        chat.setMsg(new HashMap<>());
    }
    String safeKey = sender.replace(".", ","); // Replace dots with commas

    chat.getMsg().put(safeKey, message);
    messageSysRepository.save(chat);

    return "Message sent successfully!";
}


    // Get conversation between two users
    @GetMapping("/get")
public Map<String, String> getMessages(@RequestParam String sender, @RequestParam String receiver) {
    // Validate sender and receiver inputs
    if (sender == null || receiver == null || sender.isEmpty() || receiver.isEmpty()) {
        throw new IllegalArgumentException("Sender and receiver cannot be null or empty");
    }

    // Ensure msgId is created with the correct order of sender and receiver
    String msgId = sender.compareTo(receiver) < 0 ? sender + "_" + receiver : receiver + "_" + sender;

    // Find the chat object by msgId
    MessageSys chat = messageSysRepository.findByMsgId(msgId);

    // Print the chat object for debugging
    System.out.println(chat);

    // Return messages if found, else an empty map
    return chat != null ? chat.getMsg() : new HashMap<>();
}

}
