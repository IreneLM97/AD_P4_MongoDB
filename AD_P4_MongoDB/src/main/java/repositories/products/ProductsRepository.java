package repositories.products;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.json.JSONObject;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;

import repositories.MongoRepository;

public class ProductsRepository implements MongoRepository {

	@Override
	public List<Document> findAll(MongoCollection<Document> collection) {
		List<Document> results = new ArrayList<>();
		try (MongoCursor<Document> cursor = collection.find().iterator()) {
			while (cursor.hasNext()) {
				Document doc = cursor.next();
	            Document sortedDoc = sortDocumentKeysAlphabetically(doc);
	            results.add(sortedDoc);
			}
		}
		return results;
	}
	
	@Override
	public List<Document> findAllWithProjections(List<String> fieldsToProject, MongoCollection<Document> collection) {
	    // Crear una lista de proyecciones para los campos especificados por el usuario
	    List<Bson> projections = new ArrayList<>();
	    for (String field : fieldsToProject) {
	        projections.add(Projections.include(field));
	    }

	    // Combinar todas las proyecciones en una sola usando la clase Projections
	    Bson combinedProjection = Projections.fields(projections);

	    // Obtener todos los documentos de la colección con las proyecciones especificadas
	    List<Document> results = new ArrayList<>();
	    try (MongoCursor<Document> cursor = collection.find().projection(combinedProjection).iterator()) {
	        while (cursor.hasNext()) {
	            results.add(cursor.next());
	        }
	    }
	    return results;
	}
	
	@Override
	public List<Document> findByFieldsWithProjection(String jsonCriteria, List<String> fieldsToProject, MongoCollection<Document> collection) {
	    Document criteria = Document.parse(jsonCriteria);
	    Bson filter = new Document(criteria);

	    // Crear una lista de proyecciones para los campos especificados por el usuario
	    List<Bson> projections = new ArrayList<>();
	    for (String field : fieldsToProject) {
	        projections.add(Projections.include(field));
	    }

	    // Combinar todas las proyecciones en una sola usando la clase Projections
	    Bson combinedProjection = Projections.fields(projections);

	    List<Document> results = new ArrayList<>();
	    try (MongoCursor<Document> cursor = collection.find(filter).projection(combinedProjection).iterator()) {
	        while (cursor.hasNext()) {
	            results.add(cursor.next());
	        }
	    }
	    return results;
	}
	
	@Override
	public void deleteAll(MongoCollection<Document> collection) {
        collection.deleteMany(new Document());
    }
	
	@Override
	public void insertOne(String json, MongoCollection<Document> collection) {
		Document doc = Document.parse(json);
		collection.insertOne(doc);
	}

	@Override
	public void deleteOneById(String id, MongoCollection<Document> collection) {
        // Eliminar el documento que coincida con el ID proporcionado
		collection.deleteOne(new Document("_id", new ObjectId(id)));
    }
	
	@Override
    public void deleteManyByCriteria(String jsonCriteria, MongoCollection<Document> collection) {
		Document criteria = Document.parse(jsonCriteria);
		
		Bson filter = new Document(criteria);

        // Eliminar documentos que cumplan el criterio
        collection.deleteMany(filter);
    }
	
	@Override
	public void replaceOneById(String id, String jsonUpdate, MongoCollection<Document> collection) {
		// Convertir el JSON de actualización a un Document
		Document updateDocument = Document.parse(jsonUpdate);
		
		// Crear el filtro para el ID
		Bson filter = Filters.eq("_id", new ObjectId(id));
		
		// Reemplazar completamente el documento en la base de datos con el nuevo documento de actualización
		collection.replaceOne(filter, updateDocument);
	}
	
	
	@Override
	public void insertJsonData(InputStream inputStream, MongoCollection<Document> collection) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            StringBuilder jsonString = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonString.append(line);
            }

            // Convertir el JSON a un JSONArray
            JSONArray jsonArray = new JSONArray(jsonString.toString());

            // Insertar cada documento en la colección
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Document document = Document.parse(jsonObject.toString());
                collection.insertOne(document);
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo JSON: " + e.getMessage());
        }
    }

	private Document sortDocumentKeysAlphabetically(Document document) {
	    TreeMap<String, Object> sortedMap = new TreeMap<>(document);
	    Document sortedDocument = new Document(sortedMap);
	    return sortedDocument;
	}
	
}
