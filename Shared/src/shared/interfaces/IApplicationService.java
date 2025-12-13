package shared.interfaces;

import shared.models.Application;
import java.rmi. Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface IApplicationService extends Remote {

    String CreateApplication(Application application) throws RemoteException;

String SubmitApplication(Application application) throws RemoteException;

    Application getApplicationById(String id) throws RemoteException;

    List<Application> getApplicationsByJobId(String jobId) throws RemoteException;

    List<Application> getApplicationsByApplicantId(String applicantId) throws RemoteException;

    List<Application> getAllApplications() throws RemoteException;

    boolean updateApplicationStatus(String applicationId, String status) throws RemoteException;

    boolean deleteApplication(String id) throws RemoteException;
}