package com.example.newdesign.model;

/**
 * A simple enum representing whether a user wants to teach or learn a skill.
 */
public enum SkillType {
    /** User can teach this skill to others */
    TEACH,
    /** User wants to learn this skill from others */
    LEARN;

    /**
     * A simple enum representing the proficiency level of a skill.
     */
    public enum Proficiency {
        /** Beginner level - just started learning */
        BEGINNER,
        /** Intermediate level - comfortable but not expert */
        INTERMEDIATE,
        /** Advanced level - deep knowledge and experience */
        ADVANCED,
        /** Expert level - can teach others professionally */
        EXPERT
    }

}
