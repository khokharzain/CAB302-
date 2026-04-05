package com.example.newdesign;
import java.util.List;

public interface UserDAO {
    void addUser(User user);


    User login(String email, String password);

    // search function in search page
    List<User> searchUsers(String keyword);

    //handle forgot password option
    boolean resetPassword(String email, String newPassword);


}
