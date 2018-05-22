/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import utils.Database;

/**
 *
 * @author Benjamin
 */
public class CustomerDB {
    
    private static ObservableList<Customer> allCustomers = FXCollections.observableArrayList();
    
    public static Customer getCustomer(int id) {
        try {
            Statement statement = Database.getConnection().createStatement();
            String query = "SELECT * FROM customer WHERE customerId='" + id + "'";
            ResultSet results = statement.executeQuery(query);
            if(results.next()) {
                Customer customer = new Customer();
                customer.setCustomerName(results.getString("customerName"));
                statement.close();
                return customer;
            }
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }
        return null;
    }
    
    // Returns all Customers in Database
    public static ObservableList<Customer> getAllCustomers() {
        allCustomers.clear();
        try {
            Statement statement = Database.getConnection().createStatement();
            String query = "SELECT customer.customerId, customer.customerName, address.address, address.phone, address.postalCode, city.city"
                + " FROM customer INNER JOIN address ON customer.addressId = address.addressId "
                + "INNER JOIN city ON address.cityId = city.cityId";
            ResultSet results = statement.executeQuery(query);
            while(results.next()) {
                Customer customer = new Customer(
                    results.getInt("customerId"), 
                    results.getString("customerName"), 
                    results.getString("address"),
                    results.getString("city"),
                    results.getString("phone"),
                    results.getString("postalCode"));
                allCustomers.add(customer);
            }
            statement.close();
            return allCustomers;
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            return null;
        }
    }
    
    // Saves new Customer to Database
    public static boolean saveCustomer(String name, String address, int cityId, String zip, String phone) {
        try {
            Statement statement = Database.getConnection().createStatement();
            String queryOne = "INSERT INTO address SET address='" + address + "', phone='" + phone + "', postalCode='" + zip + "', cityId=" + cityId;
            int updateOne = statement.executeUpdate(queryOne);
            if(updateOne == 1) {
                int addressId = allCustomers.size() + 1;
                String queryTwo = "INSERT INTO customer SET customerName='" + name + "', addressId=" + addressId;
                int updateTwo = statement.executeUpdate(queryTwo);
                if(updateTwo == 1) {
                    return true;
                }
            }
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }
        return false;
    }
    
    // Update existing Customer in Database
    public static boolean updateCustomer(int id, String name, String address, int cityId, String zip, String phone) {
        try {
            Statement statement = Database.getConnection().createStatement();
            String queryOne = "UPDATE address SET address='" + address + "', cityId=" + cityId + ", postalCode='" + zip + "', phone='" + phone + "' "
                + "WHERE addressId=" + id;
            int updateOne = statement.executeUpdate(queryOne);
            if(updateOne == 1) {
                String queryTwo = "UPDATE customer SET customerName='" + name + "', addressId=" + id + " WHERE customerId=" + id;
                int updateTwo = statement.executeUpdate(queryTwo);
                if(updateTwo == 1) {
                    return true;
                }
            }
        } catch(SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }
        return false;
    }
    
    // Delete Customer from Database
    public static boolean deleteCustomer(int id) {
        try {
            Statement statement = Database.getConnection().createStatement();
            String queryOne = "DELETE FROM address WHERE addressId=" + id;
            int updateOne = statement.executeUpdate(queryOne);
            if(updateOne == 1) {
                String queryTwo = "DELETE FROM customer WHERE customerId=" + id;
                int updateTwo = statement.executeUpdate(queryTwo);
                if(updateTwo == 1) {
                    return true;
                }
            }
        } catch(SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }
        return false;
    }
}
