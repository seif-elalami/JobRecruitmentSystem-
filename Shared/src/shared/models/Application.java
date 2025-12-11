package shared.models;

import java.io. Serializable;
import java.util.Date;

public class Application implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String jobId;              // Which job they're applying to
    private String applicantId;        // Who is applying
    private Date applicationDate;      // When they applied
    private String status;             // "PENDING", "ACCEPTED", "REJECTED"
    private String coverLetter;        // Optional cover letter

    // Default constructor
    public Application() {
        this.applicationDate = new Date();
        this.status = "PENDING";
    }

    // Constructor with parameters
    public Application(String jobId, String applicantId, String coverLetter) {
        this. jobId = jobId;
        this.applicantId = applicantId;
        this.coverLetter = coverLetter;
        this.applicationDate = new Date();
        this.status = "PENDING";
    }

    // Getters
    public String getId() { return id; }
    // Alias used in recruiter UI
    public String getApplicationId() { return id; }
    public String getJobId() { return jobId; }
    public String getApplicantId() { return applicantId; }
    public Date getApplicationDate() { return applicationDate; }
    public String getStatus() { return status; }
    public String getCoverLetter() { return coverLetter; }

    // Setters
    public void setId(String id) { this.id = id; }
    public void setApplicationId(String id) { this.id = id; }
    public void setJobId(String jobId) { this.jobId = jobId; }
    public void setApplicantId(String applicantId) { this.applicantId = applicantId; }
    public void setApplicationDate(Date applicationDate) { this.applicationDate = applicationDate; }
    public void setStatus(String status) { this. status = status; }
    public void setCoverLetter(String coverLetter) { this.coverLetter = coverLetter; }

    @Override
    public String toString() {
        return "Application{" +
                "id='" + id + '\'' +
                ", jobId='" + jobId + '\'' +
                ", applicantId='" + applicantId + '\'' +
                ", status='" + status + '\'' +
                ", date=" + applicationDate +
                '}';
    }
}
