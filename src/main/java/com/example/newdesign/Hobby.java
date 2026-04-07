package com.example.newdesign;

import javafx.application.Application;

public class Hobby {
    private int id;
    private int userId;           // Which user owns this hobby
    private String hobbyName;     // e.g., "Hiking", "Chess", "Reading"

    // Default constructor
    public Hobby() {}

    // Constructor for new hobby
    public Hobby(int userId, String hobbyName) {
        this.userId = userId;
        this.hobbyName = hobbyName;
    }

    // Full constructor
    public Hobby(int id, int userId, String hobbyName) {
        this.id = id;
        this.userId = userId;
        this.hobbyName = hobbyName;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getHobbyName() { return hobbyName; }
    public void setHobbyName(String hobbyName) { this.hobbyName = hobbyName; }

    @Override
    public String toString() {
        return hobbyName;
    }


}