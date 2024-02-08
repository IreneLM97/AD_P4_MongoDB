package repositories;

import java.io.InputStream;
import java.util.List;
import org.bson.Document;

import com.mongodb.client.MongoCollection;

public interface MongoRepository {
	
	List<Document> findAll(MongoCollection<Document> collection);

	void deleteOneById(String id, MongoCollection<Document> collection);

	void replaceOneById(String id, String jsonUpdate, MongoCollection<Document> collection);

	void deleteAll(MongoCollection<Document> collection);

	List<Document> findByFields(String jsonCriteria, MongoCollection<Document> collection);

	void insertJSONData(InputStream ruta, MongoCollection<Document> collection);

}
