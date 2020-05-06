package appControllers;

import appDatabase.DatabaseConnection;
import appDatabase.Query;
import static appDatabase.Query.delete_Customer;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import static appModels.Alerts.empty_Field_Error;
import static appModels.Alerts.customer_Delete_Alert;
import static appModels.Alerts.cusomer_Mod_Alert;
import static appModels.Alerts.empty_Search_Alert;
import static appModels.Alerts.no_Del_Selection_Alert;
import static appModels.Alerts.no_Mod_Selection_Alert;
import appModels.Customer;


public class Mod_Customer_Controller implements Initializable {

    
    
    @FXML private TextField txt_search;
    @FXML private TextField txt_id;
    @FXML private TextField txt_name;
    @FXML private TextField txt_address;
    @FXML private TextField txt_address2;
    @FXML private TextField txt_zip;
    @FXML private TextField txt_phone;     
    @FXML private ComboBox cbo_city;     
    @FXML private Button btn_save;
    @FXML private Button btn_delete;
    @FXML private Button btn_edit;
    @FXML private Button btn_search;
    @FXML private Button btn_cancel;

    
    
    @FXML private TableView<Customer> table_customers;
    @FXML private TableColumn<Customer, Integer> col_cust_id;
    @FXML private TableColumn<Customer, String> col_cust_name;
    @FXML private TableColumn<Customer, String> col_cust_address;
    @FXML private TableColumn<Customer, String> col_cust_address2;
    @FXML private TableColumn<Customer, String> col_cust_city;
    @FXML private TableColumn<Customer, String> col_cust_zip;
    @FXML private TableColumn<Customer, String> col_cust_country;
    @FXML private TableColumn<Customer, String> col_cust_phone;
    
    
    ObservableList<Customer> customer_list = FXCollections.observableArrayList();
    

    
    @FXML private void exit_Action (ActionEvent event) throws IOException {
        txt_name.clear();
        txt_address.clear();
        txt_address2.clear();
        txt_zip.clear();
        txt_phone.clear();
        
        Parent parent = FXMLLoader.load(getClass().getResource("/appControllers/MainScreen.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    

    public Customer cust_lookup(String searchName) {
        for (Customer customer : customer_list) {
            if (customer.get_Cust_Name().equalsIgnoreCase(searchName)) {
                return customer;
            }
        }
        return null;
    }
    
        
    @FXML private void search_Action (ActionEvent event) throws IOException {
        String search = txt_search.getText();
        if (!search.isEmpty()) {
            table_customers.getSelectionModel().select(cust_lookup(search));
        }
        else {
            empty_Search_Alert();
        }
    }
    
    @FXML private void edit_Action (ActionEvent event) throws IOException {

        Customer customer = table_customers.getSelectionModel().getSelectedItem();
        
        try {
            
            txt_id.setText(customer.get_Cust_ID());
            txt_name.setText(customer.get_Cust_Name());
            txt_address.setText(customer.get_Cust_Add());
            txt_address2.setText(customer.get_Cust_Add2());
            cbo_city.setValue(customer.get_Cust_City());
            txt_zip.setText(customer.get_Cust_Zip());
            txt_phone.setText(customer.get_Cust_Phone()); 
            
            txt_name.setDisable(false);
            txt_address.setDisable(false);
            txt_address2.setDisable(false);
            txt_zip.setDisable(false);
            cbo_city.setDisable(false);
            txt_phone.setDisable(false);
            btn_save.setDisable(false);
            btn_cancel.setDisable(false);
            
            table_customers.setDisable(true);
            btn_edit.setDisable(true);
            btn_delete.setDisable(true);
            txt_search.setDisable(true);
            btn_search.setDisable(true);
        }
        catch (Exception e) {
            String string = "customer";
            no_Mod_Selection_Alert(string);
        }
        
    }
    
    @FXML private void delete_Action (ActionEvent event) throws IOException {
        
        Customer customer = table_customers.getSelectionModel().getSelectedItem();
        
        try {
            String id = customer.get_Cust_ID();
            String name = customer.get_Cust_Name();

            Alert del_alert = new Alert(Alert.AlertType.CONFIRMATION);
            del_alert.initModality(Modality.NONE);
            del_alert.setTitle("Delete Customer?");
            del_alert.setHeaderText("Confirm");
            del_alert.setContentText("Confirm deletion of customer: " + name);
            Optional<ButtonType> result = del_alert.showAndWait();
                if (result.get() == ButtonType.OK) { 
                    delete_Customer(id);

                    Parent parent = FXMLLoader.load(getClass().getResource("/appControllers/ModifyCustomer.fxml"));
                    Scene scene = new Scene(parent);
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    stage.setScene(scene);
                    stage.show();
                    
                    customer_Delete_Alert(name);
                } 
                else {}

        } catch (IOException e) {
            String s = "customer";
            no_Del_Selection_Alert(s);
        }
    }
    
    @FXML private void cancel_Action (ActionEvent event) throws IOException {
        
        txt_id.clear();
        txt_name.clear();
        txt_address.clear();
        txt_address2.clear();
        txt_zip.clear();
        txt_phone.clear();
        cbo_city.setItems(Query.get_Cities());

        txt_id.setDisable(true);
        txt_name.setDisable(true);
        txt_address.setDisable(true);
        txt_address2.setDisable(true);
        txt_zip.setDisable(true);
        txt_phone.setDisable(true);
        cbo_city.setDisable(true);
        btn_cancel.setDisable(true);
        btn_save.setDisable(true);
        
        table_customers.setDisable(false);
        btn_edit.setDisable(false);
        btn_delete.setDisable(false);
        txt_search.setDisable(false);
        btn_search.setDisable(false);
    }
     
    @FXML private void save_Action (ActionEvent event) throws IOException {
        
        try {

            String mod_id = txt_id.getText();
            String mod_name = txt_name.getText();
            String mod_address = txt_address.getText();
            String mod_address2 = txt_address2.getText();
            String mod_zip = txt_zip.getText();
            String mod_phone = txt_phone.getText();
            String mod_city = (String) cbo_city.getValue().toString();

            if (!mod_name.isEmpty() && !mod_address.isEmpty() && !mod_city.isEmpty() && !mod_zip.isEmpty() && !mod_phone.isEmpty()) {

                Query.edit_Customer(mod_id, mod_name, mod_address, mod_address2, mod_city, mod_zip, mod_phone);

                Parent parent = FXMLLoader.load(getClass().getResource("/appControllers/ModifyCustomer.fxml"));
                Scene scene = new Scene(parent);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.show();

                cusomer_Mod_Alert(mod_name);

                btn_save.setDisable(true);
                table_customers.setDisable(false);
                btn_edit.setDisable(false);
                btn_delete.setDisable(false);

                txt_name.setDisable(true);
                txt_address.setDisable(true);
                txt_address2.setDisable(true);
                txt_zip.setDisable(true);
                cbo_city.setDisable(true);
                txt_phone.setDisable(true);
                btn_save.setDisable(true);
                btn_cancel.setDisable(true);
            }
            else {
                empty_Field_Error("Address Line 2", "modify", "a customer");
            }
        } catch (IOException e) {
            empty_Field_Error("Address Line 2", "modify", "a customer");
        }
    }
    
    
    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        txt_id.setDisable(true); 
        txt_name.setDisable(true);
        txt_address.setDisable(true);
        txt_address2.setDisable(true);
        txt_zip.setDisable(true);
        cbo_city.setItems(Query.get_Cities());
        cbo_city.setDisable(true);
        txt_phone.setDisable(true);
        btn_save.setDisable(true);
        btn_cancel.setDisable(true);

        Connection connection;
        try {
            connection = DatabaseConnection.getConnection();
            ResultSet result_set = connection.createStatement().executeQuery("SELECT customerId, customerName, address, address2, city, postalCode, country, phone\n" +
                                                          "FROM customer c INNER JOIN address a ON c.addressId = a.addressId\n" +
                                                          "INNER JOIN city i ON a.cityId = i.cityId\n" +
                                                          "INNER JOIN country o ON i.countryId = o.countryId ORDER BY customerId;");
            while (result_set.next()) {
                customer_list.add(new Customer(result_set.getString("customerId"), 
                                        result_set.getString("customerName"), 
                                        result_set.getString("address"), 
                                        result_set.getString("address2"),
                                        result_set.getString("city"), 
                                        result_set.getString("postalCode"), 
                                        result_set.getString("country"), 
                                        result_set.getString("phone")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Mod_Customer_Controller.class.getName()).log(Level.SEVERE, null, ex);
        }

            col_cust_id.setCellValueFactory(new PropertyValueFactory<>("_Cust_ID"));
            col_cust_name.setCellValueFactory(new PropertyValueFactory<>("_Cust_Name"));
            col_cust_address.setCellValueFactory(new PropertyValueFactory<>("_Cust_Add"));
            col_cust_address2.setCellValueFactory(new PropertyValueFactory<>("_Cust_Add2"));
            col_cust_city.setCellValueFactory(new PropertyValueFactory<>("_Cust_City"));
            col_cust_zip.setCellValueFactory(new PropertyValueFactory<>("_Cust_Zip"));
            col_cust_country.setCellValueFactory(new PropertyValueFactory<>("_Cust_Country"));
            col_cust_phone.setCellValueFactory(new PropertyValueFactory<>("_Cust_Phone"));

            table_customers.setItems(customer_list);  
    }           
}    
    
