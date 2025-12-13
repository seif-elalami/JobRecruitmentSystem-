package Server;

import Server.database.MongoDBConnection;
import Server.utils.PasswordUtil;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.Date;

/**
 * Utility class to seed the database with an admin account
 * Run this once to create the admin user
 */
public class AdminSeeder {

    public static void main(String[] args) {
        try {

            String adminEmail = "admin@jobsystem.com";
            String adminPassword = "Admin@123456";
            String adminUsername = "Admin";
            String adminPasskey = "ADMIN2024";  // Quick passkey for fast login

            System.out.println("🔑 Admin Seeder - Creating admin account...");
            System.out.println("================================================");

            MongoDatabase database = MongoDBConnection.getInstance().getDatabase();
            MongoCollection<Document> userCollection = database.getCollection("users");

            Document existingAdmin = userCollection.find(new Document("email", adminEmail)).first();
            if (existingAdmin != null) {
                System.out.println("⚠️  Admin account already exists!");
                System.out.println("   Email: " + adminEmail);
                System.out.println("   To reset, delete the admin record and run this seeder again.");
                System.exit(0);
            }

            String hashedPassword = PasswordUtil.hashPassword(adminPassword);

            Document adminDoc = new Document()
                    .append("username", adminUsername)
                    .append("email", adminEmail)
                    .append("password", hashedPassword)  // Store the full hash
                    .append("passkey", adminPasskey)     // Store passkey for quick login
                    .append("role", "Admin")
                    .append("phone", "+1-555-ADMIN-01")
                    .append("createdAt", new Date())
                    .append("lastLogin", null)
                    .append("isActive", true);

            System.out.println("   Hash before DB insert: " + hashedPassword);
            System.out.println("   Hash length: " + hashedPassword.length());

            userCollection.insertOne(adminDoc);

            Document storedAdmin = userCollection.find(new Document("email", adminEmail)).first();
            if (storedAdmin != null) {
                String storedHash = storedAdmin.getString("password");
                System.out.println("   Hash after DB retrieval: " + storedHash);
                System.out.println("   Stored length: " + (storedHash != null ? storedHash.length() : "NULL"));

                boolean verifyResult = PasswordUtil.verifyPassword(adminPassword, storedHash);
                System.out.println("   Password verification: " + (verifyResult ? "✅ SUCCESS" : "❌ FAILED"));

                if (!verifyResult) {
                    System.err.println("\n⚠️  WARNING: Password verification failed even after storage!");
                    System.err.println("   This might indicate a database truncation issue.");
                }
            }

            System.out.println("✅ Admin account created successfully!");
            System.out.println("================================================");
            System.out.println("📋 Admin Account Details:");
            System.out.println("   Username: " + adminUsername);
            System.out.println("   Email:    " + adminEmail);
            System.out.println("   Password: " + adminPassword);
            System.out.println("   Passkey:  " + adminPasskey);
            System.out.println("================================================");
            System.out.println("\n⚠️  IMPORTANT: Save these credentials securely!");
            System.out.println("   - Change the password after first login.");
            System.out.println("   - Use the passkey for quick admin login in the GUI.");
            System.out.println("\nYou can now login with these credentials through the GUI.");

        } catch (Exception e) {
            System.err.println("❌ Error creating admin account: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}