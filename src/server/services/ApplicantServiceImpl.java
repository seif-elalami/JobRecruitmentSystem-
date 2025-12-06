package server.services;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org. bson.types.ObjectId;
import server.database.MongoDBConnection;
import shared.interfaces.IApplicantService;
import shared.models.Applicant;

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

    // ========================================
    // FUNCTION 1: CREATE APPLICANT
    // ========================================
    @Override
    public String createApplicant(Applicant applicant) throws RemoteException {
        try {
            System.out.println("Creating applicant: " + applicant. getName());

            Document doc = new Document();
            doc.append("name", applicant. getName());
            doc.append("email", applicant.getEmail());
            doc.append("phone", applicant.getPhone());
            doc.append("resume", applicant.getResume());
            doc.append("skills", applicant.getSkills());
            doc.append("education", applicant.getEducation());
            doc.append("experience", applicant.getExperience());

            applicantCollection.insertOne(doc);
            String id = doc.getObjectId("_id"). toString();

            System.out.println("✅ Applicant created successfully!");
            System.out.println("   ID: " + id);
            System.out.println("   Name: " + applicant.getName());
            System.out.println("   Email: " + applicant.getEmail());

            return id;

        } catch (Exception e) {
            System.err.println("❌ Error creating applicant: " + e.getMessage());
            throw new RemoteException("Error creating applicant: " + e.getMessage(), e);
        }
    }

    // ========================================
    // FUNCTION 2: GET APPLICANT BY ID
    // ========================================
    @Override
    public Applicant getApplicantById(String id) throws RemoteException {
        try {
            System.out.println("Searching for applicant with ID: " + id);

            Document query = new Document("_id", new ObjectId(id));
            Document doc = applicantCollection.find(query).first();

            if (doc == null) {
                System.out.println("❌ Applicant not found with ID: " + id);
                return null;
            }

            Applicant applicant = documentToApplicant(doc);

            System.out.println("✅ Applicant found!");
            System.out.println("   Name: " + applicant.getName());
            System.out.println("   Email: " + applicant. getEmail());

            return applicant;

        } catch (Exception e) {
            System.err.println("❌ Error getting applicant by ID: " + e.getMessage());
            throw new RemoteException("Error getting applicant by ID: " + e.getMessage(), e);
        }
    }

    // ========================================
    // FUNCTION 3: GET APPLICANT BY EMAIL
    // ========================================
    @Override
    public Applicant getApplicantByEmail(String email) throws RemoteException {
        try {
            System.out.println("Searching for applicant with email: " + email);

            Document query = new Document("email", email);
            Document doc = applicantCollection.find(query).first();

            if (doc == null) {
                System.out.println("❌ Applicant not found with email: " + email);
                return null;
            }

            Applicant applicant = documentToApplicant(doc);

            System.out. println("✅ Applicant found!");
            System.out. println("   Name: " + applicant.getName());

            return applicant;

        } catch (Exception e) {
            System.err.println("❌ Error getting applicant by email: " + e.getMessage());
            throw new RemoteException("Error getting applicant by email: " + e.getMessage(), e);
        }
    }

    // ========================================
    // FUNCTION 4: GET ALL APPLICANTS
    // ========================================
    @Override
    public List<Applicant> getAllApplicants() throws RemoteException {
        try {
            System.out.println("Retrieving all applicants.. .");

            List<Applicant> applicants = new ArrayList<>();

            for (Document doc : applicantCollection. find()) {
                Applicant applicant = documentToApplicant(doc);
                applicants. add(applicant);
            }

            System.out. println("✅ Retrieved " + applicants.size() + " applicants");

            return applicants;

        } catch (Exception e) {
            System.err.println("❌ Error getting all applicants: " + e.getMessage());
            throw new RemoteException("Error getting all applicants: " + e.getMessage(), e);
        }
    }

    // ========================================
    // FUNCTION 5: SEARCH APPLICANTS BY SKILL
    // ========================================
    @Override
    public List<Applicant> searchApplicantsBySkill(String skill) throws RemoteException {
        try {
            System.out. println("Searching for applicants with skill: " + skill);

            List<Applicant> applicants = new ArrayList<>();

            // MongoDB query to find documents where skills array contains the skill
            // Case-insensitive search
            Document query = new Document("skills", new Document("$regex", skill). append("$options", "i"));

            for (Document doc : applicantCollection.find(query)) {
                Applicant applicant = documentToApplicant(doc);
                applicants.add(applicant);
            }

            System.out.println("✅ Found " + applicants.size() + " applicant(s) with skill: " + skill);

            return applicants;

        } catch (Exception e) {
            System.err.println("❌ Error searching applicants by skill: " + e.getMessage());
            throw new RemoteException("Error searching applicants by skill: " + e. getMessage(), e);
        }
    }

    // ========================================
    // FUNCTION 6: UPDATE APPLICANT
    // ========================================
    @Override
    public boolean updateApplicant(Applicant applicant) throws RemoteException {
        try {
            System. out.println("Updating applicant: " + applicant.getName());

            Document updateDoc = new Document("name", applicant.getName())
                    .append("email", applicant. getEmail())
                    .append("phone", applicant.getPhone())
                    .append("resume", applicant.getResume())
                    .append("skills", applicant.getSkills())
                    .append("education", applicant.getEducation())
                    .append("experience", applicant.getExperience());

            Document query = new Document("_id", new ObjectId(applicant.getId()));
            Document update = new Document("$set", updateDoc);

            long modifiedCount = applicantCollection.updateOne(query, update). getModifiedCount();

            if (modifiedCount > 0) {
                System. out.println("✅ Applicant updated successfully");
                return true;
            } else {
                System.out. println("❌ Applicant not found or not modified");
                return false;
            }

        } catch (Exception e) {
            System.err.println("❌ Error updating applicant: " + e.getMessage());
            throw new RemoteException("Error updating applicant: " + e.getMessage(), e);
        }
    }

    // ========================================
    // FUNCTION 7: DELETE APPLICANT
    // ========================================
    @Override
    public boolean deleteApplicant(String id) throws RemoteException {
        try {
            System.out.println("Deleting applicant with ID: " + id);

            Document query = new Document("_id", new ObjectId(id));
            long deletedCount = applicantCollection. deleteOne(query).getDeletedCount();

            if (deletedCount > 0) {
                System.out.println("✅ Applicant deleted successfully");
                return true;
            } else {
                System.out.println("❌ Applicant not found");
                return false;
            }

        } catch (Exception e) {
            System.err.println("❌ Error deleting applicant: " + e. getMessage());
            throw new RemoteException("Error deleting applicant: " + e.getMessage(), e);
        }
    }

    // ========================================
    // HELPER METHOD
    // ========================================
    @SuppressWarnings("unchecked")
    private Applicant documentToApplicant(Document doc) {
        if (doc == null) {
            return null;
        }

        Applicant applicant = new Applicant();
        applicant.setId(doc.getObjectId("_id").toString());
        applicant.setName(doc.getString("name"));
        applicant.setEmail(doc. getString("email"));
        applicant.setPhone(doc.getString("phone"));
        applicant.setResume(doc.getString("resume"));
        applicant.setEducation(doc.getString("education"));

        Integer experience = doc.getInteger("experience");
        applicant.setExperience(experience != null ? experience : 0);

        List<String> skills = (List<String>) doc.get("skills");
        applicant.setSkills(skills != null ? skills : new ArrayList<>());

        return applicant;
    }
}
