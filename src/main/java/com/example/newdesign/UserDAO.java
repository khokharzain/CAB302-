package com.example.newdesign;

public interface UserDAO {
    void addUser(User user);


    User login(String email, String password);

}
