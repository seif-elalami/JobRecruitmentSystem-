package Server.services;

import Server.database.MongoDBConnection;
import Server.utils.ValidationUtil;
import shared.interfaces.IApplicantService;
import shared.models.Applicant;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ApplicantServiceImpl extends UnicastRemoteObject implements IApplicantService {

    private MongoCollection<Document> applicantCollection;
    private MongoCollection<Document> userCollection;

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

            ObjectId objectId = applicant.getId() != null ? new ObjectId(applicant.getId()) : new ObjectId();
            applicant.setId(objectId.toString());

            upsertApplicantDocument(objectId, applicant);

            System.out.println("✅ Applicant created:  " + applicant.getName() + " (ID: " + objectId + ")");

            return objectId.toString();

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
            ObjectId objectId = new ObjectId(id);
            Document doc = applicantCollection.find(new Document("_id", objectId)).first();

            if (doc != null) {
                return documentToApplicant(doc);
            }

            Document userDoc = userCollection.find(new Document("_id", objectId)).first();
            if (userDoc != null) {
                Applicant applicant = documentToApplicantFromUser(userDoc);
                applicant.setId(id);
                upsertApplicantDocument(objectId, applicant);
                return applicant;
            }

            return null;

        } catch (IllegalArgumentException e) {
            System.err.println("❌ Invalid applicant ID format: " + id);
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

            ObjectId objectId = applicant.getId() != null ? new ObjectId(applicant.getId()) : null;

            if (objectId == null && applicant.getEmail() != null) {
                Document existing = applicantCollection.find(new Document("email", applicant.getEmail())).first();
                if (existing != null && existing.getObjectId("_id") != null) {
                    objectId = existing.getObjectId("_id");
                    applicant.setId(objectId.toString());
                }
            }

            if (objectId == null) {
                throw new RemoteException("Applicant ID is required for update");
            }

            upsertApplicantDocument(objectId, applicant);

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
