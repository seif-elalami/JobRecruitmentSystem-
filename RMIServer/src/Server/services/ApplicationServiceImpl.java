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

    private MongoDatabase database;
    private MongoCollection<Document> applicationCollection;

    public ApplicationServiceImpl() throws RemoteException {
        super();
        this.database = MongoDBConnection. getInstance().getDatabase();
        this.applicationCollection = database.getCollection("applications");

        System.out.println("✅ ApplicationService initialized");
        System.out.println("   Database: " + database.getName());
        System.out.println("   Collection: applications");
        System.out.println("   Current count: " + applicationCollection.countDocuments());
    }

    // ========================================
    // FUNCTION 1:  SUBMIT APPLICATION
    // ========================================
     @Override
    public String SubmitApplication(Application application) throws RemoteException {
        try {
            System.out.println("\n📤 Submitting application");
            System.out.println("   Job ID: " + application.getJobId());
            System.out. println("   Applicant ID: " + application.getApplicantId());

            Document doc = new Document();
            doc.append("jobId", application.getJobId());  // Store as String
            doc.append("applicantId", application.getApplicantId());  // Store as String
            doc. append("applicationDate", application.getApplicationDate());
            doc.append("status", application.getStatus());
            doc.append("coverLetter", application.getCoverLetter());

            applicationCollection.insertOne(doc);

            String applicationId = doc.getObjectId("_id").toString();
            System.out.println("✅ Application submitted with ID: " + applicationId);

            return applicationId;

        } catch (Exception e) {
            System.err. println("❌ Error submitting application: " + e.getMessage());
            e.printStackTrace();
            throw new RemoteException("Failed to submit application", e);
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
        System. out.println("📋 Getting applications for job: " + jobId);

        List<Application> applications = new ArrayList<>();

        // ✅ FIX: Try both String and ObjectId formats
        Document query;

        try {
            // Try as ObjectId first
            ObjectId jobObjectId = new ObjectId(jobId);
            query = new Document("jobId", jobObjectId);
            System.out.println("   Searching with ObjectId: " + jobObjectId);
        } catch (IllegalArgumentException e) {
            // If not valid ObjectId, search as String
            query = new Document("jobId", jobId);
            System.out.println("   Searching with String: " + jobId);
        }

        System.out.println("   Query: " + query.toJson());

        long count = applicationCollection.countDocuments(query);
        System.out. println("   Matches found: " + count);

        for (Document doc : applicationCollection.find(query)) {
            Application app = documentToApplication(doc);
            applications.add(app);
            System.out.println("   ✅ Found application: " + app.getApplicationId());
        }

        // ✅ If no results, try the OTHER format
        if (applications.isEmpty()) {
            System.out. println("\n   ⚠️ No results with first query, trying alternate format.. .");

            Document alternateQuery;
            if (query.get("jobId") instanceof ObjectId) {
                // We tried ObjectId, now try String
                alternateQuery = new Document("jobId", jobId);
                System.out. println("   Trying as String: " + alternateQuery.toJson());
            } else {
                // We tried String, now try ObjectId
                try {
                    alternateQuery = new Document("jobId", new ObjectId(jobId));
                    System.out.println("   Trying as ObjectId: " + alternateQuery.toJson());
                } catch (IllegalArgumentException ex) {
                    System.out.println("   ❌ Cannot convert to ObjectId");
                    return applications;
                }
            }

            for (Document doc : applicationCollection.find(alternateQuery)) {
                Application app = documentToApplication(doc);
                applications.add(app);
                System.out.println("   ✅ Found with alternate query: " + app.getApplicationId());
            }
        }

        System.out.println("✅ Total applications found: " + applications.size());
        return applications;

    } catch (Exception e) {
        System.err.println("❌ Error getting applications:  " + e.getMessage());
        e.printStackTrace();
        throw new RemoteException("Failed to get applications", e);
    }
}

    // ========================================
    // FUNCTION 7: UPDATE APPLICATION STATUS
    // ========================================
    @Override
    public boolean updateApplicationStatus(String applicationId, String status) throws RemoteException {
        try {
            System.out.println("Updating application status...");
            System.out.println("   Application ID: " + applicationId);
            System.out.println("   New Status: " + status);

            // Validate status
            if (!status.equals("PENDING") && !status.equals("ACCEPTED") &&
                !status.equals("REJECTED") && !status.equals("UNDER_REVIEW")) {
                System.err.println("❌ Invalid status: " + status);
                return false;
            }

            // Get current application to find applicantId
            Document query = new Document("_id", new ObjectId(applicationId));
            Document currentDoc = applicationCollection.find(query).first();
            
            if (currentDoc == null) {
                System.out.println("❌ Application not found: " + applicationId);
                return false;
            }

            String applicantId = currentDoc.getString("applicantId");

            Document update = new Document("$set", new Document("status", status));
            long modifiedCount = applicationCollection.updateOne(query, update).getModifiedCount();

            if (modifiedCount > 0) {
                System.out.println("✅ Application status updated to: " + status);
                
                // Trigger Notification
                try {
                    Server.observer.SystemNotificationObserver observer = new Server.observer.SystemNotificationObserver(applicantId);
                    observer.update("Your application for job ID " + currentDoc.getString("jobId") + " has been updated to: " + status);
                } catch (Exception e) {
                    System.err.println("⚠️ Failed to send notification: " + e.getMessage());
                }
                
                return true;
            } else {
                System.out.println("❌ Application not updated");
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
    Application app = new Application();

    app.setApplicationId(doc.getObjectId("_id").toString());

    // ✅ Handle jobId as either String or ObjectId
    Object jobIdObj = doc.get("jobId");
    if (jobIdObj instanceof ObjectId) {
        app.setJobId(((ObjectId) jobIdObj).toString());
    } else {
        app.setJobId(doc.getString("jobId"));
    }

    // ✅ Handle applicantId as either String or ObjectId
    Object applicantIdObj = doc. get("applicantId");
    if (applicantIdObj instanceof ObjectId) {
        app.setApplicantId(((ObjectId) applicantIdObj).toString());
    } else {
        app.setApplicantId(doc.getString("applicantId"));
    }

    app. setApplicationDate(doc.getDate("applicationDate"));
    app.setStatus(doc.getString("status"));
    app.setCoverLetter(doc.getString("coverLetter"));

    return app;
}
}
