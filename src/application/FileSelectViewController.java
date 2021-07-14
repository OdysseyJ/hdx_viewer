package application;

import java.io.File;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.systemsbiology.jrap.stax.MSXMLSequentialParser;
import org.systemsbiology.jrap.stax.Scan;

import com.univocity.parsers.tsv.TsvParser;
import com.univocity.parsers.tsv.TsvParserSettings;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class FileSelectViewController {

	private ArrayList<File> files = new ArrayList<File>();
	
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

		List<String[]> allDdueAnals = parser.parseAll(getReader("foo_pept_DdeuAnal.tsv"));
		List<String[]> allHDXProfiles = parser.parseAll(getReader("foo_pept_HDXProfile.tsv"));
		
		MSXMLSequentialParser ctrl_parser = new MSXMLSequentialParser();
		MSXMLSequentialParser condition_parser = new MSXMLSequentialParser();
		ArrayList<Scan> ctrl_scans = new ArrayList<Scan>();
		ArrayList<Scan> condition_scans = new ArrayList<Scan>();
		ArrayList<HDXProfile> profileList = new ArrayList<HDXProfile>();
		ArrayList<DdeuAnal> ddueList = new ArrayList<DdeuAnal>();
		
		// read all scans
		try {
			ctrl_parser.open("/Users/seongwoon/eclipse/hdx_viewer/src/application/ctrl_ms.mzXML");
			condition_parser.open("/Users/seongwoon/eclipse/hdx_viewer/src/application/d2o_10m.mzXML");
			while (ctrl_parser.hasNextScan()){
				ctrl_scans.add(ctrl_parser.getNextScan());
			}
			while (condition_parser.hasNextScan()) {
				condition_scans.add(condition_parser.getNextScan());
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		
		// read all ddeu data
		
		for (int i = 1; i < allDdueAnals.size(); i++) {
			DdeuAnal ddeu = new DdeuAnal();
			String[] line = allDdueAnals.get(i);
			System.out.println("###########");
			System.out.println(line[3]);
			System.out.println(line[9]);
			System.out.println(line[10]);
			ddeu.setId(line[0]);
			ddeu.setMz(line[1]);
			ddeu.setCharge(line[2]);
			ddeu.setPeptide(line[3]);
			ddeu.setD2OLabelfirst(line[4]);
			ddeu.setFirstDdeuNum(line[5]);
			ddeu.setFirstDdeuPercent(line[6]);
			ddeu.setSecondDdeuNum(line[7]);
			ddeu.setSecondDdeuPercent(line[8]);
			ddeu.setPredictedDdeu(line[9]);
			ddeu.setStartScan(line[10]);
			ddeu.setEndScan(line[11]);
			ddeu.setStartRT(line[12]);
			ddeu.setEndRT(line[13]);
			ddeu.setObservedDdeu(line[14]);
			ddeu.setMatchedScore(line[15]);
			ddueList.add(ddeu);
		}
		
		// read  all hdx profiles
		
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
				profile.setConditions(Arrays.copyOfRange(allHDXProfiles.get(i),13,allHDXProfiles.get(i).length));
			profileList.add(profile);
		}
		
		Main.mainViewController.setDdeuData(ddueList);
		Main.mainViewController.setTreeItem(this.files);
    	Main.mainViewController.setTableViewData(profileList);
    	Main.mainViewController.setScanData(ctrl_scans, condition_scans);

    	thisStage.close();
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