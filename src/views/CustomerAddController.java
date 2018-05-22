/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import models.CustomerDB;

/**
 * FXML Controller class
 *
 * @author Benjamin
 */
public class CustomerAddController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private TextField name;
    
    @FXML
    private TextField address;
    
    @FXML
    private ComboBox city;
    
    @FXML
    private TextField country;
    
    @FXML
    private TextField zip;
    
    @FXML
    private TextField phone;
    
    private ObservableList<String> cities = FXCollections.observableArrayList(
    "New York","Washington DC","Miami","Dallas","Phoenix","Los Angeles","London","Liverpool","Manchester","Oxford");
    
    private ObservableList<String> errors = FXCollections.observableArrayList();
    
    @FXML
    public void setCountry() {
        String currentCity = city.getSelectionModel().getSelectedItem().toString();
        if(currentCity.equals("London")||currentCity.equals("Liverpool")||currentCity.equals("Manchester")||currentCity.equals("Oxford")) {
            country.setText("England");
        } else {
            country.setText("United States");       
        }
    }
    
    public boolean handleAddCustomer() {
        errors.clear();
        String customerName = name.getText();
        String customerAddress = address.getText();
        int customerCity = city.getSelectionModel().getSelectedIndex() + 1;
        String customerZip = zip.getText();
        String customerPhone = phone.getText();
        if(!validateName(customerName)||!validateAddress(customerAddress)||!validateCity(customerCity)||
                !validateZip(customerZip)||!validatePhone(customerPhone)){
            return false;
        } else {
            return CustomerDB.saveCustomer(customerName, customerAddress, customerCity, customerZip, customerPhone);
        }
    }
    
    public boolean validateName(String name) {
        if(name.isEmpty()) {
            errors.add("Name must contain characters");
            return false;
        } else {
            return true;
        }
    }
    
    public boolean validateAddress(String address) {
        if(address.isEmpty()) {
            errors.add("Address must contain characters");
            return false;
        } else {
            return true;
        }
    }
    
    public boolean validateCity(int city) {
        if(city == 0) {
            errors.add("A City must be selected");
            return false;
        } else {
            return true;
        }
    }
    
    public boolean validateZip(String zip) {
        if(zip.isEmpty()) {
            errors.add("Zip must contain characters");
            return false;
        } else {
            return true;
        }
    }
    
    public boolean validatePhone(String phone) {
        if(phone.isEmpty()) {
            errors.add("Phone must contain characters");
            return false;
        } else {
            return true;
        }
    }
    
    public String displayErrors(){
        String s = "";
        if(errors.size() > 0) {
            for(String err : errors) {
                s = s.concat(err);
            }
            return s;
        } else {
            s = "Database Error";
            return s;
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        city.setItems(cities);
    }    
    
}
