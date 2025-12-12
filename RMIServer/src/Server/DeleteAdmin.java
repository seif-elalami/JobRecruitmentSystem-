package Server;

import Server.database.MongoDBConnection;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class DeleteAdmin {
    public static void main(String[] args) {
        try {
            MongoDatabase database = MongoDBConnection.getInstance().getDatabase();
            MongoCollection<Document> userCollection = database.getCollection("users");
            
            long deleted = userCollection.deleteMany(new Document("email", "admin@jobsystem.com")).getDeletedCount();
            System.out.println("Deleted " + deleted + " admin(s)");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
