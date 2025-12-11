package shared.interfaces;

import shared.models.Recruiter;
import shared.models.Job;
import shared.models.Application;
import shared.models. Applicant;
import shared.models.Interview;

import java.rmi.Remote;
import java. rmi.RemoteException;
import java.util.Date;
import java.util.List;

/**
 * Recruiter Service Interface
 * Handles recruiter-specific operations:  jobs, applications, interviews
 */
public interface IRecruiterService extends Remote {

    // ========================================
    // Recruiter Profile Management (Keep for Auth)
    // ========================================

    /**
     * Create a new recruiter profile (used by AuthService)
     */
    String createRecruiter(Recruiter recruiter) throws RemoteException;

    /**
     * Get recruiter by ID
     */
    Recruiter getRecruiterById(String id) throws RemoteException;

    /**
     * Get recruiter by email
     */
    Recruiter getRecruiterByEmail(String email) throws RemoteException;

    /**
     * Update recruiter profile
     */
    boolean updateRecruiter(Recruiter recruiter) throws RemoteException;

    // ========================================
    // 1. Job Posting Management
    // ========================================

    /**
     * Post a new job (Create job posting)
     * @param job Job object with details
     * @return Job ID if successful
     */
    String postJob(Job job) throws RemoteException;

    /**
     * View all jobs posted by specific recruiter
     * @param recruiterId ID of the recruiter
     * @return List of jobs posted by this recruiter
     */
    List<Job> getMyJobPostings(String recruiterId) throws RemoteException;

    /**
     * Close a job posting
     * @param jobId ID of the job to close
     * @param recruiterId ID of the recruiter (for verification)
     * @return true if successful
     */
    boolean closeJobPosting(String jobId, String recruiterId) throws RemoteException;

    // ========================================
    // 2. Application Management
    // ========================================

    /**
     * View all applications for a specific job
     * @param jobId ID of the job
     * @return List of applications for this job
     */
    List<Application> getApplicationsForJob(String jobId) throws RemoteException;

    /**
     * Update application status (accept/reject)
     * @param applicationId ID of the application
     * @param status New status ("ACCEPTED", "REJECTED", "UNDER_REVIEW")
     * @return true if successful
     */
    boolean updateApplicationStatus(String applicationId, String status) throws RemoteException;

    // ========================================
    // 3. Applicant Search
    // ========================================

    /**
     * Search for applicants by skills
     * @param skills Skills to search for (comma-separated or keywords)
     * @return List of matching applicants
     */
    List<Applicant> searchApplicantsBySkills(String skills) throws RemoteException;

    /**
     * Search for applicants by experience
     * @param experience Experience keywords
     * @return List of matching applicants
     */
    List<Applicant> searchApplicantsByExperience(String experience) throws RemoteException;

    // ========================================
    // 4. Interview Management
    // ========================================

    /**
     * Create/Schedule a new interview
     * @param interview Interview object with details
     * @return Interview ID if successful
     */
    String createInterview(Interview interview) throws RemoteException;

    /**
     * Update interview details
     * @param interview Updated interview object
     * @return true if successful
     */
    boolean updateInterview(Interview interview) throws RemoteException;

    /**
     * Cancel an interview
     * @param interviewId ID of the interview
     * @return true if successful
     */
    boolean cancelInterview(String interviewId) throws RemoteException;

    /**
     * Get all interviews for a specific recruiter
     * @param recruiterId ID of the recruiter
     * @return List of interviews
     */
    List<Interview> getMyInterviews(String recruiterId) throws RemoteException;

    /**
     * Get interview by ID
     * @param interviewId ID of the interview
     * @return Interview object
     */
    Interview getInterviewById(String interviewId) throws RemoteException;
}
