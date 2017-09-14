package usp.each;

import java.sql.ResultSet;

import usp.each.task.ConnectionFactory;
import usp.each.task.OrderTask;

public class App {
    public static void main(String[] args) {
        new App().startApp();
    }

    public void startApp() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> closeApp()));
        runTestQueries();
    }

    public void closeApp() {
        ConnectionFactory.closeConnection();
        System.out.println("Tchau!");
    }

    public void runTestQueries() {
        try {
            OrderTask task = new OrderTask();
            int month = 10;
            int year = 1997;
            int day = 10;
//            printQuery(task.ordersByDayOnMonth(month, year));
//            printQuery(task.rankByDayOnMonth(month, year));
//            printQuery(task.topTenProducts(day, month, year));
            printQuery(task.downRankCategoriesByDayOnMonth(day, month, year));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void printQuery(ResultSet rs) throws Exception {
        for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
            System.out.print(rs.getMetaData().getColumnName(i) + " ");
        }

        System.out.println("\n-----------");

        while (rs.next()) {
            for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                System.out.print(rs.getString(i) + " ");
            }

            System.out.println();
        }
    }
}
