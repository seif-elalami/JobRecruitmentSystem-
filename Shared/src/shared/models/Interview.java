package shared.models;

import java.io.Serializable;
import java.util.Date;

public class Interview implements Serializable {

    private static final long serialVersionUID = 1L;

    private String interviewId;
    private String jobId;
    private String applicantId;
    private String recruiterId;
    private Date scheduledDate;
    private String location;  // "Online" or physical address
    private String status;    // "SCHEDULED", "COMPLETED", "CANCELLED"
    private String notes;

    // Constructors
    public Interview() {
    }

    public Interview(String jobId, String applicantId, String recruiterId, Date scheduledDate, String location) {
        this.jobId = jobId;
        this.applicantId = applicantId;
        this.recruiterId = recruiterId;
        this.scheduledDate = scheduledDate;
        this.location = location;
        this.status = "SCHEDULED";
    }

    // Getters and Setters
    public String getInterviewId() {
        return interviewId;
    }

    public void setInterviewId(String interviewId) {
        this.interviewId = interviewId;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getApplicantId() {
        return applicantId;
    }

    public void setApplicantId(String applicantId) {
        this.applicantId = applicantId;
    }

    public String getRecruiterId() {
        return recruiterId;
    }

    public void setRecruiterId(String recruiterId) {
        this.recruiterId = recruiterId;
    }

    public Date getScheduledDate() {
        return scheduledDate;
    }

    public void setScheduledDate(Date scheduledDate) {
        this.scheduledDate = scheduledDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "Interview{" +
                "interviewId='" + interviewId + '\'' +
                ", jobId='" + jobId + '\'' +
                ", applicantId='" + applicantId + '\'' +
                ", recruiterId='" + recruiterId + '\'' +
                ", scheduledDate=" + scheduledDate +
                ", location='" + location + '\'' +
                ", status='" + status + '\'' +
                ", notes='" + notes + '\'' +
                '}';
    }
}
