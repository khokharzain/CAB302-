package com.example.newdesign;

import java.time.LocalDateTime;

public class Post {

    // the field for our post objectss
    private int id;
    private int userId;
    private String content;
    private LocalDateTime  createdAt;

    // constructor

    public Post(int userId, String content){

        this.userId = userId;
        this.content = content;
        this.createdAt = LocalDateTime.now();
    }


    // constructor when loading from our DataBase
    public Post(int id, int userId, String content, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.content = content;
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

    public LocalDateTime getCreatedAt (){
        return createdAt;
    }


}
