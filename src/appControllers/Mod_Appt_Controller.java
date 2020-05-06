package appControllers;

import appDatabase.DatabaseConnection;
import appDatabase.Query;
import static appDatabase.Query.bool_Self_Overlap;
import static appDatabase.Query.delete_Appointment;
import static appDatabase.Query.bool_Business_Hours;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import static appModels.Alerts.appointment_Del_Alert;
import static appModels.Alerts.appointment_Mod_Alert;
import static appModels.Alerts.appt_Time_Alert;
import static appModels.Alerts.empty_Field_Error;
import static appModels.Alerts.empty_Search_Alert;
import static appModels.Alerts.no_Del_Selection_Alert;
import static appModels.Alerts.no_Mod_Selection_Alert;
import appModels.Appointment;
import appModels.LocalDateTime_Interface;




public class Mod_Appt_Controller implements Initializable {


    
    @FXML private TextField txt_id;
    @FXML private TextField txt_name;
    @FXML private TextField txt_title;
    @FXML private TextField txt_desc;
    @FXML private TextField txt_contact;
    @FXML private ComboBox cbo_type;
    @FXML private TextField txt_url;
    @FXML private TextField txt_search;     
    @FXML private TextField txt_loc;  
    @FXML private ComboBox cbo_start;
    @FXML private ComboBox cbo_end; 
    @FXML private DatePicker date_select;
    @FXML private Button btn_search;
    @FXML private Button btn_edit;
    @FXML private Button btn_del;
    @FXML private Button btn_save;
    @FXML private Button btn_cancel;
    
    
    @FXML private TableView<Appointment> table_Appts;
    @FXML private TableColumn<Appointment, String> col_Appt_ID;
    @FXML private TableColumn<Appointment, String> col_Appt_Name;
    @FXML private TableColumn<Appointment, String> col_Appt_Title;
    @FXML private TableColumn<Appointment, String> col_Appt_Desc;
    @FXML private TableColumn<Appointment, String> col_Appt_Loc;
    @FXML private TableColumn<Appointment, String> col_Appt_Contact;
    @FXML private TableColumn<Appointment, String> col_Appt_Type;
    @FXML private TableColumn<Appointment, String> col_Appt_URL;
    @FXML private TableColumn<Appointment, String> col_Appt_Date;
    @FXML private TableColumn<Appointment, String> col_Appt_Start;
    @FXML private TableColumn<Appointment, String> col_Appt_End;
    
    
    ObservableList<Appointment> appt_List = FXCollections.observableArrayList();
    
   
    @FXML private void exit_Action (ActionEvent event) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("/appControllers/MainScreen.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    
 
    public Appointment lookupId(String searchId) {
            for (Appointment appt : appt_List) {
                if (appt.get_Appt_ID().equals(searchId)) {
                    return appt;
                }
            }
            return null;
        }
        
    @FXML private void search_Action (ActionEvent event) throws IOException {
        String searchString = txt_search.getText();
        if (!searchString.isEmpty()) {
            table_Appts.getSelectionModel().select(lookupId(searchString));
        }
        else {
            empty_Search_Alert();
        }
    }
    
    @FXML private void delete_Action (ActionEvent event) throws IOException {
        
        Appointment del_Appt = table_Appts.getSelectionModel().getSelectedItem();
        
        try {
            String str_ID = del_Appt.get_Appt_ID();
            String str_Name = del_Appt.get_Appt_Name();

     
            Alert del_alert = new Alert(Alert.AlertType.CONFIRMATION);
            del_alert.initModality(Modality.NONE);
            del_alert.setTitle("Delete Appointment?");
            del_alert.setHeaderText("Confirm");
            del_alert.setContentText("Confirm deletion of appointment" + str_ID + " for " + str_Name);
            Optional<ButtonType> result = del_alert.showAndWait();
                if (result.get() == ButtonType.OK) { 
                    delete_Appointment(str_ID);

                    Parent parent = FXMLLoader.load(getClass().getResource("/appControllers/ModifyAppointment.fxml"));
                    Scene scene = new Scene(parent);
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    stage.setScene(scene);
                    stage.show();
                    
                    appointment_Del_Alert(str_Name);
                } 
                else {}

        } catch (IOException e) {
            String string = "appointment";
            no_Del_Selection_Alert(string);
        }
    }
    
    @FXML private void edit_Action (ActionEvent event) throws IOException {
        
        Appointment appointment = (Appointment) table_Appts.getSelectionModel().getSelectedItem();
        
        try {
            
            txt_id.setText(appointment.get_Appt_ID());
            txt_name.setText(appointment.get_Appt_Name());
            txt_title.setText(appointment.get_Appt_Title());
            txt_desc.setText(appointment.get_Appt_Desc());
            txt_loc.setText(appointment.get_Appt_Loc());
            txt_contact.setText(appointment.get_Appt_Contact());
            cbo_type.setValue(appointment.get_Appt_Type());
            txt_url.setText(appointment.get_Appt_URL());
            cbo_start.setValue(appointment.get_Appt_Start_Time() + ":00");
            cbo_end.setValue(appointment.get_Appt_End_Time() + ":00");
            
            String dateString = appointment.get_Appt_Date();
            LocalDate ldt = LocalDate.parse(dateString);
            date_select.setValue(ldt);

            
            txt_title.setDisable(false);
            txt_desc.setDisable(false);
            txt_contact.setDisable(false);
            txt_url.setDisable(false);
            cbo_type.setDisable(false);
            txt_loc.setDisable(false);
            cbo_start.setDisable(false);
            date_select.setDisable(false);
            cbo_end.setDisable(false);
            btn_save.setDisable(false);
            btn_cancel.setDisable(false);
            
            btn_edit.setDisable(true);
            btn_del.setDisable(true);
            txt_search.setDisable(true);
            btn_search.setDisable(true);
        }
        catch (Exception e) {
            String string = "appointment";
            no_Mod_Selection_Alert(string);
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    @FXML private void cancel_Action (ActionEvent event) throws IOException {
        
        txt_id.clear();
        txt_name.clear();
        txt_title.clear();
        txt_desc.clear();
        txt_contact.clear();
        txt_url.clear();
        cbo_type.setItems(Query.get_Types());
        txt_loc.clear();
        date_select.setValue(null);
        cbo_start.setItems(Query.get_Times());
        cbo_end.setItems(Query.get_Times());

        txt_id.setDisable(true);
        txt_name.setDisable(true);
        txt_title.setDisable(true);
        txt_desc.setDisable(true);
        txt_contact.setDisable(true);
        txt_url.setDisable(true);
        cbo_type.setDisable(true);
        txt_loc.setDisable(true);
        date_select.setDisable(true);
        cbo_start.setDisable(true);
        cbo_end.setDisable(true);
        btn_save.setDisable(true);
        btn_cancel.setDisable(true);
        
        table_Appts.setDisable(false);
        btn_edit.setDisable(false);
        btn_del.setDisable(false);
        txt_search.setDisable(false);
        btn_search.setDisable(false);
    }
    
    
    @FXML private void save_Action (ActionEvent event) throws IOException {
        
        try {
            String apptId = txt_id.getText();
            String custName = txt_name.getText();
            String editTitle = txt_title.getText();
            String editDescription = txt_desc.getText();
            String editContact = txt_contact.getText();
            String editUrl = txt_url.getText();
            String editType = cbo_type.getValue().toString();
            String editLocation = txt_loc.getText();
            String editDate = date_select.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            int int_Start_Time = cbo_start.getSelectionModel().getSelectedIndex();
            int int_End_Time = cbo_end.getSelectionModel().getSelectedIndex();
            String str_Start_Time = cbo_start.getValue().toString();
            String str_End_Time = cbo_end.getValue().toString();
            

            if (editTitle.isEmpty() || editDescription.isEmpty() || editLocation.isEmpty() || editType.isEmpty() || editContact.isEmpty() || editDate.isEmpty() || str_Start_Time.isEmpty() || str_End_Time.isEmpty()) {
                empty_Field_Error("URL", "add", "an appointment"); 
                
            }
            
            else {
                
                if (int_End_Time == int_Start_Time) {          
                    appt_Time_Alert("Appointment Start time must be before it's End time");
                }
                
                else if (int_End_Time < int_Start_Time){
                    appt_Time_Alert("Appointment Start time must be before it's End time");
                }
                
                else {
                                 
                    if (bool_Self_Overlap(str_Start_Time, str_End_Time, editDate, custName, apptId) && bool_Business_Hours(str_Start_Time, str_End_Time, editDate)) {
                        Query.edit_Appointment(apptId, custName, editTitle, editDescription, editContact, editUrl, editType, editLocation, editDate, str_Start_Time, str_End_Time);

                        Parent parent = FXMLLoader.load(getClass().getResource("/appControllers/ModifyAppointment.fxml"));
                        Scene scene = new Scene(parent);
                        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        stage.setScene(scene);
                        stage.show();

                        appointment_Mod_Alert(apptId, custName);

                        txt_name.setDisable(true);
                        txt_title.setDisable(true);
                        txt_desc.setDisable(true);
                        txt_contact.setDisable(true);
                        txt_url.setDisable(true);
                        cbo_type.setDisable(true);
                        txt_loc.setDisable(true);
                        date_select.setDisable(true);
                        cbo_start.setDisable(true);
                        cbo_end.setDisable(true);
                        btn_save.setDisable(true);
                        btn_cancel.setDisable(true);
                    }
                    else {
                        if (!bool_Self_Overlap(str_Start_Time, str_End_Time, editDate, custName, apptId)) { appt_Time_Alert("Unable to schedule appointment due to overlap with an existing appiontment."); }
                        if (!bool_Business_Hours(str_Start_Time, str_End_Time, editDate)) { appt_Time_Alert("Appointments can not be scheduled outside of office hours.\n"
                                    + "Office hours are 8:00 - 17:00 UTC time"); }
                    }
                }
            }            
        } catch (IOException e) {
            empty_Field_Error("URL", "modify", "an appointment");
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
          //required lambda used to conver UTC time pulled from DB to users LDT
        LocalDateTime_Interface convert = (String dateTime) -> { 
            DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
            LocalDateTime ldt =  LocalDateTime.parse(dateTime, format).atZone(ZoneId.of("UTC")).withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime();
            return ldt;
        };
        
        
        txt_id.setDisable(true); 
        txt_name.setDisable(true); 
        txt_title.setDisable(true);
        txt_desc.setDisable(true);
        txt_contact.setDisable(true);
        cbo_type.setDisable(true);
        cbo_type.setItems(Query.get_Types());
        txt_url.setDisable(true);
        txt_loc.setDisable(true);
        btn_save.setDisable(true);
        btn_cancel.setDisable(true);
        date_select.setDisable(true);
        date_select.getEditor().setEditable(false);
        cbo_start.setDisable(true);
        cbo_start.setItems(Query.get_Times());
        cbo_end.setDisable(true);
        cbo_end.setItems(Query.get_Times());
        btn_save.setDisable(true);
        btn_cancel.setDisable(true);
        
        //required lambda that disables dates prior to today as well as weekends from being selected
        date_select.setDayCellFactory(picker -> new DateCell() { 
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();
                setDisable(empty || date.compareTo(today) < 0);
                if(date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY)
                    setDisable(true);
            }
        });
        
        
        
        Connection connection;
        try {
            connection = DatabaseConnection.getConnection();
            ResultSet result_set = connection.createStatement().executeQuery("SELECT appointmentId, customerName, title, description, location, contact, type, url, DATE(start) date, start, end\n" +
                                                          "FROM customer c INNER JOIN appointment a ON c.customerId = a.customerId ORDER BY start;");
           
            while (result_set.next()) {
                LocalDateTime zonedStart = convert.to_Local_DT(result_set.getString("start"));
                LocalDateTime zonedEnd = convert.to_Local_DT(result_set.getString("end"));
                String zonedStartString = zonedStart.toString().substring(11,16);
                String zonedEndString = zonedEnd.toString().substring(11,16);
                
                appt_List.add(new Appointment(result_set.getString("appointmentId"), 
                                                     result_set.getString("customerName"), 
                                                     result_set.getString("title"), 
                                                     result_set.getString("description"),
                                                     result_set.getString("location"), 
                                                     result_set.getString("contact"), 
                                                     result_set.getString("type"), 
                                                     result_set.getString("url"),
                                                     result_set.getString("date"),
                                                     zonedStartString,
                                                     zonedEndString));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Mod_Appt_Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
        
            col_Appt_ID.setCellValueFactory(new PropertyValueFactory<>("_Appt_ID"));
            col_Appt_Name.setCellValueFactory(new PropertyValueFactory<>("_Appt_Name"));
            col_Appt_Title.setCellValueFactory(new PropertyValueFactory<>("_Appt_Title"));
            col_Appt_Desc.setCellValueFactory(new PropertyValueFactory<>("_Appt_Desc"));
            col_Appt_Loc.setCellValueFactory(new PropertyValueFactory<>("_Appt_Loc"));
            col_Appt_Contact.setCellValueFactory(new PropertyValueFactory<>("_Appt_Contact"));
            col_Appt_Type.setCellValueFactory(new PropertyValueFactory<>("_Appt_Type"));
            col_Appt_URL.setCellValueFactory(new PropertyValueFactory<>("_Appt_URL"));
            col_Appt_Date.setCellValueFactory(new PropertyValueFactory<>("_Appt_Date"));
            col_Appt_Start.setCellValueFactory(new PropertyValueFactory<>("_Appt_Start_Time"));
            col_Appt_End.setCellValueFactory(new PropertyValueFactory<>("_Appt_End_Time"));
            
            table_Appts.setItems(appt_List);          
    }      
}
