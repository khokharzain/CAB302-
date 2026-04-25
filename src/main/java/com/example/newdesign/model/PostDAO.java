package com.example.newdesign.model;

import java.util.List;

public interface PostDAO {

    void addPost(Post post);
    List<Post> getAllPosts();
    Post getPostById(int id);
}
