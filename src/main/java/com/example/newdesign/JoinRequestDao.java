package com.example.newdesign;

import java.util.List;

public interface JoinRequestDao {

    // create a new join request
    void create(int postId, int requesterId, String status);

    // check if a pending request already exists
    boolean exists(int postId, int requesterId);

    // get all pending requests for posts owned by a user
    List<JoinRequest> getRequestsForUserPosts(int ownerId);

    // update request status (ACCEPTED / REJECTED)
    void updateStatus(int requestId, String status);
}