package server.services;

import server.database.MongoDBConnection;
import server.utils. PasswordUtil;
import server.utils.ValidationUtil;
import shared.interfaces.IAuthService;
import shared.interfaces.IApplicantService;
import shared. models.Session;
import shared.models.User;
import shared.models. Applicant;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org. bson.types.ObjectId;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util. Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AuthServiceImpl extends UnicastRemoteObject implements IAuthService {

    private MongoCollection<Document> userCollection;
    private IApplicantService applicantService;
    private Map<String, Session> activeSessions;

    public AuthServiceImpl(IApplicantService applicantService) throws RemoteException {
        super();
        MongoDatabase database = MongoDBConnection.getInstance().getDatabase();
        userCollection = database.getCollection("users");
        this.applicantService = applicantService;
        this.activeSessions = new HashMap<>();

        System.out.println("✅ AuthService initialized");
    }

    @Override
    public String register(String email, String password, String role, String name) throws RemoteException {
        // Call new method with empty phone
        return registerWithPhone(email, password, role, name, "");
    }

    @Override
    public String registerWithPhone(String email, String password, String role, String name, String phone) throws RemoteException {
        try {
            // ============================================
            // VALIDATION - Email Format
            // ============================================
            if (!ValidationUtil.isValidEmail(email)) {
                System. err.println("❌ Registration failed: " + ValidationUtil.getEmailErrorMessage());
                return null;
            }

            // ============================================
            // VALIDATION - Password Length
            // ============================================
            if (!ValidationUtil.isValidPassword(password)) {
                System. err.println("❌ Registration failed: " + ValidationUtil.getPasswordErrorMessage());
                return null;
            }

            // ============================================
            // VALIDATION - Name
            // ============================================
            if (!ValidationUtil.isValidName(name)) {
                System.err.println("❌ Registration failed: " + ValidationUtil.getNameErrorMessage());
                return null;
            }

            // ============================================
            // VALIDATION - Phone (if provided and applicant)
            // ============================================
            if (role. equalsIgnoreCase("APPLICANT") && phone != null && !phone.isEmpty()) {
                if (!ValidationUtil.isValidPhone(phone)) {
                    System. err.println("❌ Registration failed: " + ValidationUtil.getPhoneErrorMessage());
                    return null;
                }
            }

            // ============================================
            // Check if email already exists
            // ============================================
            Document existingUser = userCollection.find(new Document("email", email)).first();
            if (existingUser != null) {
                System.err.println("❌ Registration failed: Email already exists");
                return null;
            }

            // ============================================
            // Hash password
            // ============================================
            String passwordHash = PasswordUtil.hashPassword(password);

            // ============================================
            // Create profile based on role
            // ============================================
            String profileId = null;

            if (role.equalsIgnoreCase("APPLICANT")) {
                // Create applicant profile WITH phone
                Applicant applicant = new Applicant();
                applicant.setName(name);
                applicant.setEmail(email);
                applicant.setPhone(phone != null ? phone : "");
                applicant.setResume("");
                applicant.setEducation("");
                applicant.setExperience(0);

                profileId = applicantService. createApplicant(applicant);

            } else if (role.equalsIgnoreCase("RECRUITER")) {
                // For recruiter, placeholder
                profileId = new ObjectId().toString();
            }

            if (profileId == null) {
                System.err.println("❌ Registration failed: Could not create profile");
                return null;
            }

            // ============================================
            // Create user document
            // ============================================
            Document userDoc = new Document();
            userDoc.append("email", email);
            userDoc. append("passwordHash", passwordHash);
            userDoc.append("role", role. toUpperCase());
            userDoc. append("profileId", profileId);
            userDoc.append("isActive", true);
            userDoc.append("createdAt", new Date());
            userDoc.append("lastLogin", null);

            userCollection.insertOne(userDoc);

            String userId = userDoc.getObjectId("_id").toString();

            System.out.println("✅ User registered successfully:");
            System.out.println("   User ID: " + userId);
            System.out.println("   Email: " + email);
            System.out.println("   Role: " + role);
            if (phone != null && !phone.isEmpty()) {
                System. out.println("   Phone: " + phone);
            }

            return userId;

        } catch (Exception e) {
            System.err.println("❌ Registration error: " + e.getMessage());
            e.printStackTrace();
            throw new RemoteException("Registration failed", e);
        }
    }

    @Override
    public Session login(String email, String password) throws RemoteException {
        try {
            // Find user by email
            Document userDoc = userCollection.find(new Document("email", email)).first();

            if (userDoc == null) {
                System.err.println("❌ Login failed: User not found");
                return null;
            }

            // Verify password
            String storedHash = userDoc.getString("passwordHash");
            if (!PasswordUtil.verifyPassword(password, storedHash)) {
                System. err.println("❌ Login failed: Invalid password");
                return null;
            }

            // Check if account is active
            if (! userDoc.getBoolean("isActive", true)) {
                System.err.println("❌ Login failed: Account is inactive");
                return null;
            }

            // Update last login
            userCollection.updateOne(
                new Document("_id", userDoc.getObjectId("_id")),
                new Document("$set", new Document("lastLogin", new Date()))
            );

            // Create session
            String sessionToken = UUID.randomUUID().toString();
            String userId = userDoc.getObjectId("_id").toString();
            String role = userDoc.getString("role");
            String profileId = userDoc.getString("profileId");

            Date now = new Date();
            Date expiresAt = new Date(now.getTime() + (24 * 60 * 60 * 1000)); // 24 hours

            Session session = new Session(sessionToken, userId, email, role, profileId, now, expiresAt);

            // Store session
            activeSessions.put(sessionToken, session);

            System.out.println("✅ User logged in:  " + email + " (Role: " + role + ")");

            return session;

        } catch (Exception e) {
            System.err.println("❌ Login error: " + e.getMessage());
            e.printStackTrace();
            throw new RemoteException("Login failed", e);
        }
    }

    @Override
    public boolean logout(String sessionToken) throws RemoteException {
        try {
            Session session = activeSessions.remove(sessionToken);

            if (session != null) {
                System.out.println("✅ User logged out: " + session.getEmail());
                return true;
            }

            return false;

        } catch (Exception e) {
            System.err.println("❌ Logout error: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Session validateSession(String sessionToken) throws RemoteException {
        try {
            Session session = activeSessions.get(sessionToken);

            if (session == null) {
                return null;
            }

            if (session.isExpired()) {
                activeSessions.remove(sessionToken);
                return null;
            }

            return session;

        } catch (Exception e) {
            System.err. println("❌ Session validation error: " + e.getMessage());
            return null;
        }
    }

    @Override
    public User getUserById(String userId) throws RemoteException {
        try {
            Document userDoc = userCollection.find(new Document("_id", new ObjectId(userId))).first();

            if (userDoc == null) {
                return null;
            }

            return documentToUser(userDoc);

        } catch (Exception e) {
            System.err.println("❌ Error getting user: " + e.getMessage());
            throw new RemoteException("Failed to get user", e);
        }
    }

    @Override
    public User getUserByEmail(String email) throws RemoteException {
        try {
            Document userDoc = userCollection.find(new Document("email", email)).first();

            if (userDoc == null) {
                return null;
            }

            return documentToUser(userDoc);

        } catch (Exception e) {
            System.err.println("❌ Error getting user:  " + e.getMessage());
            throw new RemoteException("Failed to get user", e);
        }
    }

    @Override
    public boolean changePassword(String userId, String oldPassword, String newPassword) throws RemoteException {
        try {
            // Validate new password
            if (!ValidationUtil. isValidPassword(newPassword)) {
                System.err.println("❌ Password change failed: " + ValidationUtil.getPasswordErrorMessage());
                return false;
            }

            // Get user
            Document userDoc = userCollection. find(new Document("_id", new ObjectId(userId))).first();

            if (userDoc == null) {
                System.err.println("❌ Password change failed:  User not found");
                return false;
            }

            // Verify old password
            String storedHash = userDoc.getString("passwordHash");
            if (!PasswordUtil.verifyPassword(oldPassword, storedHash)) {
                System.err.println("❌ Password change failed: Old password incorrect");
                return false;
            }

            // Hash new password
            String newHash = PasswordUtil.hashPassword(newPassword);

            // Update password
            userCollection.updateOne(
                new Document("_id", new ObjectId(userId)),
                new Document("$set", new Document("passwordHash", newHash))
            );

            System.out.println("✅ Password changed for user: " + userDoc.getString("email"));

            return true;

        } catch (Exception e) {
            System.err.println("❌ Password change error: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean hasRole(String sessionToken, String requiredRole) throws RemoteException {
        try {
            Session session = validateSession(sessionToken);

            if (session == null) {
                return false;
            }

            return session.getRole().equalsIgnoreCase(requiredRole);

        } catch (Exception e) {
            System.err.println("❌ Role check error: " + e.getMessage());
            return false;
        }
    }

    private User documentToUser(Document doc) {
        User user = new User();
        user.setId(doc.getObjectId("_id").toString());
        user.setEmail(doc.getString("email"));
        user.setPasswordHash(doc.getString("passwordHash"));
        user.setRole(doc.getString("role"));
        user.setProfileId(doc. getString("profileId"));
        user.setActive(doc.getBoolean("isActive", true));
        user.setCreatedAt(doc.getDate("createdAt"));
        user.setLastLogin(doc. getDate("lastLogin"));
        return user;
    }
}
