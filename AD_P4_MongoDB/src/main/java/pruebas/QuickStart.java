package pruebas;

import static com.mongodb.client.model.Filters.*;

import org.bson.Document;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import mongoDB.MongoDB;

public class QuickStart {
	
	public static String pretty(String json) {
		JsonElement je = JsonParser.parseString(json);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(je);
	}
	
	public static void main(String[] args) {		
		MongoClient mongoClient = MongoDB.getClient();
		MongoDatabase database = mongoClient.getDatabase("sample_mflix");
        MongoCollection<Document> collection = database.getCollection("movies");
        Document doc = collection.find(eq("title", "Back to the Future")).first();
        if (doc != null) {
            System.out.println(doc.toJson());
            //System.out.println(pretty(doc.toJson()));            
        } else {
            System.out.println("No matching documents found.");
        }
	}

}
