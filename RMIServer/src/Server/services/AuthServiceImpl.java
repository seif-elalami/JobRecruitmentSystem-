package Server.services;

import Server.database.MongoDBConnection;
import Server.utils.PasswordUtil;
import Server.utils.ValidationUtil;
import shared.interfaces.IAuthService;
import shared.models.Session;
import shared.models.User;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AuthServiceImpl extends UnicastRemoteObject implements IAuthService {

    private MongoCollection<Document> userCollection;
    private Map<String, Session> activeSessions;

    public AuthServiceImpl() throws RemoteException {
        super();
        MongoDatabase database = MongoDBConnection.getInstance().getDatabase();
        userCollection = database.getCollection("users");
        this.activeSessions = new HashMap<>();

        System.out.println("✅ AuthService initialized");
    }

    @Override
    public Session register(User user) throws RemoteException {
        try {
            System.out.println("📝 Registration attempt:  " + user.getEmail());

            // Validate email format
            if (!ValidationUtil.isValidEmail(user.getEmail())) {
                System.err.println("❌ Registration failed: " + ValidationUtil.getEmailErrorMessage());
                throw new RemoteException(ValidationUtil.getEmailErrorMessage());
            }

            // Check if email already exists
            User existingUser = getUserByEmail(user.getEmail());
            if (existingUser != null) {
                System.out.println("❌ Registration failed: Email already exists - " + user.getEmail());
                throw new RemoteException("Email already exists");
            }

            // Validate password length
            if (user.getPassword() == null || user.getPassword().length() < 6) {
                throw new RemoteException("Password must be at least 6 characters");
            }

            // Validate phone if provided
            if (user.getPhone() != null && !user.getPhone().isEmpty()) {
                if (!ValidationUtil.isValidPhone(user.getPhone())) {
                    System.err.println("❌ Registration failed: " + ValidationUtil.getPhoneErrorMessage());
                    throw new RemoteException(ValidationUtil.getPhoneErrorMessage());
                }
            }

            // Create user document
            Document doc = new Document();
            doc.append("username", user.getUsername());
            doc.append("email", user.getEmail());
            // ✅ Hash password with BCrypt before storing
            String hashedPassword = PasswordUtil.hashPassword(user.getPassword());
            doc.append("password", hashedPassword);
            doc.append("role", user.getRole());
            doc.append("phone", user.getPhone());
            doc.append("createdAt", new Date());
            doc.append("lastLogin", null);
            doc.append("isActive", true);

        // Role-specific fields
        if ("APPLICANT".equals(user.getRole())) {
            doc.append("skills", user.getSkills());
            doc.append("experience", user.getExperience());
        } else if ("RECRUITER".equals(user.getRole())) {
            doc.append("department", user.getDepartment());
            doc.append("company", user.getCompany());
            doc.append("position", user.getPosition());
            doc.append("description", user.getDescription());
        }

        userCollection.insertOne(doc);

        String userId = doc.getObjectId("_id").toString();
        System.out.println("✅ User registered with ID: " + userId);

        // Create session
        Session session = new Session(userId, user.getEmail(), user.getRole());

        return session;

        } catch (RemoteException e) {
            throw e;
        } catch (Exception e) {
            System.err.println("❌ Registration error: " + e.getMessage());
            e.printStackTrace();
            throw new RemoteException("Registration failed", e);
        }
    }

  @Override
public Session login(String email, String password) throws RemoteException {
    try {
        System.out.println("🔐 Login attempt for: " + email);
        System.out.println("   Database: " + MongoDBConnection.getInstance().getDatabase().getName());
        System.out.println("   Collection: users");

        // Find user by email (case-insensitive search)
        // Try exact match first
        Document query = new Document("email", email);
        Document userDoc = userCollection.find(query).first();

        // If not found, try case-insensitive search
        if (userDoc == null) {
            System.out.println("   ⚠️  Exact email match not found, trying case-insensitive search...");
            query = new Document("email", new Document("$regex", "^" + email + "$").append("$options", "i"));
            userDoc = userCollection.find(query).first();
        }

        if (userDoc == null) {
            System.out.println("❌ User not found:  " + email);
            System.out.println("   Checking if database connection is working...");
            
            // Debug: List all emails in database
            long userCount = userCollection.countDocuments();
            System.out.println("   Total users in database: " + userCount);
            
            if (userCount > 0) {
                System.out.println("   Sample emails in database:");
                for (Document doc : userCollection.find().limit(5)) {
                    System.out.println("      - " + doc.getString("email"));
                }
            }
            
            throw new RemoteException("Invalid email or password");
        }
        
        System.out.println("   ✅ User found in database!");
        System.out.println("   Stored email: " + userDoc.getString("email"));

        // Get stored hashed password
        String storedHashedPassword = userDoc.getString("password");
        
        if (storedHashedPassword == null || storedHashedPassword.isEmpty()) {
            System.err.println("❌ User account has no password set: " + email);
            throw new RemoteException("Invalid email or password");
        }

        System.out.println("   Stored password (first 20 chars): " + storedHashedPassword.substring(0, Math.min(20, storedHashedPassword.length())) + "...");
        System.out.println("   Provided password length: " + (password != null ? password.length() : 0));

        // ✅ CRITICAL FIX:   Use PasswordUtil.verifyPassword() to compare
        boolean passwordMatches = PasswordUtil.verifyPassword(password, storedHashedPassword);

        System.out.println("   Password matches?  " + passwordMatches);

        if (! passwordMatches) {
            System.out.println("❌ Invalid password for: " + email);
            throw new RemoteException("Invalid email or password");
        }

        // Check if account is active
        Boolean isActive = userDoc.getBoolean("isActive");
        if (isActive != null && !isActive) {
            System.out.println("❌ Account is inactive: " + email);
            throw new RemoteException("Account is inactive.  Please contact support.");
        }

        // Get user details
        String userId = userDoc.getObjectId("_id").toString();
        String role = userDoc.getString("role");

        System.out.println("✅ Login successful!");
        System.out.println("   User ID: " + userId);
        System.out.println("   Role: " + role);

        // Update last login time
        Document updateDoc = new Document("$set", new Document("lastLogin", new Date()));
        userCollection.updateOne(query, updateDoc);

        // Create and return session
        Session session = new Session(userId, email, role);

        return session;

    } catch (RemoteException e) {
        throw e;
    } catch (Exception e) {
        System.err.println("❌ Login error: " + e.getMessage());
        e.printStackTrace();
        throw new RemoteException("Login failed", e);
    }
}

@Override
public Session loginWithPasskey(String email, String passkey) throws RemoteException {
    try {
        System.out.println("🔑 Passkey login attempt for: " + email);

        // Find user by email
        Document query = new Document("email", email);
        Document userDoc = userCollection.find(query).first();

        if (userDoc == null) {
            System.out.println("❌ User not found: " + email);
            throw new RemoteException("Invalid email or passkey");
        }

        // Get stored passkey (plain text comparison)
        String storedPasskey = userDoc.getString("passkey");

        if (storedPasskey == null || storedPasskey.isEmpty()) {
            System.out.println("❌ No passkey configured for user: " + email);
            throw new RemoteException("Invalid email or passkey");
        }

        System.out.println("   Stored passkey: " + storedPasskey);
        System.out.println("   Provided passkey: " + passkey);

        // Direct comparison (passkey is stored plain-text)
        boolean passkeyMatches = storedPasskey.equals(passkey);

        System.out.println("   Passkey matches? " + passkeyMatches);

        if (!passkeyMatches) {
            System.out.println("❌ Invalid passkey for: " + email);
            throw new RemoteException("Invalid email or passkey");
        }

        // Check if account is active
        Boolean isActive = userDoc.getBoolean("isActive");
        if (isActive != null && !isActive) {
            System.out.println("❌ Account is inactive: " + email);
            throw new RemoteException("Account is inactive. Please contact support.");
        }

        // Get user details
        String userId = userDoc.getObjectId("_id").toString();
        String role = userDoc.getString("role");

        System.out.println("✅ Passkey login successful!");
        System.out.println("   User ID: " + userId);
        System.out.println("   Role: " + role);

        // Update last login time
        Document updateDoc = new Document("$set", new Document("lastLogin", new Date()));
        userCollection.updateOne(query, updateDoc);

        // Create and return session
        Session session = new Session(userId, email, role);

        return session;

    } catch (RemoteException e) {
        throw e;
    } catch (Exception e) {
        System.err.println("❌ Passkey login error: " + e.getMessage());
        e.printStackTrace();
        throw new RemoteException("Passkey login failed", e);
    }
}



    @Override
    public boolean logout(String sessionToken) throws RemoteException {
        try {
            Session session = activeSessions.remove(sessionToken);

            if (session != null) {
                System.out.println("✅ User logged out: " + session.getUserEmail());
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
            System.err.println("❌ Session validation error: " + e.getMessage());
            return null;
        }
    }

    public User getUserById(String userId) throws RemoteException {
        try {
            System.out.println("   🔍 Getting user by ID: " + userId);

            ObjectId objectId = new ObjectId(userId);
            Document userDoc = userCollection.find(new Document("_id", objectId)).first(); // ✅ Use existing userCollection

            if (userDoc != null) {
                User user = documentToUser(userDoc);
                System.out.println("   ✅ Found user: " + user.getEmail() + " (Role: " + user.getRole() + ")");
                return user;
            }

            System.out.println("   ⚠️  User not found");
            return null;

        } catch (IllegalArgumentException e) {
            System.err.println("   ❌ Invalid user ID format");
            return null;
        } catch (Exception e) {
            System.err.println("   ❌ Error getting user: " + e.getMessage());
            e.printStackTrace();
            throw new RemoteException("Failed to get user", e);
        }
    }


    @Override
    public User getUserByEmail(String email) throws RemoteException {
        try {
            System.out.println("🔍 Getting user by email:  " + email);

            Document userDoc = userCollection.find(new Document("email", email)).first();

            if (userDoc != null) {
                User user = documentToUser(userDoc);
                System.out.println("✅ Found user: " + user.getEmail());
                return user;
            }

            System.out.println("⚠️  User not found with email: " + email);
            return null;

        } catch (Exception e) {
            System.err.println("❌ Error getting user by email: " + e.getMessage());
            e.printStackTrace();
            throw new RemoteException("Failed to get user by email", e);
        }
    }

    @Override
    public java.util.List<User> getAllUsers() throws RemoteException {
        try {
            System.out.println("🔍 Retrieving all users from database...");

            java.util.List<User> users = new java.util.ArrayList<>();

            for (Document userDoc : userCollection.find()) {
                User user = documentToUser(userDoc);
                users.add(user);
            }

            System.out.println("✅ Retrieved " + users.size() + " users");
            return users;

        } catch (Exception e) {
            System.err.println("❌ Error getting all users: " + e.getMessage());
            e.printStackTrace();
            throw new RemoteException("Failed to get all users", e);
        }
    }

    @Override
    public boolean deleteUser(String userId) throws RemoteException {
        try {
            System.out.println("🗑️ Deleting user: " + userId);
            ObjectId objectId = new ObjectId(userId);
            Document query = new Document("_id", objectId);
            var result = userCollection.deleteOne(query);
            boolean ok = result != null && result.getDeletedCount() > 0;
            System.out.println(ok ? "✅ User deleted" : "⚠️ User not found");
            return ok;
        } catch (IllegalArgumentException e) {
            System.err.println("❌ Invalid userId format");
            return false;
        } catch (Exception e) {
            System.err.println("❌ Delete user error: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean setUserActive(String userId, boolean active) throws RemoteException {
        try {
            System.out.println((active ? "✅ Activating" : "🚫 Deactivating") + " user: " + userId);
            ObjectId objectId = new ObjectId(userId);
            Document query = new Document("_id", objectId);
            Document update = new Document("$set", new Document("isActive", active));
            var result = userCollection.updateOne(query, update);
            boolean ok = result != null && result.getModifiedCount() > 0;
            System.out.println(ok ? "✅ User updated" : "⚠️ User not found or unchanged");
            return ok;
        } catch (IllegalArgumentException e) {
            System.err.println("❌ Invalid userId format");
            return false;
        } catch (Exception e) {
            System.err.println("❌ setUserActive error: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean changePassword(String email, String oldPassword, String newPassword) throws RemoteException {
        try {
            // Validate new password
            if (newPassword == null || newPassword.length() < 6) {
                System.err.println("❌ Password change failed: Password must be at least 6 characters");
                return false;
            }

            // Get user by email
            User user = getUserByEmail(email);

            if (user == null) {
                System.err.println("❌ Password change failed:  User not found");
                return false;
            }

            // Verify old password
            if (!user.getPassword().equals(oldPassword)) {
                System.err.println("❌ Password change failed: Old password incorrect");
                return false;
            }

            // Update password
            userCollection.updateOne(
                new Document("email", email),
                new Document("$set", new Document("password", newPassword))
            );

            System.out.println("✅ Password changed for user: " + email);

            return true;

        } catch (Exception e) {
            System.err.println("❌ Password change error:  " + e.getMessage());
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

    /**
     * Helper method to update last login time
     */
    @SuppressWarnings("unused")
    private void updateLastLogin(String userId) {
        try {
            Document query = new Document("_id", new ObjectId(userId));
            Document update = new Document("$set", new Document("lastLogin", new Date()));
            userCollection.updateOne(query, update);
        } catch (Exception e) {
            System.err.println("❌ Error updating last login: " + e.getMessage());
        }
    }

    /**
     * Helper method to convert Document to User
     */
    private User documentToUser(Document doc) {
        User user = new User();

        // Common fields
        user.setUserId(doc.getObjectId("_id").toString());
        user.setUsername(doc.getString("username"));
        user.setEmail(doc.getString("email"));
        user.setPassword(doc.getString("password"));
        user.setRole(doc.getString("role"));
        user.setPhone(doc.getString("phone"));
        user.setCreatedAt(doc.getDate("createdAt"));
        user.setLastLogin(doc.getDate("lastLogin"));
        user.setActive(doc.getBoolean("isActive", true));

        // Role-specific fields
        if ("APPLICANT".equals(user.getRole())) {
            user.setSkills(doc.getString("skills"));
            user.setExperience(doc.getString("experience"));
        } else if ("RECRUITER".equals(user.getRole())) {
            user.setDepartment(doc.getString("department"));
            user.setCompany(doc.getString("company"));
            user.setPosition(doc.getString("position"));
            user.setDescription(doc.getString("description"));
        }

        return user;
    }
}
