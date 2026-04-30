package com.example.newdesign;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserDAOImpl implements UserDAO {

    @Override
    public void addUser(User user) {
        String sql = "INSERT INTO Users (firstName, lastName, email, phone, passwordHash, profile_picture, username, bio, location, joinDate) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBconnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, user.getFirstName());
            stmt.setString(2, user.getLastName());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getPhone());
            stmt.setString(5, user.getPasswordHash());

            if (user.getProfilePicture() == null) {
                stmt.setString(6, "default.png");
            } else {
                stmt.setString(6, user.getProfilePicture());
            }

            stmt.setString(7, user.getUsername());
            stmt.setString(8, user.getBio());
            stmt.setString(9, user.getLocation());
            stmt.setString(10, user.getJoinDate() != null ? user.getJoinDate().toString() : null);

            stmt.executeUpdate();

            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                user.setId(generatedKeys.getInt(1));
            }

            System.out.println("User inserted successfully!");

            if (user.getSkills() != null && !user.getSkills().isEmpty()) {
                for (Skill skill : user.getSkills()) {
                    addSkill(skill);
                }
            }

            if (user.getHobbies() != null && !user.getHobbies().isEmpty()) {
                for (Hobby hobby : user.getHobbies()) {
                    addHobby(hobby);
                }
            }

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

                    if (profilePic == null || profilePic.isEmpty()) {
                        profilePic = "default.png";
                    }

                    System.out.println("Loaded profile pic: " + profilePic);

                    User user = new User(
                            rs.getString("firstName"),
                            rs.getString("lastName"),
                            rs.getString("email"),
                            rs.getString("phone"),
                            profilePic
                    );

                    user.setId(rs.getInt("id"));
                    user.setUsername(rs.getString("username"));
                    user.setBio(rs.getString("bio"));
                    user.setLocation(rs.getString("location"));

                    String joinDateStr = rs.getString("joinDate");
                    if (joinDateStr != null && !joinDateStr.isEmpty()) {
                        user.setJoinDate(java.time.LocalDate.parse(joinDateStr));
                    }

                    user.setSkills(getUserSkills(user.getId()));
                    user.setHobbies(getUserHobbies(user.getId()));
                    user.setReviews(getUserReviews(user.getId()));

                    return user;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<User> searchUsers(String keyword) {
        String sql = """
       SELECT DISTINCT u.*
       FROM Users u
       LEFT JOIN Skills s ON u.id = s.userId
       WHERE LOWER(u.firstName || ' ' || u.lastName) LIKE LOWER(?)
       OR LOWER(u.username) LIKE LOWER(?)
       OR LOWER(s.skillName) LIKE LOWER(?)
       """;

        List<User> users = new ArrayList<>();

        try (Connection conn = DBconnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String searchPattern = keyword.toLowerCase() + "%";

            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            stmt.setString(3, searchPattern);

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

                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setBio(rs.getString("bio"));
                user.setLocation(rs.getString("location"));

                String joinDateStr = rs.getString("joinDate");
                if (joinDateStr != null && !joinDateStr.isEmpty()) {
                    user.setJoinDate(java.time.LocalDate.parse(joinDateStr));
                }

                users.add(user);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return users;
    }

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

    // ========== BYRON'S ADDITIONS (User Profile System) ==========

    @Override
    public boolean updateUserProfile(User user) {
        String sql = "UPDATE Users SET firstName = ?, lastName = ?, email = ?, phone = ?, " +
                "username = ?, bio = ?, location = ? WHERE id = ?";

        try (Connection conn = DBconnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getFirstName());
            stmt.setString(2, user.getLastName());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getPhone());
            stmt.setString(5, user.getUsername());
            stmt.setString(6, user.getBio());
            stmt.setString(7, user.getLocation());
            stmt.setInt(8, user.getId());

            int rows = stmt.executeUpdate();
            return rows > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public User getUserById(int id) {
        String sql = "SELECT * FROM Users WHERE id = ?";

        try (Connection conn = DBconnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
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

                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setBio(rs.getString("bio"));
                user.setLocation(rs.getString("location"));

                String joinDateStr = rs.getString("joinDate");
                if (joinDateStr != null && !joinDateStr.isEmpty()) {
                    user.setJoinDate(java.time.LocalDate.parse(joinDateStr));
                }

                user.setSkills(getUserSkills(user.getId()));
                user.setHobbies(getUserHobbies(user.getId()));
                user.setReviews(getUserReviews(user.getId()));

                return user;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public boolean addSkill(Skill skill) {
        String sql = "INSERT INTO Skills (userId, skillName, type, proficiency, category) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBconnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, skill.getUserId());
            stmt.setString(2, skill.getSkillName());
            stmt.setString(3, skill.getType().toString());
            stmt.setString(4, skill.getProficiency() != null ? skill.getProficiency().toString() : null);
            stmt.setString(5, skill.getCategory());

            int rows = stmt.executeUpdate();

            if (rows > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    skill.setId(generatedKeys.getInt(1));
                }
            }

            return rows > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean removeSkill(int skillId) {
        String sql = "DELETE FROM Skills WHERE id = ?";

        try (Connection conn = DBconnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, skillId);
            int rows = stmt.executeUpdate();
            return rows > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Skill> getUserSkills(int userId) {
        String sql = "SELECT * FROM Skills WHERE userId = ?";
        List<Skill> skills = new ArrayList<>();

        try (Connection conn = DBconnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Skill skill = new Skill(
                        rs.getInt("id"),
                        rs.getInt("userId"),
                        rs.getString("skillName"),
                        SkillType.valueOf(rs.getString("type")),
                        rs.getString("proficiency") != null ? Proficiency.valueOf(rs.getString("proficiency")) : Proficiency.BEGINNER,
                        rs.getString("category")
                );
                skills.add(skill);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return skills;
    }

    @Override
    public List<Skill> getUserSkillsByType(int userId, String type) {
        String sql = "SELECT * FROM Skills WHERE userId = ? AND type = ?";
        List<Skill> skills = new ArrayList<>();

        try (Connection conn = DBconnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.setString(2, type);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Skill skill = new Skill(
                        rs.getInt("id"),
                        rs.getInt("userId"),
                        rs.getString("skillName"),
                        SkillType.valueOf(rs.getString("type")),
                        rs.getString("proficiency") != null ? Proficiency.valueOf(rs.getString("proficiency")) : Proficiency.BEGINNER,
                        rs.getString("category")
                );
                skills.add(skill);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return skills;
    }

    @Override
    public boolean addHobby(Hobby hobby) {
        String sql = "INSERT INTO Hobbies (userId, hobbyName) VALUES (?, ?)";

        try (Connection conn = DBconnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, hobby.getUserId());
            stmt.setString(2, hobby.getHobbyName());

            int rows = stmt.executeUpdate();

            if (rows > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    hobby.setId(generatedKeys.getInt(1));
                }
            }

            return rows > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean removeHobby(int hobbyId) {
        String sql = "DELETE FROM Hobbies WHERE id = ?";

        try (Connection conn = DBconnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, hobbyId);
            int rows = stmt.executeUpdate();
            return rows > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Hobby> getUserHobbies(int userId) {
        String sql = "SELECT * FROM Hobbies WHERE userId = ?";
        List<Hobby> hobbies = new ArrayList<>();

        try (Connection conn = DBconnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Hobby hobby = new Hobby(
                        rs.getInt("id"),
                        rs.getInt("userId"),
                        rs.getString("hobbyName")
                );
                hobbies.add(hobby);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return hobbies;
    }

    // ========== REVIEW MANAGEMENT (Byron) ==========

    /**
     * Adds a review to the database.
     *
     * @param review
     * @return true or false
     */
    @Override
    public boolean addReview(Review review) {
        String sql = "INSERT INTO Reviews (reviewerId, revieweeId, exchangeId, rating, comment, createdAt) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBconnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, review.getReviewerId());
            stmt.setInt(2, review.getRevieweeId());
            stmt.setInt(3, review.getExchangeId());
            stmt.setInt(4, review.getRating());
            stmt.setString(5, review.getComment());
            stmt.setString(6, review.getCreatedAt().toString());

            int rows = stmt.executeUpdate();

            if (rows > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    review.setId(generatedKeys.getInt(1));
                }
            }

            return rows > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Retrieves all reviews for a specific user (reviewee).
     *
     * @param userId
     * @return reviews
     */
    @Override
    public List<Review> getUserReviews(int userId) {
        String sql = "SELECT * FROM Reviews WHERE revieweeId = ? ORDER BY createdAt DESC";
        List<Review> reviews = new ArrayList<>();

        try (Connection conn = DBconnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Review review = new Review(
                        rs.getInt("id"),
                        rs.getInt("reviewerId"),
                        rs.getInt("revieweeId"),
                        rs.getInt("exchangeId"),
                        rs.getInt("rating"),
                        rs.getString("comment"),
                        java.time.LocalDateTime.parse(rs.getString("createdAt"))
                );
                reviews.add(review);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return reviews;
    }

    /**
     * Checks if a user has already reviewed a specific exchange.
     * Used to prevent duplicate reviews.
     *
     * @param reviewerId
     * @param revieweeId
     * @param exchangeId
     * @return true or false
     */
    public boolean canUserReview(int reviewerId, int revieweeId, int exchangeId) {
        String sql = "SELECT COUNT(*) FROM Reviews WHERE reviewerId = ? AND revieweeId = ? AND exchangeId = ?";

        try (Connection conn = DBconnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, reviewerId);
            stmt.setInt(2, revieweeId);
            stmt.setInt(3, exchangeId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int count = rs.getInt(1);
                return count == 0;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    public List<String> getMostWantedSkills(int limit) {
        String sql = """
                SELECT skillName, COUNT(*) as count FROM skills
                WHERE type = 'TEACH'
                GROUP BY skillName
                ORDER BY count DESC
                LIMIT ?
                
                """;

        List<String> skills = new ArrayList<>();
        try (Connection conn = DBconnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, limit);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                skills.add(rs.getString("skillName"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return skills;
        }return skills;

    }
}