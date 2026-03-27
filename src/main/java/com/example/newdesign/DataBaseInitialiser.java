package com.example.newdesign;

import java.sql.Connection;
import java.sql.Statement;

public class DataBaseInitialiser {
    public static void initialize() {
        try {
            Connection conn = DBconnection.connect();
            Statement stmt = conn.createStatement();

            String sql = "CREATE TABLE IF NOT EXISTS Users (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "firstName TEXT, " +
                    "lastName TEXT, " +
                    "email TEXT unique, " +
                    "phone TEXT, " +
                    "passwordHash TEXT" +
                    ")";

            stmt.execute(sql);

            // 👇 SAFE ALTER (won't crash)
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