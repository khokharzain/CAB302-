package com.example.newdesign;

import com.example.newdesign.model.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PostParticipantDaoImplTest {

    @Test
    void testAddParticipant() {

        PostParticipantDaoImpl dao =
                new PostParticipantDaoImpl();

        int postId = 1;
        int userId = 1;

        // Add participant
        dao.add(postId, userId);

        // Get users from post
        List<Integer> users =
                dao.getUserIdsByPost(postId);

        boolean found = false;

        for (Integer id : users) {

            if (id == userId) {
                found = true;
                break;
            }
        }

        assertTrue(found);
    }


    @Test
    void testCountByPost() {

        PostParticipantDaoImpl dao =
                new PostParticipantDaoImpl();

        int postId = 1;

        int count = dao.countByPost(postId);

        // Count should never be negative
        assertTrue(count >= 0);
    }


    @Test
    void testGetUserIdsByPost() {

        PostParticipantDaoImpl dao =
                new PostParticipantDaoImpl();

        int postId = 1;

        List<Integer> users =
                dao.getUserIdsByPost(postId);

        assertNotNull(users);
    }


    @Test
    void testGetUserIdsByPostInvalid() {

        PostParticipantDaoImpl dao =
                new PostParticipantDaoImpl();

        int invalidPostId = -999;

        List<Integer> users =
                dao.getUserIdsByPost(invalidPostId);

        assertNotNull(users);

        // Should return empty list
        assertEquals(0, users.size());
    }


    @Test
    void testGetPostIdsByUser() {

        PostParticipantDaoImpl dao =
                new PostParticipantDaoImpl();

        int userId = 1;

        List<Integer> posts =
                dao.getPostIdsByUser(userId);

        assertNotNull(posts);
    }


    @Test
    void testGetPostIdsByUserInvalid() {

        PostParticipantDaoImpl dao =
                new PostParticipantDaoImpl();

        int invalidUserId = -999;

        List<Integer> posts =
                dao.getPostIdsByUser(invalidUserId);

        assertNotNull(posts);

        // Should return empty list
        assertEquals(0, posts.size());
    }
}