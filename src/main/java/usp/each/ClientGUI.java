package usp.each;

import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFormattedTextField;
import javax.swing.text.MaskFormatter;
import javax.swing.JSeparator;

import usp.each.task.OrderTask;


public class ClientGUI extends JPanel {

	/**
	 * Create the panel.
	 */
	
	static GraphicResult gr;
	
	static int consultaSelecionada;
	static JComboBox<String> cboConsultas;
	static JComboBox<String> cboAno;
	static JComboBox<String> cboMes;
	static JComboBox<String> cboDia;
	JScrollPane scrollPane;

	JLabel lblTitle;
	JLabel lblConsulta;
	JLabel lblFiltros;
	JLabel lblAno;
	JLabel lblMes;
	JLabel lblDia;
	
	static JButton btnExecutarConsulta;
	
	static Map<Integer, String> listConsultas = new HashMap<Integer, String>();
	//static Map<Integer, String> listTipoDados = new HashMap<Integer, String>();
	//static Map<Integer, String> listAgrupamento = new HashMap<Integer, String>();
	
	
	public ClientGUI() {
		
		setLayout(null);
		
		lblTitle = new JLabel("Tópicos Especiais em BD - Parte 1", SwingConstants.CENTER);
		lblTitle.setBounds(6, 6, 489, 81);
		lblTitle.setFont(new Font("Serif", Font.BOLD, 20));
		add(lblTitle);
		
		lblConsulta = new JLabel("Consultas disponíveis:");
		lblConsulta.setBounds(28, 85, 453, 27);
		add(lblConsulta);
		
		cboConsultas = new JComboBox();
		cboConsultas.setBounds(21, 113, 460, 27);
		cboConsultas.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				consultaSelecionada = interpretarConsultas((String) cboConsultas.getSelectedItem());
				//System.out.println(consultaSelecionada);
				defineFiltros();
				
			}
		});
		add(cboConsultas);
		
		lblFiltros = new JLabel("Filtros disponíveis:");
		lblFiltros.setBounds(28, 152, 453, 27);
		add(lblFiltros);

		lblAno = new JLabel("Ano:");
		lblAno.setBounds(28, 191, 112, 27);
		add(lblAno);
		
		cboAno = new JComboBox();
		cboAno.setBounds(145, 191, 336, 27);
		add(cboAno);
		
		lblMes = new JLabel("Mês:");
		lblMes.setBounds(28, 230, 112, 27);
		add(lblMes);
		
		cboMes = new JComboBox();
		cboMes.setBounds(145, 230, 336, 27);
		add(cboMes);
		
		lblDia = new JLabel("Dia:");
		lblDia.setBounds(28, 270, 112, 27);
		add(lblDia);
		
		cboDia = new JComboBox();
		cboDia.setBounds(145, 270, 336, 27);
		add(cboDia);
		
		btnExecutarConsulta = new JButton("Executar Consulta");
		btnExecutarConsulta.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					
					OrderTask task = new OrderTask();
		            String year = "", month = "", day = "";
					
		            try {
			            year = (String) cboAno.getSelectedItem();
			            
					} catch (Exception e2) {}
		            
		            try {
			            month = (String) cboMes.getSelectedItem();
					} catch (Exception e2) {}

					try {
			            day = (String) cboDia.getSelectedItem();
					} catch (Exception e2) {}
		            
					//System.out.println("Ano: " + year);
					//System.out.println("Mes: " + month);
					//System.out.println("Dia: " + day);
					ResultSet rs = null;
					
					switch (consultaSelecionada) {
					case 1:
						rs = task.ordersByDayOnMonthConsideringSellsAsProductsSoldQuantity(Integer.parseInt(month), Integer.parseInt(year));
						printQuery(rs, false);
						break;
					case 2:
						rs = task.rankOrdersByDayOnMonthConsideringSellsAsProductsSoldQuantity(Integer.parseInt(month), Integer.parseInt(year));
						printQuery(rs, false);
						break;
					case 3:
						rs = task.rankOrdersByDayOnMonthConsideringSellsAsProductsSoldQuantity(Integer.parseInt(month), Integer.parseInt(year));
						printQuery(rs, true);
						break;
					case 4:
						rs = task.topRankProductsByDayOnMonth(Integer.parseInt(day), Integer.parseInt(month), Integer.parseInt(year));
						printQuery(rs, false);
						break;
					case 5:
						rs = task.downRankProductsByDayOnMonth(Integer.parseInt(day), Integer.parseInt(month), Integer.parseInt(year));
						printQuery(rs, false);
						break;
					case 6:
						rs = task.topAccumulatedRankProductsByDayOnMonth(Integer.parseInt(month), Integer.parseInt(year));
						printQuery(rs, false);
						break;
					case 7:
						rs = task.downRankAccumulatedProductsByDayOnMonth(Integer.parseInt(month), Integer.parseInt(year));
						printQuery(rs, false);
						break;
					case 8:
						rs = task.topRankCategoriesByDayOnMonth(Integer.parseInt(day), Integer.parseInt(month), Integer.parseInt(year));
						printQuery(rs, false);
						break;
					case 9:
						rs = task.downRankCategoriesByDayOnMonth(Integer.parseInt(day), Integer.parseInt(month), Integer.parseInt(year));
						printQuery(rs, false);
						break;
					case 10:
						rs = task.topAccumulatedRankCategoriesByDayOnMonth(Integer.parseInt(month), Integer.parseInt(year));
						printQuery(rs, false);
						break;
					case 11:
						rs = task.downRankAccumulatedCategoriesByDayOnMonth(Integer.parseInt(month), Integer.parseInt(year));
						printQuery(rs, false);
						break;
					case 12:
					    ResultSet productsNotSold = task.productsNotSold(Integer.parseInt(day), Integer.parseInt(month), Integer.parseInt(year));
					    printQuery(productsNotSold, false);
					    break;

					default:
						break;
					}
					
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				
			}
		});
		btnExecutarConsulta.setBounds(168, 326, 190, 29);
		add(btnExecutarConsulta);
		
		carregarConsultas();
		carregarFiltros();
		consultaSelecionada = 1;
		defineFiltros();
		
	}
	
	public static int interpretarConsultas(String consulta){
		
		for (Map.Entry<Integer, String> entry : listConsultas.entrySet()){
		    
			if(entry.getValue().equals(consulta)){
				return entry.getKey();
			}
			
		}
		return 0;
		
	}
	
	public static void showErrorMessage(String title, String message){
		JFrame frame = new JFrame("Erro - " + title);
		JOptionPane.showMessageDialog(frame, message, "Erro" + title, JOptionPane.ERROR_MESSAGE);
	}
	
	public static void carregarConsultas(){
		
		listConsultas.put(1, "Vendas acumuladas de cada dia em um mês");
		listConsultas.put(2, "Ranking de vendas acumuladas por dia em um mês");
		listConsultas.put(3, "Gráfico de vendas acumuladas por dia em um mês");
		listConsultas.put(4, "Top 10 diário por produto");
		listConsultas.put(5, "Down 10 diário por produto");
		listConsultas.put(6, "Top 10 acumulada por produto");
		listConsultas.put(7, "Down 10 acumulada por produto");
		listConsultas.put(8, "Ranking Top 10 por categoria diário");
		listConsultas.put(9, "Ranking Down 10 por categoria diário");
		listConsultas.put(10, "Ranking Top 10 por categoria acumulado");
		listConsultas.put(11, "Ranking Down 10 por categoria acumulado");
		listConsultas.put(12, "Produtos sem venda a mais de 7 dias");
		
		cboConsultas.removeAllItems();
		
		for (Map.Entry<Integer, String> entry : listConsultas.entrySet())
		{
		    //System.out.println(entry.getKey() + "/" + entry.getValue());
			cboConsultas.addItem(entry.getValue());
		}
		
	}
	
	public static void defineFiltros(){
		
		switch (consultaSelecionada) {
		case 1:
			cboAno.setEnabled(true);
			cboMes.setEnabled(true);
			cboDia.setEnabled(false);
			break;
		case 2:
			cboAno.setEnabled(true);
			cboMes.setEnabled(true);
			cboDia.setEnabled(false);
			break;
		case 3:
			cboAno.setEnabled(true);
			cboMes.setEnabled(true);
			cboDia.setEnabled(false);
			break;
		case 4:
			cboAno.setEnabled(true);
			cboMes.setEnabled(true);
			cboDia.setEnabled(true);
			break;
		case 5:
			cboAno.setEnabled(true);
			cboMes.setEnabled(true);
			cboDia.setEnabled(true);
			break;
		case 6:
			cboAno.setEnabled(true);
			cboMes.setEnabled(true);
			cboDia.setEnabled(false);
			break;
		case 7:
			cboAno.setEnabled(true);
			cboMes.setEnabled(true);
			cboDia.setEnabled(false);
			break;
		case 8:
			cboAno.setEnabled(true);
			cboMes.setEnabled(true);
			cboDia.setEnabled(true);
			break;
		case 9:
			cboAno.setEnabled(true);
			cboMes.setEnabled(true);
			cboDia.setEnabled(true);
			break;
		case 10:
			cboAno.setEnabled(true);
			cboMes.setEnabled(true);
			cboDia.setEnabled(false);
			break;
		case 11:
			cboAno.setEnabled(true);
			cboMes.setEnabled(true);
			cboDia.setEnabled(false);
			break;
		case 12:
			cboAno.setEnabled(true);
			cboMes.setEnabled(true);
			cboDia.setEnabled(true);
			break;
		default:
			break;
		}
		
	}
	
	public void printQuery(ResultSet rs, boolean graph) throws Exception {
        
        String[] columnNames = new String[rs.getMetaData().getColumnCount()];
        
        for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
        	columnNames[i] = rs.getMetaData().getColumnName((i+1));
        }
        
        
        ArrayList<String[]> result = new ArrayList<>();
        String[] rowResult = new String[rs.getMetaData().getColumnCount()];
        
        while(rs.next()){
        	for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
            	rowResult[i] = rs.getString((i+1));
            }
        	result.add(rowResult);
            rowResult = new String[rs.getMetaData().getColumnCount()];
        }
        
        String[][] data = new String[result.size()][rs.getMetaData().getColumnCount()];
        
        for(int i = 0; i < result.size(); i++){
        	String[] g = result.get(i);
        	for(int l = 0; l < g.length; l++){
        		data[i][l] = g[l];
        	}
        }
        
        if(graph){
        	gr = new GraphicResult();
        	
        	ArrayList<String> colums = new ArrayList<>();
        	
        	ArrayList<Double> result2 = new ArrayList<>();
        	for(int i = 0; i < result.size(); i++){
        		String[] g = result.get(i);
        		colums.add(g[1]);
        		result2.add(Double.valueOf(g[2]));
        	}
        	
            gr.main(colums, result2);
        }else{
            TableResult tr = new TableResult(columnNames, data);
        }
        
    }
	
	public static void carregarFiltros(){
		
		btnExecutarConsulta.setEnabled(false);
		
		OrderTask task = new OrderTask();
		ResultSet rs = null;
		
		try {
			
			rs = task.getDistinctDateOrders("year");
			while (rs.next()) {
	            for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
	                cboAno.addItem(rs.getString(i));
	            }
	        }
			
			rs = task.getDistinctDateOrders("month");
			while (rs.next()) {
	            for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
	                cboMes.addItem(rs.getString(i));
	            }
	        }
			
			rs = task.getDistinctDateOrders("day");
			while (rs.next()) {
	            for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
	                cboDia.addItem(rs.getString(i));
	            }
	        }
	        
	        btnExecutarConsulta.setEnabled(true);
	        
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
	}
}
