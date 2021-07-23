package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.xml.stream.XMLStreamException;

import org.systemsbiology.jrap.stax.MSXMLSequentialParser;
import org.systemsbiology.jrap.stax.Scan;

import javafx.beans.property.SimpleStringProperty;
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
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
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
	
	ObservableList<HDXProfile> recordList = FXCollections.observableArrayList();

	ArrayList<DdeuAnal> ddeuList = new ArrayList<DdeuAnal>();
	
	ArrayList<Scan> ctrlList = new ArrayList<Scan>();
	
	ArrayList<Scan> conditionList = new ArrayList<Scan>();
	
	private ArrayList<File> files = new ArrayList<File>();
	
	CategoryAxis xAxis = new CategoryAxis();
	
	NumberAxis yAxis = new NumberAxis();

    @FXML
    private MenuItem open;

    @FXML
    private TreeView<String> treeview;

    @FXML
    private LineChart<String,Number> linechart = new LineChart<String,Number>(xAxis,yAxis);

    @FXML
    private BarChart<?, ?> barchart_up;
    
    @FXML
    private Button peptide_view;

    @FXML
    private BarChart<?, ?> barchart_down;

    @FXML
    private TableView<HDXProfile> tableview;

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
		barchart_up.setLegendVisible(false);
		barchart_up.getXAxis().setAnimated(false);
		barchart_up.getYAxis().setAnimated(false);
		barchart_up.getXAxis().setTickLabelsVisible(false);
		barchart_down.setLegendVisible(false);
		barchart_down.getXAxis().setAnimated(false);
		barchart_down.getYAxis().setAnimated(false);
		barchart_down.getXAxis().setTickLabelsVisible(false);
		linechart.setLegendVisible(false);
		linechart.getXAxis().setAnimated(false);
		linechart.getXAxis().setAnimated(false);
		
//		treeview.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
//    	    if(newValue != null && newValue.getValue() != "FILES" && newValue.getValue() != "condition" && newValue != oldValue){
//    	    	String fileName = newValue.getValue();
//    			String filePath = "";
//    			for (int i = 0; i < files.size(); i++) {
//    				if (files.get(0).getName().equals(fileName)) {
//    					filePath = files.get(i).getPath();
//    				}
//    			}
//    			
//    			
//	    	   try {
//	    	    	MSXMLSequentialParser parser = new MSXMLSequentialParser();
//					parser.open(filePath);
//					
//					Scan scan = parser.getNextScan();
//	
//					double[][] array = scan.getMassIntensityList();
//					XYChart.Series data = new XYChart.Series();
//					
//					for (int j = 0; j < array[0].length; j++) {
//						double key = array[1][j];
//						String s = String.format("%.2f", key);
//						double value = array[0][j];
//						data.getData().add(new XYChart.Data(s, value));
//					}	
//	
//					barchart_up.getData().clear();
//					barchart_up.getData().add(data);
//				} catch (FileNotFoundException | XMLStreamException e) {
//				}
//    	    }
//    	});
	}
	
	// ------------------------------tree view------------------
	public void setTreeItem(ArrayList<File> files) {
		try {
			this.files = files;
			TreeItem<String> Root = new TreeItem<String>("Project");
			if( files.size() > 0 ) {
				TreeItem<String> newItem = new TreeItem<String>("Ctrl");
				for (int i = 1; i < files.size(); i++) {
					String condition = files.get(i).getName().split("_")[2];
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
		
		for(int i = 0 ; i < files.size() - 1; i++) {
			String HDXnum = recordList.get(index).getCondition(i);
			
			String[] splited = HDXnum.split("~");
			
			Double d = 0.0;
			
			for(int j = 0 ; j < splited.length ; j++) {
				if(splited[j].equals("-"))
					break;
				d += Double.parseDouble(splited[j]);
			}
			
			d /= splited.length;
			series.getData().add(new XYChart.Data(files.get(i+1).getName().split("_")[2], d));
		}
		
		linechart.getData().setAll(series);
	}
	
	public void setScanData(ArrayList<Scan> ctrl_scans, ArrayList<Scan> condition_scans) {
		ctrlList.addAll(ctrl_scans);
		conditionList.addAll(condition_scans);
	}
	
	public void setBarChartData(int scanNum, int startMass, int endMass, String predictedDdeu, int startScan, int endScan) {
		System.out.println(predictedDdeu);
		Scan ctrl_scan = ctrlList.get(scanNum);
		Scan initial_scan = conditionList.get(startScan+endScan/2);
		
		double[][] intensityList = ctrl_scan.getMassIntensityList();
		double[][] conditionIntensityList = initial_scan.getMassIntensityList();

		XYChart.Series dataSeries1 = new XYChart.Series();
		XYChart.Series dataSeries2 = new XYChart.Series();
		
		for(int i = startMass; i < endMass; i++) {
			String mass1 = Double.toString(intensityList[0][i]);
			Double intensity1 = intensityList[1][i];
			String mass2 = Double.toString(conditionIntensityList[0][i]);
			Double intensity2 = conditionIntensityList[1][i];
			dataSeries1.getData().add(new XYChart.Data(mass1, intensity1));
			dataSeries2.getData().add(new XYChart.Data(mass2, intensity2));
		}
		
		barchart_up.getData().setAll(dataSeries1);
		barchart_down.getData().setAll(dataSeries2);
	}
	
	// ------------------------------Ddeu Data --------------------
	public void setDdeuData(ArrayList<DdeuAnal> ddeuAnals) {
		ddeuList.addAll(ddeuAnals);
	}
	
	// ------------------------------Table View---------------------

	public void setTableViewData(ArrayList<HDXProfile> profileList) {
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
	    
	    for(int i = 1 ; i < files.size() ; i++) {
	    	String condition = files.get(i).getName().split("_")[2];
		    TableColumn<HDXProfile, String> column = new TableColumn<>(condition);
		    int idx = i - 1;
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

	            HDXProfile profile = recordList.get(index);
	            String apexScan = profile.getApexScan();
	            String peptide = profile.getPeptide();
	            String predictedDdeu = "";
	            int startScan = 0;
	            int endScan = 0;
	            for (int i = 0; i  < ddeuList.size(); i++) {
	            	DdeuAnal data = ddeuList.get(i);
	            	if (data.getPeptide().equals(peptide)) {
	            		predictedDdeu = data.getPredictedDdeu();
	            		startScan = Integer.parseInt(data.getStartScan());
	            		endScan = Integer.parseInt(data.getEndScan());
	            		break;
	            	}
	            }
	            setLineChartData(tableview.getSelectionModel().getSelectedIndex());
	            setBarChartData(Integer.parseInt(apexScan), 420, 420+peptide.length(), predictedDdeu, startScan, endScan);
	        }
	 }
}
