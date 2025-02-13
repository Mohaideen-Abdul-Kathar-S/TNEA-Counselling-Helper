package com.example.tut.repository;



import com.example.tut.model.College;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CollegeRepository extends MongoRepository<College,String>{
 
}
