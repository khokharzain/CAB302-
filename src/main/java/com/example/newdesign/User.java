package com.example.newdesign;
public class User {

    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String passwordHash;

    public User(String firstName, String lastName, String email, String phone, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        setPassword(password); // only special logic here
    }

    // getters
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getPasswordHash() { return passwordHash; }

    // only password has logic
    public void setPassword(String password) {
        this.passwordHash = Integer.toHexString(password.hashCode());
    }
}