package shared.interfaces;

import shared.models.Application;
import java.rmi. Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface IApplicationService extends Remote {

    // CREATE - Submit a job application
    String CreateApplication(Application application) throws RemoteException;

String SubmitApplication(Application application) throws RemoteException;
    // READ - Get application by ID
    Application getApplicationById(String id) throws RemoteException;

    // READ - Get all applications for a specific job (used by recruiters)
    List<Application> getApplicationsByJobId(String jobId) throws RemoteException;

    // READ - Get all applications by a specific applicant (used by applicants)
    List<Application> getApplicationsByApplicantId(String applicantId) throws RemoteException;

    // READ - Get all applications (used by recruiters/admins)
    List<Application> getAllApplications() throws RemoteException;

    // UPDATE - Update application status (used by recruiters)
    boolean updateApplicationStatus(String applicationId, String status) throws RemoteException;

    // DELETE - Cancel/delete application
    boolean deleteApplication(String id) throws RemoteException;
}
