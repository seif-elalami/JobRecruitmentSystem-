package shared.interfaces;

import shared.models.Applicant;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface IApplicantService extends Remote {

    // CREATE - Add a new applicant to the database
    String createApplicant(Applicant applicant) throws RemoteException;

    // READ - Get applicant by ID
    Applicant getApplicantById(String id) throws RemoteException;

    // READ - Get applicant by email (for login/search)
    Applicant getApplicantByEmail(String email) throws RemoteException;

    // READ - Get all applicants
    List<Applicant> getAllApplicants() throws RemoteException;

    // UPDATE - Update applicant information
    boolean updateApplicant(Applicant applicant) throws RemoteException;

    // DELETE - Remove applicant from database
    boolean deleteApplicant(String id) throws RemoteException;

    // READ - Search applicants by skill (NEW!)
    List<Applicant> searchApplicantsBySkill(String skill) throws RemoteException;
}
