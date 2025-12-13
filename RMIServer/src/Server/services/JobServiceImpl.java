package Server.services;

import Server.database.MongoDBConnection;
import shared.interfaces.IJobService;
import shared.models.Job;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org. bson.types.ObjectId;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class JobServiceImpl extends UnicastRemoteObject implements IJobService {

    private MongoDatabase database; // ✅ ADDED
    private MongoCollection<Document> jobCollection;

    public JobServiceImpl() throws RemoteException {
        super();
        this.database = MongoDBConnection.getInstance().getDatabase(); // ✅ STORE DATABASE
        this.jobCollection = database.getCollection("jobs");

        System.out.println("✅ JobService initialized");
        System.out.println("   Database: " + database.getName());
        System.out.println("   Collection: jobs");
        System.out.println("   Current job count: " + jobCollection.countDocuments());
    }

    // ========================================
    // CREATE JOB
    // ========================================

    @Override
    public String createJob(Job job) throws RemoteException {
        try {
            System.out.println("\n📤 Creating new job");
            System.out.println("   Title: " + job.getTitle());
            System.out.println("   Recruiter ID: " + job.getRecruiterId());

            // ========================================
            // VALIDATION: Check for empty parameters
            // ========================================
            List<String> validationErrors = new ArrayList<>();

            // Title validation
            if (job.getTitle() == null || job.getTitle().trim().isEmpty()) {
                validationErrors.add("Job title is required");
            }

            // Description validation
            if (job.getDescription() == null || job.getDescription().trim().isEmpty()) {
                validationErrors.add("Job description is required");
            }

            // Company validation
            if (job.getCompany() == null || job.getCompany().trim().isEmpty()) {
                validationErrors.add("Company name is required");
            }

            // Location validation
            if (job.getLocation() == null || job.getLocation().trim().isEmpty()) {
                validationErrors.add("Job location is required");
            }

            // Salary validation
            if (job.getSalary() <= 0) {
                validationErrors.add("Salary must be a positive number");
            }

            // Requirements validation (at least one requirement)
            if (job.getRequirements() == null || job.getRequirements().isEmpty()) {
                validationErrors.add("At least one job requirement is required");
            }

            // RecruiterID validation
            if (job.getRecruiterId() == null || job.getRecruiterId().trim().isEmpty()) {
                validationErrors.add("Recruiter ID is required");
            }

            // If validation errors exist, throw exception
            if (!validationErrors.isEmpty()) {
                String errorMsg = "Job validation failed: " + String.join(", ", validationErrors);
                System.err.println("❌ " + errorMsg);
                throw new RemoteException(errorMsg);
            }

            System.out.println("✅ Validation passed");

            Document doc = new Document();

            doc.append("title", job.getTitle());
            doc.append("description", job.getDescription());
            doc.append("requirements", job.getRequirements());
            doc.append("company", job.getCompany());
            doc.append("location", job.getLocation());
            doc.append("salary", job.getSalary());
            doc.append("status", "OPEN");
            doc.append("postedDate", new Date());
            doc.append("recruiterId", job.getRecruiterId());

            jobCollection.insertOne(doc);

            String jobId = doc.getObjectId("_id").toString();
            job.setJobId(jobId);

            System.out.println("✅ Job created successfully!");
            System.out.println("   Job ID: " + jobId);
            System.out.println("   Status:  OPEN");

            return jobId;

        } catch (Exception e) {
            System.err.println("❌ Error creating job: " + e.getMessage());
            e.printStackTrace();
            throw new RemoteException("Failed to create job", e);
        }
    }

    // ========================================
    // GET JOB BY ID
    // ========================================

    @Override
    public Job getJobById(String jobId) throws RemoteException {
        try {
            System.out.println("📋 Getting job by ID: " + jobId);

            Document query;
            try {
                // Job _id is always ObjectId in MongoDB
                ObjectId jobObjectId = new ObjectId(jobId);
                query = new Document("_id", jobObjectId);
            } catch (IllegalArgumentException e) {
                System.err.println("⚠️  Invalid ObjectId format: " + jobId);
                return null;
            }

            Document doc = jobCollection.find(query).first();

            if (doc != null) {
                Job job = documentToJob(doc);
                System.out.println("✅ Found job: " + job.getTitle());
                return job;
            }

            System.out.println("⚠️  Job not found with ID: " + jobId);
            return null;

        } catch (Exception e) {
            System.err.println("❌ Error getting job: " + e.getMessage());
            e.printStackTrace();
            throw new RemoteException("Failed to get job", e);
        }
    }

    // ========================================
    // GET ALL JOBS
    // ========================================

    @Override
    public List<Job> getAllJobs() throws RemoteException {
        try {
            System.out.println("📋 Getting all jobs");

            List<Job> jobs = new ArrayList<>();

            for (Document doc : jobCollection.find()) {
                Job job = documentToJob(doc);
                jobs.add(job);
            }

            System.out.println("✅ Found " + jobs.size() + " job(s)");
            return jobs;

        } catch (Exception e) {
            System.err.println("❌ Error getting all jobs: " + e.getMessage());
            e.printStackTrace();
            throw new RemoteException("Failed to get all jobs", e);
        }
    }

    // ========================================
    // GET JOBS BY RECRUITER ID
    // ========================================

    @Override
    public List<Job> getJobsByRecruiterId(String recruiterId) throws RemoteException {
        try {
            System.out.println("📋 Getting jobs for recruiter: " + recruiterId);

            List<Job> jobs = new ArrayList<>();
            Document query = new Document("recruiterId", recruiterId);

            for (Document doc : jobCollection.find(query)) {
                jobs.add(documentToJob(doc));
            }

            System.out.println("✅ Found " + jobs.size() + " job(s) for recruiter");
            return jobs;

        } catch (Exception e) {
            System.err.println("❌ Error getting jobs by recruiter:  " + e.getMessage());
            e.printStackTrace();
            throw new RemoteException("Failed to get jobs by recruiter", e);
        }
    }

    // ========================================
    // GET JOBS BY LOCATION
    // ========================================

    @Override
    public List<Job> getJobsByLocation(String location) throws RemoteException {
        try {
            System.out.println("📋 Searching jobs by location: " + location);

            List<Job> jobs = new ArrayList<>();
            Document query = new Document("location", new Document("$regex", location).append("$options", "i"));

            for (Document doc : jobCollection.find(query)) {
                jobs.add(documentToJob(doc));
            }

            System.out.println("✅ Found " + jobs.size() + " job(s) in location: " + location);
            return jobs;

        } catch (Exception e) {
            System.err.println("❌ Error getting jobs by location: " + e.getMessage());
            e.printStackTrace();
            throw new RemoteException("Failed to get jobs by location", e);
        }
    }

    // ========================================
    // SEARCH JOBS BY TITLE
    // ========================================

    @Override
    public List<Job> searchJobsByTitle(String title) throws RemoteException {
        try {
            System.out.println("🔍 Searching jobs by title: " + title);

            List<Job> jobs = new ArrayList<>();
            Document query = new Document("title", new Document("$regex", title).append("$options", "i"));

            for (Document doc : jobCollection.find(query)) {
                jobs.add(documentToJob(doc));
            }

            System.out.println("✅ Found " + jobs.size() + " job(s) matching title");
            return jobs;

        } catch (Exception e) {
            System.err.println("❌ Error searching jobs:  " + e.getMessage());
            e.printStackTrace();
            throw new RemoteException("Failed to search jobs", e);
        }
    }

    // ========================================
    // UPDATE JOB
    // ========================================

    @Override
    public boolean updateJob(Job job) throws RemoteException {
        try {
            System.out.println("📝 Updating job: " + job.getJobId());

            Document query = new Document("_id", new ObjectId(job.getJobId()));

            Document update = new Document();
            update.append("title", job.getTitle());
            update.append("description", job.getDescription());
            update.append("requirements", job.getRequirements());
            update.append("company", job.getCompany());
            update.append("location", job.getLocation());
            update.append("salary", job.getSalary());
            update.append("status", job.getStatus());

            Document updateDoc = new Document("$set", update);

            long modifiedCount = jobCollection.updateOne(query, updateDoc).getModifiedCount();

            if (modifiedCount > 0) {
                System.out.println("✅ Job updated successfully");
                return true;
            } else {
                System.out.println("⚠️  No changes made (job might not exist)");
                return false;
            }

        } catch (Exception e) {
            System.err.println("❌ Error updating job: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // ========================================
    // DELETE JOB
    // ========================================

    @Override
    public boolean deleteJob(String jobId) throws RemoteException {
        try {
            System.out.println("🗑️  Deleting job: " + jobId);

            Document query = new Document("_id", new ObjectId(jobId));
            long deletedCount = jobCollection.deleteOne(query).getDeletedCount();

            if (deletedCount > 0) {
                System.out.println("✅ Job deleted successfully");
                return true;
            } else {
                System.out.println("⚠️  Job not found");
                return false;
            }

        } catch (Exception e) {
            System.err.println("❌ Error deleting job: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // ========================================
    // CLOSE JOB
    // ========================================

    @Override
    public boolean closeJob(String jobId) throws RemoteException {
        try {
            System.out.println("🔒 Closing job: " + jobId);

            Document query = new Document("_id", new ObjectId(jobId));
            Document update = new Document("$set", new Document("status", "CLOSED"));

            long modifiedCount = jobCollection.updateOne(query, update).getModifiedCount();

            if (modifiedCount > 0) {
                System.out.println("✅ Job closed successfully");
                return true;
            } else {
                System.out.println("⚠️  Job not found or already closed");
                return false;
            }

        } catch (Exception e) {
            System.err.println("❌ Error closing job: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // ========================================
    // HELPER METHOD - DOCUMENT TO JOB
    // ========================================

    private Job documentToJob(Document doc) {
        Job job = new Job();

        try {
            // Job ID
            job.setJobId(doc.getObjectId("_id").toString());

            // Basic fields
            job.setTitle(doc.getString("title"));
            job.setDescription(doc.getString("description"));
            job.setCompany(doc.getString("company"));
            job.setLocation(doc.getString("location"));
            job.setStatus(doc.getString("status"));
            job.setPostedDate(doc.getDate("postedDate"));
            job.setRecruiterId(doc.getString("recruiterId"));

            // Salary
            Double salary = doc.getDouble("salary");
            job.setSalary(salary != null ? salary : 0.0);

            // ✅ CRITICAL: Handle requirements safely
            Object requirementsObj = doc.get("requirements");
            List<String> requirements = new ArrayList<>();

            if (requirementsObj != null) {
                if (requirementsObj instanceof List) {
                    // It's already a List
                    for (Object item : (List<?>) requirementsObj) {
                        if (item instanceof String) {
                            requirements.add((String) item);
                        } else {
                            requirements.add(item.toString());
                        }
                    }
                } else if (requirementsObj instanceof String) {
                    // It's a String - split it
                    String reqStr = (String) requirementsObj;
                    if (!reqStr.trim().isEmpty()) {
                        // Split by common delimiters
                        String[] parts = reqStr.split("[,\n;]");
                        for (String part : parts) {
                            String trimmed = part.trim();
                            if (!trimmed.isEmpty()) {
                                requirements.add(trimmed);
                            }
                        }
                    }
                } else {
                    // Unknown type - try toString
                    System.out.println("⚠️ Unexpected requirements type: " + requirementsObj.getClass().getName());
                    requirements.add(requirementsObj.toString());
                }
            }

            job.setRequirements(requirements);

        } catch (Exception e) {
            System.err.println("⚠️ Error parsing job document: " + e.getMessage());
            // Set safe defaults
            if (job.getRequirements() == null) {
                job.setRequirements(new ArrayList<>());
            }
        }

        return job;
    }
}
