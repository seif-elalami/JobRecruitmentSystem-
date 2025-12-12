package Server.services;

import Server.database.MongoDBConnection;
import Server.utils.PasswordUtil;
import Server.utils.ValidationUtil;
import shared.interfaces.IRecruiterService;
import shared.interfaces.IJobService;
import shared.interfaces.IApplicationService;
import shared.interfaces.ICandidateView;
import shared.interfaces.IApplicantService;
import shared.models.Recruiter;
import shared.models.User;
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
import java.util.Arrays;
import java.util.List;

public class RecruiterServiceImpl extends UnicastRemoteObject implements IRecruiterService {
    private MongoDatabase database;
    private MongoCollection<Document> userCollection;
    private IJobService jobService;
    private IApplicationService applicationService;
    private IApplicantService applicantService;
    private InterviewServiceImpl interviewService;
    private AuthServiceImpl authService;

    public RecruiterServiceImpl() throws RemoteException {
        super();
        this.database = MongoDBConnection.getInstance().getDatabase();
        this.userCollection = database.getCollection("users"); // ✅ NOT recruiterCollection

        System.out.println("✅ RecruiterService initialized");
        System.out.println("   Database: " + database.getName());
        System.out.println("   Collection: users");

        // Initialize other services
        this.jobService = new JobServiceImpl();
        this.applicationService = new ApplicationServiceImpl();
        this.applicantService = new ApplicantServiceImpl();
        this.interviewService = new InterviewServiceImpl();
        this.authService = new AuthServiceImpl(); // ✅ FIXED!

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
            if (recruiter.getPhone() != null && !recruiter.getPhone().isEmpty()) {
                if (!ValidationUtil.isValidPhone(recruiter.getPhone())) {
                    System.err.println("❌ Create recruiter failed: " + ValidationUtil.getPhoneErrorMessage());
                    throw new RemoteException(ValidationUtil.getPhoneErrorMessage());
                }
            }

            // Check if email already exists
            Document existingDoc = userCollection.find(new Document("email", recruiter.getEmail())).first();
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

            userCollection.insertOne(doc);

            String id = doc.getObjectId("_id").toString();
            recruiter.setId(id);

            System.out.println("✅ Recruiter created:  " + recruiter.getName() +
                    " from " + recruiter.getCompany() +
                    " (Dept: " + recruiter.getDepartment() + ", ID: " + id + ")");

            return id;

        } catch (RemoteException e) {
            throw e;
        } catch (Exception e) {
            System.err.println("❌ Error creating recruiter: " + e.getMessage());
            e.printStackTrace();
            throw new RemoteException("Failed to create recruiter", e);
        }
    }

    @Override
    public Recruiter getRecruiterById(String id) throws RemoteException {
        try {
            System.out.println("📋 Getting recruiter by ID: " + id);

            // ✅ Search in users collection
            Document query = new Document("_id", new ObjectId(id))
                    .append("role", "RECRUITER"); // ✅ Ensure it's a recruiter

            Document doc = userCollection.find(query).first();

            if (doc == null) {
                System.out.println("⚠️  Recruiter not found with ID: " + id);
                return null;
            }

            System.out.println("✅ Found recruiter:  " + doc.getString("email"));

            return documentToRecruiter(doc);

        } catch (Exception e) {
            System.err.println("❌ Error getting recruiter: " + e.getMessage());
            e.printStackTrace();
            throw new RemoteException("Failed to get recruiter", e);
        }
    }

   @Override
public Recruiter getRecruiterByEmail(String email) throws RemoteException {
    try {
        System. out.println("📋 Getting recruiter by email: " + email);

        // ✅ Search in users collection
        Document query = new Document("email", email)
                        .append("role", "RECRUITER");

        Document doc = userCollection.find(query).first();

        if (doc == null) {
            System.out.println("⚠️  Recruiter not found with email: " + email);
            return null;
        }

        System. out.println("✅ Found recruiter: " + doc.getString("username"));

        return documentToRecruiter(doc);

    } catch (Exception e) {
        System.err.println("❌ Error getting recruiter by email: " + e.getMessage());
        e.printStackTrace();
        throw new RemoteException("Failed to get recruiter by email", e);
    }
}

   @Override
public boolean updateRecruiter(Recruiter recruiter) throws RemoteException {
    try {
        System.out.println("📝 Updating recruiter: " + recruiter.getId());

        // Validation - Email Format
        if (!ValidationUtil.isValidEmail(recruiter.getEmail())) {
            System.err.println("❌ Invalid email format");
            throw new RemoteException(ValidationUtil.getEmailErrorMessage());
        }

        // Validation - Phone Number (if provided)
        if (recruiter.getPhone() != null && !recruiter.getPhone().isEmpty()) {
            if (!ValidationUtil.isValidPhone(recruiter.getPhone())) {
                System.err.println("❌ Invalid phone format");
                throw new RemoteException(ValidationUtil.getPhoneErrorMessage());
            }
        }

        // ✅ Query:  Find by ID and ensure role is RECRUITER
        Document query = new Document("_id", new ObjectId(recruiter.getId()))
                        .append("role", "RECRUITER");

        // ✅ Build update document
        Document update = new Document();
        update.append("username", recruiter.getUsername());
        update.append("email", recruiter.getEmail());
        update.append("phone", recruiter.getPhone());
        update.append("company", recruiter.getCompany());
        update.append("department", recruiter.getDepartment());
        update.append("position", recruiter.getPosition());
        update.append("description", recruiter.getDescription());
        update.append("role", "RECRUITER");  // ✅ Keep role as RECRUITER

        // Only update password if provided
        if (recruiter.getPassword() != null && !recruiter. getPassword().isEmpty()) {
            String hashedPassword = PasswordUtil.hashPassword(recruiter.getPassword());
            update.append("password", hashedPassword);
        }

        Document updateDoc = new Document("$set", update);

        // ✅ Update in users collection
        long modifiedCount = userCollection.updateOne(query, updateDoc).getModifiedCount();

        if (modifiedCount > 0) {
            System.out.println("✅ Recruiter updated successfully:  " + recruiter.getName());
            return true;
        } else {
            System.out.println("⚠️  No changes made (recruiter not found or no changes)");
            return false;
        }

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

            if (!job.getRecruiterId().equals(recruiterId)) {
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
        return applicationService.getApplicationsByJobId(jobId);
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
    // ✅ MATCH CV FEATURE IMPLEMENTATION
    // ========================================
    @Override
    public List<ICandidateView> getCandidatesForJob(String jobId) throws RemoteException {
        try {
            System.out.println("\n╔════════════════════════════════════════╗");
            System.out.println("║  DEBUG: Get Candidates For Job        ║");
            System.out.println("╚════════════════════════════════════════╝");
            System.out.println("Job ID: " + jobId);
            System.out.println("AuthService null?  " + (authService == null));

            List<ICandidateView> candidates = new ArrayList<>();

            // Get applications
            System.out.println("\n--- Fetching Applications ---");
            List<Application> applications = applicationService.getApplicationsByJobId(jobId);
            System.out.println("Applications found: " + applications.size());

            if (applications.isEmpty()) {
                System.out.println("⚠️  No applications - exiting");
                return candidates;
            }

            // Process each application
            System.out.println("\n--- Processing Applications ---");
            for (int i = 0; i < applications.size(); i++) {
                Application app = applications.get(i);

                System.out.println("\nApplication #" + (i + 1) + ":");
                System.out.println("  App ID: " + app.getApplicationId());
                System.out.println("  Applicant ID: " + app.getApplicantId());
                System.out.println("  Status: " + app.getStatus());

                try {
                    System.out.println("  Fetching user.. .");
                    User user = authService.getUserById(app.getApplicantId());

                    if (user == null) {
                        System.out.println("  ❌ User is NULL");
                        continue;
                    }

                    System.out.println("  ✅ User found:  " + user.getEmail());
                    System.out.println("     User ID: " + user.getUserId());
                    System.out.println("     Role: " + user.getRole());

                    if (!"APPLICANT".equals(user.getRole())) {
                        System.out.println("  ⚠️  User is not an APPLICANT");
                        continue;
                    }

                    System.out.println("  Converting to Applicant...");
                    Applicant applicant = convertUserToApplicant(user);
                    candidates.add(applicant);
                    System.out.println("  ✅ Added to candidates list");

                } catch (Exception e) {
                    System.out.println("  ❌ Error processing application: " + e.getMessage());
                    e.printStackTrace();
                }
            }

            System.out.println("\n╔════════════════════════════════════════╗");
            System.out.println("║  RESULT: " + candidates.size() + " candidates                 ║");
            System.out.println("╚════════════════════════════════════════╝\n");

            return candidates;

        } catch (Exception e) {
            System.err.println("❌ FATAL ERROR in getCandidatesForJob:");
            e.printStackTrace();
            throw new RemoteException("Failed to get candidates", e);
        }
    }

    @Override
    public ICandidateView getCandidateById(String candidateId) throws RemoteException {
        try {
            System.out.println("📋 Getting candidate:  " + candidateId);

            // ✅ Try getting from users collection first (unified system)
            if (authService != null) {
                User user = authService.getUserById(candidateId);

                if (user != null && "APPLICANT".equals(user.getRole())) {
                    System.out.println("✅ Found candidate in users collection:  " + user.getUsername());

                    // Convert User to Applicant
                    Applicant applicant = new Applicant();

                    // Map User fields to Applicant fields
                    applicant.setId(user.getUserId()); // ✅ User has getUserId()
                    applicant.setName(user.getUsername()); // ✅ User has getUsername()
                    applicant.setEmail(user.getEmail());
                    applicant.setPhone(user.getPhone() != null ? user.getPhone() : "");

                    // Education - User doesn't have this, set default
                    applicant.setEducation("Not specified");

                    // Experience - User has it as String, convert to int
                    if (user.getExperience() != null && !user.getExperience().isEmpty()) {
                        try {
                            // Try to extract number from "5 years" or just "5"
                            String expStr = user.getExperience().replaceAll("[^0-9]", "");
                            int expYears = expStr.isEmpty() ? 0 : Integer.parseInt(expStr);
                            applicant.setExperience(expYears);
                        } catch (NumberFormatException e) {
                            applicant.setExperience(0);
                        }
                    } else {
                        applicant.setExperience(0);
                    }

                    // Skills - User has it as String, convert to List
                    if (user.getSkills() != null && !user.getSkills().isEmpty()) {
                        List<String> skillsList = Arrays.asList(user.getSkills().split(",\\s*"));
                        applicant.setSkills(skillsList);
                    } else {
                        applicant.setSkills(new ArrayList<>());
                    }

                    // Resume - User doesn't have this field, set empty
                    applicant.setResume("");

                    return applicant; // Automatically casts to ICandidateView
                }
            }

            // ✅ Fallback to applicants collection (if exists)
            Applicant applicant = applicantService.getApplicantById(candidateId);

            if (applicant != null) {
                System.out.println("✅ Found candidate in applicants collection: " + applicant.getName());
                return applicant;
            }

            System.out.println("⚠️  Candidate not found in any collection");
            return null;

        } catch (Exception e) {
            System.err.println("❌ Error getting candidate: " + e.getMessage());
            e.printStackTrace();
            throw new RemoteException("Failed to get candidate", e);
        }
    }

    @Override
    public List<ICandidateView> searchCandidatesBySkillsReadOnly(String skills) throws RemoteException {
        try {
            System.out.println("🔍 Searching candidates by skills (read-only): " + skills);

            List<ICandidateView> candidates = new ArrayList<>();

            // Get all users with APPLICANT role from users collection
            if (authService != null) {
                List<User> allUsers = authService.getAllUsers();

                for (User user : allUsers) {
                    if ("APPLICANT".equals(user.getRole())) {
                        // Check if user has matching skills
                        if (user.getSkills() != null && user.getSkills().toLowerCase().contains(skills.toLowerCase())) {
                            // Convert to Applicant
                            Applicant applicant = convertUserToApplicant(user);
                            candidates.add(applicant);
                        }
                    }
                }
            }

            System.out.println("✅ Found " + candidates.size() + " candidate(s)");
            return candidates;

        } catch (Exception e) {
            System.err.println("❌ Error searching candidates:  " + e.getMessage());
            e.printStackTrace();
            throw new RemoteException("Failed to search candidates", e);
        }
    }

    @Override
    public List<ICandidateView> searchCandidatesByMinExperience(int minYears) throws RemoteException {
        try {
            System.out.println("🔍 Searching candidates with min " + minYears + " years experience");

            List<ICandidateView> candidates = new ArrayList<>();

            // Get all users with APPLICANT role
            if (authService != null) {
                List<User> allUsers = authService.getAllUsers();

                for (User user : allUsers) {
                    if ("APPLICANT".equals(user.getRole())) {
                        // Parse experience
                        if (user.getExperience() != null && !user.getExperience().isEmpty()) {
                            try {
                                String expStr = user.getExperience().replaceAll("[^0-9]", "");
                                int expYears = expStr.isEmpty() ? 0 : Integer.parseInt(expStr);

                                if (expYears >= minYears) {
                                    Applicant applicant = convertUserToApplicant(user);
                                    candidates.add(applicant);
                                    System.out
                                            .println("   ✅ Matched:  " + user.getUsername() + " (" + expYears
                                                    + " years)");
                                }
                            } catch (NumberFormatException e) {
                                // Skip if can't parse
                            }
                        }
                    }
                }
            }

            System.out
                    .println("✅ Found " + candidates.size() + " candidate(s) with " + minYears + "+ years experience");
            return candidates;

        } catch (Exception e) {
            System.err.println("❌ Error searching candidates: " + e.getMessage());
            e.printStackTrace();
            throw new RemoteException("Failed to search candidates", e);
        }
    }

    private Applicant convertUserToApplicant(User user) {
        Applicant applicant = new Applicant();

        applicant.setId(user.getUserId());
        applicant.setName(user.getUsername());
        applicant.setEmail(user.getEmail());
        applicant.setPhone(user.getPhone() != null ? user.getPhone() : "");
        applicant.setEducation("Not specified");

        // Experience - convert String to int
        if (user.getExperience() != null && !user.getExperience().isEmpty()) {
            try {
                String expStr = user.getExperience().replaceAll("[^0-9]", "");
                int expYears = expStr.isEmpty() ? 0 : Integer.parseInt(expStr);
                applicant.setExperience(expYears);
            } catch (NumberFormatException e) {
                applicant.setExperience(0);
            }
        } else {
            applicant.setExperience(0);
        }

        // Skills - convert String to List
        if (user.getSkills() != null && !user.getSkills().isEmpty()) {
            List<String> skillsList = Arrays.asList(user.getSkills().split(",\\s*"));
            applicant.setSkills(skillsList);
        } else {
            applicant.setSkills(new ArrayList<>());
        }

        applicant.setResume("");

        return applicant;
    }
    // ========================================
    // Helper Methods
    // ========================================

    private Recruiter documentToRecruiter(Document doc) {
        Recruiter recruiter = new Recruiter();

        // Map from User fields
        recruiter.setUserId(doc.getObjectId("_id").toString());
        recruiter.setUsername(doc.getString("username"));
        recruiter.setEmail(doc.getString("email"));
        recruiter.setPassword(doc.getString("password"));
        recruiter.setRole(doc.getString("role"));

        // Map Recruiter-specific fields
        recruiter.setPhone(doc.getString("phone"));
        recruiter.setCompany(doc.getString("company"));
        recruiter.setDepartment(doc.getString("department"));
        recruiter.setPosition(doc.getString("position"));
        recruiter.setDescription(doc.getString("description"));

        // Map dates if they exist
        recruiter.setCreatedAt(doc.getDate("createdAt"));
        recruiter.setLastLogin(doc.getDate("lastLogin"));

        // Map active status
        Boolean isActive = doc.getBoolean("isActive");
        if (isActive != null) {
            recruiter.setActive(isActive);
        }

        return recruiter;
    }
}
