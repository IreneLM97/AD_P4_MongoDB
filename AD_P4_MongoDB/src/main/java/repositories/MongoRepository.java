package repositories;

import java.util.List;
import org.bson.Document;

import com.mongodb.client.MongoCollection;

public interface MongoRepository {
	
	List<Document> findAll(MongoCollection<Document> collection);

	void deleteOneById(String id, MongoCollection<Document> collection);

	void replaceOneByCriteria(String id, String jsonUpdate, MongoCollection<Document> collection);
	
	

}
