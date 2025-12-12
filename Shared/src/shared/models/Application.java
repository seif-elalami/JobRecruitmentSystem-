package shared.models;

import java.io.Serializable;
import java.util.Date;

/**
 * Application class using concrete ApplicationState for state management
 */
public class Application implements Serializable {
    private static final long serialVersionUID = 1L;

    // Attributes as specified
    private String applicationID;
    private String jobID;
    private String applicantID;
    private Date submissionDate;
    private ApplicationState currentState;
    private String coverLetter;

    // Legacy compatibility
    private String status;

    // Default constructor
    public Application() {
        this.submissionDate = new Date();
        this.currentState = new ApplicationState();
        this.status = "APPLIED";
    }

    // Constructor with parameters
    public Application(String jobID, String applicantID, String coverLetter) {
        this.jobID = jobID;
        this.applicantID = applicantID;
        this.coverLetter = coverLetter;
        this.submissionDate = new Date();
        this.currentState = new ApplicationState();
        this.status = "APPLIED";
    }

    // State Pattern Methods

    /**
     * Submit the application (initialize in Applied state)
     */
    public void submitApplication() {
        System.out.println("📤 Submitting application...");
        this.currentState = new ApplicationState();
        this.status = currentState.getStateName();
        this.submissionDate = new Date();
        currentState.handle(this);
        System.out.println("✅ Application submitted successfully!");
    }

    /**
     * Set a new state for the application
     * @param newState The new state to transition to
     */
    public void setState(ApplicationState newState) {
        System.out.println("🔄 Changing state to " + newState.getStateName());
        this.currentState = newState;
        this.status = newState.getStateName();
        applyStateChanges();
    }

    /**
     * Apply the current state's changes
     */
    public void applyStateChanges() {
        if (currentState != null) {
            currentState.handle(this);
        }
    }

    /**
     * Update the application state based on action
     */
    public void updateState() {
        if (currentState != null) {
            System.out.println("📊 Current State: " + currentState.getStateName());
            currentState.handle(this);
        }
    }

    /**
     * Get the current application status
     * @return Status string
     */
    public String getApplicationStatus() {
        if (currentState != null) {
            return currentState.getStateName();
        }
        return status;
    }

    /**
     * Update job posting (if needed)
     */
    public void updateJobPosting() {
        System.out.println("🔄 Updating job posting for application: " + applicationID);
        // Job posting update logic handled by service layer
    }

    // State Transition Methods

    /**
     * Move to Under Review state
     */
    public void moveToReview() {
        if (currentState != null) {
            currentState.review();
            this.status = currentState.getStateName();
        }
    }

    /**
     * Schedule an interview
     */
    public void scheduleInterview() {
        if (currentState != null) {
            currentState.scheduleInterview();
            this.status = currentState.getStateName();
        }
    }

    /**
     * Accept the application
     */
    public void acceptApplication() {
        if (currentState != null) {
            currentState.accept();
            this.status = currentState.getStateName();
        }
    }

    /**
     * Reject the application
     */
    public void rejectApplication() {
        if (currentState != null) {
            currentState.reject();
            this.status = currentState.getStateName();
        }
    }

    // Getters
    public String getApplicationID() {
        return applicationID;
    }

    public String getId() {
        return applicationID;
    }

    public String getApplicationId() {
        return applicationID;
    }

    public String getJobID() {
        return jobID;
    }

    public String getJobId() {
        return jobID;
    }

    public String getApplicantID() {
        return applicantID;
    }

    public String getApplicantId() {
        return applicantID;
    }

    public Date getSubmissionDate() {
        return submissionDate;
    }

    public Date getApplicationDate() {
        return submissionDate;
    }

    public ApplicationState getCurrentState() {
        return currentState;
    }

    public String getStatus() {
        return status;
    }

    public String getCoverLetter() {
        return coverLetter;
    }

    // Setters
    public void setApplicationID(String applicationID) {
        this.applicationID = applicationID;
    }

    public void setId(String id) {
        this.applicationID = id;
    }

    public void setApplicationId(String id) {
        this.applicationID = id;
    }

    public void setJobID(String jobID) {
        this.jobID = jobID;
    }

    public void setJobId(String jobId) {
        this.jobID = jobId;
    }

    public void setApplicantID(String applicantID) {
        this.applicantID = applicantID;
    }

    public void setApplicantId(String applicantId) {
        this.applicantID = applicantId;
    }

    public void setSubmissionDate(Date submissionDate) {
        this.submissionDate = submissionDate;
    }

    public void setApplicationDate(Date date) {
        this.submissionDate = date;
    }

    public void setCurrentState(ApplicationState currentState) {
        this.currentState = currentState;
        if (currentState != null) {
            this.status = currentState.getStateName();
        }
    }

    public void setStatus(String status) {
        this.status = status;
        // Sync state object with status string
        if (currentState == null) {
            currentState = new ApplicationState();
        }
        currentState.setStateByName(status);
    }

    public void setCoverLetter(String coverLetter) {
        this.coverLetter = coverLetter;
    }

    @Override
    public String toString() {
        return "Application{" +
                "applicationID='" + applicationID + '\'' +
                ", jobID='" + jobID + '\'' +
                ", applicantID='" + applicantID + '\'' +
                ", status='" + (currentState != null ? currentState.getStateName() : status) + '\'' +
                ", submissionDate=" + submissionDate +
                '}';
    }
}
