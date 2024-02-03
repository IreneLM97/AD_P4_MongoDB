package repositories.products;

import java.util.ArrayList;
import java.util.List;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;

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
	public void updateOneByCriteria(String id, String jsonUpdate, MongoCollection<Document> collection) {
		// Convertir los datos de actualización de JSON a Document
	    Document updateDocument = Document.parse(jsonUpdate);
	    // Crear el filtro para el ID
	    Bson filter = Filters.eq("_id", new ObjectId(id));
	    // Crear el documento de operación de actualización usando $set
	    Document updateOperation = new Document("$set", updateDocument);
	    // Opciones de actualización para realizar solo un documento
	    UpdateOptions options = new UpdateOptions().upsert(false);
	    // Actualizar el documento que coincida con el filtro dado
	    collection.updateOne(filter, updateOperation, options);
	}
	
}
