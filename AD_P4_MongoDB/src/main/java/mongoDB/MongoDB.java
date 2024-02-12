package mongoDB;

import java.io.FileInputStream;
import java.util.Properties;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

/**
 * Clase que proporciona una instancia única de MongoClient para acceder a una base de datos MongoDB.
 * Los detalles de conexión se cargan desde un archivo de propiedades externo llamado "app.config".
 */
public class MongoDB {

	private final String FILE_PROPS = "app.config";
	private static MongoClient db = null;
	
	/**
     * Constructor privado para evitar la instanciación desde fuera de la clase.
     */
	private MongoDB() {
		// Cargamos el archivo de propiedades
		Properties props = new Properties();
		try {
			props.load(new FileInputStream(FILE_PROPS));
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Obtenemos la cadena de conexión
		String uri = props.getProperty("protocol")
				+ "://"
				+ props.getProperty("user")
				+ ":"
				+ props.getProperty("pass")
				+ "@"
				+ props.getProperty("host");
		db = MongoClients.create(uri);
	}
	
	/**
     * Recupera la instancia única de MongoClient.
     *
     * @return Instancia de MongoClient
     */
	public static MongoClient getClient() {
		if (db == null) {
			new MongoDB();
		}
		return db;
	}
}
