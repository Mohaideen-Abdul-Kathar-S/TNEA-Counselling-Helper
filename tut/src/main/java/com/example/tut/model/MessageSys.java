package com.example.tut.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;

@Data
@Document(collection = "MessageSys")
public class MessageSys {
    
    @Id
    private String msgId; // id format: user1 + "_" + user2
    private String sender; // user with smaller email value (alphabetically)
    private String receiver; // user with larger email value (alphabetically)
    private HashMap<String, String> msg; // Key: sender email, Value: message content

    // Constructor to ensure user1 < user2
    public MessageSys(String sender, String receiver) {
        // Use default values if sender or receiver is null
        this.sender = (sender != null) ? sender : "defaultSender";
        this.receiver = (receiver != null) ? receiver : "defaultReceiver";
    
        // Ensure the sender < receiver logic still applies
        if (this.sender.compareTo(this.receiver) > 0) {
            String temp = this.sender;
            this.sender = this.receiver;
            this.receiver = temp;
        }
    
        this.msgId = this.sender + "_" + this.receiver;
    }
    
}
