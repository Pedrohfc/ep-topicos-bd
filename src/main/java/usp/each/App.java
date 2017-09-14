package usp.each;

import java.sql.ResultSet;

import javax.swing.JFrame;

import usp.each.task.ConnectionFactory;

public class App {
    public static void main(String[] args) {
        new App().startApp();
    }

    public void startApp()
    {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> closeApp()));
        
        JFrame frame = new JFrame("Tópicos de BD - EP1");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().add(new ClientGUI());
        frame.setSize(506, 430);
        frame.setVisible(true);
        
    }

    public void closeApp() {
        ConnectionFactory.closeConnection();
    }

    
}
