package com.example.newdesign;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

/**
 * Unit tests for the User model class.
 * Tests user profile data, skill management, hobby management, and review ratings.
 */
class UserTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = new User("John", "Doe", "john@example.com", "0412345678", "password123", "default.png");
        user.setId(1);
        user.setUsername("johndoe");
        user.setBio("I love teaching coding!");
        user.setLocation("Brisbane");
    }

    @Test
    void testGetFullName() {
        assertEquals("John Doe", user.getFullName());
    }

    @Test
    void testGetDisplayName() {
        assertEquals("John Doe @johndoe", user.getDisplayName());
    }

    @Test
    void testAddSkill() {
        Skill skill = new Skill(user.getId(), "Java", SkillType.TEACH);
        skill.setProficiency(Proficiency.ADVANCED);
        user.addSkill(skill);

        assertEquals(1, user.getSkills().size());
        assertEquals("Java", user.getSkills().get(0).getSkillName());
        assertEquals(SkillType.TEACH, user.getSkills().get(0).getType());
    }

    @Test
    void testRemoveSkill() {
        Skill skill = new Skill(user.getId(), "Python", SkillType.TEACH);
        user.addSkill(skill);
        assertEquals(1, user.getSkills().size());

        user.removeSkill(skill);
        assertEquals(0, user.getSkills().size());
    }

    @Test
    void testGetTeachSkills() {
        user.addSkill(new Skill(user.getId(), "Java", SkillType.TEACH));
        user.addSkill(new Skill(user.getId(), "Guitar", SkillType.LEARN));
        user.addSkill(new Skill(user.getId(), "Python", SkillType.TEACH));

        List<Skill> teachSkills = user.getTeachSkills();
        assertEquals(2, teachSkills.size());
        assertTrue(teachSkills.stream().allMatch(s -> s.getType() == SkillType.TEACH));
    }

    @Test
    void testGetLearnSkills() {
        user.addSkill(new Skill(user.getId(), "Java", SkillType.TEACH));
        user.addSkill(new Skill(user.getId(), "Guitar", SkillType.LEARN));

        List<Skill> learnSkills = user.getLearnSkills();
        assertEquals(1, learnSkills.size());
        assertEquals(SkillType.LEARN, learnSkills.get(0).getType());
    }

    @Test
    void testAddHobby() {
        Hobby hobby = new Hobby(user.getId(), "Chess");
        user.addHobby(hobby);

        assertEquals(1, user.getHobbies().size());
        assertEquals("Chess", user.getHobbies().get(0).getHobbyName());
    }

    @Test
    void testRemoveHobby() {
        Hobby hobby = new Hobby(user.getId(), "Reading");
        user.addHobby(hobby);
        assertEquals(1, user.getHobbies().size());

        user.removeHobby(hobby);
        assertEquals(0, user.getHobbies().size());
    }

    @Test
    void testAddReview() {
        Review review = new Review(2, user.getId(), 100, 5, "Great teacher!");
        user.addReview(review);

        assertEquals(1, user.getReviews().size());
        assertEquals(5, user.getReviews().get(0).getRating());
    }

    @Test
    void testGetAverageRating() {
        user.addReview(new Review(2, user.getId(), 100, 5, "Excellent"));
        user.addReview(new Review(3, user.getId(), 101, 4, "Good"));
        user.addReview(new Review(4, user.getId(), 102, 3, "Okay"));

        assertEquals(4.0, user.getAverageRating());
    }

    @Test
    void testGetAverageRatingNoReviews() {
        assertEquals(0.0, user.getAverageRating());
    }

    @Test
    void testGetFormattedRating() {
        user.addReview(new Review(2, user.getId(), 100, 5, "Great"));
        user.addReview(new Review(3, user.getId(), 101, 4, "Good"));

        String formatted = user.getFormattedRating();
        assertTrue(formatted.contains("4.5 ★"));
        assertTrue(formatted.contains("2 reviews"));
    }

    @Test
    void testGetFormattedRatingNoReviews() {
        assertEquals("No ratings yet", user.getFormattedRating());
    }

    @Test
    void testHasCompleteProfile() {
        assertFalse(user.hasCompleteProfile());

        user.addSkill(new Skill(user.getId(), "Java", SkillType.TEACH));
        assertTrue(user.hasCompleteProfile());
    }

    @Test
    void testIsValidEmail() {
        assertTrue(user.isValidEmail());

        User badUser = new User();
        badUser.setEmail("invalid");
        assertFalse(badUser.isValidEmail());
    }
}