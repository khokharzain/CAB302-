package com.example.newdesign.model;

import java.util.List;
public interface PostParticipantDAO {

    void add(int postId, int userId);
    int countByPost(int postId);
    List<Integer> getUserIdsByPost(int postId);
}
