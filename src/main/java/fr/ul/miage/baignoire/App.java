package fr.ul.miage.baignoire;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
	

	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("TP2 - Baignoire");
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/main_panel.fxml"));
			Parent root = loader.load();
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.show();	
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
