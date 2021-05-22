package application.table;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class TableViewBuilder {
	TableView _tableView;
	public TableViewBuilder(){
		TableView tableView = new TableView();


	    TableColumn<Person, String> column1 = new TableColumn<>("First Name");
	    column1.setCellValueFactory(new PropertyValueFactory<>("firstName"));


	    TableColumn<Person, String> column2 = new TableColumn<>("Last Name");
	    column2.setCellValueFactory(new PropertyValueFactory<>("lastName"));
;


	    tableView.getColumns().add(column1);
	    tableView.getColumns().add(column2);



	    tableView.getItems().add(new Person("John", "Doe"));
	    tableView.getItems().add(new Person("Jane", "Deer"));
	    
	    _tableView = tableView;
	}
	
	public TableView getTableView(){
		return this._tableView;
	}
}
