package shared.interfaces;

import shared.models.User;
import shared.models.Session;
import java.rmi.Remote;
import java. rmi.RemoteException;

public interface IAuthService extends Remote {

    /**
     * Register a new user (Applicant or Recruiter)
     *
     * @param email User's email (must be unique)
     * @param password User's password (will be hashed)
     * @param role "APPLICANT" or "RECRUITER"
     * @param name Full name
     * @return User ID if successful, null if email already exists
     */
    String register(String email, String password, String role, String name) throws RemoteException;

    /**
     * Login user
     *
     * @param email User's email
     * @param password User's password
     * @return Session object if successful, null if invalid credentials
     */
    Session login(String email, String password) throws RemoteException;

    /**
     * Logout user
     *
     * @param sessionToken Session token to invalidate
     * @return true if logout successful
     */
    boolean logout(String sessionToken) throws RemoteException;

    /**
     * Validate session
     *
     * @param sessionToken Session token to validate
     * @return Session object if valid, null if invalid or expired
     */
    Session validateSession(String sessionToken) throws RemoteException;

    /**
     * Get user by ID
     *
     * @param userId User ID
     * @return User object
     */
    User getUserById(String userId) throws RemoteException;

    /**
     * Get user by email
     *
     * @param email User's email
     * @return User object
     */
    User getUserByEmail(String email) throws RemoteException;

    /**
     * Change password
     *
     * @param userId User ID
     * @param oldPassword Current password
     * @param newPassword New password
     * @return true if successful
     */
    boolean changePassword(String userId, String oldPassword, String newPassword) throws RemoteException;

    /**
     * Check if user has permission for a role
     *
     * @param sessionToken Session token
     * @param requiredRole Required role ("APPLICANT" or "RECRUITER")
     * @return true if user has required role
     */
    boolean hasRole(String sessionToken, String requiredRole) throws RemoteException;
}
