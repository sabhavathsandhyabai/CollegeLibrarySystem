package JDBC;


import java.sql.*;

public class PreparedStatement {
    public static void main(String[] args) {

        String url = "jdbc:mysql://localhost:3307/college_db";
        String username = "root";
        String password =null;

        try {
           
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection con = DriverManager.getConnection(url, username, password);

           String sql = "INSERT INTO student(student_id, first_name, last_name) VALUES (?, ?, ?)";

            java.sql.PreparedStatement ps = con.prepareStatement(sql);

           
            ps.setInt(1, 13);
            ps.setString(2, "Reddi");
            ps.setString(3, "Kumari");
            
            
            int rows = ps.executeUpdate();

            System.out.println(rows + " record inserted successfully.");

           
            ps.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
 }
}