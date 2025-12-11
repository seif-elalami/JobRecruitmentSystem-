package Server.services;

import Server.database.MongoDBConnection;
import Server.utils.ValidationUtil;
import shared.interfaces.IRecruiterService;
import shared.interfaces. IJobService;
import shared.interfaces. IApplicationService;
import shared.interfaces. IApplicantService;
import shared. models. Recruiter;
import shared.models.Job;
import shared.models.Application;
import shared.models.Applicant;
import shared.models.Interview;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;

import java. rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class RecruiterServiceImpl extends UnicastRemoteObject implements IRecruiterService {

    private MongoCollection<Document> recruiterCollection;
    private IJobService jobService;
    private IApplicationService applicationService;
    private IApplicantService applicantService;
    private InterviewServiceImpl interviewService;

    public RecruiterServiceImpl() throws RemoteException {
        super();
        MongoDatabase database = MongoDBConnection.getInstance().getDatabase();
        recruiterCollection = database.getCollection("recruiters");

        // Initialize other services
        this.jobService = new JobServiceImpl();
        this.applicationService = new ApplicationServiceImpl();
        this.applicantService = new ApplicantServiceImpl();
        this.interviewService = new InterviewServiceImpl();

        System.out.println("✅ RecruiterService initialized");
    }

    // ========================================
    // Recruiter Profile Management (for Auth)
    // ========================================

    @Override
    public String createRecruiter(Recruiter recruiter) throws RemoteException {
        try {
            // Validation - Email Format
            if (!ValidationUtil.isValidEmail(recruiter.getEmail())) {
                System.err.println("❌ Create recruiter failed: " + ValidationUtil.getEmailErrorMessage());
                throw new RemoteException(ValidationUtil.getEmailErrorMessage());
            }

            // Validation - Phone Number (if provided)
            if (recruiter. getPhone() != null && !recruiter.getPhone().isEmpty()) {
                if (!ValidationUtil.isValidPhone(recruiter.getPhone())) {
                    System. err.println("❌ Create recruiter failed: " + ValidationUtil.getPhoneErrorMessage());
                    throw new RemoteException(ValidationUtil.getPhoneErrorMessage());
                }
            }

            // Check if email already exists
            Document existingDoc = recruiterCollection.find(new Document("email", recruiter.getEmail())).first();
            if (existingDoc != null) {
                throw new RemoteException("❌ Email already exists");
            }

            // Create document
            Document doc = new Document();
            doc.append("username", recruiter.getUsername());
            doc.append("password", recruiter.getPassword());
            doc.append("email", recruiter.getEmail());
            doc.append("role", "RECRUITER");
            doc.append("department", recruiter.getDepartment());
            doc.append("phone", recruiter.getPhone());
            doc.append("company", recruiter.getCompany());
            doc.append("position", recruiter.getPosition());
            doc.append("description", recruiter.getDescription());

            recruiterCollection.insertOne(doc);

            String id = doc.getObjectId("_id").toString();
            recruiter.setId(id);

            System.out.println("✅ Recruiter created:  " + recruiter.getName() +
                              " from " + recruiter.getCompany() +
                              " (Dept: " + recruiter.getDepartment() + ", ID: " + id + ")");

            return id;

        } catch (RemoteException e) {
            throw e;
        } catch (Exception e) {
            System.err. println("❌ Error creating recruiter: " + e.getMessage());
            e.printStackTrace();
            throw new RemoteException("Failed to create recruiter", e);
        }
    }

    @Override
    public Recruiter getRecruiterById(String id) throws RemoteException {
        try {
            Document doc = recruiterCollection. find(new Document("_id", new ObjectId(id))).first();

            if (doc == null) {
                return null;
            }

            return documentToRecruiter(doc);

        } catch (Exception e) {
            System. err.println("❌ Error getting recruiter: " + e. getMessage());
            throw new RemoteException("Failed to get recruiter", e);
        }
    }

    @Override
    public Recruiter getRecruiterByEmail(String email) throws RemoteException {
        try {
            Document doc = recruiterCollection.find(new Document("email", email)).first();

            if (doc == null) {
                return null;
            }

            return documentToRecruiter(doc);

        } catch (Exception e) {
            System.err.println("❌ Error getting recruiter: " + e.getMessage());
            throw new RemoteException("Failed to get recruiter", e);
        }
    }

    @Override
    public boolean updateRecruiter(Recruiter recruiter) throws RemoteException {
        try {
            // Validation - Email Format
            if (!ValidationUtil.isValidEmail(recruiter.getEmail())) {
                System.err.println("❌ Update recruiter failed:  " + ValidationUtil.getEmailErrorMessage());
                throw new RemoteException(ValidationUtil. getEmailErrorMessage());
            }

            // Validation - Phone Number (if provided)
            if (recruiter.getPhone() != null && !recruiter.getPhone().isEmpty()) {
                if (!ValidationUtil.isValidPhone(recruiter.getPhone())) {
                    System.err.println("❌ Update recruiter failed: " + ValidationUtil.getPhoneErrorMessage());
                    throw new RemoteException(ValidationUtil. getPhoneErrorMessage());
                }
            }

            Document query = new Document("_id", new ObjectId(recruiter.getId()));

            Document update = new Document();
            update.append("username", recruiter.getUsername());
            update.append("email", recruiter.getEmail());
            update.append("department", recruiter.getDepartment());
            update.append("phone", recruiter.getPhone());
            update.append("company", recruiter.getCompany());
            update.append("position", recruiter. getPosition());
            update.append("description", recruiter.getDescription());

            // Only update password if it's provided and not empty
            if (recruiter.getPassword() != null && !recruiter.getPassword().isEmpty()) {
                update.append("password", recruiter.getPassword());
            }

            Document updateDoc = new Document("$set", update);

            recruiterCollection.updateOne(query, updateDoc);

            System.out.println("✅ Recruiter updated: " + recruiter.getName());

            return true;

        } catch (RemoteException e) {
            throw e;
        } catch (Exception e) {
            System.err.println("❌ Error updating recruiter: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // ========================================
    // 1. Job Posting Management
    // ========================================

    @Override
    public String postJob(Job job) throws RemoteException {
        return jobService.createJob(job);
    }

    @Override
    public List<Job> getMyJobPostings(String recruiterId) throws RemoteException {
        return jobService.getJobsByRecruiterId(recruiterId);
    }

    @Override
    public boolean closeJobPosting(String jobId, String recruiterId) throws RemoteException {
        try {
            // Verify the job belongs to this recruiter
            Job job = jobService.getJobById(jobId);

            if (job == null) {
                System.err.println("❌ Close job failed: Job not found");
                return false;
            }

            if (! job.getRecruiterId().equals(recruiterId)) {
                System.err.println("❌ Close job failed: Unauthorized - job belongs to another recruiter");
                return false;
            }

            return jobService.closeJob(jobId);

        } catch (Exception e) {
            System.err.println("❌ Error closing job: " + e.getMessage());
            return false;
        }
    }

    // ========================================
    // 2. Application Management
    // ========================================

    @Override
    public List<Application> getApplicationsForJob(String jobId) throws RemoteException {
        return applicationService. getApplicationsByJobId(jobId);
    }

    @Override
    public boolean updateApplicationStatus(String applicationId, String status) throws RemoteException {
        return applicationService.updateApplicationStatus(applicationId, status);
    }

    // ========================================
    // 3. Applicant Search
    // ========================================

    @Override
    public List<Applicant> searchApplicantsBySkills(String skills) throws RemoteException {
        return applicantService.searchApplicantsBySkills(skills);
    }

    @Override
    public List<Applicant> searchApplicantsByExperience(String experience) throws RemoteException {
        return applicantService.searchApplicantsByExperience(experience);
    }

    // ========================================
    // 4. Interview Management
    // ========================================

    @Override
    public String createInterview(Interview interview) throws RemoteException {
        return interviewService.createInterview(interview);
    }

    @Override
    public boolean updateInterview(Interview interview) throws RemoteException {
        return interviewService.updateInterview(interview);
    }

    @Override
    public boolean cancelInterview(String interviewId) throws RemoteException {
        return interviewService.cancelInterview(interviewId);
    }

    @Override
    public List<Interview> getMyInterviews(String recruiterId) throws RemoteException {
        return interviewService.getInterviewsByRecruiterId(recruiterId);
    }

    @Override
    public Interview getInterviewById(String interviewId) throws RemoteException {
        return interviewService.getInterviewById(interviewId);
    }

    // ========================================
    // Helper Methods
    // ========================================

    private Recruiter documentToRecruiter(Document doc) {
        Recruiter recruiter = new Recruiter();

        // From User (inherited)
        recruiter.setId(doc.getObjectId("_id").toString());
        recruiter.setName(doc.getString("username"));
        recruiter.setPassword(doc.getString("password"));
        recruiter.setEmail(doc.getString("email"));
        recruiter.setRole(doc.getString("role"));

        // From Recruiter
        recruiter.setDepartment(doc.getString("department"));
        recruiter.setPhone(doc.getString("phone"));
        recruiter.setCompany(doc.getString("company"));
        recruiter.setPosition(doc. getString("position"));
        recruiter.setDescription(doc.getString("description"));

        return recruiter;
    }
}
