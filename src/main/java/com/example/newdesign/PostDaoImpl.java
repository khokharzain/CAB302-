package com.example.newdesign;

//importings
import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.List;
import java.util.ArrayList;

public class PostDaoImpl implements PostDAO{

    @Override
    public void addPost(Post post){
        String sql = "INSERT INTO Posts (userId, content, createdAt) values (?,?,? )";

        try(Connection conn = DBconnection.connect();
        PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setInt(1, post.getUserId());
            stmt.setString(2, post.getContent());
            stmt.setString(3, post.getCreatedAt().toString());

            stmt.execute();
        }catch (Exception e) {
            System.out.println("Error in PostDaoImpl: " + e.getMessage());
        }


    }

    @Override
    public List<Post> getAllPosts(){
        String sql = "SELECT *\n" +
                "FROM Posts\n" +
                "WHERE createdAt >= datetime('now', '-2 days')\n" +
                "ORDER BY createdAt DESC;";
        List<Post> posts = new ArrayList<>();

        try(Connection conn = DBconnection.connect();
        PreparedStatement stmt = conn.prepareStatement(sql)){
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Post post = new Post(
                        rs.getInt("id"),
                        rs.getInt("userId"),
                        rs.getString("content"),
                        java.time.LocalDateTime.parse(rs.getString("createdAt"))
                );

                posts.add(post);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return posts;


    }
}
