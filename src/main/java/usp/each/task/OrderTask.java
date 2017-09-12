package usp.each.task;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;

public class OrderTask
{
    /**
     * Query de vendas acumuladas de cada dia em um mes
     * @param month
     * @param year
     * @return
     */
    public ResultSet ordersByDayOnMonth(int month, int year)
    {
        try
        {
            String sql = "SELECT o.OrderDate, COUNT(o.OrderID) as orders FROM Orders o"
                    + " WHERE date_part('month', o.OrderDate) = ? AND date_part('year', o.OrderDate) = ?"
                    + " GROUP BY o.OrderDate ORDER BY o.OrderDate";

            PreparedStatement stmt = ConnectionFactory.getConnection().prepareStatement(sql);
            stmt.setInt(1, month);
            stmt.setInt(2, year);
            return stmt.executeQuery();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Query de rank de vendas aculadas por dia em um mes
     * @param month
     * @param year
     * @return
     */
    public ResultSet rankByDayOnMonth(int month, int year)
    {
        try
        {
            String sql = "SELECT o.OrderDate, COUNT(o.OrderID) as orders,"
                    + " RANK() OVER (ORDER BY COUNT(o.OrderID) DESC)"
                    + " FROM Orders o"
                    + " WHERE date_part('month', o.OrderDate) = ? AND date_part('year', o.OrderDate) = ?"
                    + " GROUP BY o.OrderDate";
            PreparedStatement stmt = ConnectionFactory.getConnection().prepareStatement(sql);
            stmt.setInt(1, month);
            stmt.setInt(2, year);
            return stmt.executeQuery();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
    
    private String topTenQuery()
    {
        String sql = "SELECT p.ProductName, COUNT(o.OrderID) as orders,"
                + " SUM(od.UnitPrice) as total, AVG(od.UnitPrice) as avg_price,"
                + " RANK() OVER (ORDER BY COUNT(o.OrderID) DESC, SUM(od.UnitPrice) DESC,"
                + " AVG(od.UnitPrice) DESC)"
                + " FROM (Orders o INNER JOIN Order_Details od ON o.OrderID = od.OrderID)"
                + " INNER JOIN Products p ON od.ProductID = p.ProductID"
                + " WHERE o.OrderDate = ?"
                + " GROUP BY p.ProductName"
                + " LIMIT 10";
        return sql;
    }
    
    /**
     * Top 10 diario por produtos
     * @param day
     * @param month
     * @param year
     * @return
     */
    public ResultSet topTenProducts(int day, int month, int year)
    {
        try
        {
            String sql = topTenQuery();
            PreparedStatement stmt = ConnectionFactory.getConnection().prepareStatement(sql);
            
            SimpleDateFormat fomatter = new SimpleDateFormat("yyyy-mm-dd");
            Date d = new Date(fomatter.parse(year+"-"+month+"-"+day).getTime());
            
            stmt.setDate(1, d);
            
            return stmt.executeQuery();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Down 10 diario por produtos
     * @param day
     * @param month
     * @param year
     * @return
     */
    public ResultSet downTenProducts(int day, int month, int year)
    {
        try
        {
            String sql = topTenQuery().replaceAll("DESC", "ASC");
            PreparedStatement stmt = ConnectionFactory.getConnection().prepareStatement(sql);
            
            SimpleDateFormat fomatter = new SimpleDateFormat("yyyy-mm-dd");
            Date d = new Date(fomatter.parse(year+"-"+month+"-"+day).getTime());
            
            stmt.setDate(1, d);
            
            return stmt.executeQuery();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
    
    
}
