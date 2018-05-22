/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import utils.Database;
import utils.Logger;

/**
 *
 * @author Benjamin
 */
public class UserDB {
    private static User currentUser;
    
    public static User getCurrentUser() {
        return currentUser;
    }
    
    // Attempt Login
    public static Boolean login(String username, String password) {
        try {
            Statement statement = Database.getConnection().createStatement();
            String query = "SELECT * FROM user WHERE userName='" + username + "' AND password='" + password + "'";
            ResultSet results = statement.executeQuery(query);
            if(results.next()) {
                currentUser = new User();
                currentUser.setUsername(results.getString("userName"));
                statement.close();
                Logger.log(username, true);
                return true;
            } else {
                Logger.log(username, false);
                return false;
            }
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            return false;
        }
    }
}
