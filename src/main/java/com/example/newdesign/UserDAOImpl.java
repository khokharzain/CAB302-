package com.example.newdesign;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class UserDAOImpl implements UserDAO {

    @Override
    public void addUser(User user) {

        String sql = "INSERT INTO users (fName, lName, email, phone, password) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBconnection.getConnection();
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
        String sql = "SELECT password FROM users WHERE email = ?";;

        try( Connection conn = DBconnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, email);
            var rs = stmt.executeQuery();

            if (rs.next()) {

                String storedHash = rs.getString("password");

                String inputHash = Integer.toHexString(password.hashCode());

                return storedHash.equals(inputHash);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
        }

    }






