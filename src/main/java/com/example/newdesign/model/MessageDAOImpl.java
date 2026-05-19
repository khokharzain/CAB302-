package com.example.newdesign.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MessageDAOImpl implements MessageDAO{

    /**
     * Adds Message to the DB
     *
     * @param message
     * @param senderId
     * @param recieverid
     * @param groupid
     */
    @Override
    public void addMessage(String message, int senderId, int recieverid, int groupid) {
        String sql = "INSERT INTO Messages (senderId, recieverId, groupId, message) VALUES (?,?,?,?)";

        try(Connection conn = DBconnection.connect();
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){

            stmt.setInt(1,senderId);
            stmt.setInt(2,recieverid);
            stmt.setInt(3,groupid);
            stmt.setString(4, message);

            stmt.executeUpdate();

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void addGroupMessage(List<User> Users, int senderId, int groupId) {
        for(User user : Users) {
            String sql = "INSERT INTO Messages (senderId, recieverId, groupId, message) VALUES (?,?,?,?)";

        }
    }


    /**
     * Deletes Message from the DB
     * @param id
     */
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
    public void deleteGroupMessage() {

    }


    /**
     * Edits message from the DB
     * @param id
     * @param message
     */
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
    public void editGroupMessage() {

    }


    /**
     * Grabs a list of all the Messages between the User and a selected User
     * @param senderId
     * @param receiverId
     * @return
     */
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
                        rs.getInt("id"),
                        rs.getInt("senderId"),
                        rs.getInt("recieverId"),
                        rs.getInt("groupId"),
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
    public List<Message> getGroupMessages(int senderID, int groupId) {
        String sql = "SELECT * FROM Messages WHERE senderId = ? AND groupId = ?";

        List<Message> messages = new ArrayList<>();

        try (Connection conn = DBconnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)){

            stmt.setInt(1,senderID);
            stmt.setInt(2, groupId);

            ResultSet rs = stmt.executeQuery();

            while(rs.next()){
                Message message = new Message(
                        rs.getInt("id"),
                        rs.getInt("senderId"),
                        rs.getInt("recieverId"),
                        rs.getInt("groupId"),
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

    /**
     * Adds a user into the Group Chat
     * @param Userid
     */
    @Override
    public void addUser(int Userid) {
        String sql = "";

    }


    /**
     * Grabs all the recievers of a Group chat from the User's perspective
     * @return
     */
    @Override
    public List<User> getReceivers() {
        return List.of();
    }

    @Override
    public int getMaxGroupId() {
        String sql = "SELECT max(groupId) FROM Messages";
        int id = 0;

        try (Connection conn = DBconnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)){

            ResultSet rs = stmt.executeQuery();

            if(rs.next()){
                id = rs.getInt(1);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return id;
    }

    // ================= GET RECEIVED MESSAGES =================
    @Override
    public List<Message> getMessagesForUser(int userId) {

        String sql = "SELECT * FROM Messages WHERE recieverId = ?";

        List<Message> messages = new ArrayList<>();

        try (
                Connection conn = DBconnection.connect();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setInt(1, userId);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                Message message = new Message(
                        rs.getInt("id"),
                        rs.getInt("senderId"),
                        rs.getInt("recieverId"),
                        rs.getInt("groupId"),
                        rs.getString("message")
                );

                messages.add(message);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return messages;
    }

}
