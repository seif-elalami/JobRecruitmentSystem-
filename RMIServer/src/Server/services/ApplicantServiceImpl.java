package Server.services;

import Server.database.MongoDBConnection;
import Server.utils.ValidationUtil;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;
import org.bson.Document;
import org.bson.types.ObjectId;
import shared.interfaces.IApplicantService;
import shared.models.Applicant;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ApplicantServiceImpl extends UnicastRemoteObject implements IApplicantService {

    private final MongoCollection<Document> applicantCollection;
    private final MongoCollection<Document> userCollection;

    public ApplicantServiceImpl() throws RemoteException {
        super();
        MongoDatabase database = MongoDBConnection.getInstance().getDatabase();
        applicantCollection = database.getCollection("applicants");
        userCollection = database.getCollection("users");
        System.out.println("✅ ApplicantService initialized");
    }

    @Override
    public String createApplicant(Applicant applicant) throws RemoteException {
        try {
            if (applicant.getPhone() != null && !applicant.getPhone().isEmpty()) {
                if (!ValidationUtil.isValidPhone(applicant.getPhone())) {
                    throw new RemoteException(ValidationUtil.getPhoneErrorMessage());
                }
            }

            if (!ValidationUtil.isValidEmail(applicant.getEmail())) {
                throw new RemoteException(ValidationUtil.getEmailErrorMessage());
            }

            ObjectId objectId;
            if (applicant.getId() != null && !applicant.getId().isEmpty()) {
                try {
                    objectId = new ObjectId(applicant.getId());
                } catch (IllegalArgumentException ignored) {
                    objectId = new ObjectId();
                }
            } else {
                objectId = new ObjectId();
            }

            applicant.setId(objectId.toString());
            upsertApplicantDocument(objectId, applicant);

            System.out.println("✅ Applicant created: " + applicant.getEmail() + " (ID: " + objectId + ")");
            return objectId.toString();
        } catch (RemoteException e) {
            throw e;
        } catch (Exception e) {
            System.err.println("❌ Error creating applicant: " + e.getMessage());
            e.printStackTrace();
            throw new RemoteException("Failed to create applicant", e);
        }
    }

    @Override
    public Applicant getApplicantById(String id) throws RemoteException {
        try {
            Document doc = null;
            ObjectId objectId = null;

            try {
                objectId = new ObjectId(id);
                doc = applicantCollection.find(new Document("_id", objectId)).first();
            } catch (IllegalArgumentException ignored) {

            }

            if (doc == null && id != null) {
                doc = applicantCollection.find(new Document("email", id)).first();
            }

            if (doc != null) {
                return documentToApplicant(doc);
            }

            if (objectId != null) {
                Document userDoc = userCollection.find(new Document("_id", objectId)).first();
                if (userDoc != null) {
                    Applicant applicant = documentToApplicantFromUser(userDoc);
                    applicant.setId(objectId.toString());
                    upsertApplicantDocument(objectId, applicant);
                    return applicant;
                }
            }

            if (objectId == null && id != null) {
                Document userDoc = userCollection.find(new Document("email", id)).first();
                if (userDoc != null) {
                    ObjectId newId = userDoc.getObjectId("_id");
                    Applicant applicant = documentToApplicantFromUser(userDoc);
                    applicant.setId(newId.toString());
                    upsertApplicantDocument(newId, applicant);
                    return applicant;
                }
            }

            return null;
        } catch (Exception e) {
            System.err.println("❌ Error getting applicant: " + e.getMessage());
            throw new RemoteException("Failed to get applicant", e);
        }
    }

    @Override
    public Applicant getApplicantByEmail(String email) throws RemoteException {
        try {
            Document doc = applicantCollection.find(new Document("email", email)).first();

            if (doc != null) {
                return documentToApplicant(doc);
            }

            Document userDoc = userCollection.find(new Document("email", email)).first();
            if (userDoc != null) {
                Applicant applicant = documentToApplicantFromUser(userDoc);
                ObjectId objectId = userDoc.getObjectId("_id");
                applicant.setId(objectId.toString());
                upsertApplicantDocument(objectId, applicant);
                return applicant;
            }

            return null;
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
            System.err.println("❌ Error getting applicants: " + e.getMessage());
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
            System.out.println("✅ Found " + applicants.size() + " applicants with skill: " + skill);
            return applicants;
        } catch (Exception e) {
            System.err.println("❌ Error searching applicants: " + e.getMessage());
            throw new RemoteException("Failed to search applicants", e);
        }
    }

    @Override
    public boolean updateApplicant(Applicant applicant) throws RemoteException {
        try {
            if (applicant.getPhone() != null && !applicant.getPhone().isEmpty()) {
                if (!ValidationUtil.isValidPhone(applicant.getPhone())) {
                    System.err.println("❌ Update applicant failed: " + ValidationUtil.getPhoneErrorMessage());
                    throw new RemoteException(ValidationUtil.getPhoneErrorMessage());
                }
            }

            if (!ValidationUtil.isValidEmail(applicant.getEmail())) {
                System.err.println("❌ Update applicant failed: " + ValidationUtil.getEmailErrorMessage());
                throw new RemoteException(ValidationUtil.getEmailErrorMessage());
            }

            ObjectId objectId = null;

            if (applicant.getEmail() != null) {
                Document existing = applicantCollection.find(new Document("email", applicant.getEmail())).first();
                if (existing != null && existing.getObjectId("_id") != null) {
                    objectId = existing.getObjectId("_id");
                    applicant.setId(objectId.toString());
                }
            }

            if (objectId == null && applicant.getId() != null && !applicant.getId().isEmpty()) {
                try {
                    objectId = new ObjectId(applicant.getId());
                } catch (IllegalArgumentException ignored) {

                }
            }

            if (objectId == null) {
                objectId = new ObjectId();
                applicant.setId(objectId.toString());
            }

            upsertApplicantDocument(objectId, applicant);

            updateUserDocument(objectId, applicant);

            System.out.println("✅ Applicant updated: " + applicant.getEmail());
            return true;
        } catch (RemoteException e) {
            throw e;
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
            System.out.println("✅ Applicant deleted: " + id);
            return true;
        } catch (Exception e) {
            System.err.println("❌ Error deleting applicant: " + e.getMessage());
            return false;
        }
    }

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

    @Override
    public boolean uploadResume(String applicantId, String resumeText) throws RemoteException {
        try {
            if (applicantId == null || applicantId.isEmpty()) {
                throw new RemoteException("Applicant ID is required");
            }
            boolean ok = Server.database.ResumeDAO.getInstance().upsertResume(applicantId, resumeText);
            if (!ok) {
                throw new RemoteException("Resume upsert failed");
            }
            return true;
        } catch (RemoteException re) {
            throw re;
        } catch (Exception e) {
            System.err.println("❌ Error uploading resume: " + e.getMessage());
            e.printStackTrace();
            throw new RemoteException("Failed to upload resume", e);
        }
    }

    @Override
    public String getResume(String applicantId) throws RemoteException {
        try {
            MongoDatabase db = Server.database.MongoDBConnection.getInstance().getDatabase();
            MongoCollection<Document> resumes = db.getCollection("resumes");
            Document doc = resumes.find(new Document("applicantId", applicantId)).first();
            if (doc == null) {
                return null;
            }
            return doc.getString("content");
        } catch (Exception e) {
            System.err.println("❌ Error fetching resume: " + e.getMessage());
            e.printStackTrace();
            throw new RemoteException("Failed to get resume", e);
        }
    }

    private Applicant documentToApplicant(Document doc) {
        Applicant applicant = new Applicant();
        applicant.setId(doc.getObjectId("_id").toString());
        applicant.setName(doc.getString("name"));
        applicant.setEmail(doc.getString("email"));
        applicant.setPhone(doc.getString("phone"));
        applicant.setResume(doc.getString("resume"));
        applicant.setSkills(normalizeSkillsToString(doc.get("skills")));
        applicant.setEducation(doc.getString("education"));
        applicant.setExperience(parseExperience(doc.get("experience")));
        return applicant;
    }

    private Applicant documentToApplicantFromUser(Document doc) {
        Applicant applicant = new Applicant();
        applicant.setId(doc.getObjectId("_id").toString());
        applicant.setName(doc.getString("username"));
        applicant.setEmail(doc.getString("email"));
        applicant.setPhone(doc.getString("phone"));
        applicant.setSkills(normalizeSkillsToString(doc.get("skills")));
        applicant.setExperience(parseExperience(doc.get("experience")));
        return applicant;
    }

    private void upsertApplicantDocument(ObjectId objectId, Applicant applicant) {
        Document payload = new Document("_id", objectId);
        payload.append("name", applicant.getName());
        payload.append("email", applicant.getEmail());
        payload.append("phone", applicant.getPhone());
        payload.append("resume", applicant.getResume());
        payload.append("skills", normalizeSkillsToString(applicant.getSkills()));
        payload.append("education", applicant.getEducation());
        int years = applicant.getYearsExperience();
        if (years == 0 && applicant.getExperience() != null) {
            years = parseExperience(applicant.getExperience());
        }
        payload.append("experience", Integer.toString(years));
        applicantCollection.replaceOne(new Document("_id", objectId), payload, new UpdateOptions().upsert(true));
    }

    private void updateUserDocument(ObjectId objectId, Applicant applicant) {
        try {
            Document update = new Document();
            update.append("phone", applicant.getPhone());
            update.append("skills", normalizeSkillsToString(applicant.getSkills()));
            update.append("experience", parseExperience(applicant.getExperience()));

            if (applicant.getEducation() != null && !applicant.getEducation().isEmpty()) {
                update.append("education", applicant.getEducation());
            }

            if (applicant.getResume() != null && !applicant.getResume().isEmpty()) {
                update.append("resume", applicant.getResume());
            }

            Document updateDoc = new Document("$set", update);
            userCollection.updateOne(new Document("_id", objectId), updateDoc);
            System.out.println("✅ User document updated in users collection");
        } catch (Exception e) {
            System.err.println("⚠️ Warning: Could not update user document: " + e.getMessage());
        }
    }

    private String normalizeSkillsToString(Object rawSkills) {
        if (rawSkills == null) {
            return "";
        }

        if (rawSkills instanceof List<?>) {
            List<?> list = (List<?>) rawSkills;
            List<String> normalized = new ArrayList<>();
            for (Object item : list) {
                if (item != null) {
                    normalized.add(item.toString());
                }
            }
            return String.join(",", normalized);
        }

        return Objects.toString(rawSkills, "");
    }

    private int parseExperience(Object rawExperience) {
        if (rawExperience instanceof Number) {
            return ((Number) rawExperience).intValue();
        }

        if (rawExperience instanceof String) {
            try {
                return Integer.parseInt(((String) rawExperience).trim());
            } catch (NumberFormatException ignored) {
                String digits = ((String) rawExperience).replaceAll("[^0-9]", "");
                if (!digits.isEmpty()) {
                    try {
                        return Integer.parseInt(digits);
                    } catch (NumberFormatException ignoredAgain) {
                        return 0;
                    }
                }
            }
        }

        return 0;
    }

}