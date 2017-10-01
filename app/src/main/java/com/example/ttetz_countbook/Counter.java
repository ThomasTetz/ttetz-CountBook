package com.example.ttetz_countbook;

import java.util.Date;

/**
 * Created by thomas on 2017-09-22.
 */

public class Counter {
    private  String name;
    private Date date;
    private int initialCount;
    private int currentCount;
    private String comment;

    public Counter(String name, int initialCount, int currentCount, String comment){
        // constructor if created with a comment
        this.name = name;
//        this.date = date;
        this.date = new Date();
        this.initialCount = initialCount;
        this.currentCount = currentCount;
        this.comment = comment;
    }

    public Counter(String name, int initialCount, int currentCount){
        // constructor if created without a comment
        // what if created with comment then it is deleted while editing
        this.name = name;
//        this.date = date;
        this.date = new Date();
        this.initialCount = initialCount;
        this.currentCount = currentCount;
        this.comment = "";
    }

    public void UpdateCounter(int i){
//        setName(atts[0]);
//        setInitialCount(atts[1]);
//        setCurrentCount(atts[2]);
//        setComment(atts[4]);
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public void setDate(Date date){
        this.date = date;
    }

    public Date getDate(){
        return date;
    }

    public void setInitialCount(int initialCount){
        this.initialCount = initialCount;
    }

    public int getInitialCount(){
        return initialCount;
    }

    public void setCurrentCount(int currentCount){
        this.currentCount = currentCount;
    }

    public int getCurrentCount(){
        return currentCount;
    }

    public void incrementCount(){
        currentCount += 1;
    }

    public void decrementCount(){
        if (currentCount > 0){
            currentCount -= 1;
        }
        else{
            currentCount = 0;
        }
    }

    public void setComment(String comment){
        this.comment = comment;
    }

    public String getComment(){
        return comment;
    }

    public String toString(){
        return name + "; " + date + "; " + initialCount + "; " + currentCount + "; " + comment;
    }

}
