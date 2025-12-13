package Server.database;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

public class MongoDBConnection {
    private static MongoDBConnection instance;
    private MongoClient mongoClient;
    private MongoDatabase database;

    private static final String HOST = "localhost";
    private static final int PORT = 27020;
    private static final String DATABASE_NAME = "JobRecruitmentDB";

    private MongoDBConnection() {
        try {
            System.out.println("🔌 Connecting to MongoDB...");
            System.out.println("   Host: " + HOST);
            System.out.println("   Port: " + PORT);
            System.out.println("   Database: " + DATABASE_NAME);
            
            mongoClient = new MongoClient(HOST, PORT);
            database = mongoClient.getDatabase(DATABASE_NAME);
            
            // Test the connection by pinging the database
            database.runCommand(new org.bson.Document("ping", 1));
            
            System.out.println("✅ Connected to MongoDB successfully!");
            System.out.println("   Host: " + HOST);
            System.out.println("   Port: " + PORT);
            System.out.println("   Database: " + DATABASE_NAME);
        } catch (Exception e) {
            System.err.println("❌ MongoDB Connection Error: " + e.getMessage());
            System.err.println("   Make sure MongoDB is running on port " + PORT);
            e.printStackTrace();
            database = null; // Explicitly set to null on failure
        }
    }

    public static synchronized MongoDBConnection getInstance() {
        if (instance == null) {
            instance = new MongoDBConnection();
        }
        return instance;
    }

    public MongoDatabase getDatabase() {
        if (database == null) {
            System.err.println("❌ ERROR: Database connection is null!");
            System.err.println("   Attempting to reconnect...");
            try {
                mongoClient = new MongoClient(HOST, PORT);
                database = mongoClient.getDatabase(DATABASE_NAME);
                System.out.println("✅ Reconnected to MongoDB successfully!");
            } catch (Exception e) {
                System.err.println("❌ Failed to reconnect: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return database;
    }

    public void close() {
        if (mongoClient != null) {
            mongoClient.close();
            System.out.println("MongoDB connection closed.");
        }
    }
}
