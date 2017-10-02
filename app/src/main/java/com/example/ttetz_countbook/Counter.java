package com.example.ttetz_countbook;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by thomas on 2017-09-22.
 */

/**
 * Represents a Counter.
 *
 * @author thomas
 * @version 1.5
 * @since 1.0
 */
public class Counter {
    private  String name;
    private Date date;
    private int initialCount;
    private int currentCount;
    private String comment;

    /**
     * Constructs a Counter object.
     *
     * @param name counter name
     * @param initialCount initial value of counter
     * @param currentCount current value of counter
     * @param comment comment about counter
     * @param date date currentCount was last changed
     *
     */
    public Counter(String name, int initialCount, int currentCount, String comment, Date date){
        // constructor if created with a comment
        this.name = name;
        this.date = date;
//        this.date = new Date();
        this.initialCount = initialCount;
        this.currentCount = currentCount;
        this.comment = comment;
    }
    /**
     * Constructs a Counter object.
     *
     * @param name counter name
     * @param initialCount initial value of counter
     * @param currentCount current value of counter
     * @param date date currentCount was last changed
     *
     */
    public Counter(String name, int initialCount, int currentCount, Date date){
        // constructor if created without a comment
        this.name = name;
        this.date = date;
        this.initialCount = initialCount;
        this.currentCount = currentCount;
        this.comment = "";
    }

    /**
     * Sets the name of the counter
     *
     * @param name the name to set
     */
    public void setName(String name){
        this.name = name;
    }

    /**
     * Gets the name of the counter
     *
     * @return name name of the counter
     */
    public String getName(){
        return name;
    }

    /**
     * Sets the date that the counter was last changed
     *
     * @param date the date to set
     */
    public void setDate(Date date){
        this.date = date;
    }

    /**
     * Gets the date the counters currentCount was last updated
     *
     * @return date the date of last change
     */
    public Date getDate(){
        return date;
    }

    /**
     * Sets the initial value of the count
     *
     * @param initialCount the value to make initial
     */
    public void setInitialCount(int initialCount){
        this.initialCount = initialCount;
    }

    /**
     * Gets the  initial value of the counter
     *
     * @return intitialCount counter's initial count
     */
    public int getInitialCount(){
        return initialCount;
    }

    /**
     * Sets the counter's current value
     *
     * @param currentCount the value to make current
     */
    public void setCurrentCount(int currentCount){
        this.currentCount = currentCount;
    }

    /**
     * Gets the counter's current value
     *
     * @return currentCount counters current value
     */
    public int getCurrentCount(){
        return currentCount;
    }

    /**
     * Increments the value of the counter
     *
     */
    public void incrementCount(){
        currentCount += 1;
        setDate(new Date());
    }

    /**
     * Decrements the value of the counter
     *
     * @constraint must be >=0
     *
     */
    public void decrementCount(){
        if (currentCount > 0){
            currentCount -= 1;
            setDate(new Date());
        }
        else{
            currentCount = 0;
        }
    }

    public void resetCount(){
        if (currentCount != initialCount){
            setDate(new Date());
        }
        currentCount = initialCount;
    }

    /**
     * Sets the counter comment
     *
     * @param comment counter comment
     */
    public void setComment(String comment){
        this.comment = comment;
    }

    /**
     * Gets the counter comment
     *
     * @return counter comment
     */
    public String getComment(){
        return comment;
    }

    /**
     * Gets a string representation of the counter
     *
     * @return concatenated string of counter
     */
    public String toString(){
        return name + "; " + date + "; " + initialCount + "; " + currentCount + "; " + comment;
    }

}
