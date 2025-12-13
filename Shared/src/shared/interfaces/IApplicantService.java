package shared.interfaces;

import shared.models.Applicant;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import shared.models.Notification;

public interface IApplicantService extends Remote {


    String createApplicant(Applicant applicant) throws RemoteException;


    Applicant getApplicantById(String id) throws RemoteException;


    Applicant getApplicantByEmail(String email) throws RemoteException;


    List<Applicant> getAllApplicants() throws RemoteException;


    boolean updateApplicant(Applicant applicant) throws RemoteException;


    boolean deleteApplicant(String id) throws RemoteException;


    List<Applicant> searchApplicantsBySkills(String skills) throws RemoteException;


    List<Applicant> searchApplicantsByExperience(String experience) throws RemoteException;

    // ========================================
    // NOTIFICATIONS
    // ========================================

    List<Notification> getNotifications(String applicantId) throws RemoteException;

    // ========================================
    // RESUME STORAGE (Dedicated collection)
    // ========================================
    boolean uploadResume(String applicantId, String resumeText) throws RemoteException;
    String getResume(String applicantId) throws RemoteException;
}
