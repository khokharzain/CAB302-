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
                    "email TEXT, " +
                    "phone TEXT, " +
                    "passwordHash TEXT" +
                    ")";

            stmt.execute(sql);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

