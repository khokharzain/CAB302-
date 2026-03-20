package com.example.newdesign;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DBconnection {
    private static final String url= "jdbc:mysql://localhost:3306/application_db";
    private static final String user = "root";
    private static final String password = "Afghanistan14";

    public static Connection getConnection() throws SQLException{
        return DriverManager.getConnection(url, user, password);
    }
}
