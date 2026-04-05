package com.example.newdesign;

public class User {

    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String passwordHash;
    private String profilePicture;

    // Sign up constructor
    public User(String firstName, String lastName, String email, String phone, String password, String profilePicture) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        setPassword(password);
        this.profilePicture = profilePicture;
    }

    // Login constructor
    public User(String firstName, String lastName, String email, String phone, String profilePicture) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.profilePicture = profilePicture;
    }

    // Getters
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getPasswordHash() { return passwordHash; }
    public String getProfilePicture() { return profilePicture; }

    // Setter for profile picture ✅ IMPORTANT
    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public void setPassword(String password) {
        this.passwordHash = Integer.toHexString(password.hashCode());
    }
}