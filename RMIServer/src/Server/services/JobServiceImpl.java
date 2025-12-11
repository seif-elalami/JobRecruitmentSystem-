package Server.services;

import Server.database.MongoDBConnection;
import shared.interfaces.IJobService;
import shared.models.Job;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types. ObjectId;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class JobServiceImpl extends UnicastRemoteObject implements IJobService {

    private MongoCollection<Document> jobCollection;

    public JobServiceImpl() throws RemoteException {
        super();
        MongoDatabase database = MongoDBConnection.getInstance().getDatabase();
        jobCollection = database.getCollection("jobs");
        System.out.println("✅ JobService initialized");
    }

    @Override
    public String createJob(Job job) throws RemoteException {
        try {
            // Allow posting without recruiterId from older clients
            if (job.getRecruiterId() == null || job.getRecruiterId().isEmpty()) {
                job.setRecruiterId("UNKNOWN");
            }

            Document doc = new Document();
            doc.append("title", job.getTitle());
            doc.append("description", job.getDescription());
            doc.append("requirements", job.getRequirements());
            doc.append("company", job.getCompany());
            doc.append("location", job.getLocation());
            doc.append("salary", job.getSalary());
            doc.append("status", "OPEN");
            doc.append("postedDate", new Date());
            doc.append("recruiterId", job. getRecruiterId());  // ← ADD THIS

            jobCollection.insertOne(doc);

            String id = doc. getObjectId("_id").toString();
            job.setJobId(id);

            System.out.println("✅ Job created:  " + job.getTitle() + " (ID: " + id + ", Recruiter: " + job. getRecruiterId() + ")");

            return id;

        } catch (Exception e) {
            System.err.println("❌ Error creating job: " + e.getMessage());
            e.printStackTrace();
            throw new RemoteException("Failed to create job", e);
        }
    }

    @Override
    public Job getJobById(String id) throws RemoteException {
        try {
            Document doc = jobCollection.find(new Document("_id", new ObjectId(id))).first();

            if (doc == null) {
                return null;
            }

            return documentToJob(doc);

        } catch (Exception e) {
            System.err.println("❌ Error getting job: " + e.getMessage());
            throw new RemoteException("Failed to get job", e);
        }
    }

    @Override
    public List<Job> getAllJobs() throws RemoteException {
        try {
            List<Job> jobs = new ArrayList<>();

            for (Document doc : jobCollection.find()) {
                jobs.add(documentToJob(doc));
            }

            System.out.println("✅ Retrieved " + jobs.size() + " job(s)");

            return jobs;

        } catch (Exception e) {
            System.err.println("❌ Error getting jobs: " + e.getMessage());
            throw new RemoteException("Failed to get jobs", e);
        }
    }

    // ← ADD THIS METHOD
    @Override
    public List<Job> getJobsByRecruiterId(String recruiterId) throws RemoteException {
        try {
            List<Job> jobs = new ArrayList<>();

            Document query = new Document("recruiterId", recruiterId);

            for (Document doc : jobCollection.find(query)) {
                jobs.add(documentToJob(doc));
            }

            System.out.println("✅ Retrieved " + jobs.size() + " job(s) for recruiter: " + recruiterId);

            return jobs;

        } catch (Exception e) {
            System.err. println("❌ Error getting jobs by recruiter: " + e.getMessage());
            throw new RemoteException("Failed to get jobs by recruiter", e);
        }
    }

    @Override
    public List<Job> getJobsByLocation(String location) throws RemoteException {
        try {
            List<Job> jobs = new ArrayList<>();

            Document query = new Document("location", new Document("$regex", location).append("$options", "i"));

            for (Document doc : jobCollection.find(query)) {
                jobs.add(documentToJob(doc));
            }

            System.out.println("✅ Retrieved " + jobs.size() + " job(s) for location: " + location);

            return jobs;

        } catch (Exception e) {
            System.err.println("❌ Error getting jobs by location: " + e.getMessage());
            throw new RemoteException("Failed to get jobs by location", e);
        }
    }

    @Override
    public List<Job> searchJobsByTitle(String title) throws RemoteException {
        try {
            List<Job> jobs = new ArrayList<>();

            Document query = new Document("title", new Document("$regex", title).append("$options", "i"));

            for (Document doc : jobCollection.find(query)) {
                jobs.add(documentToJob(doc));
            }

            System.out. println("✅ Found " + jobs.size() + " job(s) matching:  " + title);

            return jobs;

        } catch (Exception e) {
            System.err.println("❌ Error searching jobs: " + e.getMessage());
            throw new RemoteException("Failed to search jobs", e);
        }
    }

    @Override
    public boolean updateJob(Job job) throws RemoteException {
        try {
            Document query = new Document("_id", new ObjectId(job.getJobId()));

            Document update = new Document();
            update.append("title", job.getTitle());
            update.append("description", job. getDescription());
            update.append("requirements", job.getRequirements());
            update.append("status", job.getStatus());

            Document updateDoc = new Document("$set", update);

            jobCollection.updateOne(query, updateDoc);

            System. out.println("✅ Job updated: " + job.getTitle());

            return true;

        } catch (Exception e) {
            System.err.println("❌ Error updating job: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteJob(String id) throws RemoteException {
        try {
            Document query = new Document("_id", new ObjectId(id));
            jobCollection.deleteOne(query);

            System.out.println("✅ Job deleted: " + id);

            return true;

        } catch (Exception e) {
            System.err.println("❌ Error deleting job: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean closeJob(String id) throws RemoteException {
        try {
            Document query = new Document("_id", new ObjectId(id));
            Document update = new Document("$set", new Document("status", "CLOSED"));

            jobCollection. updateOne(query, update);

            System.out.println("✅ Job closed: " + id);

            return true;

        } catch (Exception e) {
            System.err.println("❌ Error closing job: " + e. getMessage());
            return false;
        }
    }

    private Job documentToJob(Document doc) {
        Job job = new Job();
        job.setJobId(doc.getObjectId("_id").toString());
        job.setTitle(doc.getString("title"));
        job.setDescription(doc.getString("description"));
        job.setRequirements(doc.getList("requirements", String.class));
        job.setCompany(doc.getString("company"));
        job.setLocation(doc.getString("location"));
        Double salary = doc.getDouble("salary");
        job.setSalary(salary != null ? salary : 0.0);
        job.setStatus(doc.getString("status"));
        job.setPostedDate(doc.getDate("postedDate"));
        job.setRecruiterId(doc.getString("recruiterId"));  // ← ADD THIS

        return job;
    }
}
