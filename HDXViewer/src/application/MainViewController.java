package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.xml.stream.XMLStreamException;

import org.systemsbiology.jrap.stax.MSXMLSequentialParser;
import org.systemsbiology.jrap.stax.Scan;

import application.table.Person;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainViewController implements Initializable {
	
	private ArrayList<File> files = new ArrayList<File>();

    @FXML
    private MenuItem open;

    @FXML
    private TreeView<String> treeview;

    @FXML
    private LineChart<?, ?> linechart;

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
    void onClickTableView(MouseEvent event) {

    }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		Main.mainViewController = this;
		barchart_up.setLegendVisible(false);
		barchart_up.getXAxis().setAnimated(false);
		barchart_up.getYAxis().setAnimated(false);
		barchart_down.setLegendVisible(false);
		barchart_down.getXAxis().setAnimated(false);
		barchart_down.getYAxis().setAnimated(false);
		
		treeview.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
    	    if(newValue != null && newValue.getValue() != "FILES" && newValue.getValue() != "condition" && newValue != oldValue){
    	    	String fileName = newValue.getValue();
    			String filePath = "";
    			for (int i = 0; i < files.size(); i++) {
    				if (files.get(0).getName().equals(fileName)) {
    					filePath = files.get(i).getPath();
    				}
    			}
    			
    			
	    	   try {
	    	    	MSXMLSequentialParser parser = new MSXMLSequentialParser();
					parser.open(filePath);
					
					Scan scan = parser.getNextScan();
	
					double[][] array = scan.getMassIntensityList();
					XYChart.Series data = new XYChart.Series();
					
					for (int j = 0; j < array[0].length; j++) {
						double key = array[1][j];
						String s = String.format("%.2f", key);
						double value = array[0][j];
						data.getData().add(new XYChart.Data(s, value));
					}	
	
					barchart_up.getData().clear();
					barchart_up.getData().add(data);
				} catch (FileNotFoundException | XMLStreamException e) {
				}
    	    }
    	});
	}
	
	public void setTableViewData(ArrayList<HDXProfile> profileList) {
		
		TableColumn<HDXProfile, String> column1 = new TableColumn<>("Id");
	    column1.setCellValueFactory(new PropertyValueFactory<>("id"));

	    TableColumn<HDXProfile, String> column2 = new TableColumn<>("Mz");
	    column2.setCellValueFactory(new PropertyValueFactory<>("mz"));
	    
	    TableColumn<HDXProfile, String> column3 = new TableColumn<>("Charge");
	    column3.setCellValueFactory(new PropertyValueFactory<>("charge"));
	    
	    TableColumn<HDXProfile, String> column4 = new TableColumn<>("Peptide");
	    column4.setCellValueFactory(new PropertyValueFactory<>("peptide"));
	    
	    TableColumn<HDXProfile, String> column5 = new TableColumn<>("Protein");
	    column5.setCellValueFactory(new PropertyValueFactory<>("protein"));

	    TableColumn<HDXProfile, String> column6 = new TableColumn<>("ExpMz");
	    column6.setCellValueFactory(new PropertyValueFactory<>("expMz"));

	    TableColumn<HDXProfile, String> column7 = new TableColumn<>("MzShift");
	    column7.setCellValueFactory(new PropertyValueFactory<>("mzShift"));

	    TableColumn<HDXProfile, String> column8 = new TableColumn<>("30Second");
	    column8.setCellValueFactory(new PropertyValueFactory<>("second30"));

	    TableColumn<HDXProfile, String> column9 = new TableColumn<>("10Minute");
	    column9.setCellValueFactory(new PropertyValueFactory<>("minute10"));

	    TableColumn<HDXProfile, String> column10 = new TableColumn<>("60Minute");
	    column10.setCellValueFactory(new PropertyValueFactory<>("minute60"));

		tableview.getColumns().remove(0);
		tableview.getColumns().remove(0);

	    tableview.getColumns().add(column1);
	    tableview.getColumns().add(column2);
	    tableview.getColumns().add(column3);
	    tableview.getColumns().add(column4);
	    tableview.getColumns().add(column5);
	    tableview.getColumns().add(column6);
	    tableview.getColumns().add(column7);
	    tableview.getColumns().add(column8);
	    tableview.getColumns().add(column9);
	    tableview.getColumns().add(column10);

	    tableview.setItems(FXCollections.observableArrayList(profileList));
	}
	
	public void setTreeItem(ArrayList<File> files) {
		try {
			this.files = files;
			TreeItem<String> Root = new TreeItem<String>("FILES");
			TreeItem<String> newItem = new TreeItem<String>("condition");
			for (int i = 0; i < files.size(); i++) {
				TreeItem<String> child = new TreeItem<String>(files.get(i).getName());
				newItem.getChildren().add(child);
			}
			Root.getChildren().add(newItem);
	
	    	treeview.setRoot(Root);
		} catch(Exception e) {
			
		}
	}
}
