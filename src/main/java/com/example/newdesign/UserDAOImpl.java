package com.example.newdesign;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class UserDAOImpl implements UserDAO {

    @Override
    public void addUser(User user) {

        String sql = "INSERT INTO Users (firstName, lastName, email, phone, passwordHash, profile_picture) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBconnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getFirstName());
            stmt.setString(2, user.getLastName());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getPhone());
            stmt.setString(5, user.getPasswordHash());

            // ✅ Ensure profile picture is never null
            if (user.getProfilePicture() == null) {
                stmt.setString(6, "default.png");
            } else {
                stmt.setString(6, user.getProfilePicture());
            }

            stmt.executeUpdate();

            System.out.println("User inserted successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public User login(String email, String password) {

        String sql = "SELECT * FROM Users WHERE email = ?";

        try (Connection conn = DBconnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {

                String inputHash = Integer.toHexString(password.hashCode());
                String storedHash = rs.getString("passwordHash");

                if (storedHash.equals(inputHash)) {

                    String profilePic = rs.getString("profile_picture");

                    // ✅ Handle NULL safely
                    if (profilePic == null || profilePic.isEmpty()) {
                        profilePic = "default.png";
                    }

                    System.out.println("Loaded profile pic: " + profilePic); // DEBUG

                    return new User(
                            rs.getString("firstName"),
                            rs.getString("lastName"),
                            rs.getString("email"),
                            rs.getString("phone"),
                            profilePic
                    );
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<User> searchUsers(String keyword) {

        String sql = "SELECT * FROM Users WHERE LOWER(firstName || ' ' || lastName) LIKE LOWER(?)";

        List<User> users = new ArrayList<>();

        try (Connection conn = DBconnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String searchPattern = keyword.toLowerCase() + "%";

            stmt.setString(1, searchPattern);


            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                String profilePic = rs.getString("profile_picture");

                if (profilePic == null || profilePic.isEmpty()) {
                    profilePic = "default.png";
                }

                User user = new User(
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        profilePic
                );

                users.add(user);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return users;
    }



    // reseting the password if user cant login
    public boolean resetPassword(String email, String newPassword) {

        String sql = "UPDATE Users SET passwordHash = ? WHERE email = ?";

        try (Connection conn = DBconnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String hash = Integer.toHexString(newPassword.hashCode());

            stmt.setString(1, hash);
            stmt.setString(2, email);

            int rows = stmt.executeUpdate();

            return rows > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}