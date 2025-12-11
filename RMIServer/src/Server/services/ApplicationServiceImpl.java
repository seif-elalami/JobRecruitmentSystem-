package Server.services;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;
import Server.database.MongoDBConnection;
import shared.interfaces.IApplicationService;
import shared.models.Application;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class ApplicationServiceImpl extends UnicastRemoteObject implements IApplicationService {

    private MongoCollection<Document> applicationCollection;

    public ApplicationServiceImpl() throws RemoteException {
        super();
        MongoDatabase database = MongoDBConnection.getInstance().getDatabase();
        applicationCollection = database.getCollection("applications");
        System.out.println("✅ ApplicationService initialized");
    }

    // ========================================
    // FUNCTION 1:  SUBMIT APPLICATION
    // ========================================
    @Override
    public String SubmitApplication(Application application) throws RemoteException {
        try {
            System.out.println("Submitting application.. .");
            System.out.println("   Job ID: " + application.getJobId());
            System.out. println("   Applicant ID:  " + application.getApplicantId());

            Document doc = new Document();
            doc.append("jobId", application.getJobId());
            doc.append("applicantId", application.getApplicantId());
            doc.append("applicationDate", application.getApplicationDate());
            doc.append("status", application.getStatus());
            doc.append("coverLetter", application.getCoverLetter());

            applicationCollection.insertOne(doc);
            String id = doc.getObjectId("_id").toString();

            System.out.println("✅ Application submitted successfully!");
            System.out.println("   Application ID: " + id);
            System.out.println("   Status: " + application.getStatus());

            return id;

        } catch (Exception e) {
            System.err.println("❌ Error submitting application:  " + e.getMessage());
            throw new RemoteException("Error submitting application:  " + e.getMessage(), e);
        }
    }

    // ========================================
    // FUNCTION 2: CREATE APPLICATION (Alias for submitApplication)
    // ========================================
    @Override
    public String CreateApplication(Application application) throws RemoteException {
        return SubmitApplication(application);
    }

    // ========================================
    // FUNCTION 3: GET APPLICATION BY ID
    // ========================================
    @Override
    public Application getApplicationById(String id) throws RemoteException {
        try {
            System.out.println("Searching for application with ID: " + id);

            Document query = new Document("_id", new ObjectId(id));
            Document doc = applicationCollection. find(query).first();

            if (doc == null) {
                System.out.println("❌ Application not found with ID: " + id);
                return null;
            }

            Application application = documentToApplication(doc);

            System.out.println("✅ Application found!");
            System.out.println("   Status: " + application.getStatus());

            return application;

        } catch (Exception e) {
            System.err.println("❌ Error getting application by ID: " + e. getMessage());
            throw new RemoteException("Error getting application by ID:  " + e.getMessage(), e);
        }
    }

    // ========================================
    // FUNCTION 4: GET APPLICATIONS BY APPLICANT ID
    // ========================================
    @Override
    public List<Application> getApplicationsByApplicantId(String applicantId) throws RemoteException {
        try {
            System.out.println("Retrieving applications for applicant: " + applicantId);

            List<Application> applications = new ArrayList<>();
            Document query = new Document("applicantId", applicantId);

            for (Document doc : applicationCollection.find(query)) {
                Application application = documentToApplication(doc);
                applications.add(application);
            }

            System.out.println("✅ Found " + applications.size() + " applications for applicant");

            return applications;

        } catch (Exception e) {
            System.err.println("❌ Error getting applications by applicant ID:  " + e.getMessage());
            throw new RemoteException("Error getting applications by applicant ID: " + e.getMessage(), e);
        }
    }

    // ========================================
    // FUNCTION 5: GET ALL APPLICATIONS (RECRUITERS/ADMINS)
    // ========================================
    @Override
    public List<Application> getAllApplications() throws RemoteException {
        try {
            System.out.println("Fetching all applications...");

            List<Application> applications = new ArrayList<>();
            for (Document doc : applicationCollection.find()) {
                Application application = documentToApplication(doc);
                if (application != null) {
                    applications.add(application);
                }
            }

            System.out.println("✅ Found " + applications.size() + " application(s)");
            return applications;

        } catch (Exception e) {
            System. err.println("❌ Error fetching all applications: " + e. getMessage());
            throw new RemoteException("Error fetching all applications:  " + e.getMessage(), e);
        }
    }

    // ========================================
    // FUNCTION 6: GET APPLICATIONS BY JOB ID
    // ========================================
    @Override
    public List<Application> getApplicationsByJobId(String jobId) throws RemoteException {
        try {
            System.out.println("Retrieving applications for job:  " + jobId);

            List<Application> applications = new ArrayList<>();
            Document query = new Document("jobId", jobId);

            for (Document doc : applicationCollection. find(query)) {
                Application application = documentToApplication(doc);
                applications.add(application);
            }

            System.out.println("✅ Found " + applications. size() + " applications for job");

            return applications;

        } catch (Exception e) {
            System.err.println("❌ Error getting applications by job ID: " + e.getMessage());
            throw new RemoteException("Error getting applications by job ID:  " + e.getMessage(), e);
        }
    }

    // ========================================
    // FUNCTION 7: UPDATE APPLICATION STATUS
    // ========================================
    @Override
    public boolean updateApplicationStatus(String applicationId, String status) throws RemoteException {
        try {
            System.out.println("Updating application status.. .");
            System.out.println("   Application ID: " + applicationId);
            System.out.println("   New Status: " + status);

            // Validate status
            if (! status.equals("PENDING") && !status.equals("ACCEPTED") &&
                !status.equals("REJECTED") && !status.equals("UNDER_REVIEW")) {
                System.err.println("❌ Invalid status:  " + status);
                return false;
            }

            Document query = new Document("_id", new ObjectId(applicationId));
            Document update = new Document("$set", new Document("status", status));

            long modifiedCount = applicationCollection.updateOne(query, update).getModifiedCount();

            if (modifiedCount > 0) {
                System.out. println("✅ Application status updated to: " + status);
                return true;
            } else {
                System.out.println("❌ Application not found or not updated");
                return false;
            }

        } catch (Exception e) {
            System.err.println("❌ Error updating application status: " + e.getMessage());
            throw new RemoteException("Error updating application status: " + e.getMessage(), e);
        }
    }

    // ========================================
    // FUNCTION 8: DELETE APPLICATION
    // ========================================
    @Override
    public boolean deleteApplication(String id) throws RemoteException {
        try {
            System.out.println("Deleting application with ID:  " + id);

            Document query = new Document("_id", new ObjectId(id));
            long deletedCount = applicationCollection.deleteOne(query).getDeletedCount();

            if (deletedCount > 0) {
                System.out.println("✅ Application deleted successfully");
                return true;
            } else {
                System.out.println("❌ Application not found");
                return false;
            }

        } catch (Exception e) {
            System.err.println("❌ Error deleting application: " + e.getMessage());
            throw new RemoteException("Error deleting application: " + e.getMessage(), e);
        }
    }

    // ========================================
    // HELPER METHOD
    // ========================================
    private Application documentToApplication(Document doc) {
        if (doc == null) {
            return null;
        }

        Application application = new Application();
        application. setId(doc.getObjectId("_id").toString());
        application.setJobId(doc. getString("jobId"));
        application.setApplicantId(doc.getString("applicantId"));
        application.setApplicationDate(doc.getDate("applicationDate"));
        application.setStatus(doc. getString("status"));
        application. setCoverLetter(doc.getString("coverLetter"));

        return application;
    }
}
