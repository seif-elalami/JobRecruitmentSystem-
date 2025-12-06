package shared. interfaces;

import shared.models. Job;
import java.rmi.Remote;
import java. rmi.RemoteException;
import java.util.List;

public interface IJobService extends Remote {

    // CREATE - Post a new job (used by recruiters)
    String createJob(Job job) throws RemoteException;

    // READ - Get job by ID
    Job getJobById(String id) throws RemoteException;

    // READ - Get all open jobs (used by applicants to browse)
    List<Job> getAllJobs() throws RemoteException;

    // READ - Search jobs by title
    List<Job> searchJobsByTitle(String title) throws RemoteException;

    // READ - Get jobs by location
    List<Job> getJobsByLocation(String location) throws RemoteException;

    // UPDATE - Update job details
    boolean updateJob(Job job) throws RemoteException;

    // UPDATE - Close a job (no longer accepting applications)
    boolean closeJob(String jobId) throws RemoteException;

    // DELETE - Remove a job
    boolean deleteJob(String id) throws RemoteException;
}
