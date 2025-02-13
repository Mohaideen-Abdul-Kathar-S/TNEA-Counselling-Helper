package com.example.tut.model;


import java.util.*;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Data;

@Data
@Document(collection = "Users")
public class User {
    @Id
    String email;
    String name;
    String password;
    private List<String> friends = new ArrayList<>();
    private String dob;
    private String address;
    private String gender;
    private String group;
    
    @Field("pdf")
    private String pdfFileId;

    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return name;
    }

    public void setEmail(String email){
        this.email = email;
    }
    public String getEmail(){
        return email;
    }

    public void setPassword(String password){
        this.password = password;
    }
    public String getPassword(){
        return password;
    }

}
