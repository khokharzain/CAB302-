package com.example.newdesign;

import java.sql.Connection;
import java.sql.Statement;

public class DataBaseInitialiser {

    public static void initialize() {
        try {
            Connection conn = DBconnection.connect();
            Statement stmt = conn.createStatement();

            //  Create table with profile_picture included
            String sql = "CREATE TABLE IF NOT EXISTS Users (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "firstName TEXT, " +
                    "lastName TEXT, " +
                    "email TEXT UNIQUE, " +
                    "phone TEXT, " +
                    "passwordHash TEXT, " +
                    "profile_picture TEXT DEFAULT 'default.png'" +
                    ")";

            stmt.execute(sql);

            //  Safe ALTER
            try {
                String addProfile = "ALTER TABLE Users ADD COLUMN profile_picture TEXT DEFAULT 'default.png'";
                stmt.execute(addProfile);
            } catch (Exception e) {
                System.out.println("Column already exists ✔");
            }

            // ========== BYRON'S ADDITIONS (User Profile System) ==========
            // Add new columns to Users table for enhanced profile
            try {
                stmt.execute("ALTER TABLE Users ADD COLUMN username TEXT");
                System.out.println("Added username column ✔");
            } catch (Exception e) {
                System.out.println("username column already exists ✔");
            }

            try {
                stmt.execute("ALTER TABLE Users ADD COLUMN bio TEXT");
                System.out.println("Added bio column ✔");
            } catch (Exception e) {
                System.out.println("bio column already exists ✔");
            }

            try {
                stmt.execute("ALTER TABLE Users ADD COLUMN location TEXT");
                System.out.println("Added location column ✔");
            } catch (Exception e) {
                System.out.println("location column already exists ✔");
            }

            try {
                stmt.execute("ALTER TABLE Users ADD COLUMN joinDate TEXT");
                System.out.println("Added joinDate column ✔");
            } catch (Exception e) {
                System.out.println("joinDate column already exists ✔");
            }

            // Create Skills table (Byron's addition)
            String createSkillsTable = "CREATE TABLE IF NOT EXISTS Skills (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "userId INTEGER NOT NULL, " +
                    "skillName TEXT NOT NULL, " +
                    "type TEXT NOT NULL, " +          // 'TEACH' or 'LEARN'
                    "proficiency TEXT, " +             // 'BEGINNER', 'INTERMEDIATE', 'ADVANCED', 'EXPERT'
                    "category TEXT, " +                // Optional: 'Programming', 'Music', etc.
                    "FOREIGN KEY (userId) REFERENCES Users(id)" +
                    ")";
            stmt.execute(createSkillsTable);
            System.out.println("Created Skills table ✔");

            // Create Hobbies table (Byron's addition)
            String createHobbiesTable = "CREATE TABLE IF NOT EXISTS Hobbies (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "userId INTEGER NOT NULL, " +
                    "hobbyName TEXT NOT NULL, " +
                    "FOREIGN KEY (userId) REFERENCES Users(id)" +
                    ")";
            stmt.execute(createHobbiesTable);
            System.out.println("Created Hobbies table ✔");

            // Create Reviews table (Byron's addition)
            String createReviewsTable = "CREATE TABLE IF NOT EXISTS Reviews (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "reviewerId INTEGER NOT NULL, " +
                    "revieweeId INTEGER NOT NULL, " +
                    "exchangeId INTEGER, " +
                    "rating INTEGER NOT NULL, " +
                    "comment TEXT, " +
                    "createdAt TEXT NOT NULL, " +
                    "FOREIGN KEY (reviewerId) REFERENCES Users(id), " +
                    "FOREIGN KEY (revieweeId) REFERENCES Users(id)" +
                    ")";
            stmt.execute(createReviewsTable);
            System.out.println("Created Reviews table ✔");



            String createPostsTable = "CREATE TABLE IF NOT EXISTS Posts (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "userId INTEGER NOT NULL, " +
                    "content TEXT NOT NULL, " +
                    "createdAt TEXT NOT NULL, " +
                    "FOREIGN KEY (userId) REFERENCES Users(id)" +
                    ")";
            stmt.execute(createPostsTable);
        } catch (Exception e) {
            e.printStackTrace();
        }



    }
}