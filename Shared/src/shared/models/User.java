package shared.models;

import java.io.Serializable;
import java.util.Date;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;              // MongoDB ObjectId
    private String email;           // Used for login (unique)
    private String passwordHash;    // Hashed password (never store plain text!)
    private String role;            // "APPLICANT" or "RECRUITER"
    private String profileId;       // Links to Applicant or Recruiter profile
    private Date createdAt;         // When account was created
    private Date lastLogin;         // Last login timestamp
    private boolean isActive;       // Account status

    // Default constructor
    public User() {
        this.createdAt = new Date();
        this.isActive = true;
    }

    // Constructor
    public User(String email, String passwordHash, String role) {
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
        this.createdAt = new Date();
        this.isActive = true;
    }

    // Getters
    public String getId() { return id; }
    public String getEmail() { return email; }
    public String getPasswordHash() { return passwordHash; }
    public String getRole() { return role; }
    public String getProfileId() { return profileId; }
    public Date getCreatedAt() { return createdAt; }
    public Date getLastLogin() { return lastLogin; }
    public boolean isActive() { return isActive; }

    // Setters
    public void setId(String id) { this.id = id; }
    public void setEmail(String email) { this.email = email; }
    public void setPasswordHash(String passwordHash) { this. passwordHash = passwordHash; }
    public void setRole(String role) { this.role = role; }
    public void setProfileId(String profileId) { this. profileId = profileId; }
    public void setCreatedAt(Date createdAt) { this. createdAt = createdAt; }
    public void setLastLogin(Date lastLogin) { this. lastLogin = lastLogin; }
    public void setActive(boolean active) { isActive = active; }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                ", isActive=" + isActive +
                ", createdAt=" + createdAt +
                '}';
    }
}
