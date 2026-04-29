package com.example.newdesign.model;

import java.util.List;

public interface MessageDAO {
    void addMessage (String message, int senderId, int recieverid);
    void deleteMessage(int id);
    void editMessage(int id, String message);

    void addUser (int Userid);

    List<Message> getMessages(int senderId, int receiverId);

    List<User> getReceivers();

}
