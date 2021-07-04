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

		List<String[]> allDdueAnals = parser.parseAll(getReader("foo_pept_HDXProfile.tsv"));
		List<String[]> allHDXProfiles = parser.parseAll(getReader("foo_pept_HDXProfile.tsv"));
		ArrayList<HDXProfile> profileList = new ArrayList<HDXProfile>();
		
		for (int i = 1; i < allHDXProfiles.size(); i++) {
			HDXProfile profile = new HDXProfile();
			for (int j = 0; j < allHDXProfiles.get(0).length; j++) {
				profile.setId(allHDXProfiles.get(i)[0]);
				profile.setMz(allHDXProfiles.get(i)[1]);
				profile.setCharge(allHDXProfiles.get(i)[2]);
				profile.setPeptide(allHDXProfiles.get(i)[3]);
				profile.setProtein(allHDXProfiles.get(i)[4]);
				profile.setPosFrom(allHDXProfiles.get(i)[5]);
				profile.setPosTo(allHDXProfiles.get(i)[6]);
				profile.setExpMz(allHDXProfiles.get(i)[7]);
				profile.setMzShift(allHDXProfiles.get(i)[8]);
				profile.setStartScan(allHDXProfiles.get(i)[9]);
				profile.setEndScan(allHDXProfiles.get(i)[10]);
				profile.setApexScan(allHDXProfiles.get(i)[11]);
				profile.setApexRt(allHDXProfiles.get(i)[12]);
				profile.setSecond30(allHDXProfiles.get(i)[13]);
				profile.setMinute10(allHDXProfiles.get(i)[14]);
				profile.setMinute60(allHDXProfiles.get(i)[15]);
			}
			profileList.add(profile);
		}
		Main.mainViewController.setTreeItem(this.files);
    	Main.mainViewController.setTableViewData(profileList);

    	thisStage.close();
    	} catch(Exception e) {
    		Alert alert = new Alert(AlertType.WARNING);
    		alert.setTitle("오류가 발생했습니다.");
    		alert.setHeaderText("파일을 읽어오는데 실패했습니다.");
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