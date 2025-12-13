package Server.database;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;
import org.bson.Document;

import java.util.Date;

/**
 * DAO for storing applicant resumes in a dedicated collection: "resumes"
 * Schema: { applicantId: String, content: String, updatedAt: Date }
 */
public class ResumeDAO {

    private static ResumeDAO instance;
    private final MongoCollection<Document> resumeCollection;

    private ResumeDAO() {
        MongoDatabase database = MongoDBConnection.getInstance().getDatabase();
        resumeCollection = database.getCollection("resumes");
    }

    public static ResumeDAO getInstance() {
        if (instance == null) {
            instance = new ResumeDAO();
        }
        return instance;
    }

    public boolean upsertResume(String applicantId, String content) {
        try {
            Document query = new Document("applicantId", applicantId);
            Document payload = new Document("applicantId", applicantId)
                    .append("content", content)
                    .append("updatedAt", new Date());
            Document update = new Document("$set", payload);
            var result = resumeCollection.updateOne(query, update, new UpdateOptions().upsert(true));
            System.out.println("📄 Resume upsert for applicant=" + applicantId + 
                    ", matched=" + result.getMatchedCount() + ", modified=" + result.getModifiedCount());
            return result.getMatchedCount() > 0 || result.getUpsertedId() != null;
        } catch (Exception e) {
            System.err.println("❌ Error upserting resume: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}