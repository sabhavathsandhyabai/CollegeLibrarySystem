package JDBC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnect {

    // URL pointing to your XAMPP MySQL server and the library_db database
    private static final String URL = "jdbc:mysql://localhost:3307/library_db";
    private static final String USER = "root"; 
    private static final String PASSWORD = ""; // Default XAMPP password is empty

    public static Connection getConnection() {
        Connection conn = null;
        try {
            // Load the MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Establish connection
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("🎉 Successfully connected to library_db!");
        } catch (ClassNotFoundException e) {
            System.out.println(" Driver not found! Check your Build Path.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println(" Connection failed! Make sure XAMPP MySQL is running.");
            e.printStackTrace();
        }
        return conn;
    }

    public static void main(String[] args) {
        // Test the connection
        getConnection();
    }
}
