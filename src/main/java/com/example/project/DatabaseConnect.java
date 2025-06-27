package com.example.project;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnect {
    public static Connection connect() {
        Connection conn = null;
        try {
            String url = "jdbc:ucanaccess://C:\\PROJECT2025\\PROJECTDOCUMENTS\\NextStepUni.accdb";
            conn = DriverManager.getConnection(url);
            System.out.println("Connected to database successfully.");
        } catch (SQLException e) {
            System.out.println("Database connection failed.");
            e.printStackTrace();
        }
        return conn;
    }
}

