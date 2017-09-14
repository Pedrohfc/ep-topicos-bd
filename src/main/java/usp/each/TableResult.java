package usp.each;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class TableResult extends JFrame {
	
	public TableResult(String[] columns, String[][] data){
		
		//create table with data
        JTable table = new JTable(data, columns);
        table.setShowGrid(true);
        table.setGridColor(Color.BLACK);

        
        //add the table to the frame
        this.add(new JScrollPane(table));
         
        this.setTitle("Resultado da Consulta");      
        this.pack();
        this.setVisible(true);
        
    }
	    
}
