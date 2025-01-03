package com.example.tut.repository;

import com.example.tut.model.Clk;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CklRepository extends MongoRepository<Clk,Integer> {
    
}
