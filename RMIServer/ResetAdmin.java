import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import Server.database.MongoDBConnection;

public class ResetAdmin {
    public static void main(String[] args) {
        try {
            MongoDatabase database = MongoDBConnection.getInstance().getDatabase();
            MongoCollection<Document> userCollection = database.getCollection("users");
            
            // Delete admin account
            userCollection.deleteOne(new Document("email", "admin@jobsystem.com"));
            System.out.println("✅ Admin account deleted. Now run AdminSeeder again.");
        } catch (Exception e) {
            System.err.println("❌ Error: " + e.getMessage());
        }
    }
}
