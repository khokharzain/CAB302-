package com.example.newdesign;

/**
 * A simple model class representing a hobby with userId and hobbyName.
 */
public class Hobby {
    private int id;
    private int userId;
    private String hobbyName;

    public Hobby() {}

    /**
     * @param userId
     * @param hobbyName
     */
    public Hobby(int userId, String hobbyName) {
        this.userId = userId;
        this.hobbyName = hobbyName;
    }

    /**
     * @param id
     * @param userId
     * @param hobbyName
     */
    public Hobby(int id, int userId, String hobbyName) {
        this.id = id;
        this.userId = userId;
        this.hobbyName = hobbyName;
    }

    /**
     * @return id
     */
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    /**
     * @return userId
     */
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    /**
     * @return hobbyName
     */
    public String getHobbyName() { return hobbyName; }
    public void setHobbyName(String hobbyName) { this.hobbyName = hobbyName; }

    @Override
    public String toString() {
        return hobbyName;
    }
}