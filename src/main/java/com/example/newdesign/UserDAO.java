package com.example.newdesign;

public interface UserDAO {
    void addUser(User user);

   // boolean emailExists(String email);
    boolean validateUser(String email, String passwrod);

}
