import client.ui.ConsoleUI;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class Main {

    public static void main(String[] args) {
        // Check MongoDB connection
        if (!checkMongoDB()) {
            System. err.println("\n❌ MongoDB is not running!");
            System.err.println("💡 Start MongoDB first:");
            System.err.println("   mongod --port 27020 --dbpath <your-path>");
            System.err.println("\nPress Enter to exit...");
            try {
                System.in.read();
            } catch (Exception e) {
                // Ignore
            }
            return;
        }

        // Start the console UI
        ConsoleUI ui = new ConsoleUI();
        ui.start();
    }

    /**
     * Check if MongoDB is running
     */
    private static boolean checkMongoDB() {
        MongoClient testClient = null;
        try {
            System.out.print("🔍 Checking MongoDB...  ");
            testClient = new MongoClient("localhost", 27020);
            MongoDatabase database = testClient.getDatabase("JobRecruitmentDB");
            database.runCommand(new Document("ping", 1));
            System. out.println("✅");
            return true;
        } catch (Exception e) {
            System.out.println("❌");
            return false;
        } finally {
            if (testClient != null) {
                try {
                    testClient.close();
                } catch (Exception e) {
                    // Ignore
                }
            }
        }
    }
}
