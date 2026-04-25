package com.example.newdesign.model;

public class SessionManager {
    private static User currentUser;
    private static User selectedUserForView;

    public static void setUser(User user){
        currentUser = user;
    }

    public static User getUser() {
        return currentUser;
    }

    public static void clear() {
        currentUser = null;
        selectedUserForView = null;
    }

    public static void setSelectedUserForView(User user) {
        selectedUserForView = user;
    }

    public static User getSelectedUserForView() {
        return selectedUserForView;
    }
}