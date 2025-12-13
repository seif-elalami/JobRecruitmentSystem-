package Server.services;

import Server.database.MongoDBConnection;
import Server.utils.ValidationUtil;
import shared.interfaces.IApplicantService;
import shared.models.Applicant;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson. types.ObjectId;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class ApplicantServiceImpl extends UnicastRemoteObject implements IApplicantService {

    private MongoCollection<Document> applicantCollection;

    public ApplicantServiceImpl() throws RemoteException {
        super();
        MongoDatabase database = MongoDBConnection.getInstance().getDatabase();
        applicantCollection = database.getCollection("applicants");
        System.out.println("✅ ApplicantService initialized");
    }

    @Override
    public String createApplicant(Applicant applicant) throws RemoteException {
        try {
            // ============================================
            // VALIDATION - Phone Number (if provided)
            // ============================================
            if (applicant.getPhone() != null && !applicant.getPhone().isEmpty()) {
                if (!ValidationUtil.isValidPhone(applicant.getPhone())) {
                    System. err.println("❌ Create applicant failed: " + ValidationUtil.getPhoneErrorMessage());
                    throw new RemoteException(ValidationUtil.getPhoneErrorMessage());
                }
            }

            // ============================================
            // VALIDATION - Email Format
            // ============================================
            if (!ValidationUtil.isValidEmail(applicant.getEmail())) {
                System.err.println("❌ Create applicant failed: " + ValidationUtil.getEmailErrorMessage());
                throw new RemoteException(ValidationUtil.getEmailErrorMessage());
            }

            // Create document
            Document doc = new Document();
            doc.append("name", applicant.getName());
            doc.append("email", applicant. getEmail());
            doc.append("phone", applicant.getPhone());
            doc.append("resume", applicant.getResume());
            doc.append("skills", applicant.getSkills());
            doc.append("education", applicant.getEducation());
            doc.append("experience", applicant.getExperience());

            applicantCollection.insertOne(doc);

            String id = doc.getObjectId("_id").toString();
            applicant.setId(id);

            System.out.println("✅ Applicant created:  " + applicant.getName() + " (ID: " + id + ")");

            return id;

        } catch (RemoteException e) {
            throw e;  // Re-throw validation errors
        } catch (Exception e) {
            System.err.println("❌ Error creating applicant: " + e.getMessage());
            e.printStackTrace();
            throw new RemoteException("Failed to create applicant", e);
        }
    }

    @Override
    public Applicant getApplicantById(String id) throws RemoteException {
        try {
            Document doc = applicantCollection.find(new Document("_id", new ObjectId(id))).first();

            if (doc == null) {
                return null;
            }

            return documentToApplicant(doc);

        } catch (Exception e) {
            System.err.println("❌ Error getting applicant: " + e.getMessage());
            throw new RemoteException("Failed to get applicant", e);
        }
    }

    @Override
    public Applicant getApplicantByEmail(String email) throws RemoteException {
        try {
            Document doc = applicantCollection.find(new Document("email", email)).first();

            if (doc == null) {
                return null;
            }

            return documentToApplicant(doc);

        } catch (Exception e) {
            System.err.println("❌ Error getting applicant: " + e.getMessage());
            throw new RemoteException("Failed to get applicant", e);
        }
    }

    @Override
    public List<Applicant> getAllApplicants() throws RemoteException {
        try {
            List<Applicant> applicants = new ArrayList<>();

            for (Document doc : applicantCollection.find()) {
                applicants.add(documentToApplicant(doc));
            }

            System.out.println("✅ Retrieved " + applicants.size() + " applicants");

            return applicants;

        } catch (Exception e) {
            System.err.println("❌ Error getting applicants:  " + e.getMessage());
            throw new RemoteException("Failed to get applicants", e);
        }
    }

    @Override
    public List<Applicant> searchApplicantsBySkills(String skill) throws RemoteException {
        try {
            List<Applicant> applicants = new ArrayList<>();

            Document query = new Document("skills", new Document("$regex", skill).append("$options", "i"));

            for (Document doc : applicantCollection.find(query)) {
                applicants.add(documentToApplicant(doc));
            }

            System.out.println("✅ Found " + applicants.size() + " applicants with skill:  " + skill);

            return applicants;

        } catch (Exception e) {
            System.err.println("❌ Error searching applicants: " + e.getMessage());
            throw new RemoteException("Failed to search applicants", e);
        }
    }

    @Override
    public boolean updateApplicant(Applicant applicant) throws RemoteException {
        try {
            // ============================================
            // VALIDATION - Phone Number (if provided)
            // ============================================
            if (applicant.getPhone() != null && !applicant.getPhone().isEmpty()) {
                if (!ValidationUtil.isValidPhone(applicant.getPhone())) {
                    System.err.println("❌ Update applicant failed: " + ValidationUtil.getPhoneErrorMessage());
                    throw new RemoteException(ValidationUtil.getPhoneErrorMessage());
                }
            }

            // ============================================
            // VALIDATION - Email Format
            // ============================================
            if (!ValidationUtil.isValidEmail(applicant.getEmail())) {
                System.err.println("❌ Update applicant failed: " + ValidationUtil.getEmailErrorMessage());
                throw new RemoteException(ValidationUtil.getEmailErrorMessage());
            }

            Document query = new Document("_id", new ObjectId(applicant.getId()));

            Document update = new Document();
            update.append("name", applicant.getName());
            update.append("email", applicant.getEmail());
            update.append("phone", applicant.getPhone());
            update.append("resume", applicant.getResume());
            update.append("skills", applicant.getSkills());
            update.append("education", applicant.getEducation());
            update.append("experience", applicant.getExperience());

            Document updateDoc = new Document("$set", update);

            applicantCollection.updateOne(query, updateDoc);

            System.out.println("✅ Applicant updated: " + applicant.getName());

            return true;

        } catch (RemoteException e) {
            throw e; // Re-throw validation errors
        } catch (Exception e) {
            System.err.println("❌ Error updating applicant: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
public List<Applicant> searchApplicantsByExperience(String experience) throws RemoteException {
    try {
        List<Applicant> applicants = new ArrayList<>();

        // Search using regex (case-insensitive)
        Document query = new Document("experience", new Document("$regex", experience).append("$options", "i"));

        for (Document doc : applicantCollection.find(query)) {
            applicants.add(documentToApplicant(doc));
        }

        System.out.println("✅ Found " + applicants.size() + " applicant(s) with experience: " + experience);

        return applicants;

    } catch (Exception e) {
        System.err.println("❌ Error searching applicants by experience: " + e.getMessage());
        throw new RemoteException("Failed to search applicants", e);
    }
}
    @Override
    public boolean deleteApplicant(String id) throws RemoteException {
        try {
            Document query = new Document("_id", new ObjectId(id));
            applicantCollection.deleteOne(query);

            System.out. println("✅ Applicant deleted: " + id);

            return true;

        } catch (Exception e) {
            System.err.println("❌ Error deleting applicant: " + e.getMessage());
            return false;
        }
    }

    private Applicant documentToApplicant(Document doc) {
        Applicant applicant = new Applicant();
        applicant.setId(doc.getObjectId("_id").toString());
        applicant.setName(doc.getString("name"));
        applicant.setEmail(doc.getString("email"));
        applicant.setPhone(doc. getString("phone"));
        applicant.setResume(doc.getString("resume"));

        @SuppressWarnings("unchecked")
        List<String> skills = (List<String>) doc.get("skills");
        if (skills != null) {
            for (String skill : skills) {
                applicant.addSkill(skill);
            }
        }

        applicant.setEducation(doc. getString("education"));
        applicant.setExperience(doc. getInteger("experience", 0));

        return applicant;
    
    }

    // ========================================
    // NOTIFICATIONS
    // ========================================

    @Override
    public List<shared.models.Notification> getNotifications(String applicantId) throws RemoteException {
        try {
            return Server.database.NotificationDAO.getInstance().getNotificationsByRecipientId(applicantId);
        } catch (Exception e) {
            System.err.println("❌ Error getting notifications: " + e.getMessage());
            e.printStackTrace();
            throw new RemoteException("Failed to get notifications", e);
        }
    }
}

