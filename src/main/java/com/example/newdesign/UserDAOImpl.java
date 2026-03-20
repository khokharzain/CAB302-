package com.example.newdesign;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class UserDAOImpl implements UserDAO {

    @Override
    public void addUser(User user) {

        String sql = "INSERT INTO Users (firstName, lastName, email, phone, passwordHash) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBconnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getFirstName());
            stmt.setString(2, user.getLastName());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getPhone());
            stmt.setString(5, user.getPasswordHash());

            stmt.executeUpdate();

            System.out.println("User inserted successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean validateUser(String email, String password) {
        String sql = "SELECT passwordHash FROM Users WHERE email = ?";

        try (Connection conn = DBconnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            var rs = stmt.executeQuery();

            if (rs.next()) {

                String storedHash = rs.getString("passwordHash");
                String inputHash = Integer.toHexString(password.hashCode());

                return storedHash.equals(inputHash);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}







