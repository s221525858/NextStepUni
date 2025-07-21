package com.example.project;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
    public Connection con;

    public Connection connectToDB() {
        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");

            String url = "jdbc:ucanaccess://C:\\OtherProjectFiles\\NextStepUniDB.accdb";
            con = DriverManager.getConnection(url);
            System.out.println("--------Connected to database---------");

        } catch (Exception e) {
            System.out.println("Connection Failed! Check output console\n" + e.getMessage());
        }
        return con;
    }
}
