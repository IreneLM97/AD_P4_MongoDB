package repositories;

import java.io.InputStream;
import java.util.List;
import org.bson.Document;

import com.mongodb.client.MongoCollection;

public interface MongoRepository {
	
	List<Document> findAll(MongoCollection<Document> collection);
	
	List<Document> findAllWithProjections(List<String> fieldsToProject, MongoCollection<Document> collection);
	
	List<Document> findByFieldsWithProjection(String jsonCriteria, List<String> fieldsToProject, MongoCollection<Document> collection);
	
	void deleteAll(MongoCollection<Document> collection);
	
	void insertOne(String json, MongoCollection<Document> collection);

	void deleteOneById(String id, MongoCollection<Document> collection);
	
	void deleteManyByCriteria(String jsonCriteria, MongoCollection<Document> collection);

	void replaceOneById(String id, String jsonUpdate, MongoCollection<Document> collection);	

	void insertJsonData(InputStream ruta, MongoCollection<Document> collection);
}
