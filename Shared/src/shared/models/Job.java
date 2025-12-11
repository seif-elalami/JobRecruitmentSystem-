package shared.models;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Job implements Serializable {

    private static final long serialVersionUID = 1L;

    private String jobId;
    // Alias so UI/tests can call getId()

    private String title;
    private String description;
    private List<String> requirements;
    private String status;  // "OPEN", "CLOSED"
    private Date postedDate;
    private String company;
    private String location;
    private double salary;
    private String recruiterId;  // ← ADD THIS:  ID of recruiter who posted the job

    // Constructors
    public Job() {
        this.postedDate = new Date();
        this.status = "OPEN";
    }

    public Job(String title, String description, List<String> requirements, String recruiterId) {
        this.title = title;
        this.description = description;
        this.requirements = requirements;
        this.recruiterId = recruiterId;  // ← ADD THIS
        this.postedDate = new Date();
        this.status = "OPEN";
    }

    // Overload used by client test menus (no recruiterId, string requirements)
    public Job(String title, String description, String company, String location, double salary, String requirements) {
        this(title, description, List.of(requirements), null);
        this.company = company;
        this.location = location;
        this.salary = salary;
    }

    // Overload used by tests (list requirements, no recruiterId)
    public Job(String title, String description, String company, String location, double salary, List<String> requirements) {
        this(title, description, requirements, null);
        this.company = company;
        this.location = location;
        this.salary = salary;
    }

    // Getters and Setters
    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    // Alias for UI/tests
    public String getId() {
        return jobId;
    }

    public void setId(String id) {
        this.jobId = id;

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getRequirements() {
        return requirements;
    }

    public void setRequirements(List<String> requirements) {
        this.requirements = requirements;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(Date postedDate) {
        this.postedDate = postedDate;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    // ← ADD THIS GETTER/SETTER
    public String getRecruiterId() {
        return recruiterId;
    }

    public void setRecruiterId(String recruiterId) {
        this.recruiterId = recruiterId;
    }

    @Override
    public String toString() {
        return "Job{" +
                "jobId='" + jobId + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", requirements=" + requirements +
                ", status='" + status + '\'' +
                ", postedDate=" + postedDate +
                ", recruiterId='" + recruiterId + '\'' +  // ← ADD THIS
                '}';
    }
}
