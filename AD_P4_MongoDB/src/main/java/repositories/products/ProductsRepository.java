package repositories.products;

import java.util.ArrayList;
import java.util.List;
import org.bson.Document;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
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
	
}
