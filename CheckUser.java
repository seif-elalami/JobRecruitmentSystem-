import Server.database.MongoDBConnection;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class CheckUser {
    public static void main(String[] args) {
        try {
            MongoDatabase database = MongoDBConnection.getInstance().getDatabase();
            MongoCollection<Document> collection = database.getCollection("users");
            
            // Check hamad@gmail.com
            Document user = collection.find(new Document("email", "hamad@gmail.com")).first();
            
            if (user != null) {
                System.out.println("User: " + user.getString("email"));
                System.out.println("Role: " + user.getString("role"));
            } else {
                System.out.println("User not found");
            }
            
            // List all recruiters
            System.out.println("\nAll Recruiters:");
            for(Document doc : collection.find(new Document("role", "RECRUITER"))) {
                 System.out.println("- " + doc.getString("email"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
