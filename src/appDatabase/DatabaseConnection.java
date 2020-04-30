package appDatabase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    
    private static final String DBNAME = "U05bPD";
    private static final String DB_URL = "jdbc:mysql://3.227.166.251/" + DBNAME;
    private static final String USERNAME = "U05bPD";
    private static final String PASSWORD = "53688457744";
    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static Connection connection;
    

    public static void makeConnection() throws ClassNotFoundException, SQLException {
        
        Class.forName(JDBC_DRIVER);
        connection = (Connection) DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
        System.out.println("Connection successful");   
    }
    
    

    public static Connection getConnection() throws SQLException {
        Connection connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
        return connection;
    }
    
    
    public static void closeConnection() throws ClassNotFoundException, SQLException {
        connection.close();
        System.out.println("Connection closed.");
    }
}
