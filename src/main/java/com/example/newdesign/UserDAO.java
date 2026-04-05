package com.example.newdesign;
import java.util.List;

public interface UserDAO {
    void addUser(User user);

    User login(String email, String password);

    // search function in search page
    List<User> searchUsers(String keyword);

    //handle forgot password option
    boolean resetPassword(String email, String newPassword);

    // ========== BYRON'S ADDITIONS (User Profile System) ==========
    // Update user profile with new fields (bio, location, username, etc.)
    boolean updateUserProfile(User user);

    // Get user by ID (for loading full profile)
    User getUserById(int id);

    // Skill management
    boolean addSkill(Skill skill);
    boolean removeSkill(int skillId);
    List<Skill> getUserSkills(int userId);
    List<Skill> getUserSkillsByType(int userId, String type); // "TEACH" or "LEARN"

    // Hobby management
    boolean addHobby(Hobby hobby);
    boolean removeHobby(int hobbyId);
    List<Hobby> getUserHobbies(int userId);

    // Review management
    boolean addReview(Review review);
    List<Review> getUserReviews(int userId);
}