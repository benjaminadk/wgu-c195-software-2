/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Benjamin
 */
public class Appointment {
    private final SimpleIntegerProperty aptId = new SimpleIntegerProperty();
    private final SimpleIntegerProperty aptCustId = new SimpleIntegerProperty();
    private final SimpleStringProperty aptStart = new SimpleStringProperty();
    private final SimpleStringProperty aptEnd = new SimpleStringProperty();
    private final SimpleStringProperty aptTitle = new SimpleStringProperty();
    private final SimpleStringProperty aptDescription = new SimpleStringProperty();
    private final SimpleStringProperty aptLocation = new SimpleStringProperty();
    private final SimpleStringProperty aptContact = new SimpleStringProperty();
    
    public Appointment() {}
    
    public Appointment(int id, int custId, String start, String end, String title, String description, String location, String contact) {
        setAptId(id);
        setAptCustId(custId);
        setAptStart(start);
        setAptEnd(end);
        setAptTitle(title);
        setAptDescription(description);
        setAptLocation(location);
        setAptContact(contact);
    }
    
    public int getAptId() {
        return aptId.get();
    }
    
    public int getAptCustId() {
        return aptCustId.get();
    }
    
    public String getAptEnd() {
        return aptEnd.get();
    }
    
    public String getAptStart() {
        return aptStart.get();
    }
    
    public String getAptTitle() {
        return aptTitle.get();
    }
    
    public String getAptDescription() {
        return aptDescription.get();
    }
    
    public String getAptLocation() {
        return aptLocation.get();
    }
    
    public String getAptContact() {
        return aptContact.get();
    }
    
    public void setAptId(int aptId) {
        this.aptId.set(aptId);
    }
    
    public void setAptCustId(int aptCustId) {
        this.aptCustId.set(aptCustId);
    }
    
    public void setAptEnd(String aptEnd) {
        this.aptEnd.set(aptEnd);
    }
    
    public void setAptStart(String aptTimeStart) {
        this.aptStart.set(aptTimeStart);
    } 
    
    public void setAptTitle(String aptTitle) {
        this.aptTitle.set(aptTitle);
    }
    
    public void setAptDescription(String aptDescription) {
        this.aptDescription.set(aptDescription);
    }
    
    public void setAptLocation(String aptLocation) {
        this.aptLocation.set(aptLocation);
    }
    
    public void setAptContact(String aptContact) {
        this.aptContact.set(aptContact);
    }
    
    public StringProperty getAptEndProperty() {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S"); 
 	LocalDateTime ldt = LocalDateTime.parse(this.aptEnd.getValue(), df);
        ZonedDateTime zdt = ldt.atZone(ZoneId.of("UTC"));
        ZoneId zid = ZoneId.systemDefault();
        ZonedDateTime utcDate = zdt.withZoneSameInstant(zid); 
        StringProperty date = new SimpleStringProperty(utcDate.toLocalDateTime().toString());
        return date;
    }
    
    public StringProperty getAptStartProperty() {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S"); 
 	LocalDateTime ldt = LocalDateTime.parse(this.aptStart.getValue(), df);
        ZonedDateTime zdt = ldt.atZone(ZoneId.of("UTC"));
        ZoneId zid = ZoneId.systemDefault();
        ZonedDateTime utcDate = zdt.withZoneSameInstant(zid); 
        StringProperty date = new SimpleStringProperty(utcDate.toLocalDateTime().toString());
        return date;
    }
    
    public StringProperty getAptTitleProperty() {
        return this.aptTitle;
    }
    
    public StringProperty getAptDescriptionProperty() {
        return this.aptDescription;
    }
    
    public StringProperty getAptLocationProperty() {
        return this.aptLocation;
    }
    
    public StringProperty getAptContactProperty() {
        return this.aptContact;
    }
    
    public LocalDate getDateOnly() {
        Timestamp ts = Timestamp.valueOf(this.aptStart.get());
        ZonedDateTime zdt;
        ZoneId zid;
        LocalDate ld;
        if(this.aptLocation.get().equals("New York")) {
            zid = ZoneId.of("America/New_York");
        } else if(this.aptLocation.get().equals("Phoenix")) {
            zid = ZoneId.of("America/Phoenix");
        } else {
            zid = ZoneId.of("Europe/London");
        }
        zdt = ts.toLocalDateTime().atZone(zid);
        ld = zdt.toLocalDate();
        return ld;
    }
    
    public String getTimeOnly() {
        Timestamp ts = Timestamp.valueOf(this.aptStart.get());
        ZonedDateTime zdt;
        ZoneId zid;
        LocalTime lt;
        if(this.aptLocation.get().equals("New York")) {
            zid = ZoneId.of("America/New_York");
            zdt = ts.toLocalDateTime().atZone(zid);
            lt = zdt.toLocalTime().minusHours(4);
        } else if(this.aptLocation.get().equals("Phoenix")) {
            zid = ZoneId.of("America/Phoenix");
            zdt = ts.toLocalDateTime().atZone(zid);
            lt = zdt.toLocalTime().minusHours(7);
        } else {
            zid = ZoneId.of("Europe/London");
            zdt = ts.toLocalDateTime().atZone(zid);
            lt = zdt.toLocalTime().plusHours(1);
        }
        int rawH = Integer.parseInt(lt.toString().split(":")[0]);
        if(rawH > 12) {
            rawH -= 12;
        }
        String ampm;
        if(rawH < 9 || rawH == 12) {
            ampm = "PM";
        } else {
            ampm = "AM";
        }
        String time = rawH + ":00 " + ampm;
        return time;
    }
    
    public String get15Time() {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S"); 
 	LocalDateTime ldt = LocalDateTime.parse(this.aptStart.getValue(), df);
        ZonedDateTime zdt = ldt.atZone(ZoneId.of("UTC"));
        ZoneId zid = ZoneId.systemDefault();
        ZonedDateTime utcDate = zdt.withZoneSameInstant(zid); 
        DateTimeFormatter tFormatter = DateTimeFormatter.ofPattern("kk:mm"); 
	LocalTime localTime = LocalTime.parse(utcDate.toString().substring(11,16), tFormatter);
        return localTime.toString();
    }
}
