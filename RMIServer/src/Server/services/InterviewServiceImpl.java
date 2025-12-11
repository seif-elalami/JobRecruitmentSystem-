package Server.services;

import Server.database.MongoDBConnection;
import shared.models.Interview;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types. ObjectId;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Helper class for Interview operations
 * Used by RecruiterServiceImpl
 */
public class InterviewServiceImpl {

    private MongoCollection<Document> interviewCollection;

    public InterviewServiceImpl() {
        MongoDatabase database = MongoDBConnection.getInstance().getDatabase();
        interviewCollection = database.getCollection("interviews");
        System.out.println("✅ InterviewService initialized");
    }

    public String createInterview(Interview interview) throws RemoteException {
        try {
            Document doc = new Document();
            doc.append("jobId", interview.getJobId());
            doc.append("applicantId", interview.getApplicantId());
            doc.append("recruiterId", interview.getRecruiterId());
            doc.append("scheduledDate", interview.getScheduledDate());
            doc.append("location", interview.getLocation());
            doc.append("status", "SCHEDULED");
            doc.append("notes", interview.getNotes());
            doc.append("createdAt", new Date());

            interviewCollection.insertOne(doc);

            String id = doc.getObjectId("_id").toString();
            interview.setInterviewId(id);

            System.out.println("✅ Interview created:   ID:  " + id);

            return id;

        } catch (Exception e) {
            System.err.println("❌ Error creating interview:  " + e.getMessage());
            throw new RemoteException("Failed to create interview", e);
        }
    }

    public Interview getInterviewById(String id) throws RemoteException {
        try {
            Document doc = interviewCollection.find(new Document("_id", new ObjectId(id))).first();

            if (doc == null) {
                return null;
            }

            return documentToInterview(doc);

        } catch (Exception e) {
            System.err.println("❌ Error getting interview: " + e.getMessage());
            throw new RemoteException("Failed to get interview", e);
        }
    }

    public List<Interview> getInterviewsByRecruiterId(String recruiterId) throws RemoteException {
        try {
            List<Interview> interviews = new ArrayList<>();

            Document query = new Document("recruiterId", recruiterId);

            for (Document doc : interviewCollection. find(query)) {
                interviews.add(documentToInterview(doc));
            }

            return interviews;

        } catch (Exception e) {
            System.err.println("❌ Error getting interviews: " + e.getMessage());
            throw new RemoteException("Failed to get interviews", e);
        }
    }

    public boolean updateInterview(Interview interview) throws RemoteException {
        try {
            Document query = new Document("_id", new ObjectId(interview.getInterviewId()));

            Document update = new Document();
            update.append("scheduledDate", interview.getScheduledDate());
            update.append("location", interview.getLocation());
            update.append("status", interview.getStatus());
            update. append("notes", interview.getNotes());
            update.append("updatedAt", new Date());

            Document updateDoc = new Document("$set", update);

            interviewCollection.updateOne(query, updateDoc);

            System.out.println("✅ Interview updated:  " + interview.getInterviewId());

            return true;

        } catch (Exception e) {
            System.err.println("❌ Error updating interview: " + e.getMessage());
            return false;
        }
    }

    public boolean cancelInterview(String interviewId) throws RemoteException {
        try {
            Document query = new Document("_id", new ObjectId(interviewId));
            Document update = new Document("$set", new Document("status", "CANCELLED").append("cancelledAt", new Date()));

            interviewCollection.updateOne(query, update);

            System.out.println("✅ Interview cancelled: " + interviewId);

            return true;

        } catch (Exception e) {
            System. err.println("❌ Error cancelling interview: " + e. getMessage());
            return false;
        }
    }

    private Interview documentToInterview(Document doc) {
        Interview interview = new Interview();
        interview. setInterviewId(doc.getObjectId("_id").toString());
        interview.setJobId(doc.getString("jobId"));
        interview. setApplicantId(doc. getString("applicantId"));
        interview.setRecruiterId(doc.getString("recruiterId"));
        interview.setScheduledDate(doc.getDate("scheduledDate"));
        interview.setLocation(doc.getString("location"));
        interview.setStatus(doc.getString("status"));
        interview.setNotes(doc.getString("notes"));

        return interview;
    }
}
