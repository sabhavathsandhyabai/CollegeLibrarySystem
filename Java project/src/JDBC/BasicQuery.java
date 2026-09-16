package JDBC;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class BasicQuery {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			Connection c = JdbcConnection.getcon();
			Statement stmt = c.createStatement();
			
			String sql="select first_name,salary from employee";
			
			ResultSet rs = stmt.executeQuery(sql);
			
			while(rs.next()) {
				System.out.println("name= "+rs.getString("first_name")+" salary= "+rs.getFloat("salary"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

class JdbcConnection{
	public static Connection conn=null;
	public static Connection getcon() throws SQLException {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			
			String url="jdbc:mysql://localhost:3307/college_db";
			String user="root";
			String password=null;
			
			conn=DriverManager.getConnection(url, user, password);
			System.out.println("connection established");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return conn;
		
	}
}