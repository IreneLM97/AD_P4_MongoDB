package vistas;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class VistaPrincipal extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Crear un botón
        Button button = new Button("Haz clic!");

        // Crear un contenedor y añadir el botón
        StackPane root = new StackPane();
        root.getChildren().add(button);

        // Crear la escena
        Scene scene = new Scene(root, 300, 250);

        // Configurar la escena en el escenario principal
        primaryStage.setScene(scene);

        // Configurar el título del escenario
        primaryStage.setTitle("Mi primera aplicación JavaFX");

        // Mostrar el escenario
        primaryStage.show();
    }

    public static void main(String[] args) {
		launch(args);
	}
    
}