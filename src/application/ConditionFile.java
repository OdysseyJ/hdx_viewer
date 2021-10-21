package application;

import java.io.File;

public class ConditionFile {
	public ConditionFile(String name, File file) {
		this.name = name;
		this.file = file;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public File getFile() {
		return this.file;
	}

	public void setFille(File file) {
		this.file = file;
	}

	private String name;
	
	private File file;
}
