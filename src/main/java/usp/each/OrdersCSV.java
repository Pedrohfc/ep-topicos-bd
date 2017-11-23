package usp.each;

import java.io.File;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import usp.each.task.ConnectionFactory;

public class OrdersCSV
{
    Map<String, List<String>> ordersMap;

    public static void main(String[] args)
    {
        new OrdersCSV().start();
    }
    
    public void start()
    {
        findOrders();
        saveCSV();
    }
    
    public void findOrders()
    {
        Connection connection = ConnectionFactory.getConnection();
        String query = "SELECT O.orderid, P.productname FROM"
                        + " order_details O INNER JOIN products P"
                        + " ON O.productid = P.productid";
        try
        {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            ordersMap = new HashMap<>();
            while (rs.next())
            {
                String orderId = rs.getString(1);
                String productName = rs.getString(2);
                if (!ordersMap.containsKey(orderId))
                {
                    ordersMap.put(orderId, new ArrayList<>());
                }
                
                ordersMap.get(orderId).add(productName);
            }
            rs.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public void saveCSV()
    {
        try
        {
            File ordersFile = new File("resources/orders.csv");
            PrintWriter out = new PrintWriter(ordersFile);
            for (List<String> order : ordersMap.values())
            {
                boolean first = true;
                for (String product : order)
                {
                    if (!first)
                    {
                        out.print(',');
                    }
                    
                    out.print("\""+product+"\"");
                    
                    first = false;
                }
                out.println();
            }
            out.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
    }

}
