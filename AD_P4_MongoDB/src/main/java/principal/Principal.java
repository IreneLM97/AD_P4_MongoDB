package principal;

import java.util.List;

import javax.swing.JFrame;

import org.bson.Document;

import controller.MongoController;
import repositories.products.ProductsRepository;
import vistas.VistaPrincipal;

/**
 * Clase principal de la aplicación, que contiene el método main para iniciar la aplicación.
 */
public class Principal {
	// Creamos nuestro controlador y le añadimos y le inyectamos las dependencias
	public static MongoController controller = new MongoController(new ProductsRepository());
		
    public static void main(String[] args) {
    	// Creamos la vista principal de la aplicación
    	JFrame frame = new JFrame("Vista Principal");
        VistaPrincipal vistaPrincipal = new VistaPrincipal(controller);
        frame.getContentPane().add(vistaPrincipal);
        frame.setSize(900, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);        
		
        // Obtenemos todos los datos de la base de datos y lo agregamos a la vista principal
		List<Document> results = controller.findAll();
		vistaPrincipal.agregarTablas(results);
    }
}
