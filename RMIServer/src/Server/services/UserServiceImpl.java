package Server.services;

import Server.database.MongoDBConnection;
import Server.utils.ValidationUtil;
import shared.models.User;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * UserService - Manages all users regardless of role
 * This is used internally by AuthService and other services
 */
public class UserServiceImpl {

    private MongoCollection<Document> userCollection;

    public UserServiceImpl() {
        MongoDatabase database = MongoDBConnection.getInstance().getDatabase();
        userCollection = database.getCollection("users");
        System.out.println("✅ UserService initialized");
    }

    /**
     * Create a new user (any role)
     */
    public String createUser(User user) throws RemoteException {
        try {

            if (! ValidationUtil.isValidEmail(user.getEmail())) {
                throw new RemoteException(ValidationUtil.getEmailErrorMessage());
            }

            if (getUserByEmail(user.getEmail()) != null) {
                throw new RemoteException("Email already exists");
            }

            if (user.getPassword() == null || user.getPassword().length() < 6) {
                throw new RemoteException("Password must be at least 6 characters");
            }

            if (user.getPhone() != null && !user.getPhone().isEmpty()) {
                if (!ValidationUtil.isValidPhone(user.getPhone())) {
                    throw new RemoteException(ValidationUtil.getPhoneErrorMessage());
                }
            }

            Document doc = new Document();
            doc.append("username", user.getUsername());
            doc.append("email", user.getEmail());
            doc.append("password", user.getPassword());
            doc.append("role", user.getRole());
            doc.append("phone", user.getPhone());
            doc.append("createdAt", new Date());
            doc.append("lastLogin", null);
            doc.append("isActive", true);

            if ("APPLICANT".equals(user. getRole())) {
                doc.append("skills", user.getSkills());
                doc.append("experience", user.getExperience());
            } else if ("RECRUITER".equals(user. getRole())) {
                doc.append("department", user.getDepartment());
                doc.append("company", user.getCompany());
                doc.append("position", user.getPosition());
                doc.append("description", user. getDescription());
            }

            userCollection.insertOne(doc);

            String id = doc.getObjectId("_id").toString();
            user.setUserId(id);

            System.out.println("✅ User created:  " + user.getEmail() + " (Role: " + user.getRole() + ", ID: " + id + ")");

            return id;

        } catch (RemoteException e) {
            throw e;
        } catch (Exception e) {
            System.err.println("❌ Error creating user: " + e.getMessage());
            throw new RemoteException("Failed to create user", e);
        }
    }

    /**
     * Get user by ID
     */
    public User getUserById(String id) throws RemoteException {
        try {
            Document doc = userCollection.find(new Document("_id", new ObjectId(id))).first();

            if (doc == null) {
                return null;
            }

            return documentToUser(doc);

        } catch (Exception e) {
            System.err.println("❌ Error getting user: " + e.getMessage());
            throw new RemoteException("Failed to get user", e);
        }
    }

    /**
     * Get user by email
     */
    public User getUserByEmail(String email) throws RemoteException {
        try {
            Document doc = userCollection.find(new Document("email", email)).first();

            if (doc == null) {
                return null;
            }

            return documentToUser(doc);

        } catch (Exception e) {
            System.err.println("❌ Error getting user:  " + e.getMessage());
            throw new RemoteException("Failed to get user", e);
        }
    }

    /**
     * Get all users by role
     */
    public List<User> getUsersByRole(String role) throws RemoteException {
        try {
            List<User> users = new ArrayList<>();

            Document query = new Document("role", role);

            for (Document doc : userCollection.find(query)) {
                users.add(documentToUser(doc));
            }

            return users;

        } catch (Exception e) {
            System.err.println("❌ Error getting users by role: " + e.getMessage());
            throw new RemoteException("Failed to get users", e);
        }
    }

    /**
     * Update user
     */
    public boolean updateUser(User user) throws RemoteException {
        try {

            if (!ValidationUtil.isValidEmail(user.getEmail())) {
                throw new RemoteException(ValidationUtil.getEmailErrorMessage());
            }

            if (user.getPhone() != null && !user.getPhone().isEmpty()) {
                if (!ValidationUtil.isValidPhone(user.getPhone())) {
                    throw new RemoteException(ValidationUtil.getPhoneErrorMessage());
                }
            }

            Document query = new Document("_id", new ObjectId(user.getUserId()));

            Document update = new Document();
            update.append("username", user.getUsername());
            update.append("email", user.getEmail());
            update.append("phone", user.getPhone());

            if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                update.append("password", user.getPassword());
            }

            if ("APPLICANT".equals(user.getRole())) {
                update.append("skills", user.getSkills());
                update.append("experience", user.getExperience());
            } else if ("RECRUITER".equals(user.getRole())) {
                update.append("department", user.getDepartment());
                update.append("company", user.getCompany());
                update.append("position", user.getPosition());
                update.append("description", user.getDescription());
            }

            Document updateDoc = new Document("$set", update);

            userCollection.updateOne(query, updateDoc);

            System.out.println("✅ User updated: " + user.getEmail());

            return true;

        } catch (RemoteException e) {
            throw e;
        } catch (Exception e) {
            System.err.println("❌ Error updating user: " + e.getMessage());
            return false;
        }
    }

    /**
     * Update last login time
     */
    public void updateLastLogin(String userId) throws RemoteException {
        try {
            Document query = new Document("_id", new ObjectId(userId));
            Document update = new Document("$set", new Document("lastLogin", new Date()));

            userCollection.updateOne(query, update);

        } catch (Exception e) {
            System.err.println("❌ Error updating last login: " + e.getMessage());
        }
    }

    /**
     * Search users by skills (for applicants)
     */
    public List<User> searchUsersBySkills(String skills) throws RemoteException {
        try {
            List<User> users = new ArrayList<>();

            Document query = new Document("role", "APPLICANT")
                    .append("skills", new Document("$regex", skills).append("$options", "i"));

            for (Document doc : userCollection.find(query)) {
                users.add(documentToUser(doc));
            }

            return users;

        } catch (Exception e) {
            System.err.println("❌ Error searching users by skills: " + e.getMessage());
            throw new RemoteException("Failed to search users", e);
        }
    }

    /**
     * Search users by experience (for applicants)
     */
    public List<User> searchUsersByExperience(String experience) throws RemoteException {
        try {
            List<User> users = new ArrayList<>();

            Document query = new Document("role", "APPLICANT")
                    .append("experience", new Document("$regex", experience).append("$options", "i"));

            for (Document doc : userCollection.find(query)) {
                users.add(documentToUser(doc));
            }

            return users;

        } catch (Exception e) {
            System.err.println("❌ Error searching users by experience: " + e.getMessage());
            throw new RemoteException("Failed to search users", e);
        }
    }

    /**
     * Helper method to convert Document to User
     */
    private User documentToUser(Document doc) {
        User user = new User();

        user.setUserId(doc.getObjectId("_id").toString());
        user.setUsername(doc.getString("username"));
        user.setEmail(doc.getString("email"));
        user.setPassword(doc.getString("password"));
        user.setRole(doc.getString("role"));
        user.setPhone(doc.getString("phone"));
        user.setCreatedAt(doc. getDate("createdAt"));
        user.setLastLogin(doc.getDate("lastLogin"));
        user.setActive(doc. getBoolean("isActive", true));

        if ("APPLICANT".equals(user.getRole())) {
            user. setSkills(doc.getString("skills"));
            user.setExperience(doc.getString("experience"));
        } else if ("RECRUITER".equals(user.getRole())) {
            user. setDepartment(doc.getString("department"));
            user.setCompany(doc.getString("company"));
            user.setPosition(doc.getString("position"));
            user.setDescription(doc.getString("description"));
        }

        return user;
    }
}