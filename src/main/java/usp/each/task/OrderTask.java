package usp.each.task;

import static usp.each.task.ConnectionFactory.getConnection;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;

public class OrderTask {
	
	/**
     * Query auxiliar para trazer os distincts de data (dia, mes, ano)
     *
     * @return
     */
    public ResultSet getDistinctDateOrders(String tipo) {
        try {
            String sql = "SELECT DISTINCT date_part('" + tipo + "', o.OrderDate) as r FROM Orders o ORDER BY r";
            PreparedStatement stmt = getConnection().prepareStatement(sql);
            return stmt.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
	
	
	
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
     * Query de vendas acumuladas de cada dia em um mes considerando que as vendas s�o o valor monet�rio vendido
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
     * Query de vendas acumuladas de cada dia em um mes considerando que as vendas s�o a quantidade de produtos vendida
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
     * Query de ranking de vendas acumuladas de cada dia em um mes considerando que as vendas s�o a quantidade de produtos vendida
     *
     * @param month
     * @param year
     * @return
     */
    public ResultSet rankOrdersByDayOnMonthConsideringSellsAsProductsSoldQuantity(int month, int year) {
        try {

            String sql = "SELECT RANK() OVER (ORDER BY SUM(OD.quantity) desc) rank, O.OrderDate, SUM(OD.quantity)"
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
     * Query de ranking de vendas acumuladas de cada dia em um mes considerando que as vendas s�o o valor monet�rio vendido
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
    public ResultSet topRankProductsByDayOnMonth(int day, int month, int year) {
        try {

            String sql = "SELECT P.ProductName, RANK() OVER(ORDER BY SUM(OD.quantity) DESC) rank,"
                    + " SUM(OD.quantity), AVG(OD.UnitPrice), SUM(OD.quantity * OD.UnitPrice)"
                    + " FROM Orders O, Order_Details OD, Products P"
                    + " WHERE date_part('month', O.OrderDate) = ? AND date_part('year', O.OrderDate) = ?"
                    + " AND date_part('day', O.OrderDate) = ?"
                    + " AND O.OrderId = OD.OrderId"
                    + " AND P.ProductID = OD.ProductID"
                    + " GROUP BY P.ProductName"
                    + " ORDER BY rank";

            PreparedStatement stmt = getConnection().prepareStatement(sql);
            stmt.setInt(1, month);
            stmt.setInt(2, year);
            stmt.setInt(3, day);
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
    public ResultSet downRankProductsByDayOnMonth(int day, int month, int year) {
        try {

            String sql = "SELECT P.ProductName, RANK() OVER(ORDER BY SUM(OD.quantity) ASC) rank,"
                    + " SUM(OD.quantity), AVG(OD.UnitPrice), SUM(OD.quantity * OD.UnitPrice)"
                    + " FROM Orders O, Order_Details OD, Products P"
                    + " WHERE date_part('month', O.OrderDate) = ? AND date_part('year', O.OrderDate) = ?"
                    + " AND date_part('day', O.OrderDate) = ?"
                    + " AND O.OrderId = OD.OrderId"
                    + " AND P.ProductID = OD.ProductID"
                    + " GROUP BY P.ProductName"
                    + " ORDER BY rank";

            PreparedStatement stmt = getConnection().prepareStatement(sql);
            stmt.setInt(1, month);
            stmt.setInt(2, year);
            stmt.setInt(3, day);
            return stmt.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * Query de ranking de vendas produtos acumulado de cada mes
     *
     * @param month
     * @param year
     * @return
     */
    public ResultSet topAccumulatedRankProductsByDayOnMonth(int month, int year) {
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
     * Query de ranking acumulado decrescente de vendas produtos de cada dia em um mes
     *
     * @param month
     * @param year
     * @return
     */
    public ResultSet downRankAccumulatedProductsByDayOnMonth(int month, int year) {
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


    /**
     * Query de ranking de vendas por categoria de cada dia em um mes
     *
     * @param month
     * @param year
     * @return
     */
    public ResultSet topRankCategoriesByDayOnMonth(int day, int month, int year) {
        try {

            String sql = "SELECT C.CategoryName, RANK() OVER(ORDER BY SUM(OD.quantity) DESC) rank,"
                    + " SUM(OD.quantity), AVG(OD.UnitPrice), SUM(OD.quantity * OD.UnitPrice)"
                    + " FROM Orders O, Order_Details OD, Products P, Categories C"
                    + " WHERE date_part('month', O.OrderDate) = ? AND date_part('year', O.OrderDate) = ?"
                    + " AND date_part('day', O.OrderDate) = ?"
                    + " AND O.OrderId = OD.OrderId"
                    + " AND P.ProductID = OD.ProductID"
                    + " AND C.CategoryID = P.CategoryID"
                    + " GROUP BY C.CategoryName"
                    + " ORDER BY rank";

            PreparedStatement stmt = getConnection().prepareStatement(sql);
            stmt.setInt(1, month);
            stmt.setInt(2, year);
            stmt.setInt(3, day);
            return stmt.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Query de ranking decrescente de vendas por categoria de cada dia em um mes
     *
     * @param month
     * @param year
     * @return
     */
    public ResultSet downRankCategoriesByDayOnMonth(int day, int month, int year) {
        try {

            String sql = "SELECT C.CategoryName, RANK() OVER(ORDER BY SUM(OD.quantity) ASC) rank,"
                    + " SUM(OD.quantity), AVG(OD.UnitPrice), SUM(OD.quantity * OD.UnitPrice)"
                    + " FROM Orders O, Order_Details OD, Products P, Categories C"
                    + " WHERE date_part('month', O.OrderDate) = ? AND date_part('year', O.OrderDate) = ?"
                    + " AND date_part('day', O.OrderDate) = ?"
                    + " AND O.OrderId = OD.OrderId"
                    + " AND P.ProductID = OD.ProductID"
                    + " AND C.CategoryID = P.CategoryID"
                    + " GROUP BY C.CategoryName"
                    + " ORDER BY rank";

            PreparedStatement stmt = getConnection().prepareStatement(sql);
            stmt.setInt(1, month);
            stmt.setInt(2, year);
            stmt.setInt(3, day);
            return stmt.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Query de ranking de vendas por categoria acumulado de cada mes
     *
     * @param month
     * @param year
     * @return
     */
    public ResultSet topAccumulatedRankCategoriesByDayOnMonth(int month, int year) {
        try {

            String sql = "SELECT C.CategoryName, RANK() OVER(ORDER BY SUM(OD.quantity) DESC) rank,"
                    + " SUM(OD.quantity), AVG(OD.UnitPrice), SUM(OD.quantity * OD.UnitPrice)"
                    + " FROM Orders O, Order_Details OD, Products P, Categories C"
                    + " WHERE date_part('month', O.OrderDate) = ? AND date_part('year', O.OrderDate) = ?"
                    + " AND O.OrderId = OD.OrderId"
                    + " AND P.ProductID = OD.ProductID"
                    + " AND C.CategoryID = P.CategoryID"
                    + " GROUP BY C.CategoryName"
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
     * Query de ranking acumulado decrescente de vendas por categoria de cada dia em um mes
     *
     * @param month
     * @param year
     * @return
     */
    public ResultSet downRankAccumulatedCategoriesByDayOnMonth(int month, int year) {
        try {

            String sql = "SELECT C.CategoryName, RANK() OVER(ORDER BY SUM(OD.quantity) ASC) rank,"
                    + " SUM(OD.quantity), AVG(OD.UnitPrice), SUM(OD.quantity * OD.UnitPrice)"
                    + " FROM Orders O, Order_Details OD, Products P, Categories C"
                    + " WHERE date_part('month', O.OrderDate) = ? AND date_part('year', O.OrderDate) = ?"
                    + " AND O.OrderId = OD.OrderId"
                    + " AND P.ProductID = OD.ProductID"
                    + " AND C.CategoryID = P.CategoryID"
                    + " GROUP BY C.CategoryName"
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
    
    public ResultSet productsNotSold(int day, int month, int year) {
        try {
            String sql = " SELECT P.ProductName FROM Products P"
                    + " WHERE P.ProductName NOT IN "
                    + " (SELECT PR.ProductName"
                    + " FROM (Products PR INNER JOIN Order_Details OD"
                    + " ON PR.ProductID = OD.ProductID)"
                    + " INNER JOIN Orders O ON OD.OrderID = O.OrderID"
                    + " WHERE O.OrderDate > ? AND O.OrderDate <= ?)";
            PreparedStatement stmt = getConnection().prepareStatement(sql);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");
            Date date = new Date(formatter.parse(year+"-"+month+"-"+day).getTime());
            Date dateBefore = new Date(formatter.parse(year+"-"+month+"-"+day).getTime()-7*24*60*60*1000);
            stmt.setDate(1, dateBefore);
            stmt.setDate(2, date);
            System.out.println(stmt.toString());
            return stmt.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
