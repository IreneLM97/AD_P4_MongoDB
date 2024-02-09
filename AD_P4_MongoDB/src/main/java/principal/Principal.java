package principal;

import java.util.List;

import javax.swing.JFrame;

import org.bson.Document;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import mongoDB.MongoDB;
import repositories.products.ProductsRepository;
import vistas.VistaPrincipal;

public class Principal {
	private static MongoClient mongoClient = MongoDB.getClient();
	private static MongoDatabase database = mongoClient.getDatabase("inventorydb");
	static MongoCollection<Document> collection = database.getCollection("products");
	// Método main para probar la clase
    public static void main(String[] args) {
    	JFrame frame = new JFrame("Vista Principal");
        VistaPrincipal vistaPrincipal = new VistaPrincipal(collection);
        frame.getContentPane().add(vistaPrincipal);
        frame.setSize(900, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        
        // Crear collection
        
		
		ProductsRepository pr = new ProductsRepository();
		List<Document> results = pr.findAll(collection);
     
		System.out.println(results);
		vistaPrincipal.agregarTablas(results);
    }
}
