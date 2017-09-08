package usp.each.task;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class OrderTask
{
    public ResultSet ordersByDayOnMonth(int month, int year)
    {
        try
        {
            String sql = "SELECT o.OrderDate, COUNT(o.OrderID) FROM Orders o"
                            + " WHERE date_part('month', o.OrderDate) = ? AND date_part('year', o.OrderDate) = ?"
                            + " GROUP BY o.OrderDate ORDER BY o.OrderDate LIMIT 62";

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
