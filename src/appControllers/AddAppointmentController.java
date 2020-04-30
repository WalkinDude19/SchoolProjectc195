package appControllers;

import appDatabase.DatabaseConnection;
import appDatabase.Query;
import static appDatabase.Query.bool_Overlap;
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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import static appModels.Alerts.appointment_Add_Alert;
import static appModels.Alerts.appt_Time_Alert;
import static appModels.Alerts.empty_Field_Error;
import static appModels.Alerts.empty_Search_Alert;
import static appModels.Alerts.no_Mod_Selection_Alert;
import appModels.Appointment;
import appModels.Customer;
import appModels.LocalDateTime_Interface;


public class AddAppointmentController implements Initializable {
    
    
    
    @FXML private TextField txt_ID;
    @FXML private TextField txt_Name;
    @FXML private TextField txt_Title;
    @FXML private TextField txt_desc;
    @FXML private TextField txt_loc;
    @FXML private TextField txt_contact;
    @FXML private ComboBox cbo_type;
    @FXML private TextField txt_url;
    @FXML private TextField txt_search;       
    @FXML private ComboBox cbo_start;
    @FXML private ComboBox cbo_end; 
    @FXML private DatePicker date_appt;
    @FXML private Button btn_search;
    @FXML private Button btn_select;
    @FXML private Button btn_cancel;
    @FXML private Button btn_add;
    @FXML private Button btn_exit;
    
    
    @FXML private TableView<Customer> table_Customers;
    @FXML private TableColumn<Customer, Integer> col_Cust_ID;
    @FXML private TableColumn<Customer, String> col_Cust_Name;
    
    
    ObservableList<Customer> cust_Table_List = FXCollections.observableArrayList();
    
    
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
    
    
    ObservableList<Appointment> appt_Table_List = FXCollections.observableArrayList();
    
    
    
    
    
    @FXML 
    private void exit_Action (ActionEvent event) throws IOException {
        System.out.println("Add Appointment -> Main Menu");
        Parent parent = FXMLLoader.load(getClass().getResource("/appControllers/MainScreen.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    

    public Customer customer_Lookup(String searchName) {
            for (Customer customer : cust_Table_List) {
                if (customer.get_Cust_Name().equalsIgnoreCase(searchName)) {
                    return customer;
                }
            }
            return null;
        }
    
        
    @FXML 
    private void search_Action (ActionEvent event) throws IOException {
        String str_search = txt_search.getText();
        if (!str_search.isEmpty()) {
        table_Customers.getSelectionModel().select(customer_Lookup(str_search));
        }
        else {
            empty_Search_Alert();
        }
    }
    
    @FXML 
    private void select_Action (ActionEvent event) throws IOException {
        
        Customer add_Customer_Appt = table_Customers.getSelectionModel().getSelectedItem();
        
        try {            
            txt_Title.setDisable(false);
            txt_desc.setDisable(false);
            txt_contact.setDisable(false);
            txt_url.setDisable(false);
            cbo_type.setDisable(false);
            txt_loc.setDisable(false);
            cbo_start.setDisable(false);
            date_appt.setDisable(false);
            cbo_end.setDisable(false);
            btn_cancel.setDisable(false);
            btn_add.setDisable(false);
            
            txt_ID.setText(add_Customer_Appt.get_Cust_ID());
            txt_Name.setText(add_Customer_Appt.get_Cust_Name());

            
            table_Customers.setDisable(true);
            btn_select.setDisable(true);
            txt_search.setDisable(true);
            btn_search.setDisable(true);
        }
        catch (Exception e) {
            String string = "an appointment";
            no_Mod_Selection_Alert(string);
            
            txt_ID.setDisable(true); 
            txt_Name.setDisable(true); 
            txt_Title.setDisable(true);
            txt_desc.setDisable(true);
            txt_contact.setDisable(true);
            txt_url.setDisable(true);
            txt_loc.setDisable(true);
            cbo_type.setDisable(true);
            date_appt.setDisable(true);
            cbo_start.setDisable(true);
            cbo_end.setDisable(true);
            btn_cancel.setDisable(true);
            btn_add.setDisable(true);
        }
    }
    
    @FXML private void cancel_Action (ActionEvent event) throws IOException {
        txt_ID.clear();
        txt_Name.clear();
        txt_Title.clear();
        txt_desc.clear();
        txt_loc.clear();
        txt_contact.clear();
        txt_url.clear();
        date_appt.setValue(null);
        cbo_type.getSelectionModel().clearSelection();
        cbo_start.getSelectionModel().clearSelection();
        cbo_end.getSelectionModel().clearSelection();
        

        txt_ID.setDisable(true);
        txt_Name.setDisable(true);
        txt_Name.setText("Select a customer first");
        txt_Title.setDisable(true);
        txt_desc.setDisable(true);
        txt_contact.setDisable(true);
        cbo_type.setDisable(true);
        txt_url.setDisable(true);
        txt_loc.setDisable(true);
        date_appt.setDisable(true);
        cbo_start.setDisable(true);
        cbo_end.setDisable(true);
        btn_cancel.setDisable(true);
        btn_add.setDisable(true);
        
        table_Customers.setDisable(false);
        btn_select.setDisable(false);
        txt_search.setDisable(false);
        btn_search.setDisable(false);

    }
    
    @FXML private void add_Action (ActionEvent event) throws IOException, NullPointerException {
        
        try {
            String add_ID = txt_ID.getText();
            String add_Name = txt_Name.getText();
            String add_Title = txt_Title.getText();
            String add_Desc = txt_desc.getText();
            String add_Loc = txt_loc.getText();
            String add_Type = cbo_type.getValue().toString();
            String add_Contact = txt_contact.getText();
            String add_URL = txt_url.getText();
            String add_Date = date_appt.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            int check_Start_Time = cbo_start.getSelectionModel().getSelectedIndex();
            int check_End_Time = cbo_end.getSelectionModel().getSelectedIndex();
            String add_Start_Time = (String) cbo_start.getValue().toString();
            String add_End_Time = (String) cbo_end.getValue().toString();
            

            try {
                if (add_Title.isEmpty() || add_Desc.isEmpty() || add_Loc.isEmpty() || add_Type.isEmpty() || add_Contact.isEmpty() || add_Date.isEmpty() || add_Start_Time.isEmpty() || add_End_Time.isEmpty()) {
                    empty_Field_Error("URL", "add", "an appointment");              
                }
                else {

                    if (check_End_Time == check_Start_Time) {          
                    appt_Time_Alert("Appointment start and end times cannot be the same.");
                    }

                    else if (check_End_Time < check_Start_Time){
                    appt_Time_Alert("Appointment start time must be earlier than the end time.");
                    }
                    
                    else {                     
                
                        if (bool_Overlap(add_Start_Time, add_End_Time, add_Date) && bool_Business_Hours(add_Start_Time, add_End_Time, add_Date)) {
                            try {
                                Query.add_Appointment(add_ID, add_Name, add_Title, add_Desc, add_Loc, add_Contact, add_Type, add_URL, add_Date, add_Start_Time, add_End_Time);
                            
                                System.out.println("Addition Successful, please refresh the page");
                                Parent parent = FXMLLoader.load(getClass().getResource("/appControllers/AddAppointment.fxml"));
                                Scene scene = new Scene(parent);
                                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                                stage.setScene(scene);
                                stage.show();

                                appointment_Add_Alert(add_Name);

                                table_Customers.setDisable(false);
                                btn_search.setDisable(false);
                                txt_search.setDisable(false);
                                btn_select.setDisable(false);

                                txt_Title.setDisable(true);
                                txt_desc.setDisable(true);
                                txt_contact.setDisable(true);
                                txt_url.setDisable(true);
                                txt_loc.setDisable(true);
                                cbo_type.setDisable(true);
                                date_appt.setDisable(true);
                                cbo_start.setDisable(true);
                                cbo_end.setDisable(true);
                                btn_cancel.setDisable(true);
                                btn_add.setDisable(true); 
                                
                            } catch (SQLException e) {
                                System.out.println("ERROR WITH SQL: " + e.getMessage());
                            }
                        }      
                        else {
                            if (!bool_Overlap(add_Start_Time, add_End_Time, add_Date)) { appt_Time_Alert("Appointment could not be scheduled due to overlap with another existing appiontment."); }
                            if (!bool_Business_Hours(add_Start_Time, add_End_Time, add_Date)) { appt_Time_Alert("Appointment could not be scheduled because the start and/or end times are outside of business hours.\n"
                                    + "Business hours are 9:00 - 17:00 UTC time."); }
                        }
                    }
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            } 
        } catch (NullPointerException e) {
            System.out.println("Something was left blank.");
            empty_Field_Error("URL", "add", "an appointment");
        }
    }
    

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //One required lambda used to conver UTC time pulled from DB to users LDT
        LocalDateTime_Interface convert = (String dateTime) -> { 
            DateTimeFormatter dt_Format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
            LocalDateTime ldt =  LocalDateTime.parse(dateTime, dt_Format).atZone(ZoneId.of("UTC")).withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime();
            return ldt;
        };
        
        txt_ID.setDisable(true); 
        txt_Name.setDisable(true); 
        txt_Name.setText("Select a customer first");
        txt_Title.setDisable(true);
        txt_desc.setDisable(true);
        txt_contact.setDisable(true);
        txt_url.setDisable(true);
        txt_loc.setDisable(true);
        cbo_type.setDisable(true);
        cbo_type.setItems(Query.get_Types());
        
        date_appt.setDisable(true);
        date_appt.getEditor().setEditable(false);
        
        //One required lambda that disables dates prior to today as well as weekends from being selected
        date_appt.setDayCellFactory(picker -> new DateCell() { 
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();
                setDisable(empty || date.compareTo(today) < 0);
                if(date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY)
                    setDisable(true);
            }
        });
        
        cbo_start.setDisable(true);
        cbo_start.setItems(Query.get_Times());
        cbo_end.setDisable(true);
        cbo_end.setItems(Query.get_Times());
        btn_cancel.setDisable(true);
        btn_add.setDisable(true);
        

        Connection connection;
        try {
            connection = DatabaseConnection.getConnection();
            ResultSet result_set = connection.createStatement().executeQuery("SELECT customerId, customerName FROM customer ORDER BY customerId;");
            while (result_set.next()) {
                cust_Table_List.add(new Customer(result_set.getString("customerId"), result_set.getString("customerName")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(AddCustomerController.class.getName()).log(Level.SEVERE, null, ex);
        }

            col_Cust_ID.setCellValueFactory(new PropertyValueFactory<>("id"));
            col_Cust_Name.setCellValueFactory(new PropertyValueFactory<>("name"));
            table_Customers.setItems(cust_Table_List);
            
            
            
        try {
            connection = DatabaseConnection.getConnection();
            ResultSet rs = connection.createStatement().executeQuery("SELECT appointmentId, customerName, title, description, location, contact, type, url, DATE(start) date, start, end\n" +
                                                          "FROM customer c INNER JOIN appointment a ON c.customerId = a.customerId ORDER BY start;");
           
            while (rs.next()) {
                LocalDateTime zonedStart = convert.to_Local_DT(rs.getString("start"));
                LocalDateTime zonedEnd = convert.to_Local_DT(rs.getString("end"));
                String zonedStartString = zonedStart.toString().substring(11,16);
                String zonedEndString = zonedEnd.toString().substring(11,16);

                appt_Table_List.add(new Appointment(rs.getString("appointmentId"), 
                                                     rs.getString("customerName"), 
                                                     rs.getString("title"), 
                                                     rs.getString("description"),
                                                     rs.getString("location"), 
                                                     rs.getString("contact"), 
                                                     rs.getString("type"), 
                                                     rs.getString("url"),
                                                     rs.getString("date"),
                                                     zonedStartString,
                                                     zonedEndString));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ModifyAppointmentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
            col_Appt_ID.setCellValueFactory(new PropertyValueFactory<>("id"));
            col_Appt_Name.setCellValueFactory(new PropertyValueFactory<>("name"));
            col_Appt_Title.setCellValueFactory(new PropertyValueFactory<>("title"));
            col_Appt_Desc.setCellValueFactory(new PropertyValueFactory<>("description"));
            col_Appt_Loc.setCellValueFactory(new PropertyValueFactory<>("location"));
            col_Appt_Contact.setCellValueFactory(new PropertyValueFactory<>("contact"));
            col_Appt_Type.setCellValueFactory(new PropertyValueFactory<>("type"));
            col_Appt_URL.setCellValueFactory(new PropertyValueFactory<>("url"));
            col_Appt_Date.setCellValueFactory(new PropertyValueFactory<>("date"));
            col_Appt_Start.setCellValueFactory(new PropertyValueFactory<>("start"));
            col_Appt_End.setCellValueFactory(new PropertyValueFactory<>("end"));
            
            table_Appts.setItems(appt_Table_List);
    }    
}
