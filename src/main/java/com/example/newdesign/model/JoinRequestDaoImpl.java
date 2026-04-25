package com.example.newdesign.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JoinRequestDaoImpl implements JoinRequestDao {

    @Override
    public void create(int postId, int requesterId, String status) {
        String sql = "INSERT INTO JoinRequest (post_id, requester_id, status) VALUES (?,?,?)";

        try (Connection conn = DBconnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, postId);
            stmt.setInt(2, requesterId);
            stmt.setString(3, status);

            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // only checks PENDING requests
    @Override
    public boolean exists(int postId, int requesterId) {

        String sql = "SELECT 1 FROM JoinRequest " +
                "WHERE post_id = ? AND requester_id = ? AND status = 'PENDING'";

        try (Connection conn = DBconnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, postId);
            stmt.setInt(2, requesterId);

            ResultSet rs = stmt.executeQuery();
            return rs.next();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public List<JoinRequest> getRequestsForUserPosts(int ownerId) {

        List<JoinRequest> list = new ArrayList<>();

        String sql = "SELECT jr.* FROM JoinRequest jr " +
                "JOIN Posts p ON jr.post_id = p.id " +
                "WHERE p.userId = ? AND jr.status = 'PENDING'";

        try (Connection conn = DBconnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, ownerId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                JoinRequest req = new JoinRequest();

                req.setId(rs.getInt("id"));
                req.setPostId(rs.getInt("post_id"));
                req.setRequesterId(rs.getInt("requester_id"));
                req.setStatus(rs.getString("status"));

                list.add(req);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    //
    @Override
    public void updateStatus(int requestId, String status) {

        String sql = "UPDATE JoinRequest SET status = ? WHERE id = ?";

        try (Connection conn = DBconnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status);
            stmt.setInt(2, requestId);

            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}