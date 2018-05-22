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
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import models.Appointment;
import models.AppointmentDB;
import models.Customer;
import models.CustomerDB;

/**
 * FXML Controller class
 *
 * @author Benjamin
 */
public class AppointmentMainController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private AnchorPane appointmentMain;
    
    @FXML
    private TableView<Customer> customerTable;
    
    @FXML 
    private TableColumn<Customer, Integer> customerId;
    
    @FXML
    private TableColumn<Customer, String> customerName;
    
    @FXML
    private Label monthCustomerLabel;
    
    @FXML
    private TableView<Appointment> monthAptTable;
    
    @FXML
    private TableColumn<Appointment, String> monthDescription;
    
    @FXML
    private TableColumn<Appointment, String> monthContact;
    
    @FXML
    private TableColumn<Appointment, String> monthLocation;
    
    @FXML
    private TableColumn<Appointment, String> monthStart;
    
    @FXML
    private TableColumn<Appointment, String> monthEnd;
    
    @FXML
    private Label weekCustomerLabel;
    
    @FXML
    private TableView<Appointment> weekAptTable;
    
    @FXML
    private TableColumn<Appointment, String> weekDescription;
    
    @FXML
    private TableColumn<Appointment, String> weekContact;
    
    @FXML
    private TableColumn<Appointment, String> weekLocation;
    
    @FXML
    private TableColumn<Appointment, String> weekStart;
    
    @FXML
    private TableColumn<Appointment, String> weekEnd;
    
    @FXML 
    private Tab monthly;
    
    private Customer selectedCustomer;
    
    private Appointment selectedAppointment;
    
    private boolean isMonthly;
    
    @FXML
    public void handleCustomerClick(MouseEvent event) {
        selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
        int id = selectedCustomer.getCustomerId();
        monthCustomerLabel.setText(selectedCustomer.getCustomerName());
        weekCustomerLabel.setText(selectedCustomer.getCustomerName());
        monthAptTable.setItems(AppointmentDB.getMonthlyAppointments(id));
        weekAptTable.setItems(AppointmentDB.getWeeklyAppoinments(id));
    }
    
    @FXML 
    public void handleAddButton() {
        if(customerTable.getSelectionModel().getSelectedItem() != null) {
            selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
        } else {
            return;
        }
        Dialog<ButtonType> dialog = new Dialog();
        dialog.initOwner(appointmentMain.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("AppointmentAdd.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch(IOException e) {
            System.out.println("AppointmentAdd Error: " + e.getMessage());
        }
        ButtonType save = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(save);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        AppointmentAddController controller = fxmlLoader.getController();
        controller.populateCustomerName(selectedCustomer.getCustomerName());
        dialog.showAndWait().ifPresent((response -> {
            if(response == save) {
                if(controller.handleAddAppointment(selectedCustomer.getCustomerId())) {
                    monthAptTable.setItems(AppointmentDB.getMonthlyAppointments(selectedCustomer.getCustomerId()));
                    weekAptTable.setItems(AppointmentDB.getWeeklyAppoinments(selectedCustomer.getCustomerId()));
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Add Appointment Error");
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
        if(monthly.isSelected()) {
            if(monthAptTable.getSelectionModel().getSelectedItem() != null) {
                selectedAppointment = monthAptTable.getSelectionModel().getSelectedItem();
            } else {
                return;
            }
        } else {
            if(weekAptTable.getSelectionModel().getSelectedItem() != null) {
                selectedAppointment = weekAptTable.getSelectionModel().getSelectedItem();
            } else {
                return;
            }
        }
        Dialog<ButtonType> dialog = new Dialog();
        dialog.initOwner(appointmentMain.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("AppointmentModify.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch(IOException e) {
            System.out.println("AppointmentModify Error: " + e.getMessage());
        }
        ButtonType save = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(save);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        AppointmentModifyController controller = fxmlLoader.getController();
        controller.populateFields(selectedCustomer.getCustomerName(), selectedAppointment);
        dialog.showAndWait().ifPresent((response -> {
            if(response == save) {
                if(controller.handleModifyAppointment(selectedAppointment.getAptId())) {
                    monthAptTable.setItems(AppointmentDB.getMonthlyAppointments(selectedCustomer.getCustomerId()));
                    weekAptTable.setItems(AppointmentDB.getWeeklyAppoinments(selectedCustomer.getCustomerId()));
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Modify Appointment Error");
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
    public void handleBackButton(ActionEvent event) {
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }
    
    @FXML
    public void handleDeleteButton() {
        if(monthly.isSelected()) {
            isMonthly = true;
            if(monthAptTable.getSelectionModel().getSelectedItem() != null) {
                selectedAppointment = monthAptTable.getSelectionModel().getSelectedItem();
            } else {
                return;
            }
        } else {
            isMonthly = false;
            if(weekAptTable.getSelectionModel().getSelectedItem() != null) {
                selectedAppointment = weekAptTable.getSelectionModel().getSelectedItem();
            } else {
                return;
            }
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete");
        alert.setHeaderText("Delete Appointment Record");
        alert.setContentText("Delete Appointment?");
        alert.showAndWait().ifPresent((response -> {
            if(response == ButtonType.OK) {
                AppointmentDB.deleteAppointment(selectedAppointment.getAptId());
                if(isMonthly) {
                   monthAptTable.setItems(AppointmentDB.getMonthlyAppointments(selectedCustomer.getCustomerId())); 
                } else {
                    weekAptTable.setItems(AppointmentDB.getWeeklyAppoinments(selectedCustomer.getCustomerId()));
                }
            }
        }));
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        customerId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        customerName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        customerTable.setItems(CustomerDB.getAllCustomers());
        monthDescription.setCellValueFactory(cellData -> {
            return cellData.getValue().getAptDescriptionProperty();
        });
        monthContact.setCellValueFactory(cellData -> {
            return cellData.getValue().getAptContactProperty();
        });
        monthLocation.setCellValueFactory(cellData -> {
            return cellData.getValue().getAptLocationProperty();
        });
        monthStart.setCellValueFactory(cellData -> {
            return cellData.getValue().getAptStartProperty();
        });
        monthEnd.setCellValueFactory(cellData -> {
            return cellData.getValue().getAptEndProperty();
        });
        weekDescription.setCellValueFactory(cellData -> {
            return cellData.getValue().getAptDescriptionProperty();
        });
        weekContact.setCellValueFactory(cellData -> {
            return cellData.getValue().getAptContactProperty();
        });
        weekLocation.setCellValueFactory(cellData -> {
            return cellData.getValue().getAptLocationProperty();
        });
        weekStart.setCellValueFactory(cellData -> {
            return cellData.getValue().getAptStartProperty();
        });
        weekEnd.setCellValueFactory(cellData -> {
            return cellData.getValue().getAptEndProperty();
        });
    }    
    
}
