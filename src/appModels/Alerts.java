package appModels;

import javafx.scene.control.Alert;
import javafx.stage.Modality;

public class Alerts {
 
    public static void empty_Field_Error(String s1, String s2, String s3) {
        Alert null_Alert = new Alert(Alert.AlertType.ERROR);
        null_Alert.initModality(Modality.NONE);
        null_Alert.setTitle("Error");
        null_Alert.setHeaderText("Missing Information");
        null_Alert.setContentText("All fields (except for " + s1 + ") MUST be filled in order to " + s2 + " " + s3 + ".");  
        null_Alert.showAndWait();
    }
    
    public static void missing_Field_Error(String s) {
        Alert null_alert = new Alert(Alert.AlertType.ERROR);
        null_alert.initModality(Modality.NONE);
        null_alert.setTitle("Error");
        null_alert.setHeaderText("Missing Information");
        null_alert.setContentText(s + " field is blank.");  
        null_alert.showAndWait();
    }
    
    public static void add_Data_Error(String message) {
        Alert add_alert = new Alert(Alert.AlertType.ERROR);
        add_alert.initModality(Modality.NONE);
        add_alert.setTitle("Error");
        add_alert.setHeaderText("Data Addition Error");
        add_alert.setContentText(message);  
        add_alert.showAndWait();
    }
    
    public static void exception_Error(String message) {
        Alert exception_Alert = new Alert(Alert.AlertType.ERROR);
        exception_Alert.initModality(Modality.NONE);
        exception_Alert.setTitle("Error");
        exception_Alert.setHeaderText("Error Has Occurred");
        exception_Alert.setContentText(message);  
        exception_Alert.showAndWait();
    }
    
    public static void customer_Add_Alert(String s) {
        Alert cust_Add_Alert = new Alert(Alert.AlertType.INFORMATION);
        cust_Add_Alert.initModality(Modality.NONE);
        cust_Add_Alert.setTitle("Customer Added");
        cust_Add_Alert.setHeaderText("Addition Confirmed");
        cust_Add_Alert.setContentText(s + " has been added to Customers.");  
        cust_Add_Alert.showAndWait();
    }
    
    public static void cusomer_Mod_Alert(String s) {
        Alert cust_Mod_Alert = new Alert(Alert.AlertType.INFORMATION);
        cust_Mod_Alert.initModality(Modality.NONE);
        cust_Mod_Alert.setTitle("Customer Information Saved");
        cust_Mod_Alert.setHeaderText("Change Confirmed");
        cust_Mod_Alert.setContentText(s + "'s information has been updated");  
        cust_Mod_Alert.showAndWait();
    }
    
 
    public static void customer_Delete_Alert(String s) {
        Alert cust_Del_Alert = new Alert(Alert.AlertType.INFORMATION);
        cust_Del_Alert.initModality(Modality.NONE);
        cust_Del_Alert.setTitle("Deletion Successful");
        cust_Del_Alert.setHeaderText("Deleted");
        cust_Del_Alert.setContentText(s + " has been deleted from the database.");  
        cust_Del_Alert.showAndWait();
    }
    
   public static void appointment_Add_Alert(String s) {
        Alert appt_Add_Alert = new Alert(Alert.AlertType.INFORMATION);
        appt_Add_Alert.initModality(Modality.NONE);
        appt_Add_Alert.setTitle("Appointment Added");
        appt_Add_Alert.setHeaderText("Addition Confirmed");
        appt_Add_Alert.setContentText("Appointment created for " + s);  
        appt_Add_Alert.showAndWait();
    }
   
   public static void appointment_Mod_Alert(String s1, String s2) {
        Alert appt_Mod_Alert = new Alert(Alert.AlertType.INFORMATION);
        appt_Mod_Alert.initModality(Modality.NONE);
        appt_Mod_Alert.setTitle("Appointment Information Saved");
        appt_Mod_Alert.setHeaderText("Change Confirmed");
        appt_Mod_Alert.setContentText("Changes made to " + s2 + "'s appointment[ID: "+s1+"] have been saved.");  
        appt_Mod_Alert.showAndWait();
    }
   
   public static void appointment_Del_Alert(String s) {
        Alert appt_Del_Alert = new Alert(Alert.AlertType.INFORMATION);
        appt_Del_Alert.initModality(Modality.NONE);
        appt_Del_Alert.setTitle("Deletion Successful");
        appt_Del_Alert.setHeaderText("Deletion Successful");
        appt_Del_Alert.setContentText("Appointment for " + s + " has been deleted from the database.");  
        appt_Del_Alert.showAndWait();
    }
   
   public static void no_Mod_Selection_Alert(String s) {
        Alert null_alert = new Alert(Alert.AlertType.ERROR);
        null_alert.initModality(Modality.NONE);
        null_alert.setTitle("ERROR");
        null_alert.setHeaderText("No Selection");
        null_alert.setContentText("Please select a(n) " + s + " to edit.");  
        null_alert.showAndWait();
    }
   
   public static void no_Del_Selection_Alert(String s) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initModality(Modality.NONE);
        alert.setTitle("ERROR");
        alert.setHeaderText("No Selection");
        alert.setContentText("Please select a(n) " + s + " to delete.");  
        alert.showAndWait();
    }
   
   public static void empty_Search_Alert() {
        Alert null_alert = new Alert(Alert.AlertType.ERROR);
        null_alert.initModality(Modality.NONE);
        null_alert.setTitle("ERROR");
        null_alert.setHeaderText("Nothing Searched");
        null_alert.setContentText("Search Field is Empty.");  
        null_alert.showAndWait();
    }
   
   public static void impending_Appt(String s) {
        Alert appt_Alert = new Alert(Alert.AlertType.INFORMATION);
        appt_Alert.initModality(Modality.NONE);
        appt_Alert.setTitle("Appointment Soon");
        appt_Alert.setHeaderText("Appointment Soon");
        appt_Alert.setContentText("You have an appointment with " + s + " In 15 Minutes or less");  
        appt_Alert.showAndWait();
   }
   
   public static void appt_Time_Alert(String message) {
        Alert time_Alert = new Alert(Alert.AlertType.ERROR);
        time_Alert.initModality(Modality.NONE);
        time_Alert.setTitle("Appointment Time Issue");
        time_Alert.setHeaderText("Appointment Time");
        time_Alert.setContentText(message);  
        time_Alert.showAndWait();
   }

   public static void no_Appts_Alert(String s) {
        Alert null_Alert = new Alert(Alert.AlertType.ERROR);
        null_Alert.initModality(Modality.NONE);
        null_Alert.setTitle("No results");
        null_Alert.setHeaderText("No results found");
        null_Alert.setContentText("There are no appointments for the " + s + " selected.");  
        null_Alert.showAndWait();
    }
}
