package com.example.newdesign;

/**
 * A simple model class representing a skill with userId, skillName, type, proficiency and category.
 */
public class Skill {
    private int id;
    private int userId;
    private String skillName;
    private SkillType type;
    private Proficiency proficiency;
    private String category;

    public Skill() {}

    /**
     * @param userId
     * @param skillName
     * @param type
     */
    public Skill(int userId, String skillName, SkillType type) {
        this.userId = userId;
        this.skillName = skillName;
        this.type = type;
        this.proficiency = Proficiency.BEGINNER;
    }

    /**
     * @param id
     * @param userId
     * @param skillName
     * @param type
     * @param proficiency
     * @param category
     */
    public Skill(int id, int userId, String skillName, SkillType type,
                 Proficiency proficiency, String category) {
        this.id = id;
        this.userId = userId;
        this.skillName = skillName;
        this.type = type;
        this.proficiency = proficiency;
        this.category = category;
    }

    /**
     * @return id
     */
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    /**
     * @return userId
     */
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    /**
     * @return skillName
     */
    public String getSkillName() { return skillName; }
    public void setSkillName(String skillName) { this.skillName = skillName; }

    /**
     * @return type
     */
    public SkillType getType() { return type; }
    public void setType(SkillType type) { this.type = type; }

    /**
     * @return proficiency
     */
    public Proficiency getProficiency() { return proficiency; }
    public void setProficiency(Proficiency proficiency) { this.proficiency = proficiency; }

    /**
     * @return category
     */
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    @Override
    public String toString() {
        String proficiencyText = proficiency != null ? " (" + proficiency + ")" : "";
        return skillName + proficiencyText;
    }
}