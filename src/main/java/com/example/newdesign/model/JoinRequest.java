package com.example.newdesign.model;

import java.time.LocalDateTime;

public class JoinRequest {

    private int id;
    private int postId;
    private int requesterId;
    private String status;
    private LocalDateTime createdAt;

    // REQUIRED for DAO
    public JoinRequest() {}

    // constructor for creating new request
    public JoinRequest(int postId, int requesterId, String status){
        this.postId = postId;
        this.requesterId = requesterId;
        this.status = status;
        this.createdAt = LocalDateTime.now();
    }

    // constructor for loading from DB
    public JoinRequest(int id, int postId, int requesterId, String status, LocalDateTime createdAt) {
        this.id = id;
        this.postId = postId;
        this.requesterId = requesterId;
        this.status = status;
        this.createdAt = createdAt;
    }

    //  GETTERS
    public int getId() { return id; }
    public int getPostId(){ return postId; }
    public int getRequesterId(){ return requesterId; }
    public String getStatus(){ return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    //  SETTERS (needed by DAO)
    public void setId(int id) { this.id = id; }
    public void setPostId(int postId) { this.postId = postId; }
    public void setRequesterId(int requesterId) { this.requesterId = requesterId; }
    public void setStatus(String status) { this.status = status; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}