package vistas;


import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class VistaPrincipal extends Application {

	@Override
    public void start(Stage primaryStage) {
        try {
        	Parent root = FXMLLoader.load(getClass().getResource("VistaPrincipal.fxml"));
        	Scene scene = new Scene(root);
        	primaryStage.setScene(scene);
        	primaryStage.show();
        	
        }catch(IOException ex) {
        	Logger.getLogger(VistaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {
		launch();
	}
    
}