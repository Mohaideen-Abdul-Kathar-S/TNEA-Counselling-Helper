package com.example.tut.model;


import java.util.*;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "Colleges")
public class College {
    @Id
    private String collegeCode;
    private String collegeName;
    private String status;
    private Map<String,Integer> arr = new HashMap<>();
    private Map<String, Map<String, Integer>> community = new HashMap<>();
    private Integer hostel;
    private Integer labs;
    private Integer infrastructures;
    private String nba;
    private String naac;
    private Integer nirf;
    private String link;
    private List<String> students;
}
