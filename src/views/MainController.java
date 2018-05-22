/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import models.Appointment;
import models.AppointmentDB;
import models.Customer;
import models.CustomerDB;

/**
 * FXML Controller class
 *
 * @author Benjamin
 */
public class MainController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    public void handleCustomerButton() {
        try {
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("CustomerMain.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.out.println("Customer Main Error: " + e.getMessage());
        }
    }
    
    @FXML
    public void handleAppointmentButton() {
        try {
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("AppointmentMain.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.out.println("Appointment Main Error: " + e.getMessage());
        }
    }
    
    @FXML
    public void handleReportsButton() {
        try {
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("ReportsMain.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.out.println("Reports Main Error: " + e.getMessage());
        }
    }
    
    @FXML
    public void handleLogsButton() {
        File file = new File("log.txt");
        if(file.exists()) {
            if(Desktop.isDesktopSupported()) {
                try {
                    Desktop.getDesktop().open(file);
                } catch (IOException e) {
                    System.out.println("Error Opening Log File: " + e.getMessage());
                }
            }
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Appointment appointment = AppointmentDB.appointmentIn15Min();
        if(appointment != null) {
            Customer customer = CustomerDB.getCustomer(appointment.getAptCustId());
            String text = String.format("You have a %s appointment with %s at %s",
                appointment.getAptDescription(), 
                customer.getCustomerName(),
                appointment.get15Time());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Appointment Reminder");
            alert.setHeaderText("Appointment Within 15 Minutes");
            alert.setContentText(text);
            alert.showAndWait();
        }
    }    
    
}
