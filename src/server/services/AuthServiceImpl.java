package server.services;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;
import server.database.MongoDBConnection;
import server.utils.PasswordUtil;
import shared.interfaces.IAuthService;
import shared.interfaces.IApplicantService;
import shared.models. User;
import shared.models. Session;
import shared.models. Applicant;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util. Date;
import java.util.HashMap;
import java.util.Map;

public class AuthServiceImpl extends UnicastRemoteObject implements IAuthService {

    private MongoCollection<Document> userCollection;
    private Map<String, Session> activeSessions;  // In-memory session storage
    private IApplicantService applicantService;

    public AuthServiceImpl(IApplicantService applicantService) throws RemoteException {
        super();
        MongoDatabase database = MongoDBConnection.getInstance().getDatabase();
        userCollection = database.getCollection("users");
        activeSessions = new HashMap<>();
        this.applicantService = applicantService;
        System.out.println("✅ AuthService initialized");
    }

    // ========================================
    // REGISTER
    // ========================================
    @Override
    public String register(String email, String password, String role, String name) throws RemoteException {
        try {
            System.out.println("Registering new user: " + email + " as " + role);

            // Validate inputs
            if (! PasswordUtil.isValidEmail(email)) {
                System. out.println("❌ Invalid email format");
                return null;
            }

            if (! PasswordUtil.isValidPassword(password)) {
                System.out.println("❌ Password too weak (minimum 6 characters)");
                return null;
            }

            if (! role.equals("APPLICANT") && !role.equals("RECRUITER")) {
                System.out.println("❌ Invalid role: " + role);
                return null;
            }

            // Check if email already exists
            Document existingUser = userCollection.find(new Document("email", email)). first();
            if (existingUser != null) {
                System.out.println("❌ Email already registered");
                return null;
            }

            // Hash password
            String passwordHash = PasswordUtil.hashPassword(password);

            // Create user document
            Document userDoc = new Document();
            userDoc.append("email", email);
            userDoc.append("passwordHash", passwordHash);
            userDoc.append("role", role);
            userDoc.append("createdAt", new Date());
            userDoc.append("lastLogin", null);
            userDoc.append("isActive", true);

            // Insert user
            userCollection.insertOne(userDoc);
            String userId = userDoc.getObjectId("_id").toString();

            // Create profile based on role
            String profileId = null;
            if (role. equals("APPLICANT")) {
                // Create applicant profile
                Applicant applicant = new Applicant();
                applicant.setName(name);
                applicant. setEmail(email);
                applicant.setPhone("");
                applicant.setResume("");
                applicant.setEducation("");
                applicant.setExperience(0);

                profileId = applicantService. createApplicant(applicant);
            } else if (role.equals("RECRUITER")) {
                // Create recruiter profile (you'll implement this later)
                // For now, just use the userId as profileId
                profileId = userId;
            }

            // Update user with profileId
            userCollection.updateOne(
                new Document("_id", new ObjectId(userId)),
                new Document("$set", new Document("profileId", profileId))
            );

            System.out.println("✅ User registered successfully");
            System.out.println("   User ID: " + userId);
            System.out.println("   Profile ID: " + profileId);

            return userId;

        } catch (Exception e) {
            System.err.println("❌ Error registering user: " + e. getMessage());
            throw new RemoteException("Error registering user: " + e.getMessage(), e);
        }
    }

    // ========================================
    // LOGIN
    // ========================================
    @Override
    public Session login(String email, String password) throws RemoteException {
        try {
            System.out.println("Login attempt for: " + email);

            // Find user by email
            Document userDoc = userCollection.find(new Document("email", email)).first();

            if (userDoc == null) {
                System.out.println("❌ User not found");
                return null;
            }

            // Check if account is active
            if (!userDoc.getBoolean("isActive", true)) {
                System.out.println("❌ Account is deactivated");
                return null;
            }

            // Verify password
            String storedHash = userDoc.getString("passwordHash");
            if (!PasswordUtil.verifyPassword(password, storedHash)) {
                System. out.println("❌ Invalid password");
                return null;
            }

            // Update last login
            userCollection.updateOne(
                new Document("_id", userDoc.getObjectId("_id")),
                new Document("$set", new Document("lastLogin", new Date()))
            );

            // Create session
            String userId = userDoc.getObjectId("_id").toString();
            String role = userDoc.getString("role");
            String profileId = userDoc.getString("profileId");

            Session session = new Session(userId, email, role, profileId);
            activeSessions.put(session.getSessionToken(), session);

            System.out.println("✅ Login successful");
            System.out. println("   Session Token: " + session.getSessionToken());
            System.out.println("   Role: " + role);

            return session;

        } catch (Exception e) {
            System.err.println("❌ Error during login: " + e.getMessage());
            throw new RemoteException("Error during login: " + e.getMessage(), e);
        }
    }

    // ========================================
    // LOGOUT
    // ========================================
    @Override
    public boolean logout(String sessionToken) throws RemoteException {
        try {
            System.out.println("Logout request for token: " + sessionToken);

            if (activeSessions.containsKey(sessionToken)) {
                activeSessions.remove(sessionToken);
                System.out.println("✅ Logout successful");
                return true;
            }

            System.out.println("❌ Session not found");
            return false;

        } catch (Exception e) {
            System.err.println("❌ Error during logout: " + e.getMessage());
            throw new RemoteException("Error during logout: " + e. getMessage(), e);
        }
    }

    // ========================================
    // VALIDATE SESSION
    // ========================================
    @Override
    public Session validateSession(String sessionToken) throws RemoteException {
        try {
            Session session = activeSessions.get(sessionToken);

            if (session == null) {
                System.out.println("❌ Session not found");
                return null;
            }

            if (session.isExpired()) {
                System.out.println("❌ Session expired");
                activeSessions.remove(sessionToken);
                return null;
            }

            return session;

        } catch (Exception e) {
            System.err.println("❌ Error validating session: " + e.getMessage());
            throw new RemoteException("Error validating session: " + e.getMessage(), e);
        }
    }

    // ========================================
    // GET USER BY ID
    // ========================================
    @Override
    public User getUserById(String userId) throws RemoteException {
        try {
            Document userDoc = userCollection.find(new Document("_id", new ObjectId(userId))).first();
            return documentToUser(userDoc);
        } catch (Exception e) {
            throw new RemoteException("Error getting user by ID: " + e.getMessage(), e);
        }
    }

    // ========================================
    // GET USER BY EMAIL
    // ========================================
    @Override
    public User getUserByEmail(String email) throws RemoteException {
        try {
            Document userDoc = userCollection.find(new Document("email", email)). first();
            return documentToUser(userDoc);
        } catch (Exception e) {
            throw new RemoteException("Error getting user by email: " + e.getMessage(), e);
        }
    }

    // ========================================
    // CHANGE PASSWORD
    // ========================================
    @Override
    public boolean changePassword(String userId, String oldPassword, String newPassword) throws RemoteException {
        try {
            System.out.println("Change password request for user: " + userId);

            // Get user
            Document userDoc = userCollection.find(new Document("_id", new ObjectId(userId))).first();
            if (userDoc == null) {
                System.out.println("❌ User not found");
                return false;
            }

            // Verify old password
            String storedHash = userDoc.getString("passwordHash");
            if (!PasswordUtil.verifyPassword(oldPassword, storedHash)) {
                System.out.println("❌ Old password incorrect");
                return false;
            }

            // Validate new password
            if (!PasswordUtil.isValidPassword(newPassword)) {
                System.out. println("❌ New password too weak");
                return false;
            }

            // Hash new password
            String newPasswordHash = PasswordUtil.hashPassword(newPassword);

            // Update password
            userCollection.updateOne(
                new Document("_id", new ObjectId(userId)),
                new Document("$set", new Document("passwordHash", newPasswordHash))
            );

            System.out.println("✅ Password changed successfully");
            return true;

        } catch (Exception e) {
            System.err.println("❌ Error changing password: " + e.getMessage());
            throw new RemoteException("Error changing password: " + e.getMessage(), e);
        }
    }

    // ========================================
    // HAS ROLE
    // ========================================
    @Override
    public boolean hasRole(String sessionToken, String requiredRole) throws RemoteException {
        try {
            Session session = validateSession(sessionToken);

            if (session == null) {
                return false;
            }

            return session.getRole().equals(requiredRole);

        } catch (Exception e) {
            throw new RemoteException("Error checking role: " + e.getMessage(), e);
        }
    }

    // ========================================
    // HELPER METHOD
    // ========================================
    private User documentToUser(Document doc) {
        if (doc == null) {
            return null;
        }

        User user = new User();
        user.setId(doc.getObjectId("_id"). toString());
        user.setEmail(doc.getString("email"));
        user.setPasswordHash(doc.getString("passwordHash"));
        user.setRole(doc.getString("role"));
        user.setProfileId(doc.getString("profileId"));
        user.setCreatedAt(doc.getDate("createdAt"));
        user.setLastLogin(doc.getDate("lastLogin"));
        user.setActive(doc.getBoolean("isActive", true));

        return user;
    }
}
