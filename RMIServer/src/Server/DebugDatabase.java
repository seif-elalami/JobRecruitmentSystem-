package Server;

import Server.database.MongoDBConnection;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

/**
 * Debug utility to check what's in the users collection
 */
public class DebugDatabase {

    public static void main(String[] args) {
        try {
            System.out.println("🔍 Database Debug - Checking users collection...");
            System.out.println("================================================");

            // Connect to MongoDB
            MongoDatabase database = MongoDBConnection.getInstance().getDatabase();
            MongoCollection<Document> userCollection = database.getCollection("users");

            long userCount = userCollection.countDocuments();
            System.out.println("📊 Total users in database: " + userCount);
            System.out.println("================================================\n");

            // Print all users
            for (Document doc : userCollection.find()) {
                System.out.println("👤 User Document:");
                System.out.println("   ID:       " + doc.getObjectId("_id"));
                System.out.println("   Username: " + doc.getString("username"));
                System.out.println("   Email:    " + doc.getString("email"));
                System.out.println("   Role:     " + doc.getString("role"));
                String password = doc.getString("password");
                System.out.println("   Password: " + (password != null ? password.substring(0, Math.min(30, password.length())) + "..." : "NULL"));
                System.out.println("   Active:   " + doc.getBoolean("isActive"));
                System.out.println("   Created:  " + doc.getDate("createdAt"));
                System.out.println();
            }

            System.out.println("================================================");
            System.out.println("✅ Database check complete");

        } catch (Exception e) {
            System.err.println("❌ Error: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
