package com.example.tut.model;

public class Clk {
    Double maths;
    Double phy;
    Double chem;
    Double total;
    String email;
    public void setMaths(Double maths){
        this.maths=maths;
    }
    public Double getMaths(){
        return maths;
    }
    public void setPhy(Double phy){
        this.phy=phy;
    }
    public Double getPhy(){
        return phy;
    }
    public void setChem(Double chem){
        this.chem=chem;
    }
    public Double getChem(){
        return chem;
    }
    public void setTotal(Double total){
        this.total=total;
    }
    public Double getTotal(){
        return total;
    }
    public void setEmail(String email){
        this.email = email;
    }
    public String getEmail(){
        return email;
    }
}
