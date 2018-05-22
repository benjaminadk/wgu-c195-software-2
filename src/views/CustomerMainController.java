/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import models.Customer;
import models.CustomerDB;

/**
 * FXML Controller class
 *
 * @author Benjamin
 */
public class CustomerMainController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private AnchorPane customerMain;
    
    @FXML
    private TableView<Customer> customerTable;
    
    @FXML
    private TableColumn<Customer, Integer> customerId;
    
    @FXML
    private TableColumn<Customer, String> customerName;
    
    private Customer selectedCustomer;
    
    @FXML
    public void handleBackButton(ActionEvent event) {
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }
    
    @FXML
    public void handleAddButton() {
        Dialog<ButtonType> dialog = new Dialog();
        dialog.initOwner(customerMain.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("CustomerAdd.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch(IOException e) {
            System.out.println("CustomerAdd Error: " + e.getMessage());
        }
        ButtonType save = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(save);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        CustomerAddController controller = fxmlLoader.getController();
        dialog.showAndWait().ifPresent((response -> {
            if(response == save) {
                if(controller.handleAddCustomer()) {
                    customerTable.setItems(CustomerDB.getAllCustomers());
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Add Customer Error");
                    alert.setContentText(controller.displayErrors());
                    alert.showAndWait().ifPresent((response2 -> {
                        if(response2 == ButtonType.OK) {
                            handleAddButton();
                        }
                    }));
                }
            }
        }));
    }
    
    @FXML
    public void handleModifyButton() {
        if(customerTable.getSelectionModel().getSelectedItem() != null) {
            selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
        } else {
            return;
        }
        Dialog<ButtonType> dialog = new Dialog();
        dialog.initOwner(customerMain.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("CustomerModify.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch(IOException e) {
            System.out.println("CustomerModify Error: " + e.getMessage());
        }
        ButtonType save = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(save);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        CustomerModifyController controller = fxmlLoader.getController();
        controller.populateFields(selectedCustomer);
        dialog.showAndWait().ifPresent((response -> {
            if(response == save) {
                if(controller.handleModifyCustomer()) {
                    customerTable.setItems(CustomerDB.getAllCustomers());
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Modify Customer Error");
                    alert.setContentText(controller.displayErrors());
                    alert.showAndWait().ifPresent((response2 -> {
                        if(response2 == ButtonType.OK) {
                            handleModifyButton();
                        }
                    }));
                }
            }
        }));
    }
    
    @FXML
    public void handleDeleteButton() {
        if(customerTable.getSelectionModel().getSelectedItem() != null) {
            selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
        } else {
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete");
        alert.setHeaderText("Delete Customer Record");
        alert.setContentText("Delete Customer: " + selectedCustomer.getCustomerName() + " ?");
        alert.showAndWait().ifPresent((response -> {
            if(response == ButtonType.OK) {
                CustomerDB.deleteCustomer(selectedCustomer.getCustomerId());
                customerTable.setItems(CustomerDB.getAllCustomers());
            }
        }));
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        customerId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        customerName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        customerTable.setItems(CustomerDB.getAllCustomers());
    }    
    
}
