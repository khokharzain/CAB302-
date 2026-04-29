package com.example.newdesign;

import java.time.LocalDateTime;

public class PostParticipant {

    private int id;
    private int postId;
    private int userId;
    private LocalDateTime joinedAt;

    public PostParticipant(int postId, int userId) {
        this.postId = postId;
        this.userId = userId;
        this.joinedAt = LocalDateTime.now();
    }

    public PostParticipant(int id, int postId, int userId, LocalDateTime joinedAt) {
        this.id = id;
        this.postId = postId;
        this.userId = userId;
        this.joinedAt = joinedAt;
    }

    public int getPostId() { return postId; }
    public int getUserId() { return userId; }
}