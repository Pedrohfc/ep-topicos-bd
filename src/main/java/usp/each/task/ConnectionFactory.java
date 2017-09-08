package usp.each.task;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionFactory
{
    private static Connection connection;

    private static void createConnection() throws Exception
    {
        Class.forName("org.postgresql.Driver");
        String url = "jdbc:postgresql://localhost/northwind";
        Properties db = new Properties();
        db.put("user", "northwind_user");
        db.put("password", "thewindisblowing");
        connection = DriverManager.getConnection(url, db);
    }

    public static Connection getConnection()
    {
        try
        {
            if (connection == null || connection.isClosed())
            {
                createConnection();
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        return connection;
    }

    public static void closeConnection()
    {
        if (connection != null)
        {
            try
            {
                connection.close();
            }
            catch (SQLException ex)
            {
                ex.printStackTrace();
            }
        }
    }
}
