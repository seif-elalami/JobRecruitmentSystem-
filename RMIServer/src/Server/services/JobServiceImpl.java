package Server.services;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;
import Server.database.MongoDBConnection;
import shared.interfaces.IJobService;      // import job service Interfaces
import shared.models.Job;                 // import job model

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class JobServiceImpl extends UnicastRemoteObject implements IJobService {

    private MongoCollection<Document> jobCollection;

    public JobServiceImpl() throws RemoteException {
        super();
        MongoDatabase database = MongoDBConnection.getInstance().getDatabase();
        jobCollection = database. getCollection("jobs");
        System.out.println("✅ JobService initialized");
    }

    // ========================================
    // FUNCTION 1: CREATE JOB
    // ========================================
    @Override
    public String createJob(Job job) throws RemoteException {
        try {
            System.out.println("Creating job: " + job.getTitle());

            Document doc = new Document();
            doc. append("title", job.getTitle());
            doc.append("description", job.getDescription());
            doc.append("company", job.getCompany());
            doc.append("location", job.getLocation());
            doc. append("salary", job.getSalary());
            doc.append("requirements", job.getRequirements());
            doc.append("status", job.getStatus());

            jobCollection.insertOne(doc);
            String id = doc.getObjectId("_id"). toString();

            System. out.println("✅ Job created successfully!");
            System.out.println("   ID: " + id);
            System.out. println("   Title: " + job.getTitle());
            System. out.println("   Company: " + job.getCompany());

            return id;

        } catch (Exception e) {
            System.err.println("❌ Error creating job: " + e.getMessage());
            throw new RemoteException("Error creating job: " + e.getMessage(), e);
        }
    }

    // ========================================
    // FUNCTION 2: GET JOB BY ID
    // ========================================
    @Override
    public Job getJobById(String id) throws RemoteException {
        try {
            System.out. println("Searching for job with ID: " + id);

            Document query = new Document("_id", new ObjectId(id));
            Document doc = jobCollection. find(query).first();

            if (doc == null) {
                System.out.println("❌ Job not found with ID: " + id);
                return null;
            }

            Job job = documentToJob(doc);

            System.out.println("✅ Job found!");
            System.out.println("   Title: " + job.getTitle());
            System.out.println("   Company: " + job.getCompany());

            return job;

        } catch (Exception e) {
            System.err. println("❌ Error getting job by ID: " + e.getMessage());
            throw new RemoteException("Error getting job by ID: " + e.getMessage(), e);
        }
    }

    // ========================================
    // FUNCTION 3: GET ALL JOBS
    // ========================================
    @Override
    public List<Job> getAllJobs() throws RemoteException {
        try {
            System.out.println("Retrieving all jobs...");

            List<Job> jobs = new ArrayList<>();

            for (Document doc : jobCollection.find()) {
                Job job = documentToJob(doc);
                jobs.add(job);
            }

            System.out. println("✅ Retrieved " + jobs.size() + " jobs");

            return jobs;

        } catch (Exception e) {
            System.err. println("❌ Error getting all jobs: " + e.getMessage());
            throw new RemoteException("Error getting all jobs: " + e.getMessage(), e);
        }
    }

    // ========================================
    // FUNCTION 4: SEARCH JOBS BY TITLE
    // ========================================
    @Override
    public List<Job> searchJobsByTitle(String title) throws RemoteException {
        try {
            System.out.println("Searching for jobs with title: " + title);

            List<Job> jobs = new ArrayList<>();

            // Case-insensitive regex search
            Document regex = new Document("$regex", title). append("$options", "i");
            Document query = new Document("title", regex);

            for (Document doc : jobCollection.find(query)) {
                Job job = documentToJob(doc);
                jobs.add(job);
            }

            System.out. println("✅ Found " + jobs.size() + " job(s) matching: " + title);

            return jobs;

        } catch (Exception e) {
            System.err.println("❌ Error searching jobs: " + e.getMessage());
            throw new RemoteException("Error searching jobs: " + e.getMessage(), e);
        }
    }

    // ========================================
    // FUNCTION 5: GET JOBS BY LOCATION
    // ========================================
    @Override
    public List<Job> getJobsByLocation(String location) throws RemoteException {
        try {
            System.out.println("Searching for jobs in location: " + location);

            List<Job> jobs = new ArrayList<>();
            Document query = new Document("location", location);

            for (Document doc : jobCollection.find(query)) {
                Job job = documentToJob(doc);
                jobs.add(job);
            }

            System.out. println("✅ Found " + jobs.size() + " job(s) in: " + location);

            return jobs;

        } catch (Exception e) {
            System.err.println("❌ Error getting jobs by location: " + e. getMessage());
            throw new RemoteException("Error getting jobs by location: " + e.getMessage(), e);
        }
    }

    // ========================================
    // FUNCTION 6: UPDATE JOB
    // ========================================
    @Override
    public boolean updateJob(Job job) throws RemoteException {
        try {
            System.out.println("Updating job: " + job.getTitle());

            Document updateDoc = new Document("title", job.getTitle())
                    . append("description", job.getDescription())
                    .append("company", job.getCompany())
                    .append("location", job.getLocation())
                    . append("salary", job.getSalary())
                    .append("requirements", job.getRequirements())
                    .append("status", job.getStatus());

            Document query = new Document("_id", new ObjectId(job. getId()));
            Document update = new Document("$set", updateDoc);

            long modifiedCount = jobCollection.updateOne(query, update).getModifiedCount();

            if (modifiedCount > 0) {
                System.out.println("✅ Job updated successfully");
                return true;
            } else {
                System.out.println("❌ Job not found or not modified");
                return false;
            }

        } catch (Exception e) {
            System. err.println("❌ Error updating job: " + e.getMessage());
            throw new RemoteException("Error updating job: " + e.getMessage(), e);
        }
    }

    // ========================================
    // FUNCTION 7: CLOSE JOB
    // ========================================
    @Override
    public boolean closeJob(String jobId) throws RemoteException {
        try {
            System.out.println("Closing job with ID: " + jobId);

            Document query = new Document("_id", new ObjectId(jobId));
            Document update = new Document("$set", new Document("status", "CLOSED"));

            long modifiedCount = jobCollection.updateOne(query, update).getModifiedCount();

            if (modifiedCount > 0) {
                System.out.println("✅ Job closed successfully");
                return true;
            } else {
                System.out.println("❌ Job not found or already closed");
                return false;
            }

        } catch (Exception e) {
            System.err.println("❌ Error closing job: " + e. getMessage());
            throw new RemoteException("Error closing job: " + e.getMessage(), e);
        }
    }

    // ========================================
    // FUNCTION 8: DELETE JOB
    // ========================================
    @Override
    public boolean deleteJob(String id) throws RemoteException {
        try {
            System.out. println("Deleting job with ID: " + id);

            Document query = new Document("_id", new ObjectId(id));
            long deletedCount = jobCollection.deleteOne(query).getDeletedCount();

            if (deletedCount > 0) {
                System.out. println("✅ Job deleted successfully");
                return true;
            } else {
                System.out.println("❌ Job not found");
                return false;
            }

        } catch (Exception e) {
            System.err.println("❌ Error deleting job: " + e.getMessage());
            throw new RemoteException("Error deleting job: " + e.getMessage(), e);
        }
    }

    // ========================================
    // HELPER METHOD
    // ========================================
    private Job documentToJob(Document doc) {
        if (doc == null) {
            return null;
        }

        Job job = new Job();
        job. setId(doc.getObjectId("_id").toString());
        job.setTitle(doc.getString("title"));
        job.setDescription(doc.getString("description"));
        job.setCompany(doc.getString("company"));
        job.setLocation(doc.getString("location"));

        // Handle salary - might be Double or Integer
        Object salaryObj = doc.get("salary");
        if (salaryObj instanceof Double) {
            job.setSalary((Double) salaryObj);
        } else if (salaryObj instanceof Integer) {
            job.setSalary(((Integer) salaryObj).doubleValue());
        } else if (salaryObj != null) {
            job.setSalary(Double.parseDouble(salaryObj. toString()));
        }

        job.setRequirements(doc.getString("requirements"));
        job.setStatus(doc.getString("status"));

        return job;
    }
}
