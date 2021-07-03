package application.chart;

import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

public class LineChartBuilder {
	@SuppressWarnings("rawtypes")
	LineChart _lineChart;
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public LineChartBuilder() {
		CategoryAxis xAxis = new CategoryAxis();

		NumberAxis yAxis = new NumberAxis();
		
		LineChart lineChart = new LineChart(xAxis, yAxis);
		
		lineChart.setLegendVisible(false);
		
		XYChart.Series dataSeries1 = new XYChart.Series();

		dataSeries1.getData().add(new XYChart.Data("Desktop", 178));
		dataSeries1.getData().add(new XYChart.Data("Phone"  , 65));
		dataSeries1.getData().add(new XYChart.Data("Tablet"  , 23));


		lineChart.getData().add(dataSeries1);
		
		_lineChart = lineChart;
	}
	
	public LineChart getLineChart(){
		return this._lineChart;
	}
}
