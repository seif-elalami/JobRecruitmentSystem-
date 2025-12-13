import Server.database.MongoDBConnection;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class UpdateUserRole {
    public static void main(String[] args) {
        try {
            MongoDatabase database = MongoDBConnection.getInstance().getDatabase();
            MongoCollection<Document> collection = database.getCollection("users");
            
            // Update hamad@gmail.com to RECRUITER
            collection.updateOne(
                new Document("email", "hamad@gmail.com"),
                new Document("$set", new Document("role", "RECRUITER"))
            );
            
            System.out.println("✅ Updated hamad@gmail.com to RECRUITER");

            // Verify
            Document user = collection.find(new Document("email", "hamad@gmail.com")).first();
            System.out.println("New Role: " + user.getString("role"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
