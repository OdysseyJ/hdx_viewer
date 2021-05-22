package application.model;

import java.io.File;
import java.util.ArrayList;

public class Files {
	ArrayList<File> files;
	File currentFile;
	
	public Files() {
		files = new ArrayList<File>();
	}
	
	public void addFile(File file) {
		files.add(file);
	}
	
	public void removeFile(int index) {
		files.remove(index);
	}
	
	public ArrayList<File> getFiles(){
		return files;
	}
	
	public File getFile(int index) {
		return files.get(index);
	}
	
	public int getSize() {
		return files.size();
	}
	
	public void setCurrentFile(int index) {
		currentFile = files.get(index);
	}
}
