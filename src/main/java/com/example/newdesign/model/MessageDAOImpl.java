package com.example.newdesign.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MessageDAOImpl implements MessageDAO{

    @Override
    public void addMessage(String message, int senderId, int recieverid) {
        String sql = "INSERT INTO Messages (senderId, recieverId, message) VALUES (?,?,?)";

        try(Connection conn = DBconnection.connect();
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){

            stmt.setInt(1,senderId);
            stmt.setInt(2,recieverid);
            stmt.setString(3,message);
            stmt.executeUpdate();

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void deleteMessage(int id) {
        String sql = "DELETE FROM Messages WHERE id = ?";

        try (Connection conn = DBconnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)){

            stmt.setInt(1, id);

            stmt.execute();
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void editMessage(int id, String message){
        String sql = "UPDATE Messages SET message = ? WHERE id = ?";

        try (Connection conn = DBconnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)){

            stmt.setString(1, message);
            stmt.setInt(2, id);

            stmt.execute();
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public List<Message> getMessages(int senderId, int receiverId) {
        String sql = "SELECT * FROM Messages WHERE (senderId = ? OR senderId = ?) AND (recieverId = ? OR recieverId = ?)";

        List<Message> messages = new ArrayList<>();

        try (Connection conn = DBconnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)){

            stmt.setInt(1, senderId);
            stmt.setInt(3, senderId);
            stmt.setInt(2, receiverId);
            stmt.setInt(4, receiverId);

            ResultSet rs = stmt.executeQuery();

            while(rs.next()){
                Message message = new Message(
                        rs.getInt("senderId"),
                        rs.getInt("recieverId"),
                        rs.getString("message")
                );

                message.setId(rs.getInt("id"));

                messages.add(message);
            }

        }catch(Exception e){
            e.printStackTrace();
        }
        return messages;
    }


    @Override
    public void addUser(int Userid) {

    }

    @Override
    public List<User> getReceivers() {
        return List.of();
    }

}
