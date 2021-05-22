package application.chart;

import application.model.Files;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

public class TreeViewBuilder {
	TreeView _treeView;
	Files _files;
	
	public TreeViewBuilder(Files files){
		TreeItem rootItem = new TreeItem("파일목록");

		for (int i = 0; i < files.getSize(); i++) {
			rootItem.getChildren().add(new TreeItem(files.getFile(i)));
		}

		TreeView treeView = new TreeView();
		treeView.setRoot(rootItem);
		
		_treeView = treeView;
		_files = files;
	}
	
	public TreeView getTreeView() {
		return this._treeView;
	}
}
