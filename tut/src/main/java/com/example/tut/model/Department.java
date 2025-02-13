package com.example.tut.model;

import java.util.*;

public class Department {
    
    String collegeCode;
    String department;
    Map<String,Map<String,String>> community;

    public void setCollegeCode(String collegeCode){
        this.collegeCode = collegeCode;
    }
    public String getCollegeCode(){
        return collegeCode;
    }

    public void setDepartment(String department){
        this.department = department;
    }
    public String getDepartment(){
        return department;
    }

    public void setCommunity(Map<String,Map<String,String>> community){
        this.community = community;
    }
    public Map<String,Map<String,String>> getCommunity(){
        return community;
    }

}
