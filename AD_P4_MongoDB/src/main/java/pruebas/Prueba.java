package pruebas;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.lt;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

import mongoDB.MongoDB;

public class Prueba {
	public static String pretty(String json) {
		JsonElement je = JsonParser.parseString(json);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(je);
	}
	
	public static void main(String[] args) {		
		MongoClient mongoClient = MongoDB.getClient();
		MongoDatabase database = mongoClient.getDatabase("inventorydb");
        MongoCollection<Document> collection = database.getCollection("products");
//        System.out.println(findOneByField("type", "Bebida", collection));
//        System.out.println("CAMBIO");
//        System.out.println(findMoreByField("type", "Bebida", collection));
        
        // Insertar un nuevo documento
//        String nuevoDocumentoJson = "{ \"type\": \"Nuevo\", \"name\": \"Nuevo Producto\", \"stock\": 10 }";
//        insertDocument(nuevoDocumentoJson, collection);
        
        // Eliminar un documento por campo específico
//        deleteDocumentByField("type", "Nuevo", collection);
        
        // Actualizar un documento por campo específico
//        String jsonUpdate = "{ \"name\": \"Nuevo nombre otra vez\", \"stock\": 20 }";
//        updateDocumentByField("type", "Bebida", jsonUpdate, collection);
   
        // JSON con los campos y valores de búsqueda
//        String jsonCriteria = "{ \"type\": \"Bebida\", \"stock\": { \"$lt\": 10 } }";
//        
//        // Llamar al método para buscar documentos por los campos especificados
//        List<Document> results = findByFields(jsonCriteria, collection);
//        
//        // Imprimir los resultados
//        for (Document doc : results) {
//            System.out.println(pretty(doc.toJson()));
//        }
	}
	
	public static String findOneByField(String field, String value, MongoCollection<Document> collection) {
		Document doc = collection.find(eq(field, value)).first();
        if (doc != null) {
            return doc.toJson();            
        } else {
            return "No matching documents found.";
        }
	}
	
	public static String findMoreByField(String field, String value, MongoCollection<Document> collection) {
		MongoCursor<Document> cursor = collection.find(eq(field, value)).cursor();
		String result = "";
		try {
		     while(cursor.hasNext()) {
		         result += cursor.next().toJson();
		     }
		} finally {
		     cursor.close();
		}
		return result;
	}
	
	public static void insertDocument(String json, MongoCollection<Document> collection) {
	    Document doc = Document.parse(json);
	    collection.insertOne(doc);
	    System.out.println("Nuevo documento insertado:");
	    System.out.println(pretty(doc.toJson()));
	}
	
	public static void deleteDocumentByField(String field, String value, MongoCollection<Document> collection) {
	    Bson filter = eq(field, value);
	    collection.deleteOne(filter);
	    //System.out.println(collection.deleteOne(filter).getDeletedCount());
	    System.out.println("Documento eliminado con éxito.");
	}
	
	public static void updateDocumentByField(String field, String value, String jsonUpdate, MongoCollection<Document> collection) {
	    Bson filter = eq(field, value);
	    Document updateDocument = Document.parse(jsonUpdate);
	    Document updateOperation = new Document("$set", updateDocument);
	    collection.updateMany(filter, updateOperation);
	    System.out.println("Documento actualizado con éxito.");
	}
	
	// Método para buscar documentos por un JSON con los criterios de búsqueda
    public static List<Document> findByFields(String jsonCriteria, MongoCollection<Document> collection) {
        Document criteria = Document.parse(jsonCriteria);
        Bson filter = new Document(criteria);
        List<Document> results = new ArrayList<>();
        try (MongoCursor<Document> cursor = collection.find(filter).iterator()) {
            while (cursor.hasNext()) {
                results.add(cursor.next());
            }
        }
        return results;
    }
}
