package com.example.newdesign;

import com.example.newdesign.model.SkillType;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Skill model class.
 * Tests constructors, getters/setters, proficiency levels, and string formatting.
 */
class SkillTest {

    @Test
    void testBasicConstructor() {
        Skill skill = new Skill(1, "Java", SkillType.skillType.TEACH);

        assertEquals(1, skill.getUserId());
        assertEquals("Java", skill.getSkillName());
        assertEquals(SkillType.skillType.TEACH, skill.getType());
        assertEquals(com.example.newdesign.model.SkillType.Proficiency.BEGINNER, skill.getProficiency());
        assertNull(skill.getCategory());
    }

    @Test
    void testFullConstructor() {
        Skill skill = new Skill(10, 5, "Guitar", SkillType.skillType.LEARN, com.example.newdesign.model.SkillType.Proficiency.INTERMEDIATE, "Music");

        assertEquals(10, skill.getId());
        assertEquals(5, skill.getUserId());
        assertEquals("Guitar", skill.getSkillName());
        assertEquals(SkillType.skillType.LEARN, skill.getType());
        assertEquals(com.example.newdesign.model.SkillType.Proficiency.INTERMEDIATE, skill.getProficiency());
        assertEquals("Music", skill.getCategory());
    }

    @Test
    void testDefaultConstructorAndSetters() {
        Skill skill = new Skill();
        skill.setId(20);
        skill.setUserId(3);
        skill.setSkillName("Python");
        skill.setType(SkillType.skillType.TEACH);
        skill.setProficiency(com.example.newdesign.model.SkillType.Proficiency.EXPERT);
        skill.setCategory("Programming");

        assertEquals(20, skill.getId());
        assertEquals(3, skill.getUserId());
        assertEquals("Python", skill.getSkillName());
        assertEquals(SkillType.skillType.TEACH, skill.getType());
        assertEquals(com.example.newdesign.model.SkillType.Proficiency.EXPERT, skill.getProficiency());
        assertEquals("Programming", skill.getCategory());
    }

    @Test
    void testProficiencyLevels() {
        Skill skill = new Skill(1, "Cooking", SkillType.skillType.TEACH);

        skill.setProficiency(com.example.newdesign.model.SkillType.Proficiency.BEGINNER);
        assertEquals(com.example.newdesign.model.SkillType.Proficiency.BEGINNER, skill.getProficiency());

        skill.setProficiency(com.example.newdesign.model.SkillType.Proficiency.INTERMEDIATE);
        assertEquals(com.example.newdesign.model.SkillType.Proficiency.INTERMEDIATE, skill.getProficiency());

        skill.setProficiency(com.example.newdesign.model.SkillType.Proficiency.ADVANCED);
        assertEquals(com.example.newdesign.model.SkillType.Proficiency.ADVANCED, skill.getProficiency());

        skill.setProficiency(com.example.newdesign.model.SkillType.Proficiency.EXPERT);
        assertEquals(com.example.newdesign.model.SkillType.Proficiency.EXPERT, skill.getProficiency());
    }

    @Test
    void testToString() {
        Skill skill = new Skill(1, "Java", SkillType.skillType.TEACH);
        skill.setProficiency(com.example.newdesign.model.SkillType.Proficiency.ADVANCED);

        String result = skill.toString();
        assertTrue(result.contains("Java"));
        assertTrue(result.contains("ADVANCED"));
    }

    @Test
    void testToStringWithDefaultProficiency() {
        Skill skill = new Skill(1, "Dancing", SkillType.skillType.LEARN);

        String result = skill.toString();
        assertTrue(result.contains("Dancing"));
        assertTrue(result.contains("BEGINNER"));
    }
}