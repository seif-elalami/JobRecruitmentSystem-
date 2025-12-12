package shared.interfaces;

import shared.models.Recruiter;
import shared.models.Job;
import shared.models.Application;
import shared.models. Applicant;
import shared. models.Interview;

import java. rmi.Remote;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;

public interface IRecruiterService extends Remote {

    // ========================================
    // RECRUITER MANAGEMENT
    // ========================================

    String createRecruiter(Recruiter recruiter) throws RemoteException;

    Recruiter getRecruiterById(String id) throws RemoteException;

    Recruiter getRecruiterByEmail(String email) throws RemoteException;

    boolean updateRecruiter(Recruiter recruiter) throws RemoteException;

    // ========================================
    // JOB MANAGEMENT
    // ========================================

    String postJob(Job job) throws RemoteException;

    List<Job> getMyJobPostings(String recruiterId) throws RemoteException;

    boolean closeJobPosting(String jobId, String recruiterId) throws RemoteException;

    // ========================================
    // APPLICATION MANAGEMENT
    // ========================================

    List<Application> getApplicationsForJob(String jobId) throws RemoteException;

    boolean updateApplicationStatus(String applicationId, String status) throws RemoteException;

    // ========================================
    // APPLICANT SEARCH (Returns Full Applicant Objects)
    // ========================================

    List<Applicant> searchApplicantsBySkills(String skills) throws RemoteException;

    List<Applicant> searchApplicantsByExperience(String experience) throws RemoteException;

    // ========================================
    // INTERVIEW MANAGEMENT
    // ========================================

    String createInterview(Interview interview) throws RemoteException;

    boolean updateInterview(Interview interview) throws RemoteException;

    boolean cancelInterview(String interviewId) throws RemoteException;

    List<Interview> getMyInterviews(String recruiterId) throws RemoteException;

    Interview getInterviewById(String interviewId) throws RemoteException;

    // ========================================
    // ✅ MATCH CV FEATURE - READ-ONLY CANDIDATE VIEWS
    // ========================================

    /**
     * Get read-only candidate views for all applicants who applied to a specific job
     * @param jobId The job ID
     * @return List of read-only candidate profiles
     */
    List<ICandidateView> getCandidatesForJob(String jobId) throws RemoteException;

    /**
     * Get a single candidate's read-only profile by ID
     * @param candidateId The candidate/applicant ID
     * @return Read-only candidate view
     */
    ICandidateView getCandidateById(String candidateId) throws RemoteException;

    /**
     * Search all candidates by skills (returns read-only views)
     * Useful for finding qualified candidates across all applications
     * @param skills Skills to search for (e.g., "Java, RMI")
     * @return List of matching candidates (read-only)
     */
    List<ICandidateView> searchCandidatesBySkillsReadOnly(String skills) throws RemoteException;

    /**
     * Search candidates by minimum years of experience (returns read-only views)
     * @param minYears Minimum years of experience required
     * @return List of matching candidates (read-only)
     */
    List<ICandidateView> searchCandidatesByMinExperience(int minYears) throws RemoteException;
}
