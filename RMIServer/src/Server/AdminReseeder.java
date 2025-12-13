package Server;

import Server.database.MongoDBConnection;
import Server.utils.PasswordUtil;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.Date;

/**
 * Delete admin account and recreate it with password verification test
 */
public class AdminReseeder {

    public static void main(String[] args) {
        try {
            String adminEmail = "admin@jobsystem.com";
            String adminPassword = "Admin@123456";
            String adminUsername = "Admin";

            System.out.println("🔑 Admin Resetseeder");
            System.out.println("================================================");

            MongoDatabase database = MongoDBConnection.getInstance().getDatabase();
            MongoCollection<Document> userCollection = database.getCollection("users");

            System.out.println("\n📍 Step 1: Checking and deleting existing admin...");
            long deleted = userCollection.deleteMany(new Document("email", adminEmail)).getDeletedCount();
            System.out.println("   Deleted " + deleted + " existing admin(s)");

            System.out.println("\n📍 Step 2: Creating password hash...");
            String hashedPassword = PasswordUtil.hashPassword(adminPassword);
            System.out.println("   Hash: " + hashedPassword);
            System.out.println("   Length: " + hashedPassword.length());

            System.out.println("\n📍 Step 3: Testing password verification...");
            boolean testMatch = PasswordUtil.verifyPassword(adminPassword, hashedPassword);
            System.out.println("   Verification result: " + (testMatch ? "✅ PASS" : "❌ FAIL"));

            if (!testMatch) {
                System.err.println("❌ ERROR: Password verification failed even for newly created hash!");
                System.err.println("   This indicates an issue with PasswordUtil");
                System.exit(1);
            }

            System.out.println("\n📍 Step 4: Inserting admin into database...");
            Document adminDoc = new Document()
                    .append("username", adminUsername)
                    .append("email", adminEmail)
                    .append("password", hashedPassword)
                    .append("role", "Admin")
                    .append("phone", "+1-555-ADMIN-01")
                    .append("createdAt", new Date())
                    .append("lastLogin", null)
                    .append("isActive", true);

            userCollection.insertOne(adminDoc);
            System.out.println("   ✅ Admin inserted into database");

            System.out.println("\n📍 Step 5: Reading admin from database and testing...");
            Document readAdmin = userCollection.find(new Document("email", adminEmail)).first();
            if (readAdmin == null) {
                System.err.println("❌ ERROR: Could not read admin back from database!");
                System.exit(1);
            }

            String storedHash = readAdmin.getString("password");
            System.out.println("   Stored hash: " + storedHash);
            System.out.println("   Length: " + storedHash.length());
            System.out.println("   Matches original hash? " + storedHash.equals(hashedPassword));

            System.out.println("\n📍 Step 6: Testing password verification with stored hash...");
            boolean finalMatch = PasswordUtil.verifyPassword(adminPassword, storedHash);
            System.out.println("   Verification result: " + (finalMatch ? "✅ PASS" : "❌ FAIL"));

            if (finalMatch) {
                System.out.println("\n✅ SUCCESS! Admin account is ready.");
                System.out.println("================================================");
                System.out.println("📋 Admin Credentials:");
                System.out.println("   Email:    " + adminEmail);
                System.out.println("   Password: " + adminPassword);
                System.out.println("   Role:     Admin");
                System.out.println("================================================");
            } else {
                System.err.println("\n❌ FAILURE: Password verification failed!");
                System.err.println("   Hash stored in DB: " + storedHash.substring(0, 30) + "...");
                System.err.println("   Hash length: " + storedHash.length());
            }

        } catch (Exception e) {
            System.err.println("❌ Error: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}