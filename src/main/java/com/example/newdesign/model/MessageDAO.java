package com.example.newdesign.model;

import java.util.List;

public interface MessageDAO {
    void addMessage (String message, int senderId, int recieverid, int groupId);
    void addGroupMessage(List<User> Users, int senderId, int groupId);

    void deleteMessage(int id);
    void deleteGroupMessage();

    void editMessage(int id, String message);
    void editGroupMessage();

    void addUser (int Userid);

    List<Message> getMessages(int senderId, int receiverId);
    List<Message> getGroupMessages(int senderId, int groupId);


    List<User> getReceivers();
    int getMaxGroupId();

}
