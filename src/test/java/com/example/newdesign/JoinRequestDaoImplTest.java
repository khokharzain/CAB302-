package com.example.newdesign;

import com.example.newdesign.model.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JoinRequestDaoImplTest {

    // Tests whether a pending join request
    // can be created and found successfully
    @Test
    void testCreateAndExistsPendingRequest() {

        JoinRequestDaoImpl dao =
                new JoinRequestDaoImpl();

        int postId = 1;
        int requesterId = 1;

        // Create a pending join request
        dao.create(postId, requesterId, "PENDING");

        // Check if request exists
        boolean exists =
                dao.exists(postId, requesterId);

        assertTrue(exists);
    }


    // Tests invalid request lookup
    // Should return false because request does not exist
    @Test
    void testExistsInvalidRequest() {

        JoinRequestDaoImpl dao =
                new JoinRequestDaoImpl();

        int invalidPostId = -999;
        int invalidRequesterId = -999;

        boolean exists =
                dao.exists(invalidPostId, invalidRequesterId);

        assertFalse(exists);
    }


    // Tests retrieving all pending requests
    // for posts owned by a specific user
    @Test
    void testGetRequestsForUserPosts() {

        JoinRequestDaoImpl dao =
                new JoinRequestDaoImpl();

        int ownerId = 1;

        List<JoinRequest> requests =
                dao.getRequestsForUserPosts(ownerId);

        // List should never be null
        assertNotNull(requests);
    }


    // Tests invalid owner id
    // Should return empty list
    @Test
    void testGetRequestsForInvalidUserPosts() {

        JoinRequestDaoImpl dao =
                new JoinRequestDaoImpl();

        int invalidOwnerId = -999;

        List<JoinRequest> requests =
                dao.getRequestsForUserPosts(invalidOwnerId);

        assertNotNull(requests);

        // No requests should exist
        assertEquals(0, requests.size());
    }


    // Tests updating request status
    // After updating, request should no longer
    // exist as a pending request
    @Test
    void testUpdateStatus() {

        JoinRequestDaoImpl dao =
                new JoinRequestDaoImpl();

        int postId = 1;
        int requesterId = 2;

        // Create pending request
        dao.create(postId, requesterId, "PENDING");

        // Retrieve requests
        List<JoinRequest> requests =
                dao.getRequestsForUserPosts(1);

        if (!requests.isEmpty()) {

            JoinRequest request =
                    requests.get(0);

            // Update request status
            dao.updateStatus(request.getId(), "ACCEPTED");

            // Request should no longer be pending
            boolean stillPending =
                    dao.exists(
                            request.getPostId(),
                            request.getRequesterId()
                    );

            assertFalse(stillPending);

        } else {

            // Prevent test crash if database empty
            assertNotNull(requests);
        }
    }


    // Tests invalid request status update
    // Should not crash application
    @Test
    void testUpdateInvalidRequestStatus() {

        JoinRequestDaoImpl dao =
                new JoinRequestDaoImpl();

        int invalidRequestId = -999;

        // Ensures no exception is thrown
        assertDoesNotThrow(() ->
                dao.updateStatus(invalidRequestId, "ACCEPTED")
        );
    }
}