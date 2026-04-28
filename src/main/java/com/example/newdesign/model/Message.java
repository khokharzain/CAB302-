package com.example.newdesign.model;

public class Message {

    private int id;
    private int senderId;
    private int receiverId;
    private String messageText;

    //Constructor
    public Message (int id, int senderId, String messageText) {
        this.id = id;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.messageText = messageText;
    }

    // GET
    public int getId() {return id; }
    public int getSenderId() { return senderId; }
    public int getReceiverId() { return receiverId; }
    public String getMessageText() { return messageText; }

    //SET
    public void setId(int id) {this.id = id;};
    public void setSenderId(int senderId) {this.senderId = senderId;};
    public void setReceiverId(int receiverId) {this.receiverId = receiverId;}
    public void setMessageText(String messageText) {this.messageText = messageText;}
}
