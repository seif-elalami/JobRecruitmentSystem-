package shared.interfaces;

import shared.models.User;
import shared.models.Session;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Authentication Service Interface
 * Handles user registration, login, and session management
 */
public interface IAuthService extends Remote {

    /**
     * Register a new user
     * @param user User object with username, email, password, and role
     * @return Session object if successful
     */
    Session register(User user) throws RemoteException;

    /**
     * Login user
     * @param email User's email
     * @param password User's password
     * @return Session object if successful
     */
    Session login(String email, String password) throws RemoteException;

    /**
     * Login with passkey (for admin quick-access)
     * @param email User's email
     * @param passkey Admin passkey (stored plain-text in database)
     * @return Session object if successful
     */
    Session loginWithPasskey(String email, String passkey) throws RemoteException;

    /**
     * Logout user
     * @param sessionToken Session token
     * @return true if successful
     */
    boolean logout(String sessionToken) throws RemoteException;

    /**
     * Validate session token
     * @param sessionToken Session token to validate
     * @return Session object if valid, null otherwise
     */
    Session validateSession(String sessionToken) throws RemoteException;

    /**
     * Get user by ID
     * @param userId User ID
     * @return User object if found, null otherwise
     */
    User getUserById(String userId) throws RemoteException;

    /**
     * Get user by email
     * @param email User email
     * @return User object if found, null otherwise
     */
    User getUserByEmail(String email) throws RemoteException;

    /**
     * Get all users in the system
     * @return List of all users
     */
    java.util.List<User> getAllUsers() throws RemoteException;

    /**
     * Delete user by ID (admin only)
     * @param userId User ID to delete
     * @return true if deleted
     */
    boolean deleteUser(String userId) throws RemoteException;

    /**
     * Set user active flag (soft deactivate)
     * @param userId User ID
     * @param active true to activate, false to deactivate
     * @return true if updated
     */
    boolean setUserActive(String userId, boolean active) throws RemoteException;

    /**
     * Change user password
     * @param email User's email
     * @param oldPassword Current password
     * @param newPassword New password
     * @return true if successful
     */
    boolean changePassword(String email, String oldPassword, String newPassword) throws RemoteException;

    /**
     * Check if session has required role
     * @param sessionToken Session token
     * @param requiredRole Required role
     * @return true if session has the role
     */
    boolean hasRole(String sessionToken, String requiredRole) throws RemoteException;
}