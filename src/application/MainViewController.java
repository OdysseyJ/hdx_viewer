package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.xml.stream.XMLStreamException;

import org.systemsbiology.jrap.stax.MSXMLSequentialParser;
import org.systemsbiology.jrap.stax.Scan;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

public class MainViewController implements Initializable {
	
	DecimalFormat df = new DecimalFormat("0");
	
	ObservableList<HDXProfile> recordList = FXCollections.observableArrayList();

	ArrayList<DdeuAnal> ddeuList = new ArrayList<DdeuAnal>();
	
	ArrayList<Scan> ctrlList;
	
	ArrayList<ArrayList<Scan>> conditionLists = new ArrayList<ArrayList<Scan>>();
	
	private ArrayList<File> files = new ArrayList<File>();
	
	ArrayList<String> conditions = new ArrayList<String>();
	
	int currentConditionIndex = 0;
	
	int currentRecordIndex = 0;
	
	int currentScan_top = 0;
	
	int currentScan_bottom = 0;
	
	int currentSize_top = 0;
	
	int currentSize_bottom = 0;

    @FXML
    private MenuItem open;

    @FXML
    private TreeView<String> treeview;

    @FXML
    private LineChart<String,Number> linechart;
    
    @FXML
    private Button peptide_view;
    
    @FXML
    private NumberAxis predict_xAxis ;

    @FXML
    private NumberAxis predict_yAxis ;
    
    @FXML
    private NumberAxis result_xAxis ;

    @FXML
    private NumberAxis result_yAxis ;
    
    @FXML
    private NumberAxis control_xAxis ;

    @FXML
    private NumberAxis control_yAxis ;
    
    @FXML
    private LineChart<Number, Number> control;
    
    @FXML
    private LineChart<Number, Number> result;
    
    @FXML
    private LineChart<Number, Number> predict;

    @FXML
    private TableView<HDXProfile> tableview;
    
    @FXML
    private MenuButton menu_button;
    
    @FXML
    private Button sizedown_top;
    
    @FXML
    private Button sizeup_top;
    
    @FXML
    private Button sizedown_bottom;
    
    @FXML
    private Button sizeup_bottom;
    
    @FXML
    private Button nextScan_top;
    
    @FXML
    private Button prevScan_top;
    
    @FXML
    private Button nextScan_bottom;
    
    @FXML
    private Button prevScan_bottom;
    
    @FXML
    private Label scanLabel_top;
    
    @FXML
    private Label sizeLabel_top;
    
    @FXML
    private Label scanLabel_bottom;
    
    @FXML
    private Label sizeLabel_bottom;

    @FXML
    void onClickOpen(ActionEvent event) {
    	Stage stage = Main.getPrimaryStage();
    	showFileModal(stage);
    }
    
    private void showFileModal(Stage parentStage) {
    	try {
    	Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("FileSelectView.fxml"));
        stage.setScene(new Scene(root, 600, 400));
        stage.setTitle("file select");
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(parentStage);
        stage.showAndWait();
    	}
    	catch(Exception e){
    		System.out.println(e);
    	}
    }
    
    @FXML
    void onClickTreeView(MouseEvent event) {

    }

    @FXML
    void onClickTableView(MouseEvent event) {

    }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		Main.mainViewController = this;
	}
	
	// ------------------------------tree view------------------
	public void setTreeItem(ArrayList<File> files) {
		try {
			this.files = files;
			TreeItem<String> Root = new TreeItem<String>("Project");
			if( files.size() > 0 ) {
				TreeItem<String> newItem = new TreeItem<String>("Ctrl");
				for (int i = 0; i < files.size(); i++) {
					String condition = files.get(i).getName().replaceFirst("[.][^.]+$", "");
					TreeItem<String> child = new TreeItem<String>(condition);
					newItem.getChildren().add(child);
				}
				Root.getChildren().add(newItem);
			}
	    	treeview.setRoot(Root);
		} catch(Exception e) {
			
		}
	}

	// ------------------------------line chart------------------
	public void setLineChartData(int index) {

		linechart.getData().clear();
		XYChart.Series series = new XYChart.Series();
		
		for(int i = 0 ; i < files.size() ; i++) {
			String HDXnum = recordList.get(index).getCondition(i);
			
			String[] splited = HDXnum.split("~");

			Double d = 0.0;
			
			for(int j = 0 ; j < splited.length ; j++) {
				if(splited[j].equals("-"))
					break;
				d += Double.parseDouble(splited[j]);
			}
			
			d /= splited.length;
			series.getData().add(new XYChart.Data(files.get(i).getName().replaceFirst("[.][^.]+$", ""), d));
		}
		
		linechart.getData().setAll(series);
	}
	
	public void setScanData(ArrayList<ArrayList<Scan>> file_scans) {
		ctrlList = file_scans.get(0);
		for (int i = 1; i < file_scans.size(); i++) {
			conditionLists.add(file_scans.get(i));
		}
	}
	
	public void setInitialBarChartData(int index) {
		HDXProfile profile = recordList.get(index);
        String apexScan = profile.getApexScan();
        String peptide = profile.getPeptide();
        String mz = profile.getMz();
        int scanNum = Integer.parseInt(apexScan);
        
        Double minMass = getMinMass(mz, currentSize_top);
        Double maxMass = getMaxMass(minMass, peptide, currentSize_top);
        int tick = 1;
        int defaultConditionIndex = 0;
        
        currentRecordIndex = index;
        currentConditionIndex = 0;
        currentSize_top = 0;
        currentSize_bottom = 0;
        sizeLabel_top.setText("size : " + Integer.toString(currentSize_top));
        sizeLabel_bottom.setText("size : " +Integer.toString(currentSize_bottom));
        currentScan_top = Integer.parseInt(apexScan);
        currentScan_bottom = Integer.parseInt(apexScan);
        scanLabel_top.setText("scan : " + Integer.toString(currentScan_top));
        scanLabel_bottom.setText("scan : " +Integer.toString(currentScan_bottom));
        
        sizeup_top.setOnAction(new EventHandler<ActionEvent>() { 
        	@Override public void handle(ActionEvent e) {
        		if (currentSize_top < 5) {
	        		currentSize_top += 1;
	                sizeLabel_top.setText("size : " + Integer.toString(currentSize_top));
	                Double minMass = getMinMass(mz, currentSize_top);
	                Double maxMass = getMaxMass(minMass, peptide, currentSize_top);
	                int tick = getTick(currentSize_top);
	        		setTopIntensityData(currentScan_top, minMass, maxMass, tick);
        		}
        	}
        });
        
        sizedown_top.setOnAction(new EventHandler<ActionEvent>() { 
        	@Override public void handle(ActionEvent e) {
        		if (currentSize_top > 0) {
	        		currentSize_top -= 1;
	                sizeLabel_top.setText("size : " + Integer.toString(currentSize_top));
	                Double minMass = getMinMass(mz, currentSize_top);
	                Double maxMass = getMaxMass(minMass, peptide, currentSize_top);
	                int tick = getTick(currentSize_top);
	        		setTopIntensityData(currentScan_top, minMass, maxMass, tick);
        		}
        	}
        });
        
        sizeup_bottom.setOnAction(new EventHandler<ActionEvent>() { 
        	@Override public void handle(ActionEvent e) {
        		if (currentSize_bottom < 5) {
	        		currentSize_bottom += 1;
	                sizeLabel_bottom.setText("size : " + Integer.toString(currentSize_bottom));
	                Double minMass = getMinMass(mz, currentSize_bottom);
	                Double maxMass = getMaxMass(minMass, peptide, currentSize_bottom);
	                int tick = getTick(currentSize_bottom);
	                setBottomIntensityData(currentScan_bottom, currentConditionIndex, minMass, maxMass, tick);
        		}
        	}
        });
        
        sizedown_bottom.setOnAction(new EventHandler<ActionEvent>() { 
        	@Override public void handle(ActionEvent e) {
        		if (currentSize_bottom > 0) {
	        		currentSize_bottom -= 1;
	                sizeLabel_bottom.setText("size : " + Integer.toString(currentSize_bottom));
	                Double minMass = getMinMass(mz, currentSize_bottom);
	                Double maxMass = getMaxMass(minMass, peptide, currentSize_bottom);
	                int tick = getTick(currentSize_bottom);
	                setBottomIntensityData(currentScan_bottom, currentConditionIndex, minMass, maxMass, tick);
        		}
        	}
        });
        
        nextScan_top.setOnAction(new EventHandler<ActionEvent>() { 
        	@Override public void handle(ActionEvent e) {
        		currentScan_top += 1;
	            scanLabel_top.setText("scan : " + Integer.toString(currentScan_top));
	            Double minMass = getMinMass(mz, currentSize_top);
	            Double maxMass = getMaxMass(minMass, peptide, currentSize_top);
	            int tick = getTick(currentSize_top);
	            setTopIntensityData(currentScan_top, minMass, maxMass, tick);
        	}
        });
        
        prevScan_top.setOnAction(new EventHandler<ActionEvent>() { 
        	@Override public void handle(ActionEvent e) {
        		currentScan_top -= 1;
	            scanLabel_top.setText("scan : " + Integer.toString(currentScan_top));
	            Double minMass = getMinMass(mz, currentSize_top);
	            Double maxMass = getMaxMass(minMass, peptide, currentSize_top);
	            int tick = getTick(currentSize_top);
	            setTopIntensityData(currentScan_top, minMass, maxMass, tick);
        	}
        });
        
        nextScan_bottom.setOnAction(new EventHandler<ActionEvent>() { 
        	@Override public void handle(ActionEvent e) {
        		currentScan_bottom += 1;
	            scanLabel_bottom.setText("scan : " + Integer.toString(currentScan_bottom));
	            Double minMass = getMinMass(mz, currentSize_bottom);
	            Double maxMass = getMaxMass(minMass, peptide, currentSize_bottom);
	            int tick = getTick(currentSize_bottom);
	            setBottomIntensityData(currentScan_bottom, currentConditionIndex, minMass, maxMass, tick);
        	}
        });
        
        prevScan_bottom.setOnAction(new EventHandler<ActionEvent>() { 
        	@Override public void handle(ActionEvent e) {
        		currentScan_bottom -= 1;
	            scanLabel_bottom.setText("scan : " + Integer.toString(currentScan_bottom));
	            Double minMass = getMinMass(mz, currentSize_bottom);
	            Double maxMass = getMaxMass(minMass, peptide, currentSize_bottom);
	            int tick = getTick(currentSize_bottom);
	            setBottomIntensityData(currentScan_bottom, currentConditionIndex, minMass, maxMass, tick);
        	}
        });
		
		setChartxAxis(control_xAxis, tick, Math.floor(minMass), Math.ceil(maxMass));
		
		setTopIntensityData(scanNum, minMass, maxMass, tick);
		setBottomIntensityData(scanNum, defaultConditionIndex, minMass, maxMass, tick);
		menu_button.setText(conditions.get(defaultConditionIndex));
	}
	
	public void setTopIntensityData(int scanNum, Double minMass, Double maxMass, int tick) {
		Scan ctrl_scan = ctrlList.get(scanNum);
		double[][] intensityList = ctrl_scan.getMassIntensityList();
		XYChart.Series series = getIntensitySeries(intensityList, minMass, maxMass);
		control.getData().setAll(series);
		setChartxAxis(control_xAxis, tick, Math.floor(minMass), Math.ceil(maxMass));
	}
	
	public XYChart.Series getIntensitySeries(double[][] intensityList, Double minMass, Double maxMass) {
		XYChart.Series dataSeries = new XYChart.Series();
		for(int i = 0; i < intensityList[0].length; i++) {
			Double raw_mass = intensityList[0][i];
			Double intensity = intensityList[1][i];
			if (raw_mass > maxMass) {
				break;
			}
			dataSeries.getData().add(new XYChart.Data(raw_mass-0.0001, 0));
			dataSeries.getData().add(new XYChart.Data(raw_mass, intensity));
			dataSeries.getData().add(new XYChart.Data(raw_mass+0.0001, 0));	
		}
		return dataSeries;
	}
	
	public void setChartxAxis(NumberAxis xAxis, int tick, Double min, Double max) {
		xAxis.setAutoRanging(false);
		xAxis.setLowerBound(min);
		xAxis.setUpperBound(max);
		xAxis.setTickUnit(tick);
	}
	
	public void setChartyAxis(NumberAxis yAxis, int tick, Double min, Double max) {
		yAxis.setAutoRanging(false);
		yAxis.setLowerBound(min);
		yAxis.setUpperBound(max);
		yAxis.setTickUnit(tick);
	}
	
	public XYChart.Series getPredictSeries(String[] array, Double minMz, Double additionalMz, Double maxIntensity, Double maxIntensityMass, String name) {
		XYChart.Series dataSeries = new XYChart.Series();
		Double minDiff = Double.MAX_VALUE;
		Double nearestMz = 0.0;
		Double nearestIntensity = 0.0;
		for(int i = 0; i < array.length; i++) {
			if (array[i].equals("-")) {
				break;
			}
			Double x = minMz + (additionalMz * i);
			Double diff = Math.abs(maxIntensityMass - x);
			if (diff < minDiff) {
				minDiff = diff;
				nearestMz = x;
				nearestIntensity = Double.parseDouble(array[i]);
			}
		}
		Double fittingValue = maxIntensity / nearestIntensity;
		for(int i = 0; i < array.length; i++) {
			if (array[i].equals("-")) {
				break;
			}
			Double y = Double.parseDouble(array[i]) * fittingValue;
			Double x = minMz + (additionalMz * i);
			dataSeries.getData().add(new XYChart.Data(x, y));
		}
		dataSeries.setName(name);
		
		return dataSeries;
	}
	
	public void setBottomIntensityData(int scanNum, int conditionIndex, Double minMass, Double maxMass, int tick) {
		// ---- get data ----
		HDXProfile profile = recordList.get(currentRecordIndex);
        String peptide = profile.getPeptide();
        String condition = conditions.get(conditionIndex);
        String d2o_label = condition.split("_")[1];
        Double charge = Double.parseDouble(profile.getCharge());
        Double mz = Double.parseDouble(profile.getMz());
        
        String predictedDdeu = "";
        String observedDdeu = "";
        
        for (int i = 0; i  < ddeuList.size(); i++) {
        	DdeuAnal data = ddeuList.get(i);
        	String data_peptide = data.getPeptide();
        	String data_d2o_label = data.getD2OLabelfirst();
        	if (data_peptide.equals(peptide) && data_d2o_label.equals(d2o_label)) {
        		predictedDdeu = data.getPredictedDdeu();
        		observedDdeu = data.getObservedDdeu();
        		break;
        	}
        }
        
		ArrayList<Scan> conditionScans = conditionLists.get(conditionIndex);
		Scan scan = conditionScans.get(scanNum);
		double[][] intensityList = scan.getMassIntensityList();

		String[] predictedDdeu_array = predictedDdeu.split(";");
		String[] observedDdeu_array = observedDdeu.split(";");
		
		// --- set data ---
		
		XYChart.Series dataSeries1 = getIntensitySeries(intensityList, minMass, maxMass);
		
		Double standard_value = 1.0;
		Double additional_value = standard_value/charge;
		Double maxMz = mz + (additional_value * peptide.length());
		String intensity = getMaxIntensity(intensityList, mz, maxMz);
		Double maxIntensity = Double.parseDouble(intensity.split(",")[0]);
		Double maxIntensityMass = Double.parseDouble(intensity.split(",")[1]);
		
//		XYChart.Series dataSeries2 = getPredictSeries(predictedDdeu_array, mz, additional_value,  maxIntensity, maxIntensityMass, "predicted");
//		XYChart.Series dataSeries3 = getPredictSeries(observedDdeu_array, mz, additional_value, maxIntensity, maxIntensityMass, "observed");
		
	    setChartxAxis(result_xAxis, tick, Math.floor(minMass), Math.ceil(maxMass));
	    setChartyAxis(result_yAxis, 100, 0.0, (Math.ceil(maxIntensity/100)*100)+300);
	   
	    setChartxAxis(predict_xAxis, tick, Math.floor(minMass), Math.ceil(maxMass));
	    setChartyAxis(predict_yAxis, 100, 0.0, (Math.ceil(maxIntensity/100)*100)+300);

	    result.getData().setAll(dataSeries1);
//		predict.getData().setAll(dataSeries2, dataSeries3);
	}
	
	public String getMaxIntensity(double[][] intensityList, Double minMz, Double maxMz) {
		Double maxIntensity = 0.0;
		Double maxIntensityMass = minMz;
		String result = maxIntensity.toString()+","+maxIntensityMass.toString();
		for(int i = 0; i < intensityList[0].length; i++) {
			Double raw_mass = intensityList[0][i];
			Double intensity = intensityList[1][i];
			if (raw_mass > maxMz) {
				break;
			}
			if (raw_mass > minMz && maxIntensity < intensity) {
				maxIntensity = intensity;
				result = intensity.toString()+","+raw_mass.toString();
			}
		}
		return result;
	}
	
	public int getTick(int currentSize) {
		return 1 + (5 * currentSize);
	}
	
	public Double getMinMass(String mz, int currentSize) {
		Double value = Double.parseDouble(mz) - 1.0 - (400*currentSize);
		if (value < 0) {
			return 0.0;
		}
		else {
			return value;
		}
	}
	
	public Double getMaxMass(Double minMass, String peptide, int currentSize) {
		return minMass + peptide.length() + (400*currentSize);
	}
	
	// ------------------------------Ddeu Data --------------------
	public void setDdeuData(ArrayList<DdeuAnal> ddeuAnals) {
		ddeuList.addAll(ddeuAnals);
	}
	
	// ------------------------------Table View---------------------

	public void setTableViewData(ArrayList<HDXProfile> profileList, ArrayList<File> files) {
		menu_button.getItems().clear();
		for (int i = 0; i < files.size(); i++) {
			File file = files.get(i);
			MenuItem item = new MenuItem(file.getName().replaceFirst("[.][^.]+$", ""));
			item.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent event) {
					MenuItem selected = (MenuItem) event.getSource();
					menu_button.setText(selected.getText());
					int index = conditions.indexOf(selected.getText());
					currentConditionIndex = index;
					HDXProfile profile = recordList.get(currentRecordIndex);
			        String peptide = profile.getPeptide();
			        String mz = profile.getMz();
			        Double minMass = getMinMass(mz, currentSize_bottom);
		            Double maxMass = getMaxMass(minMass, peptide, currentSize_bottom);
		            int tick = getTick(currentSize_bottom);
			        setBottomIntensityData(currentScan_bottom, currentConditionIndex, minMass, maxMass, tick);
				}
			});
			menu_button.getItems().add(item);
			conditions.add(file.getName().replaceFirst("[.][^.]+$", ""));
		}
		
		recordList.setAll(FXCollections.observableArrayList(profileList));
        Callback<TableColumn<HDXProfile, String>, TableCell<HDXProfile, String>> stringCellFactory =
                new Callback<TableColumn<HDXProfile, String>, TableCell<HDXProfile, String>>() {
            @Override
            public TableCell<HDXProfile, String> call(TableColumn<HDXProfile, String> p) {
                StringTableCell cell = new StringTableCell();
                cell.addEventFilter(MouseEvent.MOUSE_CLICKED, new MyEventHandler());
                return cell;
            }
        };
        
        
		TableColumn<HDXProfile, String> column1 = new TableColumn<>("Id");
	    column1.setCellValueFactory(new PropertyValueFactory<HDXProfile, String>("id"));
	    column1.setCellFactory(stringCellFactory);
	    
	    TableColumn<HDXProfile, String> column2 = new TableColumn<>("Mz");
	    column2.setCellValueFactory(new PropertyValueFactory<>("mz"));
	    column2.setCellFactory(stringCellFactory);
	    
	    TableColumn<HDXProfile, String> column3 = new TableColumn<>("Charge");
	    column3.setCellValueFactory(new PropertyValueFactory<>("charge"));
	    column3.setCellFactory(stringCellFactory);
	    
	    TableColumn<HDXProfile, String> column4 = new TableColumn<>("Peptide");
	    column4.setCellValueFactory(new PropertyValueFactory<>("peptide"));
	    column4.setCellFactory(stringCellFactory);
	    
	    TableColumn<HDXProfile, String> column5 = new TableColumn<>("Protein");
	    column5.setCellValueFactory(new PropertyValueFactory<>("protein"));
	    column5.setCellFactory(stringCellFactory);

	    TableColumn<HDXProfile, String> column6 = new TableColumn<>("ExpMz");
	    column6.setCellValueFactory(new PropertyValueFactory<>("expMz"));
	    column6.setCellFactory(stringCellFactory);

	    TableColumn<HDXProfile, String> column7 = new TableColumn<>("MzShift");
	    column7.setCellValueFactory(new PropertyValueFactory<>("mzShift"));
	    column7.setCellFactory(stringCellFactory);
	    
	    tableview.getColumns().addAll(column1, column2, column3, column4, column5, column6, column7);
	    for(int i = 0 ; i < files.size() ; i++) {
	    	String condition = files.get(i).getName().replaceFirst("[.][^.]+$", "");
		    TableColumn<HDXProfile, String> column = new TableColumn<>(condition);
		    int idx = i ;
	    	column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCondition(idx)));
	    	
	    	column.setCellFactory(stringCellFactory);
	    	tableview.getColumns().add(column);
	    }

	    tableview.setItems(recordList);
	}
	  
	class StringTableCell extends TableCell<HDXProfile, String> {
	        @Override 
	        public void updateItem(String item, boolean empty) {
	            super.updateItem(item, empty);
	            setText(empty ? null : getString());
	            setGraphic(null);
	        }

	        private String getString() {
	            return getItem() == null ? "" : getItem().toString();
	        }
	 }
	
	 class MyEventHandler implements EventHandler<MouseEvent> {
	        @Override
	        public void handle(MouseEvent t) {
	            TableCell c = (TableCell) t.getSource();
	            int index = c.getIndex();
	            setLineChartData(index);
	            setInitialBarChartData(index);
	        }
	 }
}
