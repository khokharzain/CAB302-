package com.example.newdesign.model;

public class SessionManager {
    private static User currentUser;

    public static void setUser(User user){
        currentUser = user;
    }


    //getter for the user
    public static User getUser() {
        return currentUser;
    }

    // logout
    public static void clear() {
        currentUser = null;
    }
}
