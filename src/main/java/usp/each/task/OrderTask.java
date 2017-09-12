package usp.each.task;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class OrderTask
{
    /***
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
                    + " RANK() OVER (ORDER BY COUNT(o.OrderID) DESC) as rk"
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
}
