package application;

import java.io.*;
import java.util.*;

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

public class OpenProjectController {
	
	FileChooser fileChooser = new FileChooser();
	
	private File project_file;
	
	private String project_name;
	
	private File control;
	
	private File peptide;
	
	private File protein;
	
	private ArrayList<ConditionFile> condition_files = new ArrayList<ConditionFile>();

    private FileChooser.ExtensionFilter mzxmlFilter = 
    		new FileChooser.ExtensionFilter("Project File (*.dmxj)", "*.dmxj");
 
    @FXML
    private Button confirm_button;

    @FXML
    private Button project_file_select;

    @FXML
    private TextField project_file_field;

    @FXML
    public void onClickConfirm(ActionEvent event) {
    	if (project_file == null) {
    		return ;
    	}
    	File output = new File(project_file.getPath());
    	BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(output));
	    	String str;
	    	for(int i = 0 ;(str = br.readLine()) != null; i++) {
	    		if (i == 0 && !str.equals("#Params::")) {
	    			br.close();
	    			throw new Exception();
	    		}
	        	String[] values = str.split("=");
	        	if (values.length != 2) {
	        		continue;
	        	}
	    		String key = str.split("=")[0];
	    		String value = str.split("=")[1];
	    		if (key.equals("Project")) {
	    			this.project_name = value;
	    		}
	    		if (key.equals("Peptide")) {
	    			File file = new File(value);
	    			this.peptide = file;
	    		}
	    		if (key.equals("Protein")) {
	    			File file = new File(value);
	    			this.protein = file;
	    		}
	    		if (key.equals("CTRLData")) {
	    			File file = new File(value);
	    			this.control = file;
	    		}
	    		if (key.equals("HDXData")) {
	    			String name = value.split(",")[0];
	    			String path = value.split(",")[1].substring(1);
	    			File file = new File(path);
	    			ConditionFile cp = new ConditionFile(name, file);
	    			condition_files.add(cp);
	    		}
	    		if (str.equals("#HDXProfile::")) {
	    			break;
	    		}
	    	}
			br.close();
		} catch (Exception e) {
			System.out.println(e);
		}
		setMainViewData(project_file.getPath());
    	Node node = (Node) event.getSource();
    	Stage thisStage = (Stage) node.getScene().getWindow();
		thisStage.close();
    }
    
    @FXML
    public void onClickProjectFileSelect() {
    	Stage stage = Main.getPrimaryStage();
		
		// Set extension filter
    	fileChooser.getExtensionFilters().clear();
        fileChooser.getExtensionFilters().add(mzxmlFilter); 
        
		File file = fileChooser.showOpenDialog(stage);
		if (file==null) {
			return;
		}
		
		project_file = file;
		project_file_field.setText(file.getName());
    }
    
    
    public void setMainViewData(String path) {
    	try {
	    	TsvParserSettings settings = new TsvParserSettings();
	    	settings.getFormat().setLineSeparator("\n");
	    		
	    	TsvParser parser = new TsvParser(settings);
			
			List<String[]> all = parser.parseAll(getReader(path));
			
			File output = new File(path);
	    	BufferedReader br = new BufferedReader(new FileReader(output));
			
	    	String str;
			int hdxStart = 0;
			int ddeuStart = 0;
	    	for(int i = 0 ;(str = br.readLine()) != null; i++) {
	    		if( str.equals("#HDXProfile::") )
	    			hdxStart = i - 2;
	    		else if(str.equals("#DdeuAnal::")) {
	    			ddeuStart = i - 4;
	    			break;
	    		}
	    	}
			br.close();
	
			ArrayList<ArrayList<Scan>> file_scans = new ArrayList<ArrayList<Scan>>();
			ArrayList<HDXProfile> profileList = new ArrayList<HDXProfile>();
			ArrayList<DdeuAnal> ddeuList = new ArrayList<DdeuAnal>();

		// read all control scans
		try {
			MSXMLSequentialParser msxml_parser = new MSXMLSequentialParser();
			msxml_parser.open(control.getPath());
			ArrayList<Scan> scans = new ArrayList<Scan>();
			while (msxml_parser.hasNextScan()){
				scans.add(msxml_parser.getNextScan());
			}
			file_scans.add(scans);
		} catch (Exception e) {
			System.out.println(e);
		}
		for (int i = 0; i < this.condition_files.size(); i++) {
			try {
				ConditionFile file = this.condition_files.get(i);
				MSXMLSequentialParser msxml_parser = new MSXMLSequentialParser();
				msxml_parser.open(file.getFile().getPath());
				ArrayList<Scan> scans = new ArrayList<Scan>();
				while (msxml_parser.hasNextScan()){
					scans.add(msxml_parser.getNextScan());
				}
				file_scans.add(scans);
			} catch (Exception e) {
				System.out.println(e);
			}	
		}
		
		// read all ddeu data
		for (int i = ddeuStart + 1; i < all.size(); i++) {
			DdeuAnal ddeu = new DdeuAnal();
			String[] line = all.get(i);
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
			ddeuList.add(ddeu);
		}

		 //read  all hdx profiles

		if(all.get(hdxStart)[4].equals("Protein")) {
			for (int i = hdxStart + 1; i < ddeuStart ; i++) {
				HDXProfile profile = new HDXProfile();
				profile.setId(all.get(i)[0]);
				profile.setMz(all.get(i)[1]);
				profile.setCharge(all.get(i)[2]);
				profile.setPeptide(all.get(i)[3]);
				profile.setProtein(all.get(i)[4]);
				profile.setPosFrom(all.get(i)[5]);
				profile.setPosTo(all.get(i)[6]);
				profile.setExpMz(all.get(i)[7]);
				profile.setMzShift(all.get(i)[8]);
				profile.setStartScan(all.get(i)[9]);
				profile.setEndScan(all.get(i)[10]);
				profile.setApexScan(all.get(i)[11]);
				profile.setApexRt(all.get(i)[12]);
				profile.setConditions(Arrays.copyOfRange(all.get(i),13,all.get(i).length));
				profileList.add(profile);
			}
		}
		else {
			for (int i = hdxStart + 1; i < ddeuStart; i++) {
				HDXProfile profile = new HDXProfile();
				for (int j = 0; j < all.get(0).length; j++) {
					profile.setId(all.get(i)[0]);
					profile.setMz(all.get(i)[1]);
					profile.setCharge(all.get(i)[2]);
					profile.setPeptide(all.get(i)[3]);
					profile.setExpMz(all.get(i)[4]);
					profile.setMzShift(all.get(i)[5]);
					profile.setStartScan(all.get(i)[6]);
					profile.setEndScan(all.get(i)[7]);
					profile.setApexScan(all.get(i)[8]);
					profile.setApexRt(all.get(i)[9]);
					profile.setConditions(Arrays.copyOfRange(all.get(i),10,all.get(i).length));
				}
				profileList.add(profile);
			}
		}
		Main.mainViewController.setDdeuData(ddeuList);
		Main.mainViewController.setTreeItem(this.condition_files, this.project_name);
    	Main.mainViewController.setTableViewData(profileList, this.condition_files);
    	Main.mainViewController.setScanData(file_scans);
    	} catch(Exception e) {
    		Alert alert = new Alert(AlertType.WARNING);
    		alert.setTitle("error.");
    		alert.setHeaderText("error.");
    		alert.setContentText(e.getMessage());

    		alert.showAndWait();
    	}
    }
    
    public Reader getReader(String absolutePath) {
        try {
        	InputStream in = new FileInputStream(absolutePath);
			return new InputStreamReader(in, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
    }
}