package application.menu;

import application.model.Files;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

public class MenuBarBuilder {
	public MenuBar _menuBar;
	Files _files;
	public MenuBarBuilder(Stage stage, Files files) {
		MenuBar menuBar = new MenuBar();
		this.buildFileMenu(menuBar, stage);
		_menuBar = menuBar;
		_files = files;
	}
	
	private void buildFileMenu(MenuBar menuBar, Stage stage) {
		Menu fileMenu = new Menu("ÆÄÀÏ");
		FileOpenMenuItem open = new FileOpenMenuItem(stage, _files);
		
		fileMenu.getItems().add(open.getMenuItem());
		menuBar.getMenus().add(fileMenu);
	}
	
	public MenuBar getMenuBar() {
		return _menuBar;
	}
}
