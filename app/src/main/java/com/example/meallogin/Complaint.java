package com.example.meallogin;
import java.io.Serializable;
public class Complaint implements Serializable {
    private String description;
    private String suspensionDate;
    private Cook cook;
    private Meal meal;
    private Boolean actioned;
    public Complaint(){}

    public Complaint(String description, Cook cook, Meal meal){
        this.description = description;
        this.cook = cook;
        this.meal = meal;
        this.suspensionDate = "unknown";
        this.actioned = false;
    }

    public Complaint(String hello, String name) {
    }


    public void setSuspensionDate(String severity) {this.suspensionDate = severity;}

    public String getSuspensionDate() {return suspensionDate;}
    public void setDescription(String description) {this.description = description;}

    public String getDescription() {return description;}
    public void setCook(Cook cook) {this.cook = cook;}

    public Cook getCook() {
        return cook;
    }
    public void setActioned(Boolean actioned) {
        this.actioned = actioned;
    }

    public boolean getActioned(){
        return this.actioned;
    }

    public void setMeal(Meal meal) {this.meal = meal;}
    public Meal getMeal() {return meal;}

    public void action(){
        this.actioned = true;
    }
    public String toString(){return this.cook.getUsername();}
}
