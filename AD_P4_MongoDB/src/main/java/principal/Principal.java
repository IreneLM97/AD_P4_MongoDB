package principal;

import java.util.List;

import javax.swing.JFrame;

import org.bson.Document;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import mongoDB.MongoDB;
import repositories.products.ProductsRepository;
import vistas.VistaPrincipalAux;

public class Principal {
	// Método main para probar la clase
    public static void main(String[] args) {
    	JFrame frame = new JFrame("Vista Principal");
        VistaPrincipalAux vistaPrincipal = new VistaPrincipalAux();
        frame.getContentPane().add(vistaPrincipal);
        frame.setSize(900, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        
        // Crear collection
        MongoClient mongoClient = MongoDB.getClient();
		MongoDatabase database = mongoClient.getDatabase("inventorydb");
		MongoCollection<Document> collection = database.getCollection("products");
		
		ProductsRepository pr = new ProductsRepository();
		List<Document> results = pr.findAll(collection);
     
		System.out.println(results);
		vistaPrincipal.agregarTablas(results);

    }
}
