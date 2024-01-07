package com.example.movieapplication;

import java.util.Date;

public class Myliste {
    String id;
    String date;
    String movieName;
    String note;
    String time;
    public Myliste() {
    }

    public Myliste(String id, String movieName, String note,String date, String time) {
        this.id = id;
        this.date = date;
        this.movieName = movieName;
        this.note = note;
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
