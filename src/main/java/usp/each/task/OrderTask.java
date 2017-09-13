package usp.each.task;

import static usp.each.task.ConnectionFactory.getConnection;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;

public class OrderTask {
    /**
     * Query de vendas acumuladas de cada dia em um mes
     *
     * @param month
     * @param year
     * @return
     */
    public ResultSet ordersByDayOnMonth(int month, int year) {
        try {
            String sql = "SELECT o.OrderDate, COUNT(o.OrderID) as orders FROM Orders o"
                    + " WHERE date_part('month', o.OrderDate) = ? AND date_part('year', o.OrderDate) = ?"
                    + " GROUP BY o.OrderDate ORDER BY o.OrderDate";

            PreparedStatement stmt = getConnection().prepareStatement(sql);
            stmt.setInt(1, month);
            stmt.setInt(2, year);
            return stmt.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Query de rank de vendas aculadas por dia em um mes
     *
     * @param month
     * @param year
     * @return
     */
    public ResultSet rankByDayOnMonth(int month, int year) {
        try {
            String sql = "SELECT o.OrderDate, COUNT(o.OrderID) as orders,"
                    + " RANK() OVER (ORDER BY COUNT(o.OrderID) DESC)"
                    + " FROM Orders o"
                    + " WHERE date_part('month', o.OrderDate) = ? AND date_part('year', o.OrderDate) = ?"
                    + " GROUP BY o.OrderDate";
            PreparedStatement stmt = getConnection().prepareStatement(sql);
            stmt.setInt(1, month);
            stmt.setInt(2, year);
            return stmt.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String topTenQuery() {
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
     *
     * @param day
     * @param month
     * @param year
     * @return
     */
    public ResultSet topTenProducts(int day, int month, int year) {
        try {
            String sql = topTenQuery();
            PreparedStatement stmt = getConnection().prepareStatement(sql);

            SimpleDateFormat fomatter = new SimpleDateFormat("yyyy-mm-dd");
            Date d = new Date(fomatter.parse(year + "-" + month + "-" + day).getTime());

            stmt.setDate(1, d);

            return stmt.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Down 10 diario por produtos
     *
     * @param day
     * @param month
     * @param year
     * @return
     */
    public ResultSet downTenProducts(int day, int month, int year) {
        try {
            String sql = topTenQuery().replaceAll("DESC", "ASC");
            PreparedStatement stmt = getConnection().prepareStatement(sql);

            SimpleDateFormat fomatter = new SimpleDateFormat("yyyy-mm-dd");
            Date d = new Date(fomatter.parse(year + "-" + month + "-" + day).getTime());

            stmt.setDate(1, d);

            return stmt.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //NEW QUERIES

    /**
     * Query de vendas acumuladas de cada dia em um mes considerando que as vendas são o valor monetário vendido
     *
     * @param month
     * @param year
     * @return
     */
    public ResultSet ordersByDayOnMonthConsideringSellsAsMoney(int month, int year) {
        try {

            String sql = "SELECT O.OrderDate, SUM(OD.quantity * OD.UnitPrice)"
                    + " FROM Orders O, Order_Details OD"
                    + " WHERE date_part('month', O.OrderDate) = ? AND date_part('year', O.OrderDate) = ?"
                    + " AND O.OrderId = OD.OrderId"
                    + " GROUP BY O.OrderDate"
                    + " ORDER BY O.OrderDate";

            PreparedStatement stmt = getConnection().prepareStatement(sql);
            stmt.setInt(1, month);
            stmt.setInt(2, year);
            return stmt.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Query de vendas acumuladas de cada dia em um mes considerando que as vendas são a quantidade de produtos vendida
     *
     * @param month
     * @param year
     * @return
     */
    public ResultSet ordersByDayOnMonthConsideringSellsAsProductsSoldQuantity(int month, int year) {
        try {

            String sql = "SELECT O.OrderDate, SUM(OD.quantity)"
                    + " FROM Orders O, Order_Details OD"
                    + " WHERE date_part('month', O.OrderDate) = ? AND date_part('year', O.OrderDate) = ?"
                    + " AND O.OrderId = OD.OrderId"
                    + " GROUP BY O.OrderDate"
                    + " ORDER BY O.OrderDate";

            PreparedStatement stmt = getConnection().prepareStatement(sql);
            stmt.setInt(1, month);
            stmt.setInt(2, year);
            return stmt.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Query de ranking de vendas acumuladas de cada dia em um mes considerando que as vendas são a quantidade de produtos vendida
     *
     * @param month
     * @param year
     * @return
     */
    public ResultSet rankOrdersByDayOnMonthConsideringSellsAsProductsSoldQuantity(int month, int year) {
        try {

            String sql = "SELECT O.OrderDate, RANK() OVER (ORDER BY SUM(OD.quantity)) rank"
                    + " FROM Orders O, Order_Details OD"
                    + " WHERE date_part('month', O.OrderDate) = ? AND date_part('year', O.OrderDate) = ?"
                    + " AND O.OrderId = OD.OrderId"
                    + " GROUP BY O.OrderDate"
                    + " ORDER BY rank";

            PreparedStatement stmt = getConnection().prepareStatement(sql);
            stmt.setInt(1, month);
            stmt.setInt(2, year);
            return stmt.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Query de ranking de vendas acumuladas de cada dia em um mes considerando que as vendas são o valor monetário vendido
     *
     * @param month
     * @param year
     * @return
     */
    public ResultSet rankOrdersByDayOnMonthConsideringSellsAsMoney(int month, int year) {
        try {

            String sql = "SELECT O.OrderDate, RANK() OVER(ORDER BY SUM(OD.quantity * OD.UnitPrice)) rank"
                    + " FROM Orders O, Order_Details OD"
                    + " WHERE date_part('month', O.OrderDate) = ? AND date_part('year', O.OrderDate) = ?"
                    + " AND O.OrderId = OD.OrderId"
                    + " GROUP BY O.OrderDate"
                    + " ORDER BY rank";

            PreparedStatement stmt = getConnection().prepareStatement(sql);
            stmt.setInt(1, month);
            stmt.setInt(2, year);
            return stmt.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Query de ranking de vendas produtos de cada dia em um mes
     *
     * @param month
     * @param year
     * @return
     */
    public ResultSet topRankProductsByDayOnMonth(int month, int year) {
        try {

            String sql = "SELECT P.ProductName, RANK() OVER(ORDER BY SUM(OD.quantity) DESC) rank,"
                    + " SUM(OD.quantity), AVG(OD.UnitPrice), SUM(OD.quantity * OD.UnitPrice)"
                    + " FROM Orders O, Order_Details OD, Products P"
                    + " WHERE date_part('month', O.OrderDate) = ? AND date_part('year', O.OrderDate) = ?"
                    + " AND O.OrderId = OD.OrderId"
                    + " AND P.ProductID = OD.ProductID"
                    + " GROUP BY P.ProductName"
                    + " ORDER BY rank";

            PreparedStatement stmt = getConnection().prepareStatement(sql);
            stmt.setInt(1, month);
            stmt.setInt(2, year);
            return stmt.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Query de ranking decrescente de vendas produtos de cada dia em um mes
     *
     * @param month
     * @param year
     * @return
     */
    public ResultSet downRankProductsByDayOnMonth(int month, int year) {
        try {

            String sql = "SELECT P.ProductName, RANK() OVER(ORDER BY SUM(OD.quantity) ASC) rank,"
                    + " SUM(OD.quantity), AVG(OD.UnitPrice), SUM(OD.quantity * OD.UnitPrice)"
                    + " FROM Orders O, Order_Details OD, Products P"
                    + " WHERE date_part('month', O.OrderDate) = ? AND date_part('year', O.OrderDate) = ?"
                    + " AND O.OrderId = OD.OrderId"
                    + " AND P.ProductID = OD.ProductID"
                    + " GROUP BY P.ProductName"
                    + " ORDER BY rank";

            PreparedStatement stmt = getConnection().prepareStatement(sql);
            stmt.setInt(1, month);
            stmt.setInt(2, year);
            return stmt.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
