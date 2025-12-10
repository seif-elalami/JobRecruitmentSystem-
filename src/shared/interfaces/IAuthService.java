package shared.interfaces;

import shared.models. Session;
import shared.models. User;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IAuthService extends Remote {

    // Original register method (keep for backward compatibility)
    String register(String email, String password, String role, String name) throws RemoteException;

    // NEW: Register with phone number
    String registerWithPhone(String email, String password, String role, String name, String phone) throws RemoteException;

    Session login(String email, String password) throws RemoteException;

    boolean logout(String sessionToken) throws RemoteException;

    Session validateSession(String sessionToken) throws RemoteException;

    User getUserById(String userId) throws RemoteException;

    User getUserByEmail(String email) throws RemoteException;

    boolean changePassword(String userId, String oldPassword, String newPassword) throws RemoteException;

    boolean hasRole(String sessionToken, String requiredRole) throws RemoteException;
}
