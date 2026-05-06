package com.example.newdesign;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

/**
 * Tests for COMPARE USERS feature - side-by-side user comparison
 */
public class CompareUsersTest {

    private User currentUser;
    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        currentUser = new User();
        currentUser.setId(1);
        currentUser.setFirstName("Shahad");

        user1 = new User();
        user1.setId(2);
        user1.setFirstName("Amir");

        user2 = new User();
        user2.setId(3);
        user2.setFirstName("Byron");
    }

    /**
     * Test: Comparison shows correct teach skills for each user
     */
    @Test
    void testComparison_ShowsTeachSkills() {
        user1.addSkill(new Skill(2, "Java", SkillType.TEACH));
        user1.addSkill(new Skill(2, "Python", SkillType.TEACH));
        user2.addSkill(new Skill(3, "JavaScript", SkillType.TEACH));

        String user1Teach = formatSkills(user1.getTeachSkills());
        String user2Teach = formatSkills(user2.getTeachSkills());

        assertEquals("Java, Python", user1Teach);
        assertEquals("JavaScript", user2Teach);
    }

    /**
     * Test: Comparison shows correct learn skills for each user
     */
    @Test
    void testComparison_ShowsLearnSkills() {
        user1.addSkill(new Skill(2, "React", SkillType.LEARN));
        user1.addSkill(new Skill(2, "Angular", SkillType.LEARN));
        user2.addSkill(new Skill(3, "Vue", SkillType.LEARN));

        String user1Learn = formatSkills(user1.getLearnSkills());
        String user2Learn = formatSkills(user2.getLearnSkills());

        assertEquals("React, Angular", user1Learn);
        assertEquals("Vue", user2Learn);
    }

    /**
     * Test: Comparison recommends user with higher match score
     */
    @Test
    void testComparison_RecommendsHigherScore() {
        currentUser.addSkill(new Skill(1, "Java", SkillType.LEARN));

        user1.addSkill(new Skill(2, "Java", SkillType.TEACH));  // 50% match
        user2.addSkill(new Skill(3, "Python", SkillType.TEACH)); // 0% match

        String recommendation = getRecommendation(currentUser, user1, user2);

        assertEquals("Amir", recommendation);
    }

    /**
     * Test: Comparison handles users with no skills
     */
    @Test
    void testComparison_UsersWithNoSkills() {
        String user1Teach = formatSkills(user1.getTeachSkills());
        String user2Teach = formatSkills(user2.getTeachSkills());

        assertEquals("None", user1Teach);
        assertEquals("None", user2Teach);
    }

    /**
     * Test: Comparison shows rating correctly
     */
    @Test
    void testComparison_ShowsRating() {
        user1.addReview(new Review(2, 1, 0, 5, "Great!"));
        user1.addReview(new Review(3, 1, 0, 4, "Good"));

        String rating = user1.getFormattedRating();

        assertTrue(rating.contains("4.5"));
        assertTrue(rating.contains("2 reviews"));
    }

    /**
     * Test: Comparison shows "No reviews yet" when no reviews
     */
    @Test
    void testComparison_NoReviews() {
        String rating = user1.getFormattedRating();
        assertEquals("No ratings yet", rating);
    }

    private String formatSkills(List<Skill> skills) {
        if (skills == null || skills.isEmpty()) return "None";
        return skills.stream()
                .map(Skill::getSkillName)
                .collect(java.util.stream.Collectors.joining(", "));
    }

    private String getRecommendation(User current, User u1, User u2) {
        int score1 = calculateMatchScore(current, u1);
        int score2 = calculateMatchScore(current, u2);
        return score1 > score2 ? u1.getFirstName() : u2.getFirstName();
    }

    private int calculateMatchScore(User current, User other) {
        int score = 0;
        for (Skill myWant : current.getLearnSkills()) {
            for (Skill theirTeach : other.getTeachSkills()) {
                if (myWant.getSkillName().equalsIgnoreCase(theirTeach.getSkillName())) {
                    score += 50;
                }
            }
        }
        for (Skill myTeach : current.getTeachSkills()) {
            for (Skill theirWant : other.getLearnSkills()) {
                if (myTeach.getSkillName().equalsIgnoreCase(theirWant.getSkillName())) {
                    score += 50;
                }
            }
        }
        return Math.min(score, 100);
    }
}