package shared.interfaces;

import shared.models.Job;

import java.rmi.Remote;
import java. rmi.RemoteException;
import java.util.List;

public interface IJobService extends Remote {

    /**
     * Create a new job posting
     */
    String createJob(Job job) throws RemoteException;

    /**
     * Get job by ID
     */
    Job getJobById(String id) throws RemoteException;

    /**
     * Get all jobs
     */
    List<Job> getAllJobs() throws RemoteException;

    /**
     * Get jobs posted by specific recruiter
     */
    List<Job> getJobsByRecruiterId(String recruiterId) throws RemoteException;  // ← ADD THIS

    /**
     * Get jobs filtered by location
     */
    List<Job> getJobsByLocation(String location) throws RemoteException;

    /**
     * Search jobs by title
     */
    List<Job> searchJobsByTitle(String title) throws RemoteException;

    /**
     * Update job
     */
    boolean updateJob(Job job) throws RemoteException;

    /**
     * Delete job
     */
    boolean deleteJob(String id) throws RemoteException;

    /**
     * Close job (change status to CLOSED)
     */
    boolean closeJob(String id) throws RemoteException;
}
