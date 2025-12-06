package shared.models;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public class Session implements Serializable {
    private static final long serialVersionUID = 1L;

    private String sessionToken;    // Unique session identifier
    private String userId;          // Which user this session belongs to
    private String email;           // User's email
    private String role;            // User's role
    private String profileId;       // User's profile ID
    private Date createdAt;         // When session was created
    private Date expiresAt;         // When session expires

    // Default constructor
    public Session() {
    }

    // Constructor
    public Session(String userId, String email, String role, String profileId) {
        this. sessionToken = UUID.randomUUID(). toString();  // Generate unique token
        this.userId = userId;
        this.email = email;
        this.role = role;
        this.profileId = profileId;
        this. createdAt = new Date();

        // Session expires in 24 hours
        this.expiresAt = new Date(System. currentTimeMillis() + (24 * 60 * 60 * 1000));
    }

    // Check if session is expired
    public boolean isExpired() {
        return new Date().after(expiresAt);
    }

    // Getters
    public String getSessionToken() { return sessionToken; }
    public String getUserId() { return userId; }
    public String getEmail() { return email; }
    public String getRole() { return role; }
    public String getProfileId() { return profileId; }
    public Date getCreatedAt() { return createdAt; }
    public Date getExpiresAt() { return expiresAt; }

    // Setters
    public void setSessionToken(String sessionToken) { this.sessionToken = sessionToken; }
    public void setUserId(String userId) { this.userId = userId; }
    public void setEmail(String email) { this.email = email; }
    public void setRole(String role) { this.role = role; }
    public void setProfileId(String profileId) { this.profileId = profileId; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
    public void setExpiresAt(Date expiresAt) { this.expiresAt = expiresAt; }

    @Override
    public String toString() {
        return "Session{" +
                "sessionToken='" + sessionToken + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                ", expiresAt=" + expiresAt +
                '}';
    }
}
