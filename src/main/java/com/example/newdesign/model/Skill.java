package com.example.newdesign.model;

public class Skill {
    private int id;
    private int userId;           // Which user owns this skill
    private String skillName;     // e.g., "Java", "Guitar", "Photography"
    private SkillType type;       // TEACH or LEARN
    private Proficiency proficiency;
    private String category;      // Optional: "Programming", "Music", etc.

    // Default constructor
    public Skill() {}

    // Constructor for new skill
    public Skill(int userId, String skillName, SkillType type) {
        this.userId = userId;
        this.skillName = skillName;
        this.type = type;
        this.proficiency = Proficiency.BEGINNER;
    }

    // Full constructor
    public Skill(int id, int userId, String skillName, SkillType type,
                 Proficiency proficiency, String category) {
        this.id = id;
        this.userId = userId;
        this.skillName = skillName;
        this.type = type;
        this.proficiency = proficiency;
        this.category = category;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getSkillName() { return skillName; }
    public void setSkillName(String skillName) { this.skillName = skillName; }

    public SkillType getType() { return type; }
    public void setType(SkillType type) { this.type = type; }

    public Proficiency getProficiency() { return proficiency; }
    public void setProficiency(Proficiency proficiency) { this.proficiency = proficiency; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    @Override
    public String toString() {
        // Shows proficiency for all levels (including BEGINNER)
        String proficiencyText = proficiency != null ? " (" + proficiency + ")" : "";
        return skillName + proficiencyText;
    }
}