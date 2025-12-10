package shared.models;

import java.io.Serializable;
import java.util.Date;

public class Session implements Serializable {

    private static final long serialVersionUID = 1L;

    private String sessionToken;
    private String userId;
    private String email;
    private String role;
    private String profileId;
    private Date createdAt;
    private Date expiresAt;

    /**
     * Default constructor (required for serialization)
     */
    public Session() {
    }

    /**
     * Full constructor - THIS IS WHAT YOU NEED!
     * Used in AuthServiceImpl. login() method
     */
    public Session(String sessionToken, String userId, String email, String role,
                   String profileId, Date createdAt, Date expiresAt) {
        this.sessionToken = sessionToken;
        this.userId = userId;
        this.email = email;
        this.role = role;
        this.profileId = profileId;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
    }

    /**
     * Check if session is expired
     */
    public boolean isExpired() {
        if (expiresAt == null) {
            return true;
        }
        return new Date().after(expiresAt);
    }

    // ============================================
    // GETTERS AND SETTERS
    // ============================================

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Date expiresAt) {
        this.expiresAt = expiresAt;
    }

    @Override
    public String toString() {
        return "Session{" +
                "sessionToken='" + sessionToken + '\'' +
                ", userId='" + userId + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                ", profileId='" + profileId + '\'' +
                ", createdAt=" + createdAt +
                ", expiresAt=" + expiresAt +
                ", expired=" + isExpired() +
                '}';
    }
}
