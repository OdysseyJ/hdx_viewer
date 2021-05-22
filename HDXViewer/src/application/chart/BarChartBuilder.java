package application.chart;

import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

public class BarChartBuilder {
	@SuppressWarnings("rawtypes")
	BarChart _barChart;
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public BarChartBuilder() {
		CategoryAxis xAxis = new CategoryAxis();

		NumberAxis yAxis = new NumberAxis();
		
		BarChart barChart = new BarChart(xAxis, yAxis);
		
		barChart.setLegendVisible(false);
		
		XYChart.Series dataSeries1 = new XYChart.Series();

		dataSeries1.getData().add(new XYChart.Data("Desktop", 178));
		dataSeries1.getData().add(new XYChart.Data("Phone"  , 65));
		dataSeries1.getData().add(new XYChart.Data("Tablet"  , 23));


		barChart.getData().add(dataSeries1);
		
		_barChart = barChart;
	}
	
	public BarChart getBarChart(){
		return this._barChart;
	}
}
