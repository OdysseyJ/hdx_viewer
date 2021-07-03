package application.menu;

import java.io.File;
import java.io.FileNotFoundException;

import application.model.Files;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class FileOpenMenuItem {
	MenuItem _open;
	Files _files;
	
	FileOpenMenuItem(Stage stage, Files files){
		MenuItem open = new MenuItem("¿­±â");
		
		_open = open;
		_files = files;
		
		open.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e)
			{
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Open Resource File");
			
			// Set extension filter
            FileChooser.ExtensionFilter extFilter = 
            		new FileChooser.ExtensionFilter("MZXML FILES (*.mzxml)", "*.mzxml");
            fileChooser.getExtensionFilters().add(extFilter); 
            
			File file = fileChooser.showOpenDialog(stage);
			
			System.out.println(this);
			
			if (file != null) {
//				files.addFile(files);
			}
		}});
	}
	
	public Files getFiles() {
		return this._files;
	}
	
	public MenuItem getMenuItem() {
		return this._open;
	}
	
	
}
