package shared.models;

import java. io.Serializable;

public class Job implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String title;
    private String description;
    private String company;
    private String location;
    private double salary;
    private String requirements;
    private String status; // "OPEN" or "CLOSED"

    // Default constructor
    public Job() {
        this.status = "OPEN";
    }

    // Constructor with parameters
    public Job(String title, String description, String company,
               String location, double salary, String requirements) {
        this.title = title;
        this.description = description;
        this.company = company;
        this.location = location;
        this.salary = salary;
        this.requirements = requirements;
        this.status = "OPEN";
    }

    // Getters
    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getCompany() { return company; }
    public String getLocation() { return location; }
    public double getSalary() { return salary; }
    public String getRequirements() { return requirements; }
    public String getStatus() { return status; }

    // Setters
    public void setId(String id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setCompany(String company) { this.company = company; }
    public void setLocation(String location) { this. location = location; }
    public void setSalary(double salary) { this.salary = salary; }
    public void setRequirements(String requirements) { this.requirements = requirements; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return "Job{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", company='" + company + '\'' +
                ", location='" + location + '\'' +
                ", salary=" + salary +
                ", status='" + status + '\'' +
                '}';
    }
}
