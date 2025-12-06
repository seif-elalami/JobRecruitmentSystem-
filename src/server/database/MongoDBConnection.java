package server.database;

import com. mongodb.MongoClient;
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
            mongoClient = new MongoClient(HOST, PORT);
            database = mongoClient.getDatabase(DATABASE_NAME);
            System.out.println("✅ Connected to MongoDB successfully!");
            System.out.println("   Host: " + HOST);
            System.out.println("   Port: " + PORT);
            System.out.println("   Database: " + DATABASE_NAME);
        } catch (Exception e) {
            System.err.println("❌ MongoDB Connection Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static synchronized MongoDBConnection getInstance() {
        if (instance == null) {
            instance = new MongoDBConnection();
        }
        return instance;
    }

    public MongoDatabase getDatabase() {
        return database;
    }

    public void close() {
        if (mongoClient != null) {
            mongoClient.close();
            System.out.println("MongoDB connection closed.");
        }
    }
}
