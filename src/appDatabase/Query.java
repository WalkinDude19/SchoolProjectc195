package appDatabase;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import static appModels.Alerts.impending_Appt;
import appModels.User;
import static appDatabase.DatabaseConnection.connection;

public class Query {
    
    private static String query;
    private static Statement statement;
    private static ResultSet result_set;
    static ObservableList<String> cities = FXCollections.observableArrayList();
    static ObservableList<String> types = FXCollections.observableArrayList();
    static ObservableList<String> customers_Table = FXCollections.observableArrayList();
    static ObservableList<String> times = FXCollections.observableArrayList();
    
    
    
    public static boolean login_Query(String usernameInput, String passwordInput) {
            try{
                DatabaseConnection.makeConnection();
                PreparedStatement p_statement = connection.prepareStatement("SELECT * FROM user WHERE userName=? AND password=?");
                p_statement.setString(1, usernameInput);
                p_statement.setString(2, passwordInput);
                ResultSet rs = p_statement.executeQuery();
                if (rs.next()) {
                    return true;
                }
                else {
                    return false;
                }
            } catch (ClassNotFoundException | SQLException e) {
                System.out.println("Error: " + e.getMessage());
                return false;
            }
        }



    public static boolean appt_within_Fifteen() {
        try {
            ResultSet earliest_Appt = connection.createStatement().executeQuery(String.format("SELECT customerName "
                    + "FROM customer c INNER JOIN appointment a ON c.customerId=a.customerId INNER JOIN user u ON a.userId=u.userId "
                    + "WHERE a.userId='%s' AND a.start BETWEEN '%s' AND '%s'",
                    User.get_Current_User_ID(), LocalDateTime.now(ZoneId.of("UTC")), LocalDateTime.now(ZoneId.of("UTC")).plusMinutes(15)));
            earliest_Appt.next();
            
            String name = earliest_Appt.getString("customerName");
            impending_Appt(name);
            
            return true;
        } catch (SQLException e) {
            System.out.println("No Appointments beginning soon");
            return false;
        }
    }
    

    

    public static ObservableList<String> getCities() {
        try {
            cities.removeAll(cities); //prevents duplication
            ResultSet city_List = connection.createStatement().executeQuery("SELECT city FROM city;");
            while (city_List.next()) {
                cities.add(city_List.getString("city"));
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return cities;
    }
    

    

    public static void add_Customer(String name, String address1, String address2, String zip, String city, String phone) throws SQLException {
            
            ResultSet rs_Get_City_ID = connection.createStatement().executeQuery(String.format("SELECT cityId FROM city WHERE city = '%s'", city));
            rs_Get_City_ID.next();
            
            connection.createStatement().executeUpdate(String.format("INSERT INTO address "
                    + "(address, address2, cityId, postalCode, phone, createDate, createdBy, lastUpdate, lastUpdateBy) " +
                    "VALUES ('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s')", 
                    address1, address2, rs_Get_City_ID.getString("cityId"), zip, phone, LocalDateTime.now(), User.get_Current_Username(), LocalDateTime.now(), User.get_Current_Username()));
            
            ResultSet rs_Get_Address_ID = connection.createStatement().executeQuery(String.format("SELECT addressId FROM address WHERE address='%s' AND address2='%s' AND cityId='%s' AND postalCode='%s'",
                                    address1, address2, rs_Get_City_ID.getString("cityId"), zip));
            rs_Get_Address_ID.next();
            
            connection.createStatement().executeUpdate(String.format("INSERT INTO customer "
                    + "(customerName, addressId, active, createDate, createdBy, lastUpdate, lastUpdateBy) " +
                    "VALUES ('%s', '%s', 1, '%s', '%s', '%s', '%s')", 
                    name, rs_Get_Address_ID.getString("addressId"), LocalDateTime.now(), User.get_Current_Username(), LocalDateTime.now(), User.get_Current_Username()));    

    }
    
    
    
    public static void delete_Customer(String id){
        try {
            ResultSet rs_Get_Address_ID = connection.createStatement().executeQuery(String.format("SELECT addressId FROM customer WHERE customerId='%s'", id));
            rs_Get_Address_ID.next();
            
            connection.createStatement().executeUpdate(String.format("DELETE FROM customer"
                    + " WHERE customerId='%s'", id));

            connection.createStatement().executeUpdate(String.format("DELETE FROM address"
                    + " WHERE addressId='%s'", rs_Get_Address_ID.getString("addressId")));
            
            connection.createStatement().executeUpdate(String.format("DELETE FROM appointment"
                    + " WHERE customerId='%s'", id));
            
        } catch (SQLException e) {
            System.out.println("Error deleting customer: " + e.getMessage());
        }
    }

    
    
 
    public static void edit_Customer(String id, String name, String address1, String address2, String city, String zip, String phone) {
        try {
       
            connection.createStatement().executeUpdate(String.format("UPDATE customer"
                    + " SET customerName='%s', lastUpdate='%s', lastUpdateBy='%s'" 
                    + " WHERE customerId='%s'",
                    name, LocalDateTime.now(), User.get_Current_Username(), id));
            
            
            ResultSet rs_Get_City_ID = connection.createStatement().executeQuery(String.format("SELECT cityId FROM city WHERE city = '%s'", city));
            rs_Get_City_ID.next();
            

            connection.createStatement().executeUpdate(String.format("UPDATE address"
                    + " SET address='%s', address2='%s', cityId='%s', postalCode='%s', phone='%s', lastUpdate='%s', lastUpdateBy='%s'" 
                    + " WHERE addressId='%s'",
                    address1, address2, rs_Get_City_ID.getString("cityId"), zip, phone, LocalDateTime.now(), User.get_Current_Username(), id));
            
        } catch (Exception e) {
            System.out.println("Error editing customer: " + e.getMessage());
        }
    }

    
    

    

    public static ObservableList<String> get_Types() {
        types.removeAll(types); //stops list Duplication
        types.add("Beginner");
        types.add("Intermediate");
        types.add("Advanced");
        return types;
    }
    
    
          

    public static ObservableList<String> get_Times() {
        try {     
            times.removeAll(times); //stops list Duplication
            for (int i = 0; i < 24; i++ ) {
                String hour;
                    if(i < 10) {                   
                        hour = "0" + i;
                    }
                    
                    else {
                        hour = Integer.toString(i);
                    }
                    times.add(hour + ":00:00");
                    times.add(hour + ":15:00");
                    times.add(hour + ":30:00");
                    times.add(hour + ":45:00");
                }
            times.add("24:00:00");
                
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return times;
        }
    
    
    
    public static LocalDateTime string_LDT_TO_UTC(String time, String date) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime ldt =  LocalDateTime.parse(date + " " + time, format).atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime();
        return ldt;
    }
    
    
    
    public static boolean bool_Business_Hours(String startTime, String endTime, String date) {
        
        LocalDateTime ldt_Start = string_LDT_TO_UTC(startTime, date);
        LocalDateTime ldt_End = string_LDT_TO_UTC(endTime, date);
        String utc_Start = ldt_Start.toString().substring(11,16);
        String utc_End = ldt_End.toString().substring(11,16);
       
        LocalTime start = LocalTime.parse(utc_Start);
        LocalTime end = LocalTime.parse(utc_End);
        LocalTime open = LocalTime.parse("08:59");
        LocalTime closed = LocalTime.parse("17:01");
        Boolean bool_Start_Allowed = start.isAfter(open);
        Boolean bool_End_Allowed = end.isBefore(closed);
        
        if (bool_Start_Allowed && bool_End_Allowed) {
            return true;
        }
        else {
            return false;
        }
        
    }

    
    

    public static boolean bool_Overlap(String startTime, String endTime, String date) {
            try {
                
                LocalDateTime ldt_Start = string_LDT_TO_UTC(startTime, date);
                LocalDateTime ldt_End = string_LDT_TO_UTC(endTime, date);
                String utc_Start = ldt_Start.toString();
                String utc_End = ldt_End.toString();

               
                ResultSet getOverlap = connection.createStatement().executeQuery(String.format(
                           "SELECT start, end, customerName FROM appointment a INNER JOIN customer c ON a.customerId=c.customerId " +
                           "WHERE ('%s' >= start AND '%s' <= end) " +
                           "OR ('%s' <= start AND '%s' >= end) " +
                           "OR ('%s' <= start AND '%s' >= start) " +
                           "OR ('%s' <= end AND '%s' >= end)",
                           utc_Start, utc_Start, utc_End, utc_End, utc_Start, utc_End, utc_Start, utc_End));
                getOverlap.next();
                System.out.println("APPOINTMENT OVERLAP: " + getOverlap.getString("customerName"));
                return false;
            } catch (SQLException e) {
                System.out.println("There are no appointment conflicts.");
                return true;
            }
    }
    
    
    
    public static boolean bool_Self_Overlap(String startTime, String endTime, String date, String name, String apptId) {
            try {
                
                LocalDateTime ldt_Start = string_LDT_TO_UTC(startTime, date);
                LocalDateTime ldt_End = string_LDT_TO_UTC(endTime, date);
                String utc_Start = ldt_Start.toString();
                String utc_End = ldt_End.toString();

                ResultSet rs_Get_Overlap = connection.createStatement().executeQuery(String.format(
                           "SELECT start, end, customerName, appointmentId FROM appointment a INNER JOIN customer c ON a.customerId=c.customerId " +
                           "WHERE ('%s' >= start AND '%s' <= end) " +
                           "OR ('%s' <= start AND '%s' >= end) " +
                           "OR ('%s' <= start AND '%s' >= start) " +
                           "OR ('%s' <= end AND '%s' >= end)",
                           utc_Start, utc_Start, utc_End, utc_End, utc_Start, utc_End, utc_Start, utc_End));
                rs_Get_Overlap.next();
                
                String check_LDT_Start = rs_Get_Overlap.getString("start").substring(0,16);
                String check_UTC_Start = utc_Start.replace('T', ' ');
                String check_LDT_End = rs_Get_Overlap.getString("end").substring(0,16);
                String check_UTC_End = utc_End.replace('T', ' ');
                

                    if (rs_Get_Overlap.getString("customerName").equals(name) && rs_Get_Overlap.getString("appointmentId").equals(apptId)){
                            System.out.println("Only conflicting appointment is your own. Save over self: " + rs_Get_Overlap.getString("customerName"));
                            return true;
                    }
                    else {
                        System.out.println("Went to else");
                        System.out.println(rs_Get_Overlap.getString("customerName") + " " + name + " " + rs_Get_Overlap.getString("appointmentId") + " " + apptId + " "  + check_LDT_Start + " " + check_UTC_Start + " " + check_LDT_End + " " + check_UTC_End);
                        return false;
                    }

            } catch (SQLException e) {
                return true;
        }
            
    }
    
    
    
    public static void add_Appointment(String id, String name, String title, String description, String location, String contact, String type, String url, String date, String startTime, String endTime) throws SQLException {
            
            LocalDateTime ldt_Start = string_LDT_TO_UTC(startTime, date);
            LocalDateTime ldt_End = string_LDT_TO_UTC(endTime, date);
            String utc_Start = ldt_Start.toString();
            String utc_End = ldt_End.toString();
            
            System.out.println("Converted date and start time (UTC): " + utc_Start);

            connection.createStatement().executeUpdate(String.format("INSERT INTO appointment "
                    + "(customerId, userId, title, description, location, contact, type, url, start, end, createDate, createdBy, lastUpdate, lastUpdateBy) " +
                    "VALUES ('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s')", 
                    id, User.get_Current_User_ID(), title, description, location, contact, type, url, utc_Start, utc_End, LocalDateTime.now(), User.get_Current_Username(), LocalDateTime.now(), User.get_Current_Username()));  
    }
    
    
    
    public static void delete_Appointment(String id){
        try {
            connection.createStatement().executeUpdate(String.format("DELETE FROM appointment WHERE appointmentId='%s'", id));
  
        } catch (Exception e) {
            System.out.println("Error deleting appointment: " + e.getMessage());
        }
    }
    
    
    
    public static void edit_Appointment(String apptId, String custName, String title, String description, String contact, String url, String type, String location, String date, String startTime, String endTime) {
        try {
            LocalDateTime ldt_Start = string_LDT_TO_UTC(startTime, date);
            LocalDateTime ldt_End = string_LDT_TO_UTC(endTime, date);
            String utc_Start = ldt_Start.toString();
            String utc_End = ldt_End.toString();
            
            System.out.println("Converted date and start time (UTC): " + utc_Start);

            
            ResultSet rs_Get_Customer_ID = connection.createStatement().executeQuery(String.format("SELECT customerId FROM appointment WHERE appointmentId = '%s'", apptId));
            rs_Get_Customer_ID.next();

            connection.createStatement().executeUpdate(String.format("UPDATE appointment "
                    + "SET customerId='%s', userId='%s', title='%s', description='%s', location='%s', contact='%s', type='%s', url='%s', start='%s', end='%s', lastUpdate='%s', lastUpdateBy='%s' " +
                    "WHERE appointmentId='%s'", 
                    rs_Get_Customer_ID.getString("customerId"), User.get_Current_User_ID(), title, description, location, contact, type, url, utc_Start, utc_End, LocalDateTime.now(), User.get_Current_Username(), apptId));  
        } catch (SQLException e) {
            System.out.println("Error editing appointment: " + e.getMessage());
        }
    }
}