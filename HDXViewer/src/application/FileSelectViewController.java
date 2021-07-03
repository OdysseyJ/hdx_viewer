package application;

import java.io.File;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import com.univocity.parsers.tsv.TsvParser;
import com.univocity.parsers.tsv.TsvParserSettings;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class FileSelectViewController {

	private ArrayList<File> files = new ArrayList<File>(4);
	
    private FileChooser.ExtensionFilter extFilter = 
    		new FileChooser.ExtensionFilter("MZXML FILES (*.mzxml)", "*.mzxml");
	
    @FXML
    private Button confirm_button;

    @FXML
    private Button control_button;

    @FXML
    private TextField control_field;

    @FXML
    private Button f1_button;

    @FXML
    private TextField f1_field;

    @FXML
    private Button f2_button;

    @FXML
    private TextField f2_field;

    @FXML
    private Button f3_button;

    @FXML
    private TextField f3_field;

    @FXML
    void onConfirm(ActionEvent event) {
    	try {
    	Node node = (Node) event.getSource();
    	Stage thisStage = (Stage) node.getScene().getWindow();
    	
    	TsvParserSettings settings = new TsvParserSettings();
		settings.getFormat().setLineSeparator("\n");
		
		TsvParser parser = new TsvParser(settings);

		List<String[]> allRows = parser.parseAll(getReader("foo_pept_HDXProfile.tsv"));
		ArrayList<HDXProfile> profileList = new ArrayList<HDXProfile>();
		
		for (int i = 1; i < allRows.size(); i++) {
			HDXProfile profile = new HDXProfile();
			for (int j = 0; j < allRows.get(0).length; j++) {
				profile.setId(allRows.get(i)[0]);
				profile.setMz(allRows.get(i)[1]);
				profile.setCharge(allRows.get(i)[2]);
				profile.setPeptide(allRows.get(i)[3]);
				profile.setProtein(allRows.get(i)[4]);
				profile.setPosFrom(allRows.get(i)[5]);
				profile.setPosTo(allRows.get(i)[6]);
				profile.setExpMz(allRows.get(i)[7]);
				profile.setMzShift(allRows.get(i)[8]);
				profile.setStartScan(allRows.get(i)[9]);
				profile.setEndScan(allRows.get(i)[10]);
				profile.setApexScan(allRows.get(i)[11]);
				profile.setApexRt(allRows.get(i)[12]);
				profile.setSecond30(allRows.get(i)[13]);
				profile.setMinute10(allRows.get(i)[14]);
				profile.setMinute60(allRows.get(i)[15]);
			}
			profileList.add(profile);
		}
		Main.mainViewController.setTreeItem(this.files);
    	Main.mainViewController.setTableViewData(profileList);

    	thisStage.close();
    	} catch(Exception e) {
    		Alert alert = new Alert(AlertType.WARNING);
    		alert.setTitle("������ �߻��߽��ϴ�.");
    		alert.setHeaderText("������ �о���µ� �����߽��ϴ�.");
    		alert.setContentText(e.getMessage());

    		alert.showAndWait();
    	}
    }
    
    public Reader getReader(String relativePath) {
        try {
			return new InputStreamReader(this.getClass().getResourceAsStream(relativePath), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
    }
    
    void selectFile(FileChooser.ExtensionFilter filter, TextField field) {
    	Stage stage = Main.getPrimaryStage();
    	
    	FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open File");
		
		// Set extension filter
//        fileChooser.getExtensionFilters().add(filter); 
        
		File file = fileChooser.showOpenDialog(stage);
		
		if (file != null) {
			if(this.files.indexOf(file) == -1) {
				this.files.add(file);	
			}
			field.setText(file.getName());
		}
    }
    
    @FXML
    void onSelectControlFile(ActionEvent event) {
    	selectFile(extFilter, control_field);
    }

    @FXML
    void onSelectF1File(ActionEvent event) {
    	selectFile(extFilter, f1_field);
    }

    @FXML
    void onSelectF2File(ActionEvent event) {
    	selectFile(extFilter, f2_field);
    }

    @FXML
    void onSelectF3File(ActionEvent event) {
    	selectFile(extFilter, f3_field);
    }

}