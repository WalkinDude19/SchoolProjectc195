package appControllers;

import appDatabase.DatabaseConnection;
import static appDatabase.Query.appt_within_Fifteen;
import static appDatabase.Query.login_Query;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import appModels.User;
import java.time.LocalDate;
import java.time.LocalTime;

public class Login_Screen_Controller implements Initializable {
    
    
    @FXML private Label lbl_title;
    @FXML private Label lbl_username;
    @FXML private Label lbl_password;
    @FXML private TextField txt_username;      
    @FXML private PasswordField txt_password;      
    @FXML private Button btn_login;
    @FXML private Button btn_reset;
    
       
    private String error_title;
    private String error_Missing_Heading;
    private String error_Incorrect_Heading;
    private String error_Missing_Content;
    private String error_Incorrect_Content;
    
 
    
    @FXML 
    private void reset_Action (ActionEvent event) throws IOException {
        txt_username.clear();
        txt_password.clear();
    }
    
    @FXML 
    private void login_Action (ActionEvent event) throws IOException {
        
      
        String username, password;
        username = txt_username.getText();
        password = txt_password.getText();
        
        if (login_Query(username, password)) {
            
            String log = "src/appMain/logs.txt";
            FileWriter user_Logged = new FileWriter(log, true);
            try (PrintWriter output = new PrintWriter(user_Logged)) {
                output.println(txt_username.getText() + " logged in on " + LocalDate.now() +" "+LocalTime.now());
            }
            catch(Exception e)
                    {
                    System.out.println("Error adding to log file");
                    }
            
            Connection connection;
            try {
                connection = DatabaseConnection.getConnection();
                ResultSet rs_Get_User_Info = connection.createStatement().executeQuery(String.format("SELECT userId, userName FROM user WHERE userName='%s'", username));
                rs_Get_User_Info.next();
                User current_User = new User(rs_Get_User_Info.getString("userName"), rs_Get_User_Info.getString("userId"), true);
                System.out.println("userId: " + User.get_Current_User_ID() + " Current User: " + User.get_Current_Username());
            
            } catch (SQLException ex) {
                Logger.getLogger(Login_Screen_Controller.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            Parent parent = FXMLLoader.load(getClass().getResource("/appControllers/MainScreen.fxml"));
            Scene scene = new Scene(parent);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
            
            appt_within_Fifteen();
        }
        else {
            if (username.isEmpty() || password.isEmpty()) {
                Alert null_alert = new Alert(Alert.AlertType.ERROR);
                null_alert.initModality(Modality.NONE);
                null_alert.setTitle(error_title);
                null_alert.setHeaderText(error_Missing_Heading);
                null_alert.setContentText(error_Missing_Content);  
                null_alert.showAndWait();
            }
            else {
                Alert invalid_Alert = new Alert(Alert.AlertType.ERROR);
                invalid_Alert.initModality(Modality.NONE);
                invalid_Alert.setTitle(error_title);
                invalid_Alert.setHeaderText(error_Incorrect_Heading);
                invalid_Alert.setContentText(error_Incorrect_Content);  
                invalid_Alert.showAndWait();
            }
        }
    }
     
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {   
        
        try {
            /*testing Locale language switch
            Locale locale = new Locale("jp");
            Locale.setDefault(locale);  */ 
            rb = ResourceBundle.getBundle("languageFiles/rb", Locale.getDefault());
            if (Locale.getDefault().getLanguage().equals("es") || Locale.getDefault().getLanguage().equals("en") ||Locale.getDefault().getLanguage().equals("jp")) {
                lbl_title.setText(rb.getString("title"));
                lbl_username.setText(rb.getString("username"));
                lbl_password.setText(rb.getString("password"));
                btn_login.setText(rb.getString("loginButton"));
                btn_reset.setText(rb.getString("resetButton"));
                error_title = rb.getString("errorTitle");
                error_Missing_Heading = rb.getString("errorHeaderMissing");
                error_Incorrect_Heading = rb.getString("errorHeaderIncorrect");
                error_Missing_Content= rb.getString("errorContentMissing");
                error_Incorrect_Content = rb.getString("errorContentIncorrect");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }    
    }       
}
