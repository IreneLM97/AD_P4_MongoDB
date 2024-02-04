package repositories.products;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;

import repositories.MongoRepository;

public class ProductsRepository implements MongoRepository {

	@Override
	public List<Document> findAll(MongoCollection<Document> collection) {
		List<Document> results = new ArrayList<>();
		try (MongoCursor<Document> cursor = collection.find().iterator()) {
			while (cursor.hasNext()) {
				results.add(cursor.next());
			}
		}
		return results;
	}
	
	@Override
	public void deleteOneById(String id, MongoCollection<Document> collection) {
        // Eliminar el documento que coincida con el ID proporcionado
		collection.deleteOne(new Document("_id", new ObjectId(id)));
    }
	
	@Override
	public void replaceOneByCriteria(String id, String jsonUpdate, MongoCollection<Document> collection) {
		// Convertir el JSON de actualización a un Document
		Document updateDocument = Document.parse(jsonUpdate);
		
		// Crear el filtro para el ID
		Bson filter = Filters.eq("_id", new ObjectId(id));
		
		// Reemplazar completamente el documento en la base de datos con el nuevo documento de actualización
		collection.replaceOne(filter, updateDocument);
	}
	
	@Override
	public void deleteAll(MongoCollection<Document> collection) {
        collection.deleteMany(new Document());
    }
	
	@Override
	public void insertJsonFile(String jsonFilePath, MongoCollection<Document> collection) {
	    
	}

	
	
}
