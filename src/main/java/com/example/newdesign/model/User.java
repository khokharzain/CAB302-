package com.example.newdesign.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class User {

    // ========== EXISTING FIELDS (Amir's) ==========
    private int id;                    // NEW: Added for database ID
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String passwordHash;
    private String profilePicture;

    // ========== NEW FIELDS (Byron's) ==========
    private String username;           // Display handle (e.g., @john_doe)
    private String bio;                // User's bio/description
    private String location;           // City/region
    private LocalDate joinDate;        // When user registered

    // Relationships (in-memory, will connect to database later)
    private List<Skill> skills;
    private List<Review> reviews;
    private List<Hobby> hobbies;

    // ========== CONSTRUCTORS ==========

    // Default constructor (for frameworks/database)
    public User() {
        this.id = 0;
        this.skills = new ArrayList<>();
        this.reviews = new ArrayList<>();
        this.hobbies = new ArrayList<>();
        this.joinDate = LocalDate.now();
    }

    // Amir's existing constructor (for signup)
    public User(String firstName, String lastName, String email, String phone, String password, String profilePicture) {
        this();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        setPassword(password);
        this.profilePicture = profilePicture;
        // Generate default username from firstName + lastName
        this.username = (firstName + lastName).toLowerCase().replaceAll("\\s+", "");
        this.joinDate = LocalDate.now();
    }

    // Amir's existing constructor (for login)
    public User(String firstName, String lastName, String email, String phone, String profilePicture) {
        this();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.profilePicture = profilePicture;
        this.username = (firstName + lastName).toLowerCase().replaceAll("\\s+", "");
        this.joinDate = LocalDate.now();
    }

    // Byron's full constructor (with all fields including id)
    public User(int id, String firstName, String lastName, String email, String phone,
                String passwordHash, String profilePicture, String username,
                String bio, String location, LocalDate joinDate) {
        this();
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.passwordHash = passwordHash;
        this.profilePicture = profilePicture;
        this.username = username;
        this.bio = bio;
        this.location = location;
        this.joinDate = joinDate != null ? joinDate : LocalDate.now();
    }

    // ========== GETTERS & SETTERS (Amir's + Byron's) ==========

    // ID field (Byron's)
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    // Amir's existing getters/setters
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public String getProfilePicture() { return profilePicture; }
    public void setProfilePicture(String profilePicture) { this.profilePicture = profilePicture; }

    // Byron's added getters and setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public LocalDate getJoinDate() { return joinDate; }
    public void setJoinDate(LocalDate joinDate) { this.joinDate = joinDate; }

    public List<Skill> getSkills() { return skills; }
    public void setSkills(List<Skill> skills) { this.skills = skills; }

    public List<Review> getReviews() { return reviews; }
    public void setReviews(List<Review> reviews) { this.reviews = reviews; }

    public List<Hobby> getHobbies() { return hobbies; }
    public void setHobbies(List<Hobby> hobbies) { this.hobbies = hobbies; }

    // Password setter (keeps Amir's hashing method)
    public void setPassword(String password) {
        this.passwordHash = Integer.toHexString(password.hashCode());
    }

    // ========== SKILL MANAGEMENT ==========

    public void addSkill(Skill skill) {
        if (this.skills == null) {
            this.skills = new ArrayList<>();
        }
        this.skills.add(skill);
    }

    public void removeSkill(Skill skill) {
        if (this.skills != null) {
            this.skills.remove(skill);
        }
    }

    public List<Skill> getTeachSkills() {
        if (skills == null) return new ArrayList<>();
        return skills.stream()
                .filter(s -> s.getType() == SkillType.TEACH)
                .collect(Collectors.toList());
    }

    public List<Skill> getLearnSkills() {
        if (skills == null) return new ArrayList<>();
        return skills.stream()
                .filter(s -> s.getType() == SkillType.LEARN)
                .collect(Collectors.toList());
    }

    // ========== HOBBY MANAGEMENT ==========

    public void addHobby(Hobby hobby) {
        if (this.hobbies == null) {
            this.hobbies = new ArrayList<>();
        }
        this.hobbies.add(hobby);
    }

    public void removeHobby(Hobby hobby) {
        if (this.hobbies != null) {
            this.hobbies.remove(hobby);
        }
    }

    // ========== REVIEW MANAGEMENT ==========

    public void addReview(Review review) {
        if (this.reviews == null) {
            this.reviews = new ArrayList<>();
        }
        this.reviews.add(review);
    }

    public double getAverageRating() {
        if (reviews == null || reviews.isEmpty()) return 0.0;
        return reviews.stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(0.0);
    }

    public String getFormattedRating() {
        double avg = getAverageRating();
        if (avg == 0.0) return "No ratings yet";
        return String.format("%.1f ★ from %d review%s", avg, reviews.size(), reviews.size() == 1 ? "" : "s");
    }

    // ========== VALIDATION / BUSINESS LOGIC ==========

    public boolean isValidEmail() {
        return email != null && email.contains("@") && email.contains(".");
    }

    public boolean hasCompleteProfile() {
        return firstName != null && !firstName.trim().isEmpty()
                && lastName != null && !lastName.trim().isEmpty()
                && skills != null && !skills.isEmpty()
                && getTeachSkills().size() > 0;
    }

    // ========== DISPLAY HELPERS ==========

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String getDisplayName() {
        return getFullName() + (username != null ? " @" + username : "");
    }

    @Override
    public String toString() {
        return getFullName();
    }
}