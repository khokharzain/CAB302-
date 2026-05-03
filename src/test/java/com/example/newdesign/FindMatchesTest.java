package com.example.newdesign;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Tests for FIND MATCHES feature - filtering users by selected skill
 */
public class FindMatchesTest {

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
     * Test: Filter returns only users who teach the selected skill
     */
    @Test
    void testFilterUsersBySkill_ReturnsOnlyMatchingUsers() {
        currentUser.addSkill(new Skill(1, "Java", SkillType.LEARN));
        user1.addSkill(new Skill(2, "Java", SkillType.TEACH));
        user2.addSkill(new Skill(3, "Python", SkillType.TEACH));

        List<User> allUsers = Arrays.asList(user1, user2);
        String selectedSkill = "Java";

        List<User> matches = filterUsersBySkill(allUsers, selectedSkill);

        assertEquals(1, matches.size());
        assertEquals("Amir", matches.get(0).getFirstName());
    }

    /**
     * Test: Empty list when no users teach the selected skill
     */
    @Test
    void testFilterUsersBySkill_NoMatches_ReturnsEmpty() {
        currentUser.addSkill(new Skill(1, "Java", SkillType.LEARN));
        user1.addSkill(new Skill(2, "Python", SkillType.TEACH));

        List<User> allUsers = Arrays.asList(user1);
        String selectedSkill = "Java";

        List<User> matches = filterUsersBySkill(allUsers, selectedSkill);

        assertTrue(matches.isEmpty());
    }

    /**
     * Test: Case insensitive skill matching
     */
    @Test
    void testFilterUsersBySkill_CaseInsensitive() {
        currentUser.addSkill(new Skill(1, "Java", SkillType.LEARN));
        user1.addSkill(new Skill(2, "JAVA", SkillType.TEACH)); // Uppercase

        List<User> allUsers = Arrays.asList(user1);
        String selectedSkill = "java"; // Lowercase

        List<User> matches = filterUsersBySkill(allUsers, selectedSkill);

        assertEquals(1, matches.size());
    }

    /**
     * Test: Match reason when user can teach them
     */
    @Test
    void testGetMatchReason_YouCanTeachThem() {
        currentUser.addSkill(new Skill(1, "Python", SkillType.TEACH));
        user1.addSkill(new Skill(2, "Python", SkillType.LEARN));

        String reason = getMatchReason(currentUser, user1);

        assertTrue(reason.contains("You can teach them"));
        assertTrue(reason.contains("Python"));
    }

    /**
     * Test: Match reason when they can teach you
     */
    @Test
    void testGetMatchReason_TheyCanTeachYou() {
        currentUser.addSkill(new Skill(1, "Java", SkillType.LEARN));
        user1.addSkill(new Skill(2, "Java", SkillType.TEACH));

        String reason = getMatchReason(currentUser, user1);

        assertTrue(reason.contains("They can teach you"));
    }

    private List<User> filterUsersBySkill(List<User> users, String skill) {
        List<User> matches = new ArrayList<>();
        for (User u : users) {
            for (Skill s : u.getTeachSkills()) {
                if (s.getSkillName().equalsIgnoreCase(skill)) {
                    matches.add(u);
                    break;
                }
            }
        }
        return matches;
    }

    private String getMatchReason(User current, User match) {
        for (Skill myTeach : current.getTeachSkills()) {
            for (Skill theirWant : match.getLearnSkills()) {
                if (myTeach.getSkillName().equalsIgnoreCase(theirWant.getSkillName())) {
                    return "You can teach them " + myTeach.getSkillName() + "!";
                }
            }
        }
        return "They can teach you a skill you want to learn!";
    }
}