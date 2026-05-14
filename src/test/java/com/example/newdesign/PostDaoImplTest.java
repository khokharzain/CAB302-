package com.example.newdesign;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PostDaoImplTest {

    @Test
    void testAddPost() {

        PostDaoImpl dao = new PostDaoImpl();

        Post post = new Post();

        post.setUserId(1);
        post.setContent("JUnit test post");
        post.setCreatedAt(LocalDateTime.now());
        post.setMaxParticipants(5);

        dao.addPost(post);

        List<Post> posts = dao.getAllPosts();

        boolean found = false;

        for (Post p : posts) {
            if (p.getContent().equals("JUnit test post")) {
                found = true;
                break;
            }
        }

        assertTrue(found);
    }

    @Test
    void testGetAllPosts() {

        PostDaoImpl dao = new PostDaoImpl();

        List<Post> posts = dao.getAllPosts();

        assertNotNull(posts);
    }

    @Test
    void testGetPostByIdValid() {

        PostDaoImpl dao = new PostDaoImpl();

        Post post = dao.getPostById(1);

        assertNotNull(post);
    }

    @Test
    void testGetPostByIdInvalid() {

        PostDaoImpl dao = new PostDaoImpl();

        Post post = dao.getPostById(-999);

        assertNull(post);
    }
}