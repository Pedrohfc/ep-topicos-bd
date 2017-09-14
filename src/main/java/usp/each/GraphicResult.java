package usp.each;

import java.util.ArrayList;
import java.util.HashMap;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import java.util.Map;
import javafx.scene.Node;
import javafx.scene.chart.Axis;
import javafx.scene.text.Text;

public class GraphicResult extends Application {
	
	static ArrayList<String> colunas;
	static ArrayList<Double> values;

    public static void main(ArrayList<String> colunas_, ArrayList<Double> values_) {
    	colunas = colunas_;
    	values = values_;
        Application.launch(null);
    }

	@Override
	public void start(Stage stage) throws Exception {
		
		stage.setTitle("Resultado da Consulta");
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        final BarChartExt<String,Number> bc = new BarChartExt<String,Number>(xAxis,yAxis);
        xAxis.setLabel("Dias");       
        yAxis.setLabel("Vendas");
 
        XYChart.Series series1 = new XYChart.Series();
        series1.setName("Qtd de Vendas"); 
        
        for(int i = 0; i < values.size(); i++){
        	series1.getData().add(new XYChart.Data(colunas.get(i), values.get(i)));
        }
       
        
        Scene scene  = new Scene(bc,800,600);
        bc.getData().addAll(series1);
        stage.setScene(scene);
        
        stage.setOnCloseRequest(this.getCloseSystemEvent());
        
        stage.show();
		
	}
	
	private static class BarChartExt<X, Y> extends BarChart<X, Y> {

        /**
         * Registry for text nodes of the bars
         */
        Map<Node, Node> nodeMap = new HashMap<>();

        public BarChartExt(Axis xAxis, Axis yAxis) {
            super(xAxis, yAxis);
        }

        /**
         * Add text for bars
         */
        @Override
        protected void seriesAdded(Series<X, Y> series, int seriesIndex) {

            super.seriesAdded(series, seriesIndex);

            for (int j = 0; j < series.getData().size(); j++) {

                Data<X, Y> item = series.getData().get(j);

                Node text = new Text(String.valueOf(item.getYValue()));
                nodeMap.put(item.getNode(), text);
                getPlotChildren().add(text);

            }

        }

        /**
         * Remove text of bars
         */
        @Override
        protected void seriesRemoved(final Series<X, Y> series) {

            for (Node bar : nodeMap.keySet()) {

                Node text = nodeMap.get(bar);
                getPlotChildren().remove(text);

            }

            nodeMap.clear();

            super.seriesRemoved(series);
        }

        /**
         * Adjust text of bars, position them on top
         */
        @Override
        protected void layoutPlotChildren() {

            super.layoutPlotChildren();

            for (Node bar : nodeMap.keySet()) {

                Node text = nodeMap.get(bar);

                text.relocate(bar.getBoundsInParent().getMinX(), bar.getBoundsInParent().getMinY() - 30);

            }

        }
    }
	
	public EventHandler<WindowEvent> getCloseSystemEvent() {
	    return new EventHandler<WindowEvent>() {
	        @Override
	        public void handle(WindowEvent event) {
	            Platform.exit();
	        }
	    };
	}
	    
}
