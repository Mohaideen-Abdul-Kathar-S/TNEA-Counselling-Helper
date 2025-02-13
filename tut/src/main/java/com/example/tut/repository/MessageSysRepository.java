package com.example.tut.repository;

import com.example.tut.model.MessageSys;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MessageSysRepository extends MongoRepository<MessageSys, String> {
    MessageSys findByMsgId(String msgId);
}
