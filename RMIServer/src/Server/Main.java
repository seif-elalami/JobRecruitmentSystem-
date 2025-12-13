package Server;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class Main {


    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║    Job Recruitment System - Server    ║");
        System.out.println("╚════════════════════════════════════════╝");
        System.out.println();

        // Check MongoDB connection first
        if (! checkMongoDB()) {
            System.err.println("\n❌ Cannot start server:  MongoDB is not running!");
            System.err.println("\n💡 Start MongoDB first:");
            System.err.println("   mongod --port 27020 --dbpath <your-path>");
            System.err.println("\nExample:");
            System.err.println("   mongod --port 27020 --dbpath D:\\mongodb-data\\JobRecruitmentDB");
            System.err.println();
            System.err.println("Press Enter to exit...");
            try {
                System.in.read();
            } catch (Exception e) {
                // Ignore
            }
            System.exit(1);
        }

        // Start RMI Server
        try {
            System.out.println("✅ MongoDB is connected");
            System.out.println("🚀 Starting RMI Server...\n");
            RMIServer.main(null);
        } catch (Exception e) {
            System.err.println("\n❌ Server startup failed!");
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Check if MongoDB is running and accessible
     */
    private static boolean checkMongoDB() {
        MongoClient testClient = null;
        try {
            System.out.print("🔍 Checking MongoDB connection...   ");
            // Use port 27017 to match MongoDBConnection configuration
            testClient = new MongoClient("localhost", 27017);
            MongoDatabase database = testClient.getDatabase("JobRecruitmentDB");

            // Ping the database to verify connection
            database.runCommand(new Document("ping", 1));

            System.out.println("✅");
            return true;

        } catch (Exception e) {
            System.out.println("❌");
            System.err.println("\n⚠️  MongoDB Connection Error:");
            System.err.println("   " + e.getMessage());
            return false;

        } finally {
            if (testClient != null) {
                try {
                    testClient.close();
                } catch (Exception e) {
                    // Ignore closing errors
                }
            }
        }
    }
}
