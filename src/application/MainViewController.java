package application;

import java.io.File;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;

import org.systemsbiology.jrap.stax.Scan;

import Peptide.IsotopicCluster;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

public class MainViewController implements Initializable {
	
	DecimalFormat df = new DecimalFormat("0");
	
	ObservableList<HDXProfile> recordList = FXCollections.observableArrayList();

	ArrayList<DdeuAnal> ddeuList = new ArrayList<DdeuAnal>();
	
	ArrayList<Scan> ctrlList;
	
	ArrayList<ArrayList<Scan>> conditionLists = new ArrayList<ArrayList<Scan>>();
	
	private ArrayList<ConditionFile> files = new ArrayList<ConditionFile>();
	
	ArrayList<String> conditions = new ArrayList<String>();
	
	int currentConditionIndex = 0;
	
	int currentRecordIndex = 0;
	
	int currentScan_top = 0;
	
	int currentScan_bottom = 0;
	
	int currentSize_top = 0;
	
	int currentSize_bottom = 0;

	int currentRowIndex = -1;

    @FXML
    private MenuItem open;
    
    @FXML
    private MenuItem run;

    @FXML
    private TreeView<String> treeview;

    @FXML
    private LineChart<String,Number> linechart;
    
    @FXML
    private Button peptide_view;
    
    @FXML
    private NumberAxis predict_xAxis;

    @FXML
    private NumberAxis predict_yAxis;
    
    @FXML
    private NumberAxis result_xAxis;

    @FXML
    private NumberAxis result_yAxis;
    
    @FXML
    private NumberAxis control_xAxis;

    @FXML
    private NumberAxis control_yAxis;
    
    @FXML
    private NumberAxis isotopic_xAxis;

    @FXML
    private NumberAxis isotopic_yAxis;
    
    @FXML
    private LineChart<Number, Number> control;
    
    @FXML
    private LineChart<Number, Number> result;
    
    @FXML
    private AreaChart<Number, Number> predict;
    
    @FXML
    private AreaChart<Number, Number> isotopic;

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
    
    // right labels
    
    @FXML
    private Label posFromLabel;
    
    @FXML
    private Label posToLabel;
    
    @FXML
    private Label expMzLabel;
    
    @FXML
    private Label mzShiftLabel;
    
    @FXML
    private Label upStartScanLabel;
    
    @FXML
    private Label upEndScanLabel;
    
    @FXML
    private Label apexScanLabel;

    @FXML
    private Label apexRtLabel;

    @FXML
    private Label firstDeuNumLabel;

    @FXML
    private Label firstDeuPercentLabel;

    @FXML
    private Label secondDeuNumLabel;

    @FXML
    private Label secondDeuPercentLabel;

    @FXML
    private Label downStartScanLabel;

    @FXML
    private Label downEndScanLabel;

    @FXML
    private Label startRtLabel;

    @FXML
    private Label endRtLabel;
    

    @FXML
    void onClickRun(ActionEvent event) {
    	Stage stage = Main.getPrimaryStage();
    	showRunModal(stage);
    }
    
    @FXML
    void onClickOpen(ActionEvent event) {
    	Stage stage = Main.getPrimaryStage();
    	showOpenModal(stage);
    }
    
    private void showRunModal(Stage parentStage) {
    	try {
    	Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("FileSelectView.fxml"));
        stage.setScene(new Scene(root, 600, 500));
        stage.setTitle("Run");
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(parentStage);
        stage.showAndWait();
    	}
    	catch(Exception e){
    		System.out.println(e);
    	}
    }
    
    private void showOpenModal(Stage parentStage) {
    	try {
    	Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("OpenProjectView.fxml"));
        stage.setScene(new Scene(root, 290, 220));
        stage.setTitle("Open");
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
	public void setTreeItem(ArrayList<ConditionFile> files, String project_name) {
		try {
			this.files = files;
			TreeItem<String> Root = new TreeItem<String>("Project");
			Root.setExpanded(true);
			if( files.size() > 0 ) {
				TreeItem<String> newItem = new TreeItem<String>(project_name);
				newItem.setExpanded(true);
				for (int i = 0; i < files.size(); i++) {
					String condition = files.get(i).getName();
					TreeItem<String> child = new TreeItem<String>(condition);
					child.setExpanded(true);
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
        Integer charge = Integer.parseInt(profile.getCharge());
        int scanNum = Integer.parseInt(apexScan);
        
        currentRecordIndex = index;
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
        		if (currentSize_top < 10) {
	        		currentSize_top += 1;
	                sizeLabel_top.setText("size : " + Integer.toString(currentSize_top));
	        		setTopIntensityData(currentScan_top);
        		}
        	}
        });
        
        sizedown_top.setOnAction(new EventHandler<ActionEvent>() { 
        	@Override public void handle(ActionEvent e) {
        		if (currentSize_top > 0) {
	        		currentSize_top -= 1;
	                sizeLabel_top.setText("size : " + Integer.toString(currentSize_top));
	        		setTopIntensityData(currentScan_top);
        		}
        	}
        });
        
        sizeup_bottom.setOnAction(new EventHandler<ActionEvent>() { 
        	@Override public void handle(ActionEvent e) {
        		if (currentSize_bottom < 10) {
	        		currentSize_bottom += 1;
	                sizeLabel_bottom.setText("size : " + Integer.toString(currentSize_bottom));
	                setBottomIntensityData(currentScan_bottom, currentConditionIndex, false);
        		}
        	}
        });
        
        sizedown_bottom.setOnAction(new EventHandler<ActionEvent>() { 
        	@Override public void handle(ActionEvent e) {
        		if (currentSize_bottom > 0) {
	        		currentSize_bottom -= 1;
	                sizeLabel_bottom.setText("size : " + Integer.toString(currentSize_bottom));
	                setBottomIntensityData(currentScan_bottom, currentConditionIndex, false);
        		}
        	}
        });
        
        nextScan_top.setOnAction(new EventHandler<ActionEvent>() { 
        	@Override public void handle(ActionEvent e) {
        		currentScan_top += 1;
	            scanLabel_top.setText("scan : " + Integer.toString(currentScan_top));
	            setTopIntensityData(currentScan_top);
        	}
        });
        
        prevScan_top.setOnAction(new EventHandler<ActionEvent>() { 
        	@Override public void handle(ActionEvent e) {
        		currentScan_top -= 1;
	            scanLabel_top.setText("scan : " + Integer.toString(currentScan_top));
	            setTopIntensityData(currentScan_top);
        	}
        });
        
        nextScan_bottom.setOnAction(new EventHandler<ActionEvent>() { 
        	@Override public void handle(ActionEvent e) {
        		currentScan_bottom += 1;
	            scanLabel_bottom.setText("scan : " + Integer.toString(currentScan_bottom));
	            setBottomIntensityData(currentScan_bottom, currentConditionIndex, true);
        	}
        });
        
        prevScan_bottom.setOnAction(new EventHandler<ActionEvent>() { 
        	@Override public void handle(ActionEvent e) {
        		currentScan_bottom -= 1;
	            scanLabel_bottom.setText("scan : " + Integer.toString(currentScan_bottom));
	            setBottomIntensityData(currentScan_bottom, currentConditionIndex, true);
        	}
        });
		
        int tick = getTick(currentSize_bottom);
		Double minMass = getMinMass(Double.parseDouble(mz), currentSize_bottom);
		Double maxMass = getMaxMass(Double.parseDouble(mz), charge, peptide, currentSize_bottom);
		setChartxAxis(control_xAxis, tick, minMass, maxMass);
		
		setTopIntensityData(scanNum);
		setBottomIntensityData(scanNum, currentConditionIndex, true);
		menu_button.setText(conditions.get(currentConditionIndex));
	}
	
	public void setTopIntensityData(int scanNum) {
		Scan ctrl_scan = ctrlList.get(scanNum);
		double[][] intensityList = ctrl_scan.getMassIntensityList();
		XYChart.Series series = getIntensitySeries(intensityList);
		
		HDXProfile profile = recordList.get(currentRecordIndex);
		setProfileLabels(profile);
        String peptide = profile.getPeptide();
        Double mz = Double.parseDouble(profile.getMz());
        int charge = Integer.parseInt(profile.getCharge());
        ArrayList<Double> isotopicCluster = IsotopicCluster.get(peptide, charge);
        
        Double standard_value = 1.0;
		Double additional_value = standard_value/charge;
        
        String data = getMaxIsotopic(isotopicCluster, intensityList, mz, additional_value);
		Double maxIntensity = Double.parseDouble(data.split(",")[0]);
		int maxRatioIndex = Integer.parseInt(data.split(",")[1]);

		XYChart.Series dataSeries = getIsotopicSeries(isotopicCluster, mz, additional_value,  maxIntensity, maxRatioIndex, "isotopicCluster");
        
		int tick = getTick(currentSize_top);
		Double minMass = getMinMass(mz, currentSize_top);
		Double maxMass = getMaxMass(mz, charge, peptide, currentSize_top);
		
		setChartxAxis(control_xAxis, tick, minMass, maxMass);
	    setChartyAxis(control_yAxis, 100, 0.0, (Math.ceil(maxIntensity/100)*100)+300);
		
		setChartxAxis(isotopic_xAxis, tick, minMass, maxMass);
	    setChartyAxis(isotopic_yAxis, 100, 0.0, (Math.ceil(maxIntensity/100)*100)+300);

		isotopic.getData().setAll(dataSeries);
		control.getData().setAll(series);
	}
	
	public XYChart.Series getIntensitySeries(double[][] intensityList) {
		XYChart.Series<Double, Double> dataSeries = new XYChart.Series<Double, Double>();
		ObservableList<Data<Double, Double>> data = dataSeries.getData();
		ArrayList<Data<Double, Double>> dataList = new ArrayList<Data<Double, Double>>();
		for(int i = 0; i < intensityList[0].length; i++) {
			Double raw_mass = intensityList[0][i];
			Double intensity = intensityList[1][i];
			dataList.add(new XYChart.Data(raw_mass-0.001, 0));
			dataList.add(new XYChart.Data(raw_mass, intensity));
			dataList.add(new XYChart.Data(raw_mass+0.001, 0));
		}
		data.addAll(dataList);
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
	
	public XYChart.Series getIsotopicSeries(ArrayList<Double> array, Double minMz, Double additionalMz, Double maxIntensity, int maxRatioIndex, String name) {		
		XYChart.Series dataSeries = new XYChart.Series();
		Double maxRatio = array.get(maxRatioIndex);
		Double fittingValue = maxIntensity / maxRatio;
		ArrayList<XYChart.Data<Double, Double>> dataArray = new ArrayList<Data<Double, Double>>();
		
		for(int i = 0; i < array.size(); i++) {
			Double y = array.get(i) * fittingValue;
			Double x = minMz + (additionalMz * i);
			dataArray.add(new XYChart.Data(x, y));
		}
		dataSeries.getData().addAll(dataArray);
		dataSeries.setName(name);
		
		return dataSeries;
	}
	
	public XYChart.Series getPredictSeries(String[] array, Double minMz, Double additionalMz, Double maxIntensity, int maxRatioIndex, String name) {		
		XYChart.Series dataSeries = new XYChart.Series();
		if (array[0].equals("-")) {
			return dataSeries;
		}
		ArrayList<XYChart.Data<Double, Double>> dataArray = new ArrayList<Data<Double, Double>>();
		Double maxRatio = Double.parseDouble(array[maxRatioIndex]);
		Double fittingValue = maxIntensity / maxRatio;
		
		for(int i = 0; i < array.length; i++) {
			if (array[i].equals("-")) {
				break;
			}
			Double y = Double.parseDouble(array[i]) * fittingValue;
			Double x = minMz + (additionalMz * i);
			dataArray.add(new XYChart.Data(x, y));
		}
		
		dataSeries.getData().addAll(dataArray);
		dataSeries.setName(name);
		
		return dataSeries;
	}
	
	public void setBottomIntensityData(int scanNum, int conditionIndex, boolean isFirstRead) {
		// ---- get data ----
		HDXProfile profile = recordList.get(currentRecordIndex);
        String peptide = profile.getPeptide();
        String condition = conditions.get(conditionIndex);
        String d2o_label = condition;
        Double charge = Double.parseDouble(profile.getCharge());
        Double mz = Double.parseDouble(profile.getMz());

        String predictedDdeu = "";
        String observedDdeu = "";
        
        for (int i = 0; i  < ddeuList.size(); i++) {
        	DdeuAnal data = ddeuList.get(i);
        	String data_peptide = data.getPeptide();
        	String data_d2o_label = data.getD2OLabelfirst();
        	if (data_peptide.equals(peptide) && data_d2o_label.contains(d2o_label)) {
        		predictedDdeu = data.getPredictedDdeu();
        		observedDdeu = data.getObservedDdeu();
        		setDdeuLabels(data);
        		break;
        	}
        }
        
		ArrayList<Scan> conditionScans = conditionLists.get(conditionIndex);
		Scan scan = conditionScans.get(scanNum);
		double[][] intensityList = scan.getMassIntensityList();
		
		String[] predictedDdeu_array = predictedDdeu.split(";");
		String[] observedDdeu_array = observedDdeu.split(";");
		
		// --- set data ---
		
		Double standard_value = 1.0;
		Double additional_value = standard_value/charge;
		
		String data = getMaxIntensity(predictedDdeu_array, intensityList, mz, additional_value);
		
		Double maxIntensity = Double.parseDouble(data.split(",")[0]);
		int maxRatioIndex = Integer.parseInt(data.split(",")[1]);
		
		XYChart.Series dataSeries2 = getPredictSeries(predictedDdeu_array, mz, additional_value,  maxIntensity, maxRatioIndex, "predicted");
		XYChart.Series dataSeries3 = getPredictSeries(observedDdeu_array, mz, additional_value, maxIntensity, maxRatioIndex, "observed");
		
		
		int tick = getTick(currentSize_bottom);
		Double minMass = getMinMass(mz, currentSize_bottom);
		Double maxMass = getMaxMass(mz, Integer.parseInt(profile.getCharge()), peptide, currentSize_bottom);
		
	    setChartxAxis(result_xAxis, tick, minMass, maxMass);
	    setChartyAxis(result_yAxis, 100, 0.0, (Math.ceil(maxIntensity/100)*100)+300);
	   
	    setChartxAxis(predict_xAxis, tick, minMass, maxMass);
	    setChartyAxis(predict_yAxis, 100, 0.0, (Math.ceil(maxIntensity/100)*100)+300);

		// --- set data ---
		if (isFirstRead) {
			XYChart.Series dataSeries1 = getIntensitySeries(intensityList);
		    result.getData().setAll(dataSeries1);
		}
	    
		predict.getData().setAll(dataSeries2, dataSeries3);
	}
	
	public String getMaxIsotopic(ArrayList<Double> isotopic_array, double[][] intensityList, Double mz, Double additional_value) {
		Double maxRatio = 0.0;
		int maxRatioIndex = 0;
		for (int i = 0; i < isotopic_array.size(); i++) {
			Double currentRatio = isotopic_array.get(i);
			if (maxRatio < currentRatio) {
				maxRatio = currentRatio;
				maxRatioIndex = i; 
			}
		}
		Double maxRatioX = mz + (additional_value * maxRatioIndex);
		
		Double minDiff = Double.MAX_VALUE;
		Double nearestIntensity = 0.0;
		for(int i = 0; i < intensityList[0].length; i++) {
			Double x = intensityList[0][i];
			Double y = intensityList[1][i];
			
			Double diff = Math.abs(maxRatioX - x);
			if (diff < minDiff) {
				minDiff = diff;
				nearestIntensity = y;
			}
		}
		return nearestIntensity.toString() + "," + maxRatioIndex;
	}
	
	public String getMaxIntensity(String[] predictedDdeu_array, double[][] intensityList, Double mz, Double additional_value) {
		Double maxRatio = 0.0;
		int maxRatioIndex = 0;
		for (int i = 0; i < predictedDdeu_array.length; i++) {
			if (predictedDdeu_array[i].equals("-")) {
				break;
			}
			Double currentRatio = Double.parseDouble(predictedDdeu_array[i]);
			if (maxRatio < currentRatio) {
				maxRatio = currentRatio;
				maxRatioIndex = i; 
			}
		}
		Double maxRatioX = mz + (additional_value * maxRatioIndex);
		
		Double minDiff = Double.MAX_VALUE;
		Double nearestIntensity = 0.0;
		for(int i = 0; i < intensityList[0].length; i++) {
			Double x = intensityList[0][i];
			Double y = intensityList[1][i];
			
			Double diff = Math.abs(maxRatioX - x);
			if (diff < minDiff) {
				minDiff = diff;
				nearestIntensity = y;
			}
		}
		return nearestIntensity.toString() + "," + maxRatioIndex;
	}
	
	public int getTick(int currentSize) {
		return 1 + (5 * currentSize);
	}
	
	public Double getMinMass(Double mz, int currentSize) {
		Double res = mz - 1.0 - currentSize*5;
		return res;
	}
	
	public Double getMaxMass(Double mz, Integer charge, String peptide, int currentSize) {
		return mz + (peptide.length() / charge) + currentSize*5;
	}
	
	public void setProfileLabels(HDXProfile p) {
		this.posFromLabel.setText(p.getPosFrom());
		this.posToLabel.setText(p.getPosTo());
		this.expMzLabel.setText(p.getExpMz());
		this.mzShiftLabel.setText(p.getMzShift());
		this.upStartScanLabel.setText(p.getStartScan());
		this.upEndScanLabel.setText(p.getEndScan());
		this.apexScanLabel.setText(p.getApexScan());
		this.apexRtLabel.setText(p.getApexRt());
	}
	
	public void setDdeuLabels(DdeuAnal d) {
		this.firstDeuNumLabel.setText(d.getFirstDdeuNum());
		this.firstDeuPercentLabel.setText(d.getFirstDdeuPercent());
		this.secondDeuNumLabel.setText(d.getSecondDdeuNum());
		this.secondDeuPercentLabel.setText(d.getSecondDdeuPercent());
		this.downStartScanLabel.setText(d.getStartScan());
		this.downEndScanLabel.setText(d.getEndScan());
		this.startRtLabel.setText(d.getStartRT());
		this.endRtLabel.setText(d.getEndRT());
	}

	// ------------------------------Ddeu Data --------------------
	public void setDdeuData(ArrayList<DdeuAnal> ddeuAnals) {
		ddeuList.addAll(ddeuAnals);
	}
	
	// ------------------------------Table View---------------------

	public void setTableViewData(ArrayList<HDXProfile> profileList, ArrayList<ConditionFile> files) {
		tableview.getColumns().clear();
		menu_button.getItems().clear();
		for (int i = 0; i < files.size(); i++) {
			ConditionFile file = files.get(i);
			MenuItem item = new MenuItem(file.getName());
			item.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent event) {
					MenuItem selected = (MenuItem) event.getSource();
					menu_button.setText(selected.getText());
					int index = conditions.indexOf(selected.getText());
					currentConditionIndex = index;
			        setBottomIntensityData(currentScan_bottom, currentConditionIndex, true);
				}
			});
			menu_button.getItems().add(item);
			conditions.add(file.getName());
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
	            if (index != currentRowIndex) {
		            setLineChartData(index);
		            setInitialBarChartData(index);
		            currentRowIndex = index;
	            }
	        }
	 }
}
