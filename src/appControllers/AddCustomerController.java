package appControllers;

import appDatabase.DatabaseConnection;
import appDatabase.Query;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import static appModels.Alerts.empty_Field_Error;
import static appModels.Alerts.missing_Field_Error;
import static appModels.Alerts.customer_Add_Alert;
import static appModels.Alerts.add_Data_Error;
import appModels.Customer;


public class AddCustomerController implements Initializable {

    
    @FXML private TextField txt_name;
    @FXML private TextField txt_address;
    @FXML private TextField txt_address2;
    @FXML private TextField txt_zip;
    @FXML private TextField txt_phone;     
    @FXML private ComboBox cbo_city;     
    
    
    @FXML private TableView<Customer> table_customers;
    @FXML private TableColumn<Customer, Integer> col_cust_id;
    @FXML private TableColumn<Customer, String> col_cust_name;
    @FXML private TableColumn<Customer, String> col_cust_address;
    @FXML private TableColumn<Customer, String> col_cust_address2;
    @FXML private TableColumn<Customer, String> col_cust_city;
    @FXML private TableColumn<Customer, String> col_cust_zip;
    @FXML private TableColumn<Customer, String> col_cust_country;
    @FXML private TableColumn<Customer, String> col_cust_phone;
    
    
    ObservableList<Customer> customer_table_list = FXCollections.observableArrayList();
    

    
    @FXML private void exit_Action (ActionEvent event) throws IOException {
        txt_name.clear();
        txt_address.clear();
        txt_address2.clear();
        txt_zip.clear();
        txt_phone.clear();
        
        System.out.println("Add Customer -> Main Menu");
        Parent parent = FXMLLoader.load(getClass().getResource("/view_controller/MainScreen.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    
    @FXML private void clear_Action (ActionEvent event) throws IOException {
        txt_name.clear();
        txt_address.clear();
        txt_address2.clear();
        txt_zip.clear();
        txt_phone.clear();
        cbo_city.setItems(Query.getCities());
    }
    
    @FXML private void add_Action (ActionEvent event) throws IOException, NullPointerException {
    
        try {    
            String add_Name = txt_name.getText();
            String add_Address = txt_address.getText();
            String add_Address2 = txt_address2.getText();
            String add_Zip = txt_zip.getText();
            String add_Phone = txt_phone.getText();
            String add_City = cbo_city.getValue().toString();

                try {
                    if (!add_Name.isEmpty() && !add_Address.isEmpty() && !add_City.isEmpty() && !add_Zip.isEmpty() && !add_Phone.isEmpty()) {

                        Query.add_Customer(add_Name, add_Address, add_Address2, add_Zip, add_City, add_Phone);

                        System.out.println("Customer added, refreshing page");
                        Parent parent = FXMLLoader.load(getClass().getResource("/appControllers/AddCustomer.fxml"));
                        Scene scene = new Scene(parent);
                        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        stage.setScene(scene);
                        stage.show();

                        customer_Add_Alert(add_Name);
                    }
                    else {
                        if (add_Name.isEmpty()) missing_Field_Error("Name");
                        if (add_Address.isEmpty()) missing_Field_Error("Address Line 1");
                        if (add_Zip.isEmpty()) missing_Field_Error("Zip Code");
                        if (add_Phone.isEmpty()) missing_Field_Error("Phone");
                        if (add_City.isEmpty()) missing_Field_Error("City");
                    }
                    
                } catch (SQLException s) {
                    System.out.println("SQL exception occurred. Unable to Add data due to error in fields.");

                    if (s.getMessage().contains("postalCode")) {
                        add_Data_Error(s.getMessage() + " (Zip Code)");
                    }

                    else {
                        add_Data_Error(s.getMessage());
                    }
                }
                
        } catch (NullPointerException e) {
            System.out.println("A required field is empty");
            empty_Field_Error("Address Line 2", "add", "a customer");
        }
    }
    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        cbo_city.setItems(Query.getCities());
        
        Connection connection;
        try {
            connection = DatabaseConnection.getConnection();
            ResultSet result_set = connection.createStatement().executeQuery("SELECT customerId, customerName, address, address2, city, postalCode, country, phone\n" +
                                                          "FROM customer c INNER JOIN address a ON c.addressId = a.addressId\n" +
                                                          "INNER JOIN city i ON a.cityId = i.cityId\n" +
                                                          "INNER JOIN country o ON i.countryId = o.countryId ORDER BY customerId;");
            while (result_set.next()) {
                customer_table_list.add(new Customer(result_set.getString("customerId"), 
                                        result_set.getString("customerName"), 
                                        result_set.getString("address"), 
                                        result_set.getString("address2"),
                                        result_set.getString("city"), 
                                        result_set.getString("postalCode"), 
                                        result_set.getString("country"), 
                                        result_set.getString("phone")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(AddCustomerController.class.getName()).log(Level.SEVERE, null, ex);
        }

            col_cust_id.setCellValueFactory(new PropertyValueFactory<>("id"));
            col_cust_name.setCellValueFactory(new PropertyValueFactory<>("name"));
            col_cust_address.setCellValueFactory(new PropertyValueFactory<>("address"));
            col_cust_address2.setCellValueFactory(new PropertyValueFactory<>("address2"));
            col_cust_city.setCellValueFactory(new PropertyValueFactory<>("city"));
            col_cust_zip.setCellValueFactory(new PropertyValueFactory<>("zip"));
            col_cust_country.setCellValueFactory(new PropertyValueFactory<>("country"));
            col_cust_phone.setCellValueFactory(new PropertyValueFactory<>("phone"));

            table_customers.setItems(customer_table_list);  
    }       
}
