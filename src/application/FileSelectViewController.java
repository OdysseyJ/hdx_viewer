package application;

import java.io.*;
import java.util.*;

import org.systemsbiology.jrap.stax.MSXMLSequentialParser;
import org.systemsbiology.jrap.stax.Scan;

import com.univocity.parsers.tsv.TsvParser;
import com.univocity.parsers.tsv.TsvParserSettings;

import deMix.*;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

public class FileSelectViewController {
	
	FileChooser fileChooser = new FileChooser();

	private File control;
	
	private File peptide;
	
	private File protein;
	
	private ArrayList<File> condition_files = new ArrayList<File>();
	
	ObservableList<String> recordList = FXCollections.observableArrayList();
	
    private FileChooser.ExtensionFilter mzxmlFilter = 
    		new FileChooser.ExtensionFilter("MZXML FILES (*.mzxml)", "*.mzxml");
    
    private FileChooser.ExtensionFilter tsvFilter = 
    		new FileChooser.ExtensionFilter("tsv FILES (*.tsv)", "*.tsv");

    private FileChooser.ExtensionFilter fastaFilter = 
    		new FileChooser.ExtensionFilter("fasta FILES (*.fasta)", "*.fasta");
	
	private int selected_condition_index = -1;
    
    @FXML
    private Button confirm_button;

    @FXML
    private Button control_button;

    @FXML
    private TextField control_field;

    @FXML
    private Button peptide_button;

    @FXML
    private TextField peptide_field;

    @FXML
    private Button condition_button;

    @FXML
    private TextField condition_field;

    @FXML
    private Button protein_button;

    @FXML
    private TextField protein_field;

    @FXML
    private TextField mass_tolerance_field;
   
    @FXML
    private TableView<String> condition_table_view;
    
    @FXML
    private Button down_button;

    @FXML
    private Button up_button;
    
    @FXML
    void onPressDown(ActionEvent event) {
        if (selected_condition_index != -1) {
        	if (selected_condition_index == recordList.size()-1) {
        		return;
        	}
        	else {
        		String current = recordList.get(selected_condition_index);
            	String temp = recordList.get(selected_condition_index+1);
            	
            	File current_file = condition_files.get(selected_condition_index);
            	File temp_file = condition_files.get(selected_condition_index+1);
            	
            	recordList.set(selected_condition_index+1, current);
            	recordList.set(selected_condition_index, temp);
      
            	condition_files.set(selected_condition_index+1, current_file);
            	condition_files.set(selected_condition_index, temp_file);
            	
            	selected_condition_index = selected_condition_index + 1;
        	}
        }
    }
    
    @FXML
    void onPressUp(ActionEvent event) {
    	if (selected_condition_index != -1) {
        	if (selected_condition_index == 0) {
        		return;
        	}
        	else {
        		String current = recordList.get(selected_condition_index);
            	String temp = recordList.get(selected_condition_index-1);
            	
            	File current_file = condition_files.get(selected_condition_index);
            	File temp_file = condition_files.get(selected_condition_index-1);
            	
            	recordList.set(selected_condition_index-1, current);
            	recordList.set(selected_condition_index, temp);
      
            	condition_files.set(selected_condition_index-1, current_file);
            	condition_files.set(selected_condition_index, temp_file);
            	
            	selected_condition_index = selected_condition_index - 1;
        	}
        }
    }

    @FXML
    void onConfirm(ActionEvent event) {
    	try {
    		
    	runDemix();
    	
    	Node node = (Node) event.getSource();
    	Stage thisStage = (Stage) node.getScene().getWindow();
		String pept = this.peptide.getAbsolutePath();
		String ddeuPath = pept.substring(0, pept.lastIndexOf('.'));
		ddeuPath += "_DdeuAnal.tsv";
		String hdxPath = pept.substring(0, pept.lastIndexOf('.'));
		hdxPath += "_HDXProfile.tsv";
		setMainViewData(ddeuPath, hdxPath);
    	thisStage.close();
    	} catch(Exception e) {
    		Alert alert = new Alert(AlertType.WARNING);
    		alert.setTitle("error.");
    		alert.setHeaderText("error.");
    		alert.setContentText(e.getMessage());

    		alert.showAndWait();
    	}
    }
    
    public void setMainViewData(String ddeuPath, String hdxPath) {
    	try {
    	TsvParserSettings settings = new TsvParserSettings();
    	settings.getFormat().setLineSeparator("\n");
    		
    	TsvParser parser = new TsvParser(settings);
		
		List<String[]> allDdueAnals = parser.parseAll(getReader(ddeuPath));
		List<String[]> allHDXProfiles = parser.parseAll(getReader(hdxPath));
		
		ArrayList<ArrayList<Scan>> file_scans = new ArrayList<ArrayList<Scan>>();
		ArrayList<HDXProfile> profileList = new ArrayList<HDXProfile>();
		ArrayList<DdeuAnal> ddueList = new ArrayList<DdeuAnal>();

		// read all control scans
		try {
			File file = this.control;
			MSXMLSequentialParser msxml_parser = new MSXMLSequentialParser();
			msxml_parser.open(file.getPath());
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
				File file = this.condition_files.get(i);
				MSXMLSequentialParser msxml_parser = new MSXMLSequentialParser();
				msxml_parser.open(file.getPath());
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
		for (int i = 1; i < allDdueAnals.size(); i++) {
			DdeuAnal ddeu = new DdeuAnal();
			String[] line = allDdueAnals.get(i);
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
		
		 //read  all hdx profiles
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
			}
			profileList.add(profile);
		}
		Main.mainViewController.setDdeuData(ddueList);
		Main.mainViewController.setTreeItem(this.condition_files);
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
    
    void selectFile(FileChooser.ExtensionFilter filter, TextField field, File f) {
    	Stage stage = Main.getPrimaryStage();
		
		// Set extension filter
    	fileChooser.getExtensionFilters().clear();
        fileChooser.getExtensionFilters().add(filter); 
        
		File file = fileChooser.showOpenDialog(stage);
		if (file != null) {
			field.setText(file.getName());
		}
    }
    
    void selectMultipleFile(FileChooser.ExtensionFilter filter, TextField field, ArrayList<File> f) {
    	Stage stage = Main.getPrimaryStage();
		
		// Set extension filter
    	fileChooser.getExtensionFilters().clear();
        fileChooser.getExtensionFilters().add(filter); 
        
		List<File> files = fileChooser.showOpenMultipleDialog(stage);
		condition_files.addAll(files);
		
		if (files == null) return;
		
		Callback<TableColumn<String, String>, TableCell<String, String>> stringCellFactory =
                new Callback<TableColumn<String, String>, TableCell<String, String>>() {
            @Override
            public TableCell<String, String> call(TableColumn<String, String> p) {
            	StringTableCell cell = new StringTableCell();
                cell.addEventFilter(MouseEvent.MOUSE_CLICKED, new MyEventHandler());
                return cell;
            }
        };
		ArrayList<String> file_names = new ArrayList<String>();
		
		int size = condition_table_view.getColumns().size();
		if (size == 0) {
			TableColumn<String, String> col = new TableColumn<>("Conditions");
			col.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue()));
			col.setCellFactory(stringCellFactory);
			col.setEditable(true);
			col.setSortable(false);
			condition_table_view.getColumns().add(col);	
		}
		for(int i = 0 ; i < files.size() ; i++) {
	    	String file_name = files.get(i).getName();
	    	file_names.add(file_name);
	    }
		recordList.addAll(file_names);
		condition_table_view.setEditable(true);
		condition_table_view.setItems(recordList);
    }
    
    class StringTableCell extends TableCell<String, String> {
        @Override
        public void updateItem(String item, boolean empty) {
        	setText(item);
        }
    }
    
    class MyEventHandler implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent t) {
            TableCell c = (TableCell) t.getSource();
            int index = c.getIndex();
            if (index < condition_files.size()){
            	selected_condition_index = index;
            }
        }
    }
    
    @FXML
    void onSelectControlFile(ActionEvent event) {
        Stage stage = Main.getPrimaryStage();
    	
    	// Set extension filter
        fileChooser.getExtensionFilters().clear();
    	fileChooser.getExtensionFilters().add(mzxmlFilter);
            
    	File file = fileChooser.showOpenDialog(stage);
    	if (file != null) {
    		control = file;
    		control_field.setText(file.getName());
    	}
    }

    @FXML
    void onSelectPeptideFile(ActionEvent event) {
        Stage stage = Main.getPrimaryStage();
    	
    	// Set extension filter
    	fileChooser.getExtensionFilters().clear();
    	fileChooser.getExtensionFilters().add(tsvFilter); 
            
    	File file = fileChooser.showOpenDialog(stage);
    	if (file != null) {
    		peptide = file;
    		peptide_field.setText(file.getName());
    	}
    }

    @FXML
    void onSelectConditionFiles(ActionEvent event) {
    	selectMultipleFile(mzxmlFilter, condition_field, condition_files);
    }

    @FXML
    void onSelectProteinFile(ActionEvent event) {
        Stage stage = Main.getPrimaryStage();
    	
    	// Set extension filter
        fileChooser.getExtensionFilters().clear();
    	fileChooser.getExtensionFilters().add(fastaFilter); 
            
    	File file = fileChooser.showOpenDialog(stage);
    	if (file != null) {
    		protein = file;
    		protein_field.setText(file.getName());
    	}
    }
    
    void runDemix() {
    	try {
	    	File param = new File("deMix.params");
	    	BufferedWriter bw = new BufferedWriter(new FileWriter(param));
	    	bw.write("Peptide= "+ this.peptide + "\n" + "CTRLData= "+this.control+"\n");
	    	for(int i = 0 ; i < this.condition_files.size(); i++) {
	    		String file_name = this.condition_files.get(i).getName().split("\\.")[0];
	    		String label = null;
	    		if( Character.isDigit(file_name.charAt(file_name.length()-3)) )
	    			label = file_name.substring(file_name.length()-3);
	    		else
	    			label = file_name.substring(file_name.length()-2);
	    		if(label == null) {
	    			bw.close();
	    			throw new Exception("�ùٸ� ���ϸ��� �ƴմϴ�.");
	    		}
	    		bw.write("HDXData=" + label + ", " + this.condition_files.get(i)+"\n");
	    	}
	    	bw.write("MassTolerance= "+ this.mass_tolerance_field.getText() + "\n");
	    	if(this.protein != null)
	    		bw.write("Protein= "+ this.protein + "\n");
	    	bw.close();
	    	
	    	Stage stage = new Stage();
	        Parent root = FXMLLoader.load(getClass().getResource("Popup.fxml"));
	        stage.setScene(new Scene(root, 300, 0));
	        stage.setTitle("Running DeMix ...");
	        stage.initModality(Modality.WINDOW_MODAL);
	        stage.initOwner(Main.getPrimaryStage());
	        stage.show();
	        
            String[] args = {"-i","deMix.params","-o","result"};
            deMix.main(args);
            stage.close();
    	}
    	catch(Exception e) {
    		Alert alert = new Alert(AlertType.WARNING);
    		alert.setTitle("error.");
    		alert.setHeaderText("error.");
    		alert.setContentText(e.getMessage());

    		alert.showAndWait();
    	}
    }
}
