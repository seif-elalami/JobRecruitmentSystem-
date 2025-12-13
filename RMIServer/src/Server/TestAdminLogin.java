package Server;

import Server.database.MongoDBConnection;
import Server.utils.PasswordUtil;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class TestAdminLogin {
    public static void main(String[] args) {
        try {
            String testEmail = "admin@jobsystem.com";
            String testPassword = "Admin@123456";

            System.out.println("🔐 Testing Admin Login");
            System.out.println("================================================");
            System.out.println("Email: " + testEmail);
            System.out.println("Password: " + testPassword);
            System.out.println();

            MongoDatabase database = MongoDBConnection.getInstance().getDatabase();
            MongoCollection<Document> userCollection = database.getCollection("users");

            System.out.println("📍 Step 1: Finding user in database...");
            Document query = new Document("email", testEmail);
            Document userDoc = userCollection.find(query).first();

            if (userDoc == null) {
                System.out.println("❌ User not found in database!");
                return;
            }

            System.out.println("✅ User found!");
            System.out.println("   Username: " + userDoc.getString("username"));
            System.out.println("   Role: " + userDoc.getString("role"));
            System.out.println();

            System.out.println("📍 Step 2: Testing password verification...");
            String storedHash = userDoc.getString("password");
            System.out.println("   Stored hash (first 30 chars): " + storedHash.substring(0, 30));
            System.out.println("   Hash length: " + storedHash.length());
            System.out.println();

            System.out.println("📍 Step 3: Verifying password...");
            boolean matches = PasswordUtil.verifyPassword(testPassword, storedHash);

            System.out.println();
            if (matches) {
                System.out.println("✅ SUCCESS! Admin login would succeed!");
            } else {
                System.out.println("❌ FAILURE! Password verification failed!");
            }

        } catch (Exception e) {
            System.err.println("❌ Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}