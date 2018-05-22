/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import models.Appointment;
import models.AppointmentDB;

/**
 * FXML Controller class
 *
 * @author Benjamin
 */
public class AppointmentModifyController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private TextField customerName;
    
    @FXML
    private ComboBox contact;
    
    @FXML
    private TextField location;
    
    @FXML 
    private DatePicker date;
    
    @FXML
    private ComboBox time;
    
    @FXML
    private ComboBox type;
    
    private final ObservableList<String> contacts = FXCollections.observableArrayList("ben", "moses", "anne");
    
    private final ObservableList<String> times = FXCollections.observableArrayList("9:00 AM","10:00 AM","11:00 AM","12:00 PM","1:00 PM","2:00 PM","3:00 PM","4:00 PM");
    
    private final ObservableList<String> types = FXCollections.observableArrayList(
    "Introduction:First Meeting","Consultation:Retirement Plan","Consultation:Stock Market","Consultation:Tax Preparation");
    
    private ObservableList<String> errors = FXCollections.observableArrayList();
    
    public boolean handleModifyAppointment(int id) {
        errors.clear();
        int aptContact = contact.getSelectionModel().getSelectedIndex();
        int aptType = type.getSelectionModel().getSelectedIndex();
        int aptTime = time.getSelectionModel().getSelectedIndex();
        LocalDate ld = date.getValue();
        if(!validateContact(aptContact)||!validateType(aptType)||!validateTime(aptTime)||!validateDate(ld)) {
            return false;
        }
        if(AppointmentDB.overlappingAppointment(id, location.getText(), ld.toString(), times.get(aptTime))) {
            errors.add("Overlapping Appointments");
            return false;
        }
        if(AppointmentDB.updateAppointment(id, types.get(aptType), contacts.get(aptContact), location.getText(), ld.toString(), times.get(aptTime))) {
            return true;
        } else {
            errors.add("Database Error");
            return false;
        }
    }
    
    public void populateFields(String name, Appointment apt) {
        String t = apt.getAptTitle() + ":" + apt.getAptDescription();
        customerName.setText(name);
        contact.setValue(apt.getAptContact());
        location.setText(apt.getAptLocation());
        type.setValue(t);
        time.setValue(apt.getTimeOnly());
        date.setValue(apt.getDateOnly());
    }
    
    public boolean validateContact(int aptContact) {
        if(aptContact == -1) {
            errors.add("A Contact must be selected");
            return false;
        } else {
            return true;
        }
    }
    
    public boolean validateType(int aptType) {
        if(aptType == -1) {
            errors.add("An Appointment Type must be selected");
            return false;
        } else {
            return true;
        }
    }
    
    public boolean validateTime(int aptTime) {
        if(aptTime == -1) {
            errors.add("An Appointment Time must be selected");
            return false;
        } else {
            return true;
        }
    }
    
    public boolean validateDate(LocalDate aptDate) {
        if(aptDate == null) {
            errors.add("An Appointment Date must be selected");
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
    
    @FXML
    public void handleLocation() {
        String c = contact.getSelectionModel().getSelectedItem().toString();
        if(c.equals("ben")) {
            location.setText("New York");
        } else if(c.equals("anne")) {
            location.setText("London");
        } else if(c.equals("moses")) {
            location.setText("Phoenix");
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        contact.setItems(contacts);
        time.setItems(times);
        type.setItems(types);
        date.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(
                    empty || 
                    date.getDayOfWeek() == DayOfWeek.SATURDAY || 
                    date.getDayOfWeek() == DayOfWeek.SUNDAY ||
                    date.isBefore(LocalDate.now()));
                if(date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY || date.isBefore(LocalDate.now())) {
                    setStyle("-fx-background-color: #ffc4c4;");
                }
            }
        });
    }    
    
}
