import Server.database.MongoDBConnection;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class CheckYoussef {
    public static void main(String[] args) {
        try {
            MongoDatabase database = MongoDBConnection.getInstance().getDatabase();
            MongoCollection<Document> collection = database.getCollection("users");
            
            // Check youssef@gmail.com
            Document user = collection.find(new Document("email", "youssef@gmail.com")).first();
            
            if (user != null) {
                System.out.println("User: " + user.getString("email"));
                System.out.println("Role: " + user.getString("role"));
            } else {
                System.out.println("User not found");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
