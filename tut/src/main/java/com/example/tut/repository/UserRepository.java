package com.example.tut.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.example.tut.model.User;


public interface UserRepository extends MongoRepository<User,String> {
    
}
