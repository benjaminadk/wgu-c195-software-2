/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Benjamin
 */
public class Database {
    private static final String DBNAME = "U057rs";
    private static final String URL = "jdbc:mysql://52.206.157.109/" + DBNAME;
    private static final String USER = "U057rs";
    private static final String PASS = "53688433519";
    private static final String DRIVER = "com.mysql.jdbc.Driver"; 
    private static Connection conn;
    
    public Database() {}
    
    // Connect to Database
    public static void connect() {
        try {
            Class.forName(DRIVER);
            conn = DriverManager.getConnection(URL, USER, PASS);
            System.out.println("Connected to MySQL Database");
        } catch (ClassNotFoundException e) {
            System.out.println("Class Not Found " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage()); 
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
    }
    
    // Close Database Connection
    public static void disconnect() {
        try {
            conn.close();
            System.out.println("Disconnected From MySQL Database");
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }
    }
    
    // Returns Database Connection
    public static Connection getConnection() {
        return conn;
    }
}
