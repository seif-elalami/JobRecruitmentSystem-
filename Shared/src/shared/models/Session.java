package shared.models;

import java.io.Serializable;
import java.util.Date;

public class Session implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final long SESSION_TIMEOUT = 30 * 60 * 1000;  // 30 minutes

    private String sessionId;
    private String userId;
    private String userEmail;
    private String role;
    private Date loginTime;
    private Date lastActivity;

    public Session() {
    }

    public Session(String userId, String userEmail, String role) {
        this.userId = userId;
        this.userEmail = userEmail;
        this.role = role;
        this.loginTime = new Date();
        this.lastActivity = new Date();
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    public Date getLastActivity() {
        return lastActivity;
    }

    public void setLastActivity(Date lastActivity) {
        this.lastActivity = lastActivity;
    }

    /**
     * Update last activity time to current time
     */
    public void updateActivity() {
        this.lastActivity = new Date();
    }

    /**
     * Check if session has expired based on last activity
     * Session expires after SESSION_TIMEOUT milliseconds of inactivity
     *
     * @return true if session is expired, false otherwise
     */
    public boolean isExpired() {
        if (lastActivity == null) {
            return true;
        }

        long currentTime = System.currentTimeMillis();
        long lastActivityTime = lastActivity.getTime();
        long timeSinceLastActivity = currentTime - lastActivityTime;

        return timeSinceLastActivity > SESSION_TIMEOUT;
    }

    /**
     * Get remaining time before session expires (in minutes)
     *
     * @return remaining minutes, or 0 if expired
     */
    public long getRemainingMinutes() {
        if (isExpired()) {
            return 0;
        }

        long currentTime = System.currentTimeMillis();
        long lastActivityTime = lastActivity.getTime();
        long timeSinceLastActivity = currentTime - lastActivityTime;
        long remainingTime = SESSION_TIMEOUT - timeSinceLastActivity;

        return remainingTime / (60 * 1000);  // Convert to minutes
    }

    @Override
    public String toString() {
        return "Session{" +
                "sessionId='" + sessionId + '\'' +
                ", userId='" + userId + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", role='" + role + '\'' +
                ", loginTime=" + loginTime +
                ", lastActivity=" + lastActivity +
                ", expired=" + isExpired() +
                '}';
    }
}