/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import utils.Database;

/**
 * FXML Controller class
 *
 * @author Benjamin
 */
public class ReportsMainController implements Initializable {

    /**
     * Initializes the controller class.
     * @param event
     */
    @FXML
    private TextArea reportOne;
    
    @FXML
    private TextArea reportTwo;
    
    @FXML
    private TextArea reportThree;
    
    @FXML
    public void handleBackButton(ActionEvent event) {
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }
    
    public void handleReportOne() {
        try {
            Statement statement = Database.getConnection().createStatement();
            String query = "SELECT description, MONTHNAME(start) as 'Month', COUNT(*) as 'Total' FROM appointment GROUP BY description, MONTH(start)";
            ResultSet results = statement.executeQuery(query);
            StringBuilder reportOneText = new StringBuilder();
            reportOneText.append(String.format("%1$-55s %2$-55s %3$s \n", "Month", "Appointment Type", "Total"));
            reportOneText.append(String.join("", Collections.nCopies(110, "-")));
            reportOneText.append("\n");
            while(results.next()) {
                reportOneText.append(String.format("%1$-55s %2$-60s %3$d \n", 
                    results.getString("Month"), results.getString("description"), results.getInt("Total")));
            }
            statement.close();
            reportOne.setText(reportOneText.toString());
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }
    }    
    
    public void handleReportTwo() {
        try {
            Statement statement = Database.getConnection().createStatement();
            String query = "SELECT appointment.contact, appointment.description, customer.customerName, start, end " +
                    "FROM appointment JOIN customer ON customer.customerId = appointment.customerId " +
                    "GROUP BY appointment.contact, MONTH(start), start";
            ResultSet results = statement.executeQuery(query);
            StringBuilder reportTwoText = new StringBuilder();
            reportTwoText.append(String.format("%1$-25s %2$-25s %3$-25s %4$-25s %5$s \n", 
                    "Consultant", "Appointment", "Customer", "Start", "End"));
            reportTwoText.append(String.join("", Collections.nCopies(110, "-")));
            reportTwoText.append("\n");
            while(results.next()) {
                reportTwoText.append(String.format("%1$-25s %2$-25s %3$-25s %4$-25s %5$s \n", 
                    results.getString("contact"), results.getString("description"), results.getString("customerName"),
                    results.getString("start"), results.getString("end")));
            }
            statement.close();
            reportTwo.setText(reportTwoText.toString());
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }
    }
    
    public void handleReportThree() {
        try {
            Statement statement = Database.getConnection().createStatement();
            String query = "SELECT customer.customerName, COUNT(*) as 'Total' FROM customer JOIN appointment " +
                "ON customer.customerId = appointment.customerId GROUP BY customerName";
            ResultSet results = statement.executeQuery(query);
            StringBuilder reportThreeText = new StringBuilder();
            reportThreeText.append(String.format("%1$-65s %2$-65s \n", "Customer", "Total Appointments"));
            reportThreeText.append(String.join("", Collections.nCopies(110, "-")));
            reportThreeText.append("\n");
            while(results.next()) {
                reportThreeText.append(String.format("%1$s %2$65d \n", 
                    results.getString("customerName"), results.getInt("Total")));
            }
            statement.close();
            reportThree.setText(reportThreeText.toString());
        } catch (SQLException e) {
            System.out.println("SQLExcpetion: " + e.getMessage());
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        handleReportOne();
        handleReportTwo();
        handleReportThree();
    }    
    
}
