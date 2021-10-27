package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class Main extends Application {
	private static Stage primaryStage;
	public static MainViewController mainViewController;
	
	private void setPrimaryStage(Stage stage) {
        Main.primaryStage = stage;
    }

    static public Stage getPrimaryStage() {
        return Main.primaryStage;
    }
	
	@Override
	public void start(Stage primaryStage) {
		try {
			setPrimaryStage(primaryStage);
			Parent root = FXMLLoader.load(getClass().getResource("MainView.fxml"));
			Scene scene = new Scene(root, 1280, 720);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("HDX Viewer");
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
