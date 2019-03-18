package com.safademirel.quizgame.Models;

/**
 * Created by SAFA on 4.7.2017.
 */

public class New {
    private String title;
    private String description;
    private String date;

    public New(String mTitle, String mDescription, String mDate){
        this.title = mTitle;
        this.description = mDescription;
        this.date = mDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
