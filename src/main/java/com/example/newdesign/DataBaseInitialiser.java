package com.example.newdesign;

import java.sql.Connection;
import java.sql.Statement;

public class DataBaseInitialiser {

    public static void initialize() {
        try {
            Connection conn = DBconnection.connect();
            Statement stmt = conn.createStatement();

            // ✅ Create table with profile_picture included
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

            // ✅ Safe ALTER for existing databases (won’t crash if column already exists)
            try {
                String addProfile = "ALTER TABLE Users ADD COLUMN profile_picture TEXT DEFAULT 'default.png'";
                stmt.execute(addProfile);
            } catch (Exception e) {
                System.out.println("Column already exists ✔");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}