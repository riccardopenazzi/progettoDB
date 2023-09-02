package com.example.pizzapp.utils.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private static String dbName = "sql7643971";
    private static String user = "sql7643971";
    private static String password = "j9NB1vBmdj";

    public static Connection connectDb(){
        System.out.println("Cerco di connettermi");
        try {
            String connectionString = "jdbc:mysql://sql7.freesqldatabase.com:3306/" + dbName;
            Connection connection = DriverManager.getConnection(connectionString, user, password);
            if(connection == null){
                System.out.println("Connection è null");
            }
            return connection;
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
