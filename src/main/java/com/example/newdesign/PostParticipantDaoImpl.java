package com.example.newdesign;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import java.sql.ResultSet;
import java.util.ArrayList;



public class PostParticipantDaoImpl implements PostParticipantDAO {

    @Override
    public void add(int postId, int userId) {
        String sql = "INSERT INTO PostParticipant (post_id, user_id) VALUES (?, ?)";

        try (Connection conn = DBconnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, postId);
            stmt.setInt(2, userId);
            stmt.execute();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int countByPost(int postId) {
        String sql = "SELECT COUNT(*) FROM PostParticipant WHERE post_id = ?";

        try (Connection conn = DBconnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, postId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }


    //in here we can get the user by post Id
    @Override
    public List<Integer> getUserIdsByPost(int postId) {
        List<Integer> users = new ArrayList<>();
        String sql = "SELECT user_id FROM PostParticipant WHERE post_id = ?";

        try (Connection conn = DBconnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, postId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                users.add(rs.getInt("user_id"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return users;
    }

    // this function needed in the profile to show all of our group members
    //we need to get all users by Post Id
    @Override
    public List<Integer> getPostIdsByUser(int userId){
        List<Integer> posts = new ArrayList<>();
        String sql = "SELECT post_id FROM PostParticipant WHERE user_id=?";

        try(Connection conn = DBconnection.connect();
        PreparedStatement stmt= conn.prepareStatement(sql)){
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while(rs.next()){
                posts.add(rs.getInt("post_id"));
            }

            }catch(Exception e){
            e.printStackTrace();


        }
        return posts;

    }



}
