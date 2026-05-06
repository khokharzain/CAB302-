package com.example.newdesign;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Unit tests for the AI Assistant features including match calculation
 * and skill formatting functionality.
 *
 * <p>This test class verifies the core logic used in:
 * <ul>
 *   <li>FIND MATCHES feature - calculating compatibility scores</li>
 *   <li>COMPARE USERS feature - comparing two users side-by-side</li>
 *   <li>Skill display formatting</li>
 * </ul>
 *
 * @author Shahad W
 * @version 1.0
 * @since Week 9
 */
public class MatchCalculationTest {

    private User currentUser;
    private User otherUser;

    /**
     * Sets up test data before each test method.
     * Creates two test users with distinct IDs and names.
     */
    @BeforeEach
    void setUp() {
        currentUser = new User();
        currentUser.setId(1);
        currentUser.setFirstName("Shahad");
        currentUser.setLastName("W");

        otherUser = new User();
        otherUser.setId(2);
        otherUser.setFirstName("Amir");
        otherUser.setLastName("S");
    }

    // ========== MATCH CALCULATION TESTS ==========

    /**
     * Test 1: Verifies that a mutual perfect match returns 100%.
     * Both users teach what the other wants to learn.
     *
     * <p>Scenario: Current wants Java, other teaches Java.
     *            Other wants Python, current teaches Python.
     * <p>Expected: Match score = 100
     */
    @Test
    void testPerfectMutualMatch_Returns100() {
        // Current wants Java, other teaches Java
        currentUser.addSkill(new Skill(1, "Java", SkillType.LEARN));
        otherUser.addSkill(new Skill(2, "Java", SkillType.TEACH));

        // Other wants Python, current teaches Python
        otherUser.addSkill(new Skill(2, "Python", SkillType.LEARN));
        currentUser.addSkill(new Skill(1, "Python", SkillType.TEACH));

        int score = calculateMatchScore(currentUser, otherUser);

        assertEquals(100, score, "Mutual exchange should return 100%");
    }

    /**
     * Test 2: Verifies that one-way match returns 50%.
     * Other teaches what current wants, but no exchange back.
     *
     * <p>Scenario: Current wants Java, other teaches Java.
     *            No skills that current teaches that other wants.
     * <p>Expected: Match score = 50
     */
    @Test
    void testOneWayMatch_Returns50() {
        currentUser.addSkill(new Skill(1, "Java", SkillType.LEARN));
        otherUser.addSkill(new Skill(2, "Java", SkillType.TEACH));

        int score = calculateMatchScore(currentUser, otherUser);

        assertEquals(50, score, "One-way match should return 50%");
    }

    /**
     * Test 3: Verifies that no match returns 0%.
     *
     * <p>Scenario: Current wants Java, other teaches Python.
     * <p>Expected: Match score = 0
     */
    @Test
    void testNoMatch_Returns0() {
        currentUser.addSkill(new Skill(1, "Java", SkillType.LEARN));
        otherUser.addSkill(new Skill(2, "Python", SkillType.TEACH));

        int score = calculateMatchScore(currentUser, otherUser);

        assertEquals(0, score, "No matching skills should return 0%");
    }

    /**
     * Test 4: Verifies that multiple skill matches still cap at 100%.
     *
     * <p>Scenario: Three skills match (Java, Python, C++) but score should cap at 100.
     * <p>Expected: Match score = 100 (not 150)
     */
    @Test
    void testMultipleMatches_CapsAt100() {
        // Three skills match - should still be 100% not 150%
        currentUser.addSkill(new Skill(1, "Java", SkillType.LEARN));
        currentUser.addSkill(new Skill(1, "Python", SkillType.LEARN));
        currentUser.addSkill(new Skill(1, "C++", SkillType.LEARN));

        otherUser.addSkill(new Skill(2, "Java", SkillType.TEACH));
        otherUser.addSkill(new Skill(2, "Python", SkillType.TEACH));
        otherUser.addSkill(new Skill(2, "C++", SkillType.TEACH));

        int score = calculateMatchScore(currentUser, otherUser);

        assertEquals(100, score, "Multiple matches should cap at 100%");
    }

    // ========== SKILL FORMATTING TESTS ==========

    /**
     * Test 5: Verifies that an empty skill list returns "None".
     *
     * <p>Scenario: User has no skills added
     * <p>Expected: Formatted string = "None"
     */
    @Test
    void testFormatSkills_EmptyList_ReturnsNone() {
        List<Skill> emptySkills = new ArrayList<>();

        String result = formatSkills(emptySkills);

        assertEquals("None", result, "Empty skill list should return 'None'");
    }

    /**
     * Test 6: Verifies that a null skill list returns "None".
     *
     * <p>Scenario: User object has null skills list
     * <p>Expected: Formatted string = "None"
     */
    @Test
    void testFormatSkills_NullList_ReturnsNone() {
        String result = formatSkills(null);

        assertEquals("None", result, "Null skill list should return 'None'");
    }

    /**
     * Test 7: Verifies that multiple skills are formatted as comma-separated string.
     *
     * <p>Scenario: User has skills "Java" and "Python"
     * <p>Expected: Formatted string = "Java, Python"
     */
    @Test
    void testFormatSkills_WithSkills_ReturnsCommaSeparated() {
        List<Skill> skills = Arrays.asList(
                new Skill(1, "Java", SkillType.TEACH),
                new Skill(1, "Python", SkillType.TEACH)
        );

        String result = formatSkills(skills);

        assertEquals("Java, Python", result, "Skills should be comma-separated");
    }

    /**
     * Test 8: Verifies that a single skill returns just the skill name.
     *
     * <p>Scenario: User has only one skill "Java"
     * <p>Expected: Formatted string = "Java" (no comma)
     */
    @Test
    void testFormatSkills_SingleSkill_ReturnsSkillNameOnly() {
        List<Skill> skills = Arrays.asList(new Skill(1, "Java", SkillType.TEACH));

        String result = formatSkills(skills);

        assertEquals("Java", result, "Single skill should return just the name");
    }

    // ========== HELPER METHODS ==========

    /**
     * Calculates the compatibility match score between two users.
     *
     * <p>The scoring algorithm:
     * <ul>
     *   <li>+50 points for each skill the other user teaches that current user wants to learn</li>
     *   <li>+50 points for each skill current user teaches that other user wants to learn</li>
     *   <li>Maximum score is capped at 100%</li>
     * </ul>
     *
     * @param current the current logged-in user
     * @param other the other user to compare with
     * @return match score between 0 and 100
     */
    private int calculateMatchScore(User current, User other) {
        int score = 0;

        // Check skills current user wants to learn vs what other user can teach
        for (Skill myWant : current.getLearnSkills()) {
            for (Skill theirTeach : other.getTeachSkills()) {
                if (myWant.getSkillName().equalsIgnoreCase(theirTeach.getSkillName())) {
                    score += 50;
                }
            }
        }

        // Check skills current user can teach vs what other user wants to learn
        for (Skill myTeach : current.getTeachSkills()) {
            for (Skill theirWant : other.getLearnSkills()) {
                if (myTeach.getSkillName().equalsIgnoreCase(theirWant.getSkillName())) {
                    score += 50;
                }
            }
        }

        return Math.min(score, 100);
    }

    /**
     * Formats a list of skills into a human-readable string.
     *
     * <p>Examples:
     * <ul>
     *   <li>Empty list → "None"</li>
     *   <li>["Java"] → "Java"</li>
     *   <li>["Java", "Python"] → "Java, Python"</li>
     * </ul>
     *
     * @param skills the list of skills to format (can be null)
     * @return formatted string of skill names or "None" if empty/null
     */
    private String formatSkills(List<Skill> skills) {
        if (skills == null || skills.isEmpty()) return "None";
        return skills.stream()
                .map(Skill::getSkillName)
                .collect(java.util.stream.Collectors.joining(", "));
    }
}