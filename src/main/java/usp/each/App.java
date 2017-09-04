package usp.each;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

public class App 
{
    public static void main( String[] args ) throws Exception
    {
    	Class.forName("org.postgresql.Driver");
    	String url = "jdbc:postgresql://localhost/northwind";
    	Properties db = new Properties();
    	db.put("user", "northwind_user");
    	db.put("password", "thewindisblowing");
        Connection c = DriverManager.getConnection(url, db);
        queryTest(c);
        c.close();
    }
    
    
    public static void queryTest(Connection c) throws Exception
    {
    	PreparedStatement stmt = c.prepareStatement("select * from products limit 10");
        ResultSet rs = stmt.executeQuery();
        for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++)
        {
        	System.out.print(rs.getMetaData().getColumnName(i)+" ");
        }
        
        System.out.println("\n-----------");
        
        while (rs.next())
        {
        	for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++)
        	{
        		System.out.print(rs.getString(i)+ " ");
        	}
        	
        	System.out.println();
        }
    }
}
