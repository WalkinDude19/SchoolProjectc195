package appControllers;

import appDatabase.DatabaseConnection;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.Locale;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import static appModels.Alerts.no_Appts_Alert;
import appModels.Appointment;
import appModels.LocalDateTime_Interface;
import appModels.User;


public class Main_Screen_Controller implements Initializable {

    
    @FXML private RadioButton rdo_weekly;
    @FXML private RadioButton rdo_monthly;
    @FXML private RadioButton rdo_all;
    @FXML private DatePicker date_selector;
    private ToggleGroup tg_Calendar;
    private boolean bool_Weekly;
    private boolean bool_Monthly;
    
    @FXML private TableView<Appointment> table_calendar;
    @FXML private TableColumn<Appointment, String> col_Appt_ID;
    @FXML private TableColumn<Appointment, String> col_Appt_Name;
    @FXML private TableColumn<Appointment, String> col_Appt_Title;
    @FXML private TableColumn<Appointment, String> col_Appt_Desc;
    @FXML private TableColumn<Appointment, String> col_Appt_Loc;
    @FXML private TableColumn<Appointment, String> col_Appt_Type;
    @FXML private TableColumn<Appointment, String> col_Appt_Date;
    @FXML private TableColumn<Appointment, String> col_Appt_Start;
    @FXML private TableColumn<Appointment, String> col_Appt_End;
    
    
    ObservableList<Appointment> appointment_list = FXCollections.observableArrayList();
    
    //required lambda used to convert UTC time pulled from DB to users LDT
    LocalDateTime_Interface convert = (String dateTime) -> { 
            DateTimeFormatter dt_format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
            LocalDateTime ldt =  LocalDateTime.parse(dateTime, dt_format).atZone(ZoneId.of("UTC")).withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime();
            return ldt;
    };
    
    
    @FXML 
    private void logout_Action (ActionEvent event) throws IOException, ClassNotFoundException, SQLException {
        User.set_Current_User_ID(null);
        User.set_Current_Username(null);
        DatabaseConnection.closeConnection();
        
        Parent parent = FXMLLoader.load(getClass().getResource("/appControllers/LoginScreen.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    
    @FXML 
    private void add_Cust_Action (ActionEvent event) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("/appControllers/AddCustomer.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    
    @FXML 
    private void mod_Cust_Action (ActionEvent event) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("/appControllers/ModifyCustomer.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    
    @FXML 
    private void add_Appt_Action (ActionEvent event) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("/appControllers/AddAppointment.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    
    @FXML 
    private void mod_Appt_Action (ActionEvent event) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("/appControllers/ModifyAppointment.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    
    @FXML 
    private void reports_Action (ActionEvent event) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("/appControllers/Reports.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    
    
    @FXML 
    private void date_Select_Action (ActionEvent event) throws IOException {
        if (bool_Weekly) {  
            view_Week();
        }
        else if (bool_Monthly) { 
            view_Month(); 
        }
        else { 
            view_All(); 
        }
    }
    
    
    
    public void view_Month() {
        bool_Weekly = false;
        bool_Monthly = true;
        
        LocalDate date = date_selector.getValue();
        String month = date.toString().substring(5,7);
        String year = date.toString().substring(0,4);
        
        
        Connection connection;
        try {
            appointment_list.clear();
            connection = DatabaseConnection.getConnection();
            ResultSet result_set = connection.createStatement().executeQuery(String.format("SELECT appointmentId, customerName, title, description, location, type, DATE(start) date, start, end " +
                                                                          "FROM customer c INNER JOIN appointment a ON c.customerId = a.customerId " +
                                                                          "WHERE MONTH(start) = '%s' AND YEAR(start) = '%s' ORDER BY start", month, year));
            while (result_set.next()) {
                LocalDateTime ldt_start = convert.to_Local_DT(result_set.getString("start"));
                LocalDateTime ldt_end = convert.to_Local_DT(result_set.getString("end"));
                String str_Start = ldt_start.toString().substring(11,16);
                String str_End = ldt_end.toString().substring(11,16);
                appointment_list.add(new Appointment(result_set.getString("appointmentId"), 
                                                        result_set.getString("customerName"), 
                                                        result_set.getString("title"), 
                                                        result_set.getString("description"),
                                                        result_set.getString("location"),   
                                                        result_set.getString("type"),
                                                        result_set.getString("date"),
                                                        str_Start,
                                                        str_End));
            }
            table_calendar.setItems(appointment_list);
            
            if (appointment_list.isEmpty()) no_Appts_Alert("month and year");
            
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
        }
    }
    
    
    
    public void view_Week() {
        bool_Weekly = true;
        bool_Monthly = false;
        
        LocalDate date = date_selector.getValue();
        String year = date_selector.getValue().toString().substring(0,4);
        WeekFields week = WeekFields.of(Locale.US);
        int int_Week = date.get(week.weekOfWeekBasedYear());
        String str_Week = Integer.toString(int_Week);
        
        
        Connection connection;
        try {
            appointment_list.clear();
            connection = DatabaseConnection.getConnection();
            ResultSet result_set = connection.createStatement().executeQuery(String.format("SELECT appointmentId, customerName, title, description, location, type, DATE(start) date, start, end " +
                                                                          "FROM customer c INNER JOIN appointment a ON c.customerId = a.customerId " +
                                                                          "WHERE WEEK(DATE(start))+1 = '%s' AND YEAR(start) = '%s' ORDER BY start", str_Week, year));
            while (result_set.next()) {
                LocalDateTime ldt_start = convert.to_Local_DT(result_set.getString("start"));
                LocalDateTime ldt_end = convert.to_Local_DT(result_set.getString("end"));
                String str_Start = ldt_start.toString().substring(11,16);
                String str_End = ldt_end.toString().substring(11,16);

                appointment_list.add(new Appointment(result_set.getString("appointmentId"), 
                                                        result_set.getString("customerName"), 
                                                        result_set.getString("title"), 
                                                        result_set.getString("description"),
                                                        result_set.getString("location"),   
                                                        result_set.getString("type"),
                                                        result_set.getString("date"),
                                                        str_Start,
                                                        str_End));
            }
            table_calendar.setItems(appointment_list);
            
            if (appointment_list.isEmpty()) no_Appts_Alert("week and year");
            
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
        }
    }
    
    
    
    public void view_All() {
        bool_Weekly = false;
        bool_Monthly = false;
        
        Connection connection;
        try {
            appointment_list.clear();
            connection = DatabaseConnection.getConnection();
            ResultSet result_set = connection.createStatement().executeQuery("SELECT appointmentId, customerName, title, description, location, type, DATE(start) date, start, end\n" +
                                                          "FROM customer c INNER JOIN appointment a ON c.customerId = a.customerId ORDER BY start;");
            while (result_set.next()) {
                LocalDateTime ldt_start = convert.to_Local_DT(result_set.getString("start"));
                LocalDateTime ldt_end = convert.to_Local_DT(result_set.getString("end"));
                String str_Start = ldt_start.toString().substring(11,16);
                String str_End = ldt_end.toString().substring(11,16);

                appointment_list.add(new Appointment(result_set.getString("appointmentId"), 
                                                        result_set.getString("customerName"), 
                                                        result_set.getString("title"), 
                                                        result_set.getString("description"),
                                                        result_set.getString("location"),   
                                                        result_set.getString("type"),
                                                        result_set.getString("date"),
                                                        str_Start,
                                                        str_End));
                
            }            
            table_calendar.setItems(appointment_list);
            
        } catch (SQLException ex) {
            Logger.getLogger(Main_Screen_Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    
    @FXML private void cal_Week_Action (ActionEvent event) throws IOException {
        view_Week(); 
    }
    
    @FXML private void cal_Mon_Action (ActionEvent event) throws IOException {
        view_Month(); 
    }
    
    @FXML private void cal_All_Action (ActionEvent event) throws IOException { 
        view_All(); 
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        tg_Calendar = new ToggleGroup();
        this.rdo_weekly.setToggleGroup(tg_Calendar);
        this.rdo_monthly.setToggleGroup(tg_Calendar);
        this.rdo_all.setToggleGroup(tg_Calendar);
        this.rdo_all.setSelected(true);
        date_selector.setValue(LocalDate.now());
        bool_Weekly = false;
        bool_Monthly = false;
        view_All();
     
        
        
        col_Appt_ID.setCellValueFactory(new PropertyValueFactory<>("_Appt_ID"));
        col_Appt_Name.setCellValueFactory(new PropertyValueFactory<>("_Appt_Name"));
        col_Appt_Title.setCellValueFactory(new PropertyValueFactory<>("_Appt_Title"));
        col_Appt_Desc.setCellValueFactory(new PropertyValueFactory<>("_Appt_Desc"));
        col_Appt_Loc.setCellValueFactory(new PropertyValueFactory<>("_Appt_Loc"));
        col_Appt_Type.setCellValueFactory(new PropertyValueFactory<>("_Appt_Type"));
        col_Appt_Date.setCellValueFactory(new PropertyValueFactory<>("_Appt_Date"));
        col_Appt_Start.setCellValueFactory(new PropertyValueFactory<>("_Appt_Start_Time"));
        col_Appt_End.setCellValueFactory(new PropertyValueFactory<>("_Appt_End_Time"));       
    }    
}
