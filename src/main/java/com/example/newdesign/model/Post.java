package com.example.newdesign.model;

import java.time.LocalDateTime;

public class Post {


    // empty constructionf for settings
    public Post(){}

    // the field for our post objectss
    private int id;
    private int userId;
    private String content;
    private LocalDateTime  createdAt;
    private int maxParticipants;

    // constructor

    public Post(int userId, String content, int maxParticipants){

        this.userId = userId;
        this.content = content;
        this.maxParticipants = maxParticipants;
        this.createdAt = LocalDateTime.now();
    }


    // constructor when loading from our DataBase
    public Post(int id, int userId, String content, LocalDateTime createdAt, int maxParticipants) {
        this.id = id;
        this.userId = userId;
        this.content = content;
        this.maxParticipants = maxParticipants;
        this.createdAt = createdAt;
    }

    //getters for post class
    public int getId (){
        return id;
    }

    public String getContent (){
        return content;
    }

    public int getUserId(){
        return userId;
    }

    public int getMaxParticipants(){
        return maxParticipants;
    }

    public LocalDateTime getCreatedAt (){
        return createdAt;
    }

    //setters

    public void setId(int id){
        this.id = id;
    }

    public void setUserId(int userId){
        this.userId = userId;
    }
    public void setContent(String content){
        this.content = content;
    }
    public void setCreatedAt(LocalDateTime date){
        this.createdAt = date;
    }
    public void setMaxParticipants(int max){
        this.maxParticipants = max;
    }



}
