package com.example.pizzapp.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String dbName = "sql11645122";

    private static final String user = "sql11645122";

    private static final String password = "l177bF6ARd";

    public static Connection connectDb() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            //String connectionString = "jdbc:mysql://sql7.freesqldatabase.com:3306/" + dbName;
            String connectionString = "jdbc:mysql://sql11.freesqldatabase.com:3306/" + dbName;
            Connection connection = DriverManager.getConnection(connectionString, user, password);
            return connection;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
