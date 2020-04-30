package appControllers;

import appDatabase.DatabaseConnection;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
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
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import appModels.LocalDateTime_Interface;
import appModels.User;


public class ReportsController implements Initializable {

       
    @FXML private ChoiceBox cbo_report;
    @FXML private TextArea txt_report;
    @FXML private Button btn_reset;
    @FXML private Button btn_report;
    @FXML private Button btn_exit;
    
    
    static ObservableList<String> reports_List = FXCollections.observableArrayList();

    
    LocalDateTime_Interface convert = (String dateTime) -> { //One required lambda used to conver UTC time pulled from DB to users LDT
            DateTimeFormatter dt_Format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
            LocalDateTime ldt =  LocalDateTime.parse(dateTime, dt_Format).atZone(ZoneId.of("UTC")).withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime();
            return ldt;
    };
    

 
    @FXML private void exit_Action (ActionEvent event) throws IOException {
        System.out.println("Reports -> Main Menu");
        Parent parent = FXMLLoader.load(getClass().getResource("/view_controller/MainScreen.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    
    @FXML private void report_Action (ActionEvent event) throws IOException, SQLException {
        txt_report.clear();
        cbo_report.setDisable(true);
        btn_report.setDisable(true);
        btn_reset.setDisable(false);
        
        String chosenReport = cbo_report.getValue().toString();
        
        switch (chosenReport) {
            case "Appointment Types by Month":
                txt_report.setText(rpt_Appt_Types());
                break;
            case "Schedule for Consultant":
                txt_report.setText(rpt_Schedule());
                break;
            case "Appointments per Month":
                txt_report.setText(rpt_num_Appts());
                break;
            default:
                break;
        }                     
    }
    
    @FXML private void reset_Action (ActionEvent event) throws IOException {
        txt_report.clear();
        btn_reset.setDisable(true);
        cbo_report.setDisable(false);
        cbo_report.setItems(get_reports_list());
        btn_report.setDisable(false);
        
    }
    
    
    
    public static ObservableList<String> get_reports_list() {
        reports_List.removeAll(reports_List); //prevents duplication
        reports_List.add("Appointment Types by Month");
        reports_List.add("Schedule for Consultant");
        reports_List.add("Appointments per Month");
        return reports_List;
    }
    
    
    
    //Generates report: number of appointment types by month 
    public String rpt_Appt_Types() throws SQLException {
        Connection connection = DatabaseConnection.getConnection();
        try {
            
            StringBuilder report = new StringBuilder();
            report.append("Month    | # of each Type  \n______________________________________________________________________\n");
            
            ResultSet result_set = connection.createStatement().executeQuery(String.format("SELECT MONTHNAME(start) as Month, type, COUNT(*) as Amount\n" +
                                                                                        "FROM appointment GROUP BY MONTH(start), type;"));
            while (result_set.next()) {
                report.append(result_set.getString("Month") + "          " + result_set.getString("Amount") + "   " + result_set.getString("type") + "\n");
            }

            return report.toString();
            
        } catch (SQLException e) {
            System.out.println("Error getting report: " + e.getMessage());
            return "Report Failed"; 
        }
        
    }
    
    
    
    public String rpt_Schedule() throws SQLException {
        Connection conn = DatabaseConnection.getConnection();
        try {
            
            StringBuilder report = new StringBuilder();
            report.append("Consultant ").append(User.get_Current_Username()).append("'s Schedule\n_______________________________________________________________________________________________\nDate                Start       End           Appointment Info\n_______________________________________________________________________________________________\n");
            
            ResultSet result_set = conn.createStatement().executeQuery(String.format("SELECT DATE(start) date, start, end, customerName, title, description, type, location "
                                                                                    + "FROM appointment a INNER JOIN customer c ON a.customerId=c.customerId WHERE userId='%s' ORDER BY start;", User.get_Current_User_ID()));
            
            while (result_set.next()) {
                LocalDateTime ldt_Start = convert.to_Local_DT(result_set.getString("start"));
                LocalDateTime ldt_End = convert.to_Local_DT(result_set.getString("end"));              
                
                String date = result_set.getString("date");
                String str_LDT_Start = ldt_Start.toString().substring(11,16);
                String str_LDT_End = ldt_End.toString().substring(11,16);
                String cust_name = result_set.getString("customerName");
                String title = result_set.getString("title");
                String desc = result_set.getString("description");
                String type = result_set.getString("type");
                String loc = result_set.getString("location");
                
                report.append(date).append("\t").append(str_LDT_Start).append("\t").append(str_LDT_End).append("\t").append(cust_name).append("     ").append(title).append("     ").append(desc).append("     ").append(type).append("     ").append(loc).append("\n\n");   
            }
            
           
            return report.toString();
 
        } catch (SQLException e) {
            System.out.println("Error getting report: " + e.getMessage());
             return "Report Failed"; 
        }
    }
    
    public String rpt_num_Appts() throws SQLException {
        Connection connection = DatabaseConnection.getConnection();
        try {
            
            StringBuilder report = new StringBuilder();
            report.append("Month - Number of Appointments\n___________________________________\n");
            
            ResultSet result_set = connection.createStatement().executeQuery(String.format("SELECT MONTHNAME(start) Month, YEAR(start) Year, COUNT(*) Amount "
                                                                                         + "FROM appointment GROUP BY Month;"));
            
            while (result_set.next()) {
                String month = result_set.getString("Month");
                String year = result_set.getString("Year");
                String amount = result_set.getString("Amount");
                
                report.append(month).append(", ").append(year).append(" - ").append(amount).append("\n");   
            }

            return report.toString(); 
            
        } catch (SQLException e) {
            System.out.println("Error getting report: " + e.getMessage());
           return "Report Failed"; 
        }
    }        
    
   
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btn_reset.setDisable(true);
        cbo_report.setItems(get_reports_list());
    }       
}
